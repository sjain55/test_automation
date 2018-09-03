package com.manh.test.util.dataloader

class LoadDataFromExcel extends DataLoader {

    String dataProvider;

    public LoadDataFromExcel(String dataProvider){

        super();
        this.dataProvider = dataProvider;
    }

    @Override
    public Map<String, String> loadData(){

        Map<String, String> map = new TreeMap<Object, Object>();

        /*
         * logic to load data from excel sheet
         */

        return map;
    }
}
