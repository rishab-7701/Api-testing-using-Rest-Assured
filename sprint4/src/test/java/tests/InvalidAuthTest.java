package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.Constants;

public class InvalidAuthTest {

    @Test(description = "Login with an invalid password")
    public void loginWithInvalidPassword() {
        RestAssured.baseURI = Constants.AUTH_BASE_URI;
        
        String requestBody = "{\"username\": \"Admin\", \"password\": \"wrongpass\"}";

        Response res = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.SIGNIN_ENDPOINT)
            .then()
            .statusCode(401)
            .extract().response();

        System.out.println("Invalid password response: " + res.asPrettyString());
    }

    @Test(description = "Login with blank username and password")
    public void loginWithBlankCredentials() {
        RestAssured.baseURI = Constants.AUTH_BASE_URI;
        
        String requestBody = "{\"username\": \"\", \"password\": \"\"}";

        Response res = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.SIGNIN_ENDPOINT)
            .then()
            .statusCode(401)
            .extract().response();

        System.out.println("Blank credentials response: " + res.asPrettyString());
    }

    @Test(description = "Login with an invalid username")
    public void loginWithInvalidUsername() {
        RestAssured.baseURI = Constants.AUTH_BASE_URI;
        
        String requestBody = "{\"username\": \"InvalidUser\", \"password\": \"admin123\"}";

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.SIGNIN_ENDPOINT)
            .then()
            .statusCode(401);
    }
    
    @Test(description = "Login with missing username field in the payload")
    public void loginWithMissingUsernameField() {
        RestAssured.baseURI = Constants.AUTH_BASE_URI;
        
        String requestBody = "{\"password\": \"admin123\"}";

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.SIGNIN_ENDPOINT)
            .then()
            .statusCode(400); // Bad Request
    }

    @Test(description = "Login with missing password field in the payload")
    public void loginWithMissingPasswordField() {
        RestAssured.baseURI = Constants.AUTH_BASE_URI;
        
        String requestBody = "{\"username\": \"Admin\"}";

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.SIGNIN_ENDPOINT)
            .then()
            .statusCode(400); // Bad Request
    }
}