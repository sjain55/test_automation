package com.manh.test.util.dataloader

import java.util.logging.Logger

class BaseTest {

    TreeMap<String, String> map = new TreeMap<String, String>();
    private Logger log;

    public Map<String, String> loadData(String locatorFile, Logger log) {

        this.log = log;
        DataLoaderFactory factory = new DataLoaderFactory(locatorFile);
        DataLoader dataLoader = factory.getInstance();

        if(dataLoader instanceof LoadDataFromPropertiesFile){

            LoadDataFromPropertiesFile locator  = new LoadDataFromPropertiesFile(locatorFile);
            map =  locator.loadData();
        } else if(dataLoader instanceof LoadDataFromExcel){

            LoadDataFromExcel locator = new LoadDataFromExcel(locatorFile);
            map = locator.loadData();
        } else if(dataLoader instanceof LoadDataFromDB){

            LoadDataFromDB locator = new LoadDataFromDB(locatorFile);
            map = locator.loadData();
        }
        ReplaceDynamicValues replaceValues = new ReplaceDynamicValues(map);
        //return map;
        return replaceValues.replaceDynamicContents();
    }

    public String fetchValue(String key){

        if(!map.containsKey(key)){
            log.severe("Key: "+key+" is not present in property");

        } else{
            return map.get(key);
        }

    }
}