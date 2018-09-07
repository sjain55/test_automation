package com.manh.test.util.commonutil
/**
 * Created by Abhishek Nagpurkar and Parmveer Singh Nijher
 * Modified the Existing Validation Framework TestNGWMUtil file and Integrated with our Automation framework
 * Date: 4 spetember 2018
 * This class will create the DB connection, Execute the Query and return the Result list of the query
 */
import com.manh.test.util.dataloader.BaseTest
import com.manh.test.util.validation.WMValidationUtil
import db.DbConnectionFactory
import groovy.sql.Sql

import java.sql.Connection
import java.util.logging.Logger

class TestNGWMUtil {

    public static String result = null
    Sql shipment
    Sql tender
    public static def tracking
    public static def order
    private Logger log
    Connection conn = null
    WMValidationUtil wmValidationUtil
    BaseTest baseTest = new BaseTest()
    DbConnectionFactory dbConnectionFactory = new DbConnectionFactory()
// All the common methods will come here

    //we should use this constructor always for our setup
    TestNGWMUtil(String className, Logger log) {
        this.log = log
        baseTest.loadData(className, log)
        wmValidationUtil = new WMValidationUtil("validation_mapping_sheet.xlsx", this, log)
        //initialize("testdata/properties/" + className + ".properties");
        shipment = dbConnectionFactory.shipmentDbProperties()
        tender = dbConnectionFactory.tenderDbProperties()
    }

    public def getMultipleFieldsFromDB(String querytoExecute) {
        def resultSet = null;
        log.info(querytoExecute);
        for (int i = 0; i < 40; i++) {
            resultSet = executeSelectQuery(querytoExecute)
            //resultSet = querytoExecute.executeSelectQuery()
            log.info("ResultSet =" + resultSet)
            if (resultSet.size() == 0) {
                log.info("Waiting for 1500 from getMultipleFieldsFromDB")
                //ta.waitFor(4000)
                resultSet = null
            } else {
                break
            }
        }
        return resultSet
    }

    public def getMultipleFieldsFromDB(String querytoExecute, String dbName) {
        def resultSet = null
        log.info(querytoExecute)
        for (int i = 0; i < 40; i++) {
            if (dbName.equalsIgnoreCase("SCS_SHIPMENT")) {
                resultSet = executeSelectQuery(querytoExecute, shipment);
            } else {
                resultSet = executeSelectQuery(querytoExecute)
            }

            log.info("ResultSet =" + resultSet);
            if (resultSet.size() == 0) {
                log.info("Waiting for 1500 from getMultipleFieldsFromDB")
                //ta.waitFor(4000);
                resultSet = null;
            } else {
                break;
            }
        }
        return resultSet;
    }

    List<Map<String, ?>> executeSelectQuery(String queryStr, Sql conn) {
        return executeSelectQuery(queryStr, null, conn)
    }

    /**
     * Executes a select query and returns the result as a list-of-map, with map having columnName as key and columnValue as value.
     * Connection is an optional parameter. If not supplied, a new connection will be used.
     * @return
     */
    List<Map<String, ?>> executeSelectQuery(String queryStr, List params, Sql conn) {
        boolean shouldCloseConn = false;
        try {
            println("SQLExecuteSupport::executeSelectQuery ${queryStr}")
            def rows
            if (params) {
                rows = conn.rows(queryStr, params)
                shouldCloseConn = true
            } else {
                rows = conn.rows(queryStr)
                shouldCloseConn = true
            }

            return rows
        } finally {
            if (shouldCloseConn) {
                log.info("Closing the SQL connection....")
                conn.close();
            }
        }
    }


}
