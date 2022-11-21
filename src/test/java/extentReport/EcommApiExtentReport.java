package extentReport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class EcommApiExtentReport {
	
	static ExtentReports extent;

	public static ExtentReports getEcommApiReport() {
		
		String path = System.getProperty("user.dir") + "\\reports\\EcommReport.html";

		ExtentSparkReporter spark = new ExtentSparkReporter(path);
		spark.config().setReportName("E-comm Api Automation");
		spark.config().setDocumentTitle("E-comm Api Test Result");
		spark.config().setTheme(Theme.DARK);

		extent = new ExtentReports();
		extent.attachReporter(spark);
		extent.setSystemInfo("QA", "Tanveer");
		return extent;

	}


}
