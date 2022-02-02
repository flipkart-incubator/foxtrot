package com.foxtrot.flipkart.translator;


import io.appform.hbase.ds.AbstractRowKeyDistributor;

public class IdentityKeyDistributor extends AbstractRowKeyDistributor {

    @Override
    public byte[] getDistributedKey(byte[] bytes) {
        return bytes;
    }

    @Override
    public byte[] getOriginalKey(byte[] bytes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[][] getAllDistributedKeys(byte[] bytes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getParamsToStore() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void init(String s) {
        throw new UnsupportedOperationException();
    }
}
