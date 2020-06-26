package tomTom;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class GenerateAdminKey {
	
	String SecretBody, ApiKey, adminKey, projectCreationBody, ProjectName, ProjectID;
	

	@BeforeSuite
	public void EnvSetUp() {
		ApiKey = "GepqSmAsdaxDE63WqpGNpXgbTxJRUr9O";
		RestAssured.baseURI = "https://api.tomtom.com/geofencing/1";
	}
	
	@BeforeMethod
	
	public void generateAdminKey() {
		
		
		SecretBody = "{\"secret\":\"mygeofencing\"}";
		
		Response AdminKeyGenerated = RestAssured
		.given()
		.contentType(ContentType.JSON)
		.body(SecretBody)
		.post("regenerateKey?key="+ApiKey);
		;
		
		JsonPath jsonPath = AdminKeyGenerated.jsonPath();
		//System.out.println(AdminKeyGenerated.prettyPrint());
		adminKey = jsonPath.getString("adminKey");
		System.out.println("The Admin Key is: "+adminKey);
		if (AdminKeyGenerated.statusCode()==200) {
			System.out.println("AdminKey generated successfully");
		} else {
			System.out.println("AdminKey not generated");

		}
	}
}
