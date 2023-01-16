package ApiTesting.DemoApi;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.createUser.CreateUser;
import pojo.createUser.GetUserResponse;
import pojo.single.SingleUser;
import pojo.unknown.Unknown;
import pojo.updateUser.GetUpatedResponse;
import pojo.updateUser.UpdateUser;

import static io.restassured.RestAssured.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.testng.annotations.Test;

public class Product extends BaseClass{
	int userId;
	@Test(priority = 1)
	public void getUser() throws FileNotFoundException {
		//Request Specification
		PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));
		RequestSpecification req = BaseClass.setConfig().addQueryParam("page", 1).build();
		RequestSpecification request  = given().spec(req);
		
		//Response
		Response response = request
				.when().get("/api/users")
				.then().extract().response();
		
		System.out.println(response.asPrettyString());	
		
	}
	
	@Test(priority = 2)
	public void getSingleUser() {
		RequestSpecification reqSpec = BaseClass.setConfig().addPathParam("id", 3).build();
		RequestSpecification req= given().spec(reqSpec);
		
		//ResponseSpecification resspec = responseBuilder.build();
		
		Response res = req.log().all().expect().defaultParser(defaultParser.JSON)
				.when().get("/api/users/{id}")
				.then().extract().response();
				
		//Check the response body
		System.out.println(res.getStatusCode());
		if(res.getStatusCode() == 200) {
			SingleUser user = res.as(SingleUser.class);
			System.out.println(user.getData().getLast_name());
		}
		if(res.statusCode() == 404) {
			System.out.println("No User Found");
		}
		
	}
	
	@Test(priority = 3)
	public void UserList() {
		RequestSpecification specification = BaseClass.setConfig().build();
		RequestSpecification req = given().spec(specification);
		
		Response res = req.log().all().expect().defaultParser(defaultParser.JSON)
				.when().get("/api/unknown")
				.then().extract().response();
		System.out.println(res.asPrettyString());
		System.out.println(res.as(Unknown.class));
		
	}
	
	@Test(priority = 4)
	public void singleUser() {
		RequestSpecification userSpec = BaseClass.setConfig().addPathParams("id", 3).build();
		RequestSpecification req = given().spec(userSpec);
		
		Response res  = req.log().all().expect().defaultParser(defaultParser.JSON)
				.when().get("/api/unknown/{id}")
				.then().extract().response();
		if(res.statusCode() == 200) {
			System.out.println(res.asPrettyString());
		}
		if(res.statusCode() == 404) {
			System.out.println("No User Found");
		}
	}
	
	@Test(priority = 5)
	public void creatUser() {
		CreateUser user = new CreateUser();
		user.setName("Rushikesh");
		user.setJob("QA Lead");
		
		RequestSpecification createSpec = BaseClass.setConfig().setBody(user).build();
		RequestSpecification req = given().spec(createSpec);
		
		Response res =  req.log().all()
				.when().post("/api/users")
				.then().extract().response();
		
		System.out.println(res.asPrettyString());
		GetUserResponse responseData = res.as(GetUserResponse.class);
		userId = responseData.getId();
		System.out.println(userId);
		
	}
	
	@Test(priority = 6)
	public void updateUser() {
		UpdateUser update = new UpdateUser();
		update.setName("Mukesh");
		update.setJob("Team Lead");
		RequestSpecification updateSpec = BaseClass.setConfig().addPathParams("id",userId).setBody(update).build();
		RequestSpecification req = given().spec(updateSpec);
		
		Response res = req.log().all()
				.when().post("/api/users/{id}")
				.then().extract().response();
		
		System.out.println(res.asPrettyString());
		
		GetUpatedResponse responseData =  res.as(GetUpatedResponse.class);
		System.out.println(responseData.getName());
	}
	
	@Test(priority = 7)
	public void deleteuser() {
		RequestSpecification deleteSpecs =  BaseClass.setConfig().addPathParam("id",userId).build();
		RequestSpecification req = given().spec(deleteSpecs);
		
		Response res = req.log().all()
				.when().delete("/api/users/{id}")
				.then().assertThat().statusCode(204).extract().response();
		
		System.out.println(res.statusCode());
		
	}
	
}
