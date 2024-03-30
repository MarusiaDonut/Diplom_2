package api;

import classes.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderAPI {

    final static private String PATH_TO_ORDER = "/api/orders";
    @Step("Send GET request get ingredients to /api/orders")
    public Response sendGetRequestListOrders(String token) {
        return given()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .get(PATH_TO_ORDER);
    }

    @Step ("Send POST request create order to /api/orders")
    public Response sendPostRequestCreateOrder(Order order, String token) {
        return given()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(PATH_TO_ORDER);
    }
}

