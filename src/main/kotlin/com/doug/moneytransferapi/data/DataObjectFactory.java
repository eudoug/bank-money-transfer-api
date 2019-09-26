package com.doug.moneytransferapi.data;

public abstract class DataObjectFactory {

    public static final int H2 = 1;

    public abstract CustomerDataObject getCustomerDataObject();

    public abstract AccountDataObject getAccountDataObject();

    public abstract void generateApplicationData();

    public static DataObjectFactory getDataObjectFactory(int factoryCode) {

        if (factoryCode == H2) {
            return new DataFactory();
        }
        return new DataFactory();

    }
}
