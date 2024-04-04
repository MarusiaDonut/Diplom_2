package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.praktikum.burgers.api.client.UserAPI;
import ru.praktikum.burgers.api.model.ResponseUser;
import ru.praktikum.burgers.api.model.User;

public class BaseTest {

    UserAPI userAPI = new UserAPI();
    ResponseUser responseUser = new ResponseUser();
    User createUser = new User("test_change@yandex.ru", "123456", "TestChange");
    public ResponseUser createUser() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        Response response = userAPI.sendPostRequestCreateUser(createUser);
        responseUser = response.body().as(ResponseUser.class);
        return responseUser;
    }

    public void deleteUser(ResponseUser responseUser) {
        if (responseUser.getAccessToken() != null) {
            userAPI.sendDeleteRequestUser(responseUser.getAccessToken());
        }
    }
}
