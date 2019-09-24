package com.doug.moneytransferapi.service;

import com.doug.moneytransferapi.data.DataObjectFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * This class is responsible to initialize the application environment for services tests
 *
 * */

public abstract class ApplicationServiceTest {

    protected static Server server = null;
    protected static PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    protected static HttpClient client ;
    protected static DataObjectFactory dataObjectFactory = DataObjectFactory.getDataObjectFactory(DataObjectFactory.H2);
    protected ObjectMapper mapper = new ObjectMapper();
    protected URIBuilder builder = new URIBuilder().setScheme("http").setHost("localhost:8084");

    /**
     *
     *  Configurations for application during the tests executions
     *
     * */
    @BeforeClass
    public static void setup() throws Exception {
        dataObjectFactory.generateApplicationData();
        startServer();
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(100);
        client= HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setConnectionManagerShared(true)
                .build();
    }

    /**
     * Close application client after tests
     *
     * */
    @AfterClass
    public static void closeClient() throws Exception {
        //server.stop();
        HttpClientUtils.closeQuietly(client);
    }

    /**
     * Start the application before services tests be executed
     *
     * */
    private static void startServer() throws Exception {
        if (server == null) {
            server = new Server(8084);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
            servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                    CustomerService.class.getCanonicalName() + "," +
                            AccountService.class.getCanonicalName() + "," +
                            ExceptionService.class.getCanonicalName() + "," +
                            TransactionService.class.getCanonicalName());
            server.start();
        }
    }
}