package com.doug.moneytransferapi.data;

import com.doug.moneytransferapi.data.support.AccountSupport;
import com.doug.moneytransferapi.data.support.CustomerSupport;
import com.doug.moneytransferapi.utils.Utils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataFactory extends DataObjectFactory {
    private static final String H2_DRIVER = Utils.getStringProperty("h2_driver");
    private static final String H2_CONNECTION_URL = Utils.getStringProperty("h2_connection_url");
    private static final String H2_USER = Utils.getStringProperty("h2_user");
    private static final String H2_PASSWORD = Utils.getStringProperty("h2_password");
    private static Logger log = Logger.getLogger(DataFactory.class);
    private static final String CONFIG_FILE = "/database.sql";

    private final CustomerDataObject customerObject = new CustomerSupport();
    private final AccountDataObject accountObject = new AccountSupport();

    DataFactory() {
        // load h2 driver
        DbUtils.loadDriver(H2_DRIVER);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(H2_CONNECTION_URL, H2_USER, H2_PASSWORD);

    }

    public CustomerDataObject getCustomerDataObject() {
        return customerObject;
    }

    public AccountDataObject getAccountDataObject() {
        return accountObject;
    }




    @Override
    public void generateApplicationData() {
        log.info("Populating Test Customer Table and data ..... ");
        Connection connection = null;
        try {
            connection = DataFactory.getConnection();

            InputStream in = getClass().getResourceAsStream(CONFIG_FILE);

            RunScript.execute(connection, new BufferedReader(new InputStreamReader(in)));

        } catch (SQLException e) {
            log.error("generateApplicationData(): Error populating customer data: ", e);
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

}