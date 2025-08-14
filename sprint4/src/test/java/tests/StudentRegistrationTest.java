package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.Constants;
import utils.TokenManager;

public class StudentRegistrationTest {

    // ---------- POSITIVE TEST CASES ----------
    @Test(dependsOnMethods = {"tests.AuthTest.getAccessToken"}, description = "Register a new student with valid data")
    public void registerNewStudent() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        String requestBody = "{\"studentName\": \"John Doe\", \"mobileNumber\": \"9876543210\", \"emailId\": \"johndoe@example.com\", \"cgpa\": 8.5, \"department\": \"Computer Science\", \"backlogs\": 0}";

        Response res = RestAssured
            .given()
            .header("Authorization", "Bearer " + TokenManager.getAccessToken())
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.REGISTER_STUDENT_ENDPOINT)
            .then()
            .statusCode(200)
            .extract().response();

        System.out.println("Register new student response: " + res.asPrettyString());
    }

    @Test(dependsOnMethods = {"tests.AuthTest.getAccessToken"}, description = "Register a student with boundary CGPA value 10.0")
    public void registerStudentWithMaxCGPA() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        String requestBody = "{\"studentName\": \"Topper Student\", \"mobileNumber\": \"9998887776\", \"emailId\": \"topper@example.com\", \"cgpa\": 10.0, \"department\": \"Mathematics\", \"backlogs\": 0}";

        RestAssured
            .given()
            .header("Authorization", "Bearer " + TokenManager.getAccessToken())
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.REGISTER_STUDENT_ENDPOINT)
            .then()
            .statusCode(200);
    }

    @Test(dependsOnMethods = {"tests.AuthTest.getAccessToken"}, description = "Register a student with minimum CGPA value 0.0")
    public void registerStudentWithMinCGPA() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        String requestBody = "{\"studentName\": \"Low CGPA Student\", \"mobileNumber\": \"9998887775\", \"emailId\": \"lowcgpa@example.com\", \"cgpa\": 0.0, \"department\": \"History\", \"backlogs\": 5}";

        RestAssured
            .given()
            .header("Authorization", "Bearer " + TokenManager.getAccessToken())
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.REGISTER_STUDENT_ENDPOINT)
            .then()
            .statusCode(200);
    }

    @Test(dependsOnMethods = {"tests.AuthTest.getAccessToken"}, description = "Register a student with minimal required fields")
    public void registerStudentWithMinimalFields() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        // Assuming department/backlogs are optional
        String requestBody = "{\"studentName\": \"Minimal User\", \"mobileNumber\": \"9876500000\", \"emailId\": \"minimal@example.com\", \"cgpa\": 7.0}";

        RestAssured
            .given()
            .header("Authorization", "Bearer " + TokenManager.getAccessToken())
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.REGISTER_STUDENT_ENDPOINT)
            .then()
            .statusCode(200);
    }

    // ---------- NEGATIVE TEST CASES ----------
    @Test(dependsOnMethods = {"tests.AuthTest.getAccessToken"}, description = "Attempt to register a student with an invalid email format")
    public void registerStudentWithInvalidEmail() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        String requestBody = "{\"studentName\": \"Jane Doe\", \"mobileNumber\": \"9876543211\", \"emailId\": \"jane_example.com\", \"cgpa\": 7.2, \"department\": \"Electrical\", \"backlogs\": 1}";

        Response res = RestAssured
            .given()
            .header("Authorization", "Bearer " + TokenManager.getAccessToken())
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.REGISTER_STUDENT_ENDPOINT)
            .then()
            .statusCode(400)
            .extract().response();

        System.out.println("Invalid email response: " + res.asPrettyString());
    }
    
    @Test(dependsOnMethods = {"tests.AuthTest.getAccessToken"}, description = "Attempt to register a student with a missing name field")
    public void registerStudentWithMissingName() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        String requestBody = "{\"mobileNumber\": \"9876543212\", \"emailId\": \"test@example.com\", \"cgpa\": 6.5, \"department\": \"Mechanical\", \"backlogs\": 2}";

        Response res = RestAssured
            .given()
            .header("Authorization", "Bearer " + TokenManager.getAccessToken())
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.REGISTER_STUDENT_ENDPOINT)
            .then()
            .statusCode(400)
            .extract().response();
        
        System.out.println("Missing name response: " + res.asPrettyString());
    }

    @Test(dependsOnMethods = {"tests.AuthTest.getAccessToken"}, description = "Attempt to register a student with an invalid mobile number format")
    public void registerStudentWithInvalidMobileNumber() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        String requestBody = "{\"studentName\": \"Test User\", \"mobileNumber\": \"123\", \"emailId\": \"testuser@example.com\", \"cgpa\": 8.0, \"department\": \"IT\", \"backlogs\": 0}";

        RestAssured
            .given()
            .header("Authorization", "Bearer " + TokenManager.getAccessToken())
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.REGISTER_STUDENT_ENDPOINT)
            .then()
            .statusCode(400);
    }
    
    @Test(dependsOnMethods = {"tests.AuthTest.getAccessToken"}, description = "Attempt to register a student with a CGPA outside the valid range (e.g., > 10)")
    public void registerStudentWithHighCGPA() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        String requestBody = "{\"studentName\": \"Max CGPA\", \"mobileNumber\": \"9999999999\", \"emailId\": \"maxcgpa@example.com\", \"cgpa\": 11.0, \"department\": \"Civil\", \"backlogs\": 0}";

        RestAssured
            .given()
            .header("Authorization", "Bearer " + TokenManager.getAccessToken())
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.REGISTER_STUDENT_ENDPOINT)
            .then()
            .statusCode(400);
    }

    @Test(dependsOnMethods = {"tests.AuthTest.getAccessToken"}, description = "Attempt to register a student with a negative backlog count")
    public void registerStudentWithNegativeBacklogs() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        String requestBody = "{\"studentName\": \"Backlog Fail\", \"mobileNumber\": \"8888888888\", \"emailId\": \"backlogfail@example.com\", \"cgpa\": 5.5, \"department\": \"Aero\", \"backlogs\": -1}";

        RestAssured
            .given()
            .header("Authorization", "Bearer " + TokenManager.getAccessToken())
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.REGISTER_STUDENT_ENDPOINT)
            .then()
            .statusCode(400);
    }

    @Test(description = "Attempt to register a student without an Authorization header")
    public void registerStudentWithoutAuthHeader() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        String requestBody = "{\"studentName\": \"Unauthorized User\", \"mobileNumber\": \"7777777777\", \"emailId\": \"unauth@example.com\", \"cgpa\": 9.1, \"department\": \"CSE\", \"backlogs\": 0}";

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.REGISTER_STUDENT_ENDPOINT)
            .then()
            .statusCode(401);
    }

    @Test(dependsOnMethods = {"tests.AuthTest.getAccessToken"}, description = "Attempt to register a student with an invalid or expired Authorization token")
    public void registerStudentWithInvalidToken() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        String requestBody = "{\"studentName\": \"Invalid Token User\", \"mobileNumber\": \"6666666666\", \"emailId\": \"invalidtoken@example.com\", \"cgpa\": 7.5, \"department\": \"Mech\", \"backlogs\": 1}";

        RestAssured
            .given()
            .header("Authorization", "Bearer " + "invalid_token_12345")
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.REGISTER_STUDENT_ENDPOINT)
            .then()
            .statusCode(401);
    }
}
