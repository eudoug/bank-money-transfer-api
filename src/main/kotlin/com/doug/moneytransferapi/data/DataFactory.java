package com.doug.moneytransferapi.data;

import com.doug.moneytransferapi.data.support.AccountSupport;
import com.doug.moneytransferapi.data.support.CustomerSupport;
import com.doug.moneytransferapi.utils.Utils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataFactory extends DataObjectFactory {
    private static final String h2_driver = Utils.getStringProperty("h2_driver");
    private static final String h2_connection_url = Utils.getStringProperty("h2_connection_url");
    private static final String h2_user = Utils.getStringProperty("h2_user");
    private static final String h2_password = Utils.getStringProperty("h2_password");
    private static Logger log = Logger.getLogger(DataFactory.class);

    private final CustomerDataObject customerObject = new CustomerSupport();
    private final AccountDataObject accountObject = new AccountSupport();

    DataFactory() {
        // init: load driver
        DbUtils.loadDriver(h2_driver);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(h2_connection_url, h2_user, h2_password);

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
        Connection conn = null;
        try {
            conn = DataFactory.getConnection();
            RunScript.execute(conn, new FileReader("src/main/resources/database.sql"));
        } catch (SQLException e) {
            log.error("generateApplicationData(): Error populating customer data: ", e);
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            log.error("generateApplicationData(): Error finding test script file ", e);
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

}