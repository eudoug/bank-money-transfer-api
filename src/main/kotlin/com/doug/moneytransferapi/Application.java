package com.doug.moneytransferapi;

import com.doug.moneytransferapi.data.DataObjectFactory;
import com.doug.moneytransferapi.model.CustomerTransaction;
import com.doug.moneytransferapi.service.AccountService;
import com.doug.moneytransferapi.service.CustomerService;
import com.doug.moneytransferapi.service.ExceptionService;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.servlet.ServletContainer;

public class Application {

    private static Logger log = Logger.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        // Initialize H2 database with demo data
        log.info("Initialize demo .....");

        DataObjectFactory dataFactory = DataObjectFactory.getDataObjectFactory(DataObjectFactory.H2);
        dataFactory.generateApplicationData();

        log.info("Initialisation Complete....");
        // Host service on jetty
        startService();
    }

    private static void startService() throws Exception {
        Server server = new Server(8081);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ServletHandler handler = new ServletHandler();

        FilterHolder filter = new FilterHolder();
        filter.setInitParameter("allowedOrigins", "http://localhost:8081,https://editor.swagger.io/");
        filter.setInitParameter("allowedMethods", "POST,GET,OPTIONS,PUT,DELETE,HEAD");
        filter.setInitParameter("allowedHeaders", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        filter.setInitParameter("preflightMaxAge", "728000");
        filter.setInitParameter("allowCredentials", "true");
        CrossOriginFilter corsFilter = new CrossOriginFilter();
        filter.setFilter(corsFilter);

        handler.addFilter(filter);

        context.setContextPath("/");
        server.setHandler(context);

        ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                CustomerService.class.getCanonicalName() + "," + AccountService.class.getCanonicalName() + ","
                        + ExceptionService.class.getCanonicalName() + ","
                        + CustomerTransaction.class.getCanonicalName());
        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }

}