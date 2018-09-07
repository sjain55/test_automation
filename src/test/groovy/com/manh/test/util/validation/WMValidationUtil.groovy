package com.manh.test.util.validation
/**
 * Created by Parmveer Singh Nijher and Abhishek Nagpurkar
 * Modified the Existing Validation Framework WMValidationUtil file and Integrated with our Automation framework
 * Date: 4 spetember 2018
 * This class contains validation part, it stores the expected data and actual data and Assert the same and print the logger in the console whether the assertion is passed or not.
 */
import com.manh.test.util.commonutil.TestNGWMUtil

import java.util.logging.Logger

class WMValidationUtil {
    public TestNGWMUtil twm;
    ExcelRead excelRead = new ExcelRead();
    private Logger log;

    /*constructor*/

    WMValidationUtil(String resource, TestNGWMUtil twm, Logger log) {
        this.twm = twm;
        this.log = log;
        excelRead.readExcelData(resource);
    }

    //validate method
    //This method will be called by the automation test case to validate a particular transaction.
    public boolean validateFor(String queryKey, TreeMap whereConditionKeyValuePairs, TreeMap expectedKeyValuePairs) {
        int noOfTimesToRevalidate;
        int validationResultCounter = 0;
        int validationResult = 0;
        boolean revalidateFromDB = true;

        TreeMap<String, String> actualQueryResultMap = new TreeMap<String, String>();
        List<Map> queryResultList = new ArrayList<Map>();


        List<TreeMap<String, String>> queryWithDB = this.getQueries(queryKey);
        TreeMap<String, String> validationQueries = queryWithDB.get(0);
        TreeMap<String, String> dbNameMap = queryWithDB.get(1);
        TreeSet<String> expectedQueryResultKeySet = expectedKeyValuePairs.keySet();

        TreeSet<String> querySet = validationQueries.keySet();

        for (String eachValidationQuery : querySet) {
            String dbName = ""
            String fullyFormedQuery = this.replaceWithWhereConditionsUtil(eachValidationQuery, whereConditionKeyValuePairs);
            log.info("Query : " + fullyFormedQuery);

            //check for mandatory columns presence in actual formed query
            if (this.checkMandatoryColumnsInQuery(validationQueries.get(eachValidationQuery), fullyFormedQuery)) {
                log.info("All mandatory validation columns can be fetched by above query.. Let's proceed..");
            } else {
                log.severe("Query will not be able to fetch all mandatory validation columns as specified !! Check the above query in validation excel..");
                return false;
            }

            //check for mandatory columns key-value pair presence in expectedKeyValuePairs sent from the script
            if (this.checkMandatoryKeyInExpectedKeyList(validationQueries.get(eachValidationQuery), expectedKeyValuePairs)) {
                log.info("Script has provided expected column-value pair for all mandatory validation columns for above query.. Let's proceed..");
            } else {
                log.severe("Above mandatory validation columns need to be verified against above query.. Check the script and provide expected key-value pairs correctly..");
                return false;
            }

            revalidateFromDB = true;
            noOfTimesToRevalidate = 1;
            validationResult = 0;

            //get DB name for query to execute
            dbName = dbNameMap.get(eachValidationQuery);

            //fire the query and get the actual results and compare
            while (revalidateFromDB && noOfTimesToRevalidate > 0) {
                if (dbName == null) {
                    log.info("ORACLE DB");
                    //
                    queryResultList = twm.getMultipleFieldsFromDB(fullyFormedQuery);
                    //Thread.sleep(2000);
                } else {
                    log.info("SQL DB NAME: " + dbName);
                    queryResultList = twm.getMultipleFieldsFromDB(fullyFormedQuery, dbName);
                    //Thread.sleep(2000);
                }
                if (queryResultList == null) {
                    log.severe("NULL query result... Check the values for where condition");
                    validationResult = 1;
                    break;
                } else {
                    actualQueryResultMap = queryResultList.get(0);
                    validationResult = this.compareActualWithExpectedUtil(actualQueryResultMap, expectedKeyValuePairs);
                }
                noOfTimesToRevalidate = noOfTimesToRevalidate - 1;
                if (validationResult == 0) {
                    revalidateFromDB = false;
                }
                //Thread.sleep(1000);
                Thread.sleep(2000);
            }
            validationResultCounter = validationResultCounter + validationResult;
        }

        if (validationResultCounter > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validateFor(String queryKey, TreeMap whereConditionKeyValuePairs, TreeMap expectedKeyValuePairs, int noOfTimesToRevalidate) {

        int validationResultCounter = 0;
        int validationResult = 0;
        boolean revalidateFromDB = true;
        TreeMap<String, String> actualQueryResultMap = new TreeMap<String, String>();
        List<Map> queryResultList = new ArrayList<Map>();

        List<TreeMap<String, String>> queryWithDB = this.getQueries(queryKey);
        TreeMap<String, String> validationQueries = queryWithDB.get(0);
        TreeMap<String, String> dbNameMap = queryWithDB.get(1);
        TreeSet<String> querySet = validationQueries.keySet();

        for (String eachValidationQuery : querySet) {
            String dbName = "";
            String fullyFormedQuery = this.replaceWithWhereConditionsUtil(eachValidationQuery, whereConditionKeyValuePairs);
            log.info(fullyFormedQuery);
            revalidateFromDB = true;
            validationResult = 0;

            dbName = dbNameMap.get(eachValidationQuery);

            while (revalidateFromDB && noOfTimesToRevalidate > 0) {
                if (dbName == null) {
                    queryResultList = twm.getMultipleFieldsFromDB(fullyFormedQuery);
                    //Thread.sleep(2000);
                } else {
                    //log.info("Inside SQL query execution :"+dbName);
                    queryResultList = twm.getMultipleFieldsFromDB(fullyFormedQuery, dbName);
                    //Thread.sleep(2000);
                }

                if (queryResultList == null) {
                    log.severe("NULL query result... Check the values for where condition");
                    validationResult = 1;
                    break;
                } else {
                    actualQueryResultMap = queryResultList.get(0);
                    validationResult = this.compareActualWithExpectedUtil(validationQueries.get(eachValidationQuery), actualQueryResultMap, expectedKeyValuePairs);
                }
                noOfTimesToRevalidate = noOfTimesToRevalidate - 1;
                if (validationResult == 0) {
                    revalidateFromDB = false;
                }
                //Thread.sleep(1000);
                Thread.sleep(4000);
            }
            validationResultCounter = validationResultCounter + validationResult;
        }

        if (validationResultCounter > 0) {
            return false;
        } else {
            return true;
        }
    }

    // Get the list of queries from XL sheet as per user entered Key with respective DB name
    private List<Map> getQueries(String queryKey) {

        List<TreeMap<String, String>> validationQueriesWithDB = new ArrayList();
        TreeMap<String, String> validationQueries = new TreeMap<String, String>();
        TreeMap<String, String> dbName = new TreeMap<String, String>();
        String subKey = "";
        if (excelRead.masterKeyMap.containsKey(queryKey)) {
            for (String eachSubKey : excelRead.masterKeyMap.get(queryKey)) {
                validationQueries.put(excelRead.queryKeyMap.get(eachSubKey.substring(0, eachSubKey.indexOf(":"))), eachSubKey.substring(eachSubKey.indexOf(":") + 1));
                subKey = eachSubKey.substring(0, eachSubKey.indexOf(":"));
                if (subKey.contains("SCS_SHIPMENT")) {

                    dbName.put(excelRead.queryKeyMap.get(eachSubKey.substring(0, eachSubKey.indexOf(":"))), "SCS_SHIPMENT")
                }
            }
        } else {
            validationQueries.put(excelRead.queryKeyMap.get(queryKey), queryKey);
        }

        validationQueriesWithDB.add(validationQueries);
        validationQueriesWithDB.add(dbName);
        return validationQueriesWithDB;
    }

    //create final queries after replacing user given where condition values
    private String replaceWithWhereConditionsUtil(String query, TreeMap whereConditionKeyValuePairs) {
        TreeSet<String> whereConditionKeys = whereConditionKeyValuePairs.keySet();
        for (String eachKey : whereConditionKeys) {
            if (query.contains("?" + eachKey + "?")) {
                query = query.replace("?" + eachKey + "?", whereConditionKeyValuePairs.get(eachKey));
            }
        }
        return query
    }

    //check for mandatory columns presence in actual formed query
    private boolean checkMandatoryColumnsInQuery(String mandatoryKeys, String fullyFormedQuery) {

        int checkCounter = 0;
        ArrayList<String> querySubStringList = new ArrayList<String>();

        for (String eachSubString : Arrays.asList(fullyFormedQuery.split(" "))) {
            for (String eachSubSubSring : Arrays.asList(eachSubString.split(","))) {
                querySubStringList.add(eachSubSubSring.trim());
            }
        }

        for (String mandatoryKey : mandatoryKeys.split(",")) {
            if (querySubStringList.contains((mandatoryKey.trim()))) {
            } else {
                checkCounter = checkCounter + 1;
                log.severe("Expected mandatory column : " + mandatoryKey.trim() + " not found in fully formed query..");
            }
        }

        if (checkCounter > 0) {
            return false;
        } else {
            return true;
        }
    }

    //check for mandatory columns presence in key list of expectedKeyValuePairs sent from the script
    private boolean checkMandatoryKeyInExpectedKeyList(String mandatoryKeys, TreeMap expectedKeyValuePairs) {
        int checkCounter = 0;

        for (String mandatoryKey : mandatoryKeys.split(",")) {
            if (expectedKeyValuePairs.containsKey(mandatoryKey.trim())) {
            } else {
                checkCounter = checkCounter + 1;
                log.severe("Expected Key-value has not been provided for mandatory column : " + mandatoryKey.trim());
            }
        }

        if (checkCounter > 0) {
            return false;
        } else {
            return true;
        }
    }

    //compare the actual DB values with user provided expected values
    private int compareActualWithExpectedUtil(TreeMap actualQueryResultMap, TreeMap expectedQueryResultMap) {
        int validationResult = 0;
        String actual = "";
        String expected = "";

        TreeSet<String> expectedQueryResultKeySet = expectedQueryResultMap.keySet();

        for (String eachExpectedKey : expectedQueryResultKeySet) {
            if (actualQueryResultMap.containsKey(eachExpectedKey)) {
                actual = actualQueryResultMap.get(eachExpectedKey).toString();
                expected = expectedQueryResultMap.get(eachExpectedKey).toString();

                if (actual.equalsIgnoreCase(expected)) {
                    // if expected and actual value directly match as a string
                    log.info("***********************************************************************");
                    log.info("Comparison Column Name : " + (eachExpectedKey));
                    log.info("Expected value : " + expectedQueryResultMap.get(eachExpectedKey));
                    log.info("Actual value : " + actualQueryResultMap.get(eachExpectedKey));
                    log.info("Expected value matched with actual value");
                    log.info("***********************************************************************");
                } else {
                    // if expected and actual do not match as a string
                    if (isNumeric(actual) && isNumeric(expected)) {
                        // if actual and expected values are numeric, we compare them as double which itself handles any trailing zeros after the decimal point
                        double actualDouble = Double.parseDouble(actual);
                        double expectedDouble = Double.parseDouble(expected);
                        // any numeric value should first be converted to double and then compared.
                        if (actualDouble == expectedDouble) {
                            log.info("***********************************************************************");
                            log.info("Comparison Column Name : " + (eachExpectedKey));
                            log.info("Expected value : " + expectedQueryResultMap.get(eachExpectedKey));
                            log.info("Actual value : " + actualQueryResultMap.get(eachExpectedKey));
                            log.info("Expected value matched with actual value");
                            log.info("***********************************************************************");
                        } else {
                            //if both expected and actual are numeric and still do not match after comparing them as double
                            validationResult = 1;
                            log.severe("***********************************************************************");
                            log.severe("Comparison Column Name : " + (eachExpectedKey));
                            log.severe("Expected value: " + expectedQueryResultMap.get(eachExpectedKey));
                            log.severe("Actual value: " + actualQueryResultMap.get(eachExpectedKey));
                            log.severe("Expected value did not match with actual value");
                            log.severe("***********************************************************************");

                        }
                    } else {
                        //this else comes into picture when one of expected and actual values is numeric and the other is non-numeric or both are non numeric and non matching
                        validationResult = 1;
                        log.severe("***********************************************************************");
                        log.severe("Comparison Column Name : " + (eachExpectedKey));
                        log.severe("Expected value: " + expectedQueryResultMap.get(eachExpectedKey));
                        log.severe("Actual value: " + actualQueryResultMap.get(eachExpectedKey));
                        log.severe("Expected value did not match with actual value");
                        log.severe("***********************************************************************");
                    }
                }
            }
        }
        return validationResult;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


}
