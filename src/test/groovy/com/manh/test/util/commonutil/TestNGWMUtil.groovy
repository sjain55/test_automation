package com.manh.test.util.commonutil

import com.manh.test.util.dataloader.BaseTest
import com.manh.test.util.validation.WMValidationUtil
import sun.security.pkcs11.Session

import java.sql.Connection
import java.util.logging.Logger

class TestNGWMUtil {

    public static String result = null
    public static final int MIN_WAIT = 1
    public static final int AVG_WAIT = 2000
    public static final int TELNET_WAIT_FOR_SCREEN_TEXT = 40
    public static Connection dcConn = null
    public static Connection doConn = null
    public static Connection wrConn = null
    public static Connection cubingConn = null
    public static Connection dcconsolidationConn = null
    public static Connection dcInventoryConn = null
    public static Connection deviceIntegrationConn = null
    public static Connection olpnConn = null
    public static Connection lmConn = null

    // DI
    String dICntrId
    String dIRunHost
    String message
    String msgPrefix
    String MsgSuffix

    public static Connection dbConnection = null
    private Logger log
    public Properties props
    private Properties rfProps
    private Session session
    private OutputStream rfout
    private InputStream rfin
    private String actualStringFromScreen = ""
    private String previousScreen
    private Socket client
    private Properties envProps
    Connection conn = null
    WMValidationUtil wmValidationUtil
    BaseTest baseTest = new BaseTest()
// All the common methods will come here

    //we should use this constructor always for our setup
    TestNGWMUtil(String className, Logger log) {
        this.log = log
        baseTest.loadData(className, log)
        wmValidationUtil = new WMValidationUtil("validation_mapping_sheet.xlsx", this, log)
        //initialize("testdata/properties/" + className + ".properties");
        /*dcConn = getOracleDBConnections("DCAllocation")
        doConn = getOracleDBConnections("DCOrder")
        wrConn = getOracleDBConnections("WorkRelease")
        lmConn = getOracleDBConnections("lmCore")*/
        // DI
        //deviceIntegrationConn = getOracleDBConnections("DIDatabase")
        /*if(EXECUTION_TYPE.MDA.equals(exeType)){
            ta = new TestNGAdapter(baseTest.fetchValue("g_MDA_User_Name"), baseTest.fetchValue("g_MDA_Pwd"), baseTest.fetchValue("g_MDA_URL"))
        }
        else{
            ta = new TestNGAdapter(baseTest.fetchValue("g_UI_User_Name"), baseTest.fetchValue("g_UI_Pwd"), null)
            if (EXECUTION_TYPE.RF.equals(exeType) || WMValidationUtil.EXECUTION_TYPE.BOTH.equals(exeType)){
                try{
                    driver = new RFDriver(baseTest.fetchValue("g_RF_User_Name"), baseTest.fetchValue("g_RF_Pwd"), envProps, this, baseTest, log)
                } catch(Exception e){
                    log.severe("Exception caught in RFDriver : Retrying : "+e.getMessage())
                    ta.waitFor(10000)

                    driver = new RFDriver(baseTest.fetchValue("g_RF_User_Name"), baseTest.fetchValue("g_RF_Pwd"), envProps, this, baseTest, log)

                }
            }
        }*/


    }


}
