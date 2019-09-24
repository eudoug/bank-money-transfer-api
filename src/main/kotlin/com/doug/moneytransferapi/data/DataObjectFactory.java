package com.doug.moneytransferapi.data;

public abstract class DataObjectFactory {

    public static final int H2 = 1;

    public abstract CustomerDataObject getCustomerDataObject();

    public abstract AccountDataObject getAccountDataObject();

    public abstract void generateApplicationData();

    public static DataObjectFactory getDataObjectFactory(int factoryCode) {

        switch (factoryCode) {
            default:
                // by default using H2 in memory database
                return new DataFactory();
        }
    }
}
