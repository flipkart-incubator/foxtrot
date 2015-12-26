/**
 * Copyright 2014 Flipkart Internet Pvt. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flipkart.foxtrot.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.flipkart.foxtrot.core.common.Action;
import com.flipkart.foxtrot.core.querystore.actions.spi.ActionMetadata;
import com.flipkart.foxtrot.core.querystore.actions.spi.AnalyticsLoader;
import com.flipkart.foxtrot.core.querystore.actions.spi.AnalyticsProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yammer.dropwizard.lifecycle.Managed;
import net.sourceforge.cobertura.CoverageIgnore;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * User: Santanu Sinha (santanu.sinha@flipkart.com)
 * Date: 27/03/14
 * Time: 2:23 AM
 */

@CoverageIgnore
@Singleton
public class ManagedActionScanner implements Managed {
    private static final Logger logger = LoggerFactory.getLogger(ManagedActionScanner.class.getSimpleName());

    private final AnalyticsLoader analyticsLoader;
    private final ObjectMapper objectMapper;

    @Inject
    public ManagedActionScanner(AnalyticsLoader analyticsLoader, ObjectMapper objectMapper) {
        this.analyticsLoader = analyticsLoader;
        this.objectMapper = objectMapper;
    }

    @Override
    public void start() throws Exception {
        Reflections reflections = new Reflections("com.flipkart.foxtrot", new SubTypesScanner());
        Set<Class<? extends Action>> actions = reflections.getSubTypesOf(Action.class);
        if (actions.isEmpty()) {
            throw new Exception("No analytics actions found!!");
        }
        List<NamedType> types = new ArrayList<>();
        for (Class<? extends Action> action : actions) {
            AnalyticsProvider analyticsProvider = action.getAnnotation(AnalyticsProvider.class);
            if (null == analyticsProvider.request()
                    || null == analyticsProvider.opcode()
                    || analyticsProvider.opcode().isEmpty()
                    || null == analyticsProvider.response()) {
                throw new Exception("Invalid annotation on " + action.getCanonicalName());
            }
            if (analyticsProvider.opcode().equalsIgnoreCase("default")) {
                logger.warn("Action " + action.getCanonicalName() + " does not specify cache token. " +
                        "Using default cache.");
            }
            analyticsLoader.register(new ActionMetadata(
                    analyticsProvider.request(), action,
                    analyticsProvider.cacheable(), analyticsProvider.opcode()));
            types.add(new NamedType(analyticsProvider.request(), analyticsProvider.opcode()));
            types.add(new NamedType(analyticsProvider.response(), analyticsProvider.opcode()));
            logger.info("Registered action: " + action.getCanonicalName());
        }
        objectMapper.getSubtypeResolver().registerSubtypes(types.toArray(new NamedType[types.size()]));
    }

    @Override
    public void stop() throws Exception {

    }

}
