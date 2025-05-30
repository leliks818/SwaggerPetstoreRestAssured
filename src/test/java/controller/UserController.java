package controller;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.User;

import java.util.List;

import static constants.CommonConstants.BASE_URL;
import static constants.CommonConstants.USER_ENDPOINT;
import static io.restassured.RestAssured.given;

public class UserController {

    private final RequestSpecification requestSpecification;

    public UserController() {
        requestSpecification = given()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .filter(new AllureRestAssured());
    }

    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given(requestSpecification)
                .body(user)
                .post(USER_ENDPOINT);
    }

    @Step("Создание пользователей через список (createWithList)")
    public Response createUsersWithList(List<User> users) {
        return given(requestSpecification)
                .body(users)
                .post(USER_ENDPOINT + "/createWithList");
    }

    @Step("Получение пользователя по username: {username}")
    public Response getUserByUsername(String username) {
        return given(requestSpecification)
                .get(USER_ENDPOINT + "/{username}", username);
    }

    @Step("Получение пользователя из объекта User: {user.username}")
    public Response getUser(User user) {
        return getUserByUsername(user.getUsername());
    }

    @Step("Обновление пользователя по username: {username}")
    public Response updateUser(String username, User updatedUser) {
        return given(requestSpecification)
                .body(updatedUser)
                .put(USER_ENDPOINT + "/{username}", username);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String username) {
        return given(requestSpecification)
                .delete(USER_ENDPOINT + "/{username}", username);
    }


    @Step("Логин пользователя")
    public Response loginUser(String username, String password) {
        return given(requestSpecification)
                .queryParam("username", username)
                .queryParam("password", password)
                .get(USER_ENDPOINT + "/login");
    }

    @Step("Логаут пользователя")
    public Response logoutUser() {
        return given(requestSpecification)
                .get(USER_ENDPOINT + "/logout");
    }

    @Step("создание пользователей через массив (createWithArray)")
    public Response createUsersWithArray(List<User> users) {
        return given(requestSpecification)
                .body(users)
                .post(USER_ENDPOINT + "/createWithArray");
    }
}
