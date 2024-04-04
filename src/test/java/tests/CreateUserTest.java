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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateUserTest {

    UserAPI userAPI = new UserAPI();
    User user = new User("test_masha_createUser@yandex.ru", "123456", "TestMashaCreateUser");

    ResponseUser responseUser = new ResponseUser();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Check create new user")
    @Description("Basic create test for /api/auth/register endpoint")
    public void createUser() {
        Response response = userAPI.sendPostRequestCreateUser(user);
        response.then().statusCode(SC_OK);
        responseUser = response.body().as(ResponseUser.class);
        MatcherAssert.assertThat(responseUser.isSuccess(), equalTo(true));
    }

    @Test
    @DisplayName("Check create new same user")
    public void createUserSame() {
        this.createUser();

        Response responseSame = userAPI.sendPostRequestCreateUser(user);
        responseSame.then().statusCode(SC_FORBIDDEN);
        ResponseUser responseUserSame = responseSame.body().as(ResponseUser.class);

        MatcherAssert.assertThat(responseUserSame.getMessage(), equalTo("User already exists"));
    }

    @Test
    @DisplayName("Check create new user without password")
    public void createUserWithoutPassword() {
        User user = new User("test_masha_withoutpassword@yandex.ru", "", "TestMashaWithoutPassword");
        Response response =  userAPI.sendPostRequestCreateUser(user);
        response.then().statusCode(SC_FORBIDDEN);
        responseUser = response.body().as(ResponseUser.class);
        MatcherAssert.assertThat(responseUser.getMessage(), equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Check create new user without name")
    public void createUserWithoutName() {
        User user = new User("test_masha_withoutname@yandex.ru", "123456", "");
        Response response =  userAPI.sendPostRequestCreateUser(user);
        response.then().statusCode(SC_FORBIDDEN);
        responseUser = response.body().as(ResponseUser.class);
        MatcherAssert.assertThat(responseUser.getMessage(), equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Check create new user without email")
    public void createUserWithoutEmail() {
        User user = new User("", "123456", "TestMashaWithoutEmail");
        Response response =  userAPI.sendPostRequestCreateUser(user);
        response.then().statusCode(SC_FORBIDDEN);
        responseUser = response.body().as(ResponseUser.class);
        MatcherAssert.assertThat(responseUser.getMessage(), equalTo("Email, password and name are required fields"));
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

