package tests

import com.manh.test.util.commonutil.TestNGWMUtil
import com.manh.test.util.dataloader.BaseTest
import com.manh.test.util.logger.TestLogger
import com.manh.test.util.validation.WMValidationUtil
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
    @BeforeClass
    public void preconfig(){
        log = TestLogger.getLogger(this.getClass().getSimpleName());
        twm = new TestNGWMUtil(this.getClass().getSimpleName(), this.log)
        loader = twm.getBaseTest()
        wmValidationUtil = twm.wmValidationUtil
    }
    @Test
    public void dataValidation()
    {
        whereConditionKeyValuePairs = new TreeMap<String, String>();
        whereConditionKeyValuePairs.put("SHIPMENT_ID", loader.fetchValue("SHIPMENT_ID"))
        /*expectedKeyValuePairs = new TreeMap<String, String>()
        expectedKeyValuePairs.put("ORG_ID", loader.fetchValue("ORG_ID"))
        expectedKeyValuePairs.put("TRANSPORTATION_STATUS", loader.fetchValue("TRANSPORTATION_STATUS"))
        expectedKeyValuePairs.put("TENDER_STATUS", loader.fetchValue("TENDER_STATUS"))
        expectedKeyValuePairs.put("TOTAL_COST", loader.fetchValue("TOTAL_COST"))*/
        wmValidationUtil.validateFor("SHIPMENT_TLM_FLOW",whereConditionKeyValuePairs)

    }

}
