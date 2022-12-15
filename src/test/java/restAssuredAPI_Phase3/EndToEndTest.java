package restAssuredAPI_Phase3;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndToEndTest {

	JsonPath jpath;
	Response response;
	Map<String,Object> MapObj;
	String baseURI = "http://localhost:3000";
	int EmpID;

	public Response GetAllEmp() {
		RestAssured.baseURI = this.baseURI;
		RequestSpecification request = RestAssured.given();
		response = request.get("employees");
		return response;
	}

	public Response GetSingleEmp(int empId)	{
		RestAssured.baseURI = this.baseURI;
		RequestSpecification request = RestAssured.given();
		response=request.get("employees/"+empId);

		return response ;

	}

	public Response CreateEmp(String givenName, String givenSal){
		RestAssured.baseURI = this.baseURI;
		RequestSpecification request = RestAssured.given();
		MapObj = new HashMap<String,Object>();

		MapObj.put("name", givenName);
		MapObj.put("salary",givenSal);
		response = request.contentType(ContentType.JSON).accept(ContentType.JSON).body(MapObj).post("employees/create");
		return response;

	}

	public Response UpdateEmp() {
		RestAssured.baseURI = this.baseURI;
		RequestSpecification request = RestAssured.given();
		MapObj = new HashMap<String,Object>();

		MapObj.put("name", "Smith");
		response = request.contentType(ContentType.JSON).accept(ContentType.JSON).body(MapObj).patch("employees/"+EmpID);
		return response;
	}

    public Response DeleteEmp() {
    	RestAssured.baseURI = this.baseURI;
		RequestSpecification request = RestAssured.given();
    	
		response = request.delete("employees/"+EmpID);
		return response;
    	
    }

	@Test
	public void E2ETest() {


		//To Get All Employees
		response = GetAllEmp();
		Assert.assertEquals(200, response.getStatusCode());
		System.out.println(response.getBody().asString());	

		//Create an employee
		response= CreateEmp("John","8000");
		jpath = response.jsonPath();
		EmpID = jpath.get("id");
		Assert.assertEquals(201, response.getStatusCode());

		//Get single employee
		response = GetSingleEmp(EmpID);
		Assert.assertEquals(200, response.getStatusCode());
		jpath = response.jsonPath();
		String names = jpath.get("name");
		System.out.println("The name is "+names);
		Assert.assertEquals(names, "John");

		//Update the employee
		response = UpdateEmp();
		Assert.assertEquals(200, response.getStatusCode());

		//Get single employee
		response = GetSingleEmp(EmpID);
		Assert.assertEquals(200, response.getStatusCode());
		jpath = response.jsonPath();
		String names1 = jpath.get("name");
		System.out.println("The name is "+names1);
		Assert.assertEquals(names1, "Smith");

        //Delete the employee
		response = DeleteEmp();
		Assert.assertEquals(200, response.getStatusCode());
		
		//Get single employee
		response = GetSingleEmp(EmpID);
		Assert.assertEquals(404, response.getStatusCode());
		
		//Get All Employee
		response = GetAllEmp();
		String responseBody = response.getBody().asString();
		String str= String.valueOf(EmpID);
		Assert.assertFalse(responseBody.contains(str));
        System.out.println(responseBody);
	}

	







}
