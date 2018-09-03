package com.manh.test.util.dataloader

class LoadDataFromPropertiesFile extends DataLoader{

    String dataProvider;
    Properties props;

    public LoadDataFromPropertiesFile(String dataProvider){

        super();
        this.dataProvider = dataProvider;
        initializeProps("testdata/properties/wm/" + dataProvider + ".properties");
    }

    @Override
    public Map<String, String> loadData(){

        Map<String, String> map = new TreeMap<Object, Object>();

        Enumeration e = props.propertyNames();
        while (e.hasMoreElements()) {

            String key = (String) e.nextElement();
            map.put(key, props.getProperty(key));
        }
        return map;
    }


    private void initializeProps(String resource){

        props = new Properties();
        props.load(getClass().getClassLoader().getResourceAsStream(resource));
        //props.load(new FileInputStream(resource));
    }
}
