package tests;

import api.OrderAPI;
import api.UserAPI;
import classes.Order;
import classes.ResponseOrder;
import classes.ResponseUser;
import classes.User;
import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrderTest {
    UserAPI userAPI = new UserAPI();
    OrderAPI orderAPI = new OrderAPI();
    ResponseUser responseUser = new ResponseUser();
    User createUser = new User("test_masha_get_order1@yandex.ru", "123456", "TestMashaCreateOrder1");

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";

        Response response = userAPI.sendPostRequestCreateUser(createUser);
        responseUser = response.body().as(ResponseUser.class);
    }

    @Test
    @DisplayName("Check get orders with authorization")
    @Description("Basic create test for /api/orders endpoint")
    public void getOrdersWithAuthorization() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add(("61c0c5a71d1f82001bdaaa72"));
        Order order = new Order(ingredients);
        orderAPI.sendPostRequestCreateOrder(order, responseUser.getAccessToken());

        Response response = orderAPI.sendGetRequestListOrders(responseUser.getAccessToken());
        ResponseOrder responseOrder = response.body().as(ResponseOrder.class);

        response.then().statusCode(SC_OK);
        MatcherAssert.assertThat(responseOrder.isSuccess(), equalTo(true));
        String checkIngredients = responseOrder.getOrders().get(0).getIngredients().get(0);
        MatcherAssert.assertThat(checkIngredients, equalTo("61c0c5a71d1f82001bdaaa72"));
    }

    @Test
    @DisplayName("Check get orders without authorization")
    public void getOrdersWithoutAuthorization() {
        Response response = orderAPI.sendGetRequestListOrders("");
        ResponseOrder responseOrder = response.body().as(ResponseOrder.class);

        response.then().statusCode(SC_UNAUTHORIZED);
        MatcherAssert.assertThat(responseOrder.isSuccess(), equalTo(false));
        MatcherAssert.assertThat(responseOrder.getMessage(), equalTo("You should be authorised"));
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

