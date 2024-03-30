package tests;

import api.UserAPI;
import classes.ResponseUser;
import classes.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeUserTest {
    UserAPI userAPI = new UserAPI();
    ResponseUser responseUser = new ResponseUser();
    User user = new User("test_change@yandex.ru", "123456");
    User createUser = new User("test_change@yandex.ru", "123456", "TestChange");

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";

        Response response = userAPI.sendPostRequestCreateUser(createUser);
        responseUser = response.body().as(ResponseUser.class);
    }

    @Test
    @DisplayName("Check successful change name for login user")
    @Description("Basic create test for /api/auth/login endpoint")
    public void changeNameLoginUser() {
        Response responseLogin = userAPI.sendPostRequestLoginCourier(user);
        ResponseUser responseUser = responseLogin.body().as(ResponseUser.class);

        user.setName("TestMashaChange");

        Response response = userAPI.sendChangeRequestUser(responseUser.getAccessToken(), user);
        ResponseUser responseChangeUser = response.body().as(ResponseUser.class);

        response.then().statusCode(SC_OK);
        MatcherAssert.assertThat(responseChangeUser.isSuccess(), equalTo(true));
        MatcherAssert.assertThat(responseChangeUser.getUser().getName(), equalTo("TestMashaChange"));
    }

    @Test
    @DisplayName("Check successful change email for login user")
    public void changeEmailLoginUser() {
        Response responseLogin = userAPI.sendPostRequestLoginCourier(user);
        ResponseUser responseUser = responseLogin.body().as(ResponseUser.class);

        user.setEmail("test_change_1@yandex.ru");

        Response response = userAPI.sendChangeRequestUser(responseUser.getAccessToken(), user);
        ResponseUser responseChangeUser = response.body().as(ResponseUser.class);

        response.then().statusCode(SC_OK);
        MatcherAssert.assertThat(responseChangeUser.isSuccess(), equalTo(true));
        MatcherAssert.assertThat(responseChangeUser.getUser().getEmail(), equalTo("test_change_1@yandex.ru"));
    }

    @Test
    @DisplayName("Check unsuccessful change same email for login user")
    public void changeSameEmailLoginUser() {
        Response responseLogin = userAPI.sendPostRequestLoginCourier(user);
        ResponseUser responseUser = responseLogin.body().as(ResponseUser.class);

        user.setEmail("test_change@yandex.ru");

        Response response = userAPI.sendChangeRequestUser(responseUser.getAccessToken(), user);
        ResponseUser responseChangeUser = response.body().as(ResponseUser.class);

        response.then().statusCode(SC_FORBIDDEN);
        MatcherAssert.assertThat(responseChangeUser.isSuccess(), equalTo(false));
        MatcherAssert.assertThat(responseChangeUser.getMessage(), equalTo("User with such email already exists"));
    }

    @Test
    @DisplayName("Check unsuccessful change email for logout user")
    public void changeEmailLogoutUser() {
        user.setEmail("test_logout_change@yandex.ru");

        Response response = userAPI.sendChangeRequestUser("", user);
        ResponseUser responseUser = response.body().as(ResponseUser.class);

        response.then().statusCode(SC_UNAUTHORIZED);
        MatcherAssert.assertThat(responseUser.isSuccess(), equalTo(false));
        MatcherAssert.assertThat(responseUser.getMessage(), equalTo("You should be authorised"));
    }

    @After
    @DisplayName("Delete user")
    public void deleteUser() {
        if (responseUser.getAccessToken() != null) {
            Response responseDelete = userAPI.sendDeleteRequestUser(responseUser.getAccessToken());
            responseDelete.then().statusCode(SC_ACCEPTED);
        }
    }
}

