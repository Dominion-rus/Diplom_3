package org.praktikum.clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.praktikum.models.Credentials;
import org.praktikum.models.User;
import static io.restassured.RestAssured.given;

public class UserClient extends Client {
    @Step("Создание пользователя через API")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post("/register/")
                .then();
    }
    public ValidatableResponse delete(String accessToken) {
        return given()
                .header("authorization", "bearer " + accessToken)
                .spec(getSpec())
                .when()
                .delete("/user/")
                .then();
    }

    public ValidatableResponse login(Credentials credentials) {
        return given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post("/login/")
                .then();
    }
}
