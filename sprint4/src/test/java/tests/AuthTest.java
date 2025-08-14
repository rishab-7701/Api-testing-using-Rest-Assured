package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Constants;
import utils.TokenManager;

public class AuthTest {

    @Test
    public void getAccessToken() {
        RestAssured.baseURI = Constants.AUTH_BASE_URI;

        String requestBody = "{\"username\": \"admin\", \"password\": \"admin123\"}";

        Response res = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post(Constants.SIGNIN_ENDPOINT)
            .then()
            .statusCode(200)
            .extract().response();

        String accessToken = res.jsonPath().getString("accessToken");
        Assert.assertNotNull(accessToken, "Access token should not be null");
        System.out.println("Access Token received: " + accessToken);

        TokenManager.setAccessToken(accessToken);
    }
}