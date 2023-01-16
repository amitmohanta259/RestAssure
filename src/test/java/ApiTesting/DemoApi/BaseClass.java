package ApiTesting.DemoApi;

import org.testng.annotations.BeforeTest;

import com.beust.jcommander.Parameter;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseClass {
	//public RequestSpecBuilder requestBuilder;
	public ResponseSpecBuilder responseBuilder;
	@BeforeTest
	public static RequestSpecBuilder  setConfig() {
		RequestSpecBuilder requestBuilder;
		return requestBuilder = new RequestSpecBuilder().setBaseUri("https://reqres.in").setContentType(ContentType.JSON);
		
		//responseBuilder = new ResponseSpecBuilder();
	}
	
}	
