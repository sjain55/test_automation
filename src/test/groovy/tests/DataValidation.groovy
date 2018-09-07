package tests
/**
 * Created by Parmveer Singh Nijher and Abhishek Nagpurkar
 * Created a test method inside the class to test the DB validation framework.
 * Date: 4 spetember 2018
 * This class is a test class to perform the DB validation of existing data in DB.
 * Info: Tihs class is a dummy class to verify the DB validation framework.
 */
import com.manh.test.util.commonutil.TestNGWMUtil
import com.manh.test.util.dataloader.BaseTest
import com.manh.test.util.logger.TestLogger
import com.manh.test.util.validation.WMValidationUtil
import org.testng.Assert
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import java.util.logging.Logger

class DataValidation {
    public TestNGWMUtil twm
    private Logger log
    public WMValidationUtil wmValidationUtil
    TreeMap<String, String> whereConditionKeyValuePairs
    TreeMap<String, String> expectedKeyValuePairs
    BaseTest loader

    /*Creating the instance of logger, TestNGWMUtil, BaseTest, wmValidationUtil classes. */

    @BeforeClass
    public void preconfigure() {
        log = TestLogger.getLogger(this.getClass().getSimpleName());
        twm = new TestNGWMUtil(this.getClass().getSimpleName(), this.log)
        /*Load the properties file indicated by the class name*/
        loader = twm.getBaseTest()
        wmValidationUtil = twm.wmValidationUtil
    }

    /*Test to verify whether the Shipment is created having same data in DB or not.*/

    @Test
    public void dataValidation() {
        whereConditionKeyValuePairs = new TreeMap<String, String>();
        whereConditionKeyValuePairs.put("SHIPMENT_ID", loader.fetchValue("SHIPMENT_ID"))
        expectedKeyValuePairs = new TreeMap<String, String>()
        expectedKeyValuePairs.put("ORG_ID", loader.fetchValue("ORG_ID"))
        expectedKeyValuePairs.put("TRANSPORTATION_STATUS", loader.fetchValue("TRANSPORTATION_STATUS"))
        expectedKeyValuePairs.put("TENDER_STATUS", loader.fetchValue("TENDER_STATUS"))
        expectedKeyValuePairs.put("TOTAL_COST", loader.fetchValue("TOTAL_COST"))
        Assert.assertEquals(wmValidationUtil.validateFor("SHIPMENT_TLM_FLOW", whereConditionKeyValuePairs, expectedKeyValuePairs), true)

    }

}
