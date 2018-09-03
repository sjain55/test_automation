package com.manh.test.util.dataloader

class LoadDataFromDB extends DataLoader{

    String dataProvider;

    public LoadDataFromDB(String dataProvider){

        super();
        this.dataProvider = dataProvider;
    }

    @Override
    public Map<String, String> loadData(){

        Map<String, String> map = new TreeMap<Object, Object>();

        /*
         * logic to load data from sql
         */

        return map;
    }


}
