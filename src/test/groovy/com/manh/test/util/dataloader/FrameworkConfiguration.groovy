package com.manh.test.util.dataloader

class FrameworkConfiguration {

    private static FrameworkConfiguration _instance;

    private LOCATORCONSTANTS locator;

    public synchronized static FrameworkConfiguration getInstance() {
        if (_instance == null) {
            _instance = new FrameworkConfiguration();
        }
        return _instance;
    }

    public LOCATORCONSTANTS getDataProvider() {

        return locator;
    }

    public void setDataProvider(LOCATORCONSTANTS locator){

        this.locator = locator;
    }


}
