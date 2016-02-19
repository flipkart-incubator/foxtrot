package com.flipkart.foxtrot.common.stats;

import com.flipkart.foxtrot.common.ActionRequest;
import com.flipkart.foxtrot.common.query.FilterCombinerType;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by rishabh.goyal on 02/08/14.
 */
public class StatsRequest extends ActionRequest {

    private String table;

    private String field;

    private FilterCombinerType combiner = FilterCombinerType.and;

    public StatsRequest() {

    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public FilterCombinerType getCombiner() {
        return combiner;
    }

    public void setCombiner(FilterCombinerType combiner) {
        this.combiner = combiner;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("table", table)
                .append("field", field)
                .append("filters", getFilters())
                .append("combiner", combiner)
                .toString();
    }
}
