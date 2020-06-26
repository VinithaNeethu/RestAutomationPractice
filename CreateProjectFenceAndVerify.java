package tomTom;

import java.util.List;
import java.util.Random;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CreateProjectFenceAndVerify extends GenerateAdminKey {

	String createFence, FenceName, FenceId;
	int RandomNumber;

	Random random = new Random();

	@Test(priority=1)
	public void createProject() {

		RandomNumber = random.nextInt(100000);
		projectCreationBody = "{ \"name\": \"Project On API " + RandomNumber + "\"}";

		Response CreatProject = RestAssured.given().contentType(ContentType.JSON).body(projectCreationBody)
				.post("projects/project?key=" + ApiKey + "&adminKey=" + adminKey);

		JsonPath CreateProjectJsonPath = CreatProject.jsonPath();
		ProjectID = CreateProjectJsonPath.getString("id");
		ProjectName = CreateProjectJsonPath.getString("name");
		System.out.println("The Project Id: " + ProjectID);
		// System.out.println("Project id: "+ProjectID);
		if (CreatProject.getStatusCode() == 201) {
			System.out.println("Project created");
		} else {
			System.out.println("Project not created");

		}
	}
	
	  
	  @Test(priority=2,dependsOnMethods = {"createProject"}) 
	  
	  public void VerifyProject() 
	  { 
		  Response VerifyProject = RestAssured.given().param("key", ApiKey).get("projects");
		  JsonPath verifyJsonPath = VerifyProject.jsonPath();
		  List<String> projectsName = verifyJsonPath.get("projects.name");
		  System.out.println(VerifyProject.getStatusCode());
		  
		  for (int i = 0; i < projectsName.size(); i++) {
			  String ProjList = projectsName.get(i);
			  if (ProjList.contains(ProjectName)) {
				System.out.println("Verified that the project is created");
			} 			
		}
	  }  
		  @Test(priority = 3, dependsOnMethods = {"tomTom.CreateProjectAndVerify.createProject"})
	  
		  public void CreateFence() {
				
				//File createFence = new File("createFence.json");
				
				createFence = "{\r\n" + 
						"  \"name\": \"Fence "+RandomNumber+"\",\r\n" + 
						"  \"type\": \"Feature\",\r\n" + 
						"  \"geometry\": {\r\n" + 
						"    \"radius\": 100,\r\n" + 
						"    \"type\": \"Point\",\r\n" + 
						"    \"shapeType\": \"Circle\",\r\n" + 
						"    \"coordinates\": [10.340435, 76.280886]\r\n" + 
						"  },\r\n" + 
						"  \"properties\": {\r\n" + 
						"    \"maxSpeedKmh\": 90\r\n" + 
						"  }\r\n" + 
						"}";
				
				Response FenceRequest = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body(createFence)
				.post("projects/"+ProjectID+"/fence?key="+ApiKey+"&adminKey="+adminKey)
				;
				
				JsonPath FenceJsonPath = FenceRequest.jsonPath();
				FenceName = FenceJsonPath.get("name");
				FenceId = FenceJsonPath.get("id");
				System.out.println(FenceName);
				System.out.println(FenceRequest.statusCode());
				
				if (FenceRequest.statusCode()==201) {
						System.out.println(FenceJsonPath.prettyPrint());
					
				} 			}
		 @Test(priority = 4, dependsOnMethods = {"tomTom.CreateProjectAndVerify.CreateFence"})

	    public void GetFence() {
	    	Response FenceGet = RestAssured.given().contentType(ContentType.JSON).get("fences/"+FenceId+"?key="+ApiKey);
			JsonPath FenGetJson = FenceGet.jsonPath();
			System.out.println(FenceGet.statusCode());
			String fencID = FenGetJson.get("id");
			if (fencID.contains(FenceId)) {
				System.out.println("FenceId Verifed" +FenceId);
			}
	  
	 
}
}