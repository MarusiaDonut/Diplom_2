package tests;

import api.OrderAPI;
import api.UserAPI;
import classes.*;
import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {
    OrderAPI orderAPI = new OrderAPI();
    UserAPI userAPI = new UserAPI();
    ResponseUser responseUser = new ResponseUser();
    User createUser = new User("test_create_order@yandex.ru", "123456", "TestCreateOrder");
    List<String> ingredients = new ArrayList<>();

    public void addIngredient() {
        ingredients.add("61c0c5a71d1f82001bdaaa6f");
        ingredients.add(("61c0c5a71d1f82001bdaaa71"));
        ingredients.add(("61c0c5a71d1f82001bdaaa72"));
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";

        Response response = userAPI.sendPostRequestCreateUser(createUser);
        responseUser = response.body().as(ResponseUser.class);
    }

    @Test
    @DisplayName("Check create order without authorization")
    @Description("Basic create test for /api/orders endpoint")
    public void createOrderWithoutAuthorization() {
        addIngredient();
        Order order = new Order(ingredients);

        Response response = orderAPI.sendPostRequestCreateOrder(order, "");
        ResponseOrder responseOrder = response.body().as(ResponseOrder.class);

        MatcherAssert.assertThat(responseOrder.isSuccess(), equalTo(false));
        MatcherAssert.assertThat(responseOrder.getMessage(), equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Check create order with authorization")
    public void createOrderWithAuthorization() {
        addIngredient();
        Order order = new Order(ingredients);

        Response response = orderAPI.sendPostRequestCreateOrder(order, responseUser.getAccessToken());
        ResponseOrder responseOrder = response.body().as(ResponseOrder.class);

        MatcherAssert.assertThat(responseOrder.isSuccess(), equalTo(true));
        MatcherAssert.assertThat(responseOrder.getName(), equalTo("Spicy бессмертный био-марсианский бургер"));
    }

    @Test
    @DisplayName("Check create order without ingredients")
    public void createOrderWithoutIngredients() {
        Order order = new Order(ingredients);

        Response response = orderAPI.sendPostRequestCreateOrder(order, responseUser.getAccessToken());
        ResponseOrder responseOrder = response.body().as(ResponseOrder.class);

        response.then().statusCode(SC_BAD_REQUEST);

        MatcherAssert.assertThat(responseOrder.isSuccess(), equalTo(false));
        MatcherAssert.assertThat(responseOrder.getMessage(), equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Check create order with incorrect hash")
    @Description("Basic create test for  endpoint")
    public void createOrderWithIncorrectHash() {
        ingredients.add("61c0c5a71d1f82001bdaaa");
        Order order = new Order(ingredients);

        Response response = orderAPI.sendPostRequestCreateOrder(order, responseUser.getAccessToken());

        response.then().statusCode(SC_INTERNAL_SERVER_ERROR);
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

