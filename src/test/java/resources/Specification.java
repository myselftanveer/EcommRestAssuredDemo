package resources;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specification {

	public static RequestSpecification req;
	public static ResponseSpecification res;
	
	
	public static PrintStream printLog() throws FileNotFoundException {
		PrintStream ps = new PrintStream(new FileOutputStream("./reports/allLogs"));
		return ps;
	}
	public static RequestSpecification request() throws FileNotFoundException {

		if(req==null) {
			req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
					.addFilter(RequestLoggingFilter.logRequestTo(Specification.printLog()))
					.build();
			return req;	
		}
		return req;
		
	}

	public static ResponseSpecification response() {

		res = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		return res;
	}

}
