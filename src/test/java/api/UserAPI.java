package api;

import classes.ResponseUser;
import classes.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserAPI {

    final static private String PATH_TO_CREATE_USER = "/api/auth/register";
    final static private String PATH_TO_LOGIN_USER = "/api/auth/login";
    final static private String PATH_TO_LOGOUT_USER = "/api/auth/logout";
    final static private String PATH_TO_CHANGE_USER = "/api/auth/user";

    @Step("Send POST request create user to /api/auth/register")
    public Response sendPostRequestCreateUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(PATH_TO_CREATE_USER);
    }

    @Step ("Send POST request login user to /api/auth/login")
    public Response sendPostRequestLoginCourier(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(PATH_TO_LOGIN_USER);
    }

    @Step("Send DELETE request user to /api/auth/user")
    public Response sendDeleteRequestUser(String token) {
        return given()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .delete(PATH_TO_CHANGE_USER);
    }

    @Step("Send POST request logout user to /api/auth/logout")
    public Response sendLogoutRequestUser(String token) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(token)
                .when()
                .post(PATH_TO_LOGOUT_USER);
    }

    @Step("Send PATCH request change user to /api/auth/user")
    public Response sendChangeRequestUser(String token, User user) {
        return given()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(PATH_TO_CHANGE_USER);
    }
}

