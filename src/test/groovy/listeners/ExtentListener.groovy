package listeners

import java.io.File
import java.text.SimpleDateFormat
import org.testng.ITestContext
import org.testng.ITestListener
import org.testng.ITestResult
import com.relevantcodes.extentreports.ExtentReports
import com.relevantcodes.extentreports.ExtentTest
import com.relevantcodes.extentreports.LogStatus


class ExtentListener implements ITestListener{

    ExtentReports reports
    ExtentTest test

    public void onTestStart(ITestResult result) {
        System.out.println("on test start")
        test = reports.startTest(result.getMethod().getDescription())
        test.log(LogStatus.INFO, result.getMethod().getMethodName() + "test is started")
    }
    public void onTestSuccess(ITestResult result) {
        System.out.println("on test success")
        test.log(LogStatus.PASS, result.getMethod().getMethodName() + "test is passed")
    }
    public void onTestFailure(ITestResult result) {
        System.out.println("on test failure")
        test.log(LogStatus.FAIL, result.getThrowable())
    }
    public void onTestSkipped(ITestResult result) {
        System.out.println("on test skipped")
        test.log(LogStatus.SKIP, result.getMethod().getMethodName() + "test is skipped")
    }
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("on test sucess within percentage")
    }
    public void onStart(ITestContext context) {
        System.out.println("on start")
        reports = new ExtentReports("TLM_AUTOMATION_"+ new SimpleDateFormat("yyyy-MM-dd hh-mm-ss-ms").format(new Date())+".html")
    }
    public void onFinish(ITestContext context) {
        System.out.println("on finish")
        reports.endTest(test)
        reports.flush()
    }


}
