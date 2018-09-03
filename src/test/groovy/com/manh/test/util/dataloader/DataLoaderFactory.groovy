package com.manh.test.util.dataloader

class DataLoaderFactory {

    String dataProvider;

    private DataLoaderFactory(String dataProvider) {

        //get hold of env.props and determine which locator to use
        this.dataProvider = dataProvider;
    }

    public DataLoader getInstance() {

        FrameworkConfiguration frameworkConfig = FrameworkConfiguration.getInstance();
        frameworkConfig.setDataProvider(LOCATORCONSTANTS.USE_PROPERTY_FILE);

        if(frameworkConfig.getDataProvider() == LOCATORCONSTANTS.USE_PROPERTY_FILE) {

            return new LoadDataFromPropertiesFile(dataProvider);
        } else if(frameworkConfig.getDataProvider() == LOCATORCONSTANTS.USE_EXCEL) {

            return new LoadDataFromExcel(dataProvider);
        } else if(frameworkConfig.getDataProvider() == LOCATORCONSTANTS.USE_DB) {

            return new LoadDataFromDB(dataProvider);
        }

        return null;
    }
}
