package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Constants;
import utils.TokenManager;

import static org.hamcrest.Matchers.*;

public class ViewAllStudentsTest {

    @Test(dependsOnMethods = {"tests.AuthTest.getAccessToken"}, description = "View all registered students with a valid access token")
    public void viewAllStudents() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        Response res = RestAssured
            .given()
            .header("Authorization", "Bearer " + TokenManager.getAccessToken())
            .accept(ContentType.JSON)
            .when()
            .get(Constants.VIEW_ALL_STUDENTS_ENDPOINT)
            .then()
            .statusCode(200)
            .body("$", notNullValue()) // Ensure response body is not null
            .extract().response();

        System.out.println("View all students response: " + res.asPrettyString());
        Assert.assertTrue(res.jsonPath().getList("$").size() > 0, "Expected at least one student in the list");
    }

    @Test(description = "Attempt to view all students without an Authorization header")
    public void viewAllStudentsWithoutAuthHeader() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        RestAssured
            .given()
            .accept(ContentType.JSON)
            .when()
            .get(Constants.VIEW_ALL_STUDENTS_ENDPOINT)
            .then()
            .statusCode(401); // Unauthorized
    }

    // ✅ Positive Test Case 1: Verify that all students have an 'id' field
    @Test(dependsOnMethods = {"tests.AuthTest.getAccessToken"}, description = "Verify each student record contains 'id' field")
    public void verifyStudentsHaveId() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        Response res = RestAssured
            .given()
            .header("Authorization", "Bearer " + TokenManager.getAccessToken())
            .accept(ContentType.JSON)
            .when()
            .get(Constants.VIEW_ALL_STUDENTS_ENDPOINT)
            .then()
            .statusCode(200)
            .extract().response();

        res.jsonPath().getList("id").forEach(id -> {
            Assert.assertNotNull(id, "Student 'id' should not be null");
        });
    }

    // ✅ Positive Test Case 2: Verify student list contains expected fields
    @Test(dependsOnMethods = {"tests.AuthTest.getAccessToken"}, description = "Verify student records contain name and email fields")
    public void verifyStudentFields() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        Response res = RestAssured
            .given()
            .header("Authorization", "Bearer " + TokenManager.getAccessToken())
            .accept(ContentType.JSON)
            .when()
            .get(Constants.VIEW_ALL_STUDENTS_ENDPOINT)
            .then()
            .statusCode(200)
            .extract().response();

        res.jsonPath().getList("name").forEach(name -> {
            Assert.assertNotNull(name, "Student 'name' should not be null");
        });

        res.jsonPath().getList("email").forEach(email -> {
            Assert.assertTrue(email.toString().contains("@"), "Invalid student email: " + email);
        });
    }

    // ✅ Positive Test Case 3: Verify total student count is greater than zero
    @Test(dependsOnMethods = {"tests.AuthTest.getAccessToken"}, description = "Verify total student count is greater than zero")
    public void verifyStudentCount() {
        RestAssured.baseURI = Constants.STUDENT_BASE_URI;

        int studentCount = RestAssured
            .given()
            .header("Authorization", "Bearer " + TokenManager.getAccessToken())
            .accept(ContentType.JSON)
            .when()
            .get(Constants.VIEW_ALL_STUDENTS_ENDPOINT)
            .then()
            .statusCode(200)
            .extract().jsonPath().getList("$").size();

        Assert.assertTrue(studentCount > 0, "Expected student count to be greater than zero");
    }
}
