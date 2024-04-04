package tests;

import ru.praktikum.burgers.api.client.OrderAPI;
import ru.praktikum.burgers.api.client.UserAPI;
import ru.praktikum.burgers.api.model.Order;
import ru.praktikum.burgers.api.model.ResponseOrder;
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

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrderTest {
    UserAPI userAPI = new UserAPI();
    OrderAPI orderAPI = new OrderAPI();
    ResponseUser responseUser = new ResponseUser();
    BaseTest baseTest = new BaseTest();

    @Before
    public void setUp() {
        responseUser = baseTest.createUser();
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
        baseTest.deleteUser(responseUser);
    }

}

