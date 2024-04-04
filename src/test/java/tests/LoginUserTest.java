package tests;

import ru.praktikum.burgers.api.client.UserAPI;
import ru.praktikum.burgers.api.model.ResponseUser;
import ru.praktikum.burgers.api.model.User;
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

public class LoginUserTest {
    UserAPI userAPI = new UserAPI();
    User user = new User("test_masha_login_user@yandex.ru", "123456");
    ResponseUser responseUser = new ResponseUser();
    BaseTest baseTest = new BaseTest();

    @Before
    public void setUp() {
        responseUser = baseTest.createUser();
    }

    @Test
    @DisplayName("Check success login")
    @Description("Basic create test for /api/auth/login endpoint")
    public void loginUser() {
        Response response = userAPI.sendPostRequestLoginCourier(user);
        response.then().statusCode(SC_OK);
        ResponseUser responseUser = response.body().as(ResponseUser.class);
        MatcherAssert.assertThat(responseUser.isSuccess(), equalTo(true));
    }

    @Test
    @DisplayName("Check login user with incorrect login")
    public void loginIncorrectLoginUser() {
        User user = new User("test_masha_incorrect@yandex.ru", "123456");
        Response response = userAPI.sendPostRequestLoginCourier(user);
        response.then().statusCode(SC_UNAUTHORIZED);
        ResponseUser responseUser = response.body().as(ResponseUser.class);
        MatcherAssert.assertThat(responseUser.getMessage(), equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Check login user with incorrect password")
    public void loginIncorrectPasswordUser() {
        User user = new User("test_masha_login@yandex.ru", "1234");
        Response response = userAPI.sendPostRequestLoginCourier(user);
        response.then().statusCode(SC_UNAUTHORIZED);
        ResponseUser responseUser = response.body().as(ResponseUser.class);
        MatcherAssert.assertThat(responseUser.getMessage(), equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Check login user with incorrect login and incorrect password")
    public void loginIncorrectLoginPasswordUser() {
        User user = new User("test_masha_incorrect@yandex.ru", "1234");
        Response response = userAPI.sendPostRequestLoginCourier(user);
        response.then().statusCode(SC_UNAUTHORIZED);
        ResponseUser responseUser = response.body().as(ResponseUser.class);
        MatcherAssert.assertThat(responseUser.getMessage(), equalTo("email or password are incorrect"));
    }

    @After
    @DisplayName("Delete user")
    public void logoutAndDeleteUser() {
        baseTest.deleteUser(responseUser);
    }
}

