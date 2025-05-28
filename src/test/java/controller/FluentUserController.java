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

public class FluentUserController {

    private final RequestSpecification requestSpecification;

    public FluentUserController() {
        requestSpecification = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .filter(new AllureRestAssured());
    }

    @Step("Создание пользователя")
    public HttpResponseUser createUser(User user) {
        Response response = given(requestSpecification)
                .body(user)
                .post(USER_ENDPOINT);
        return new HttpResponseUser(response);
    }

    @Step("Создание пользователей через список (createWithList)")
    public HttpResponseUser createUsersWithList(List<User> users) {
        Response response = given(requestSpecification)
                .body(users)
                .post(USER_ENDPOINT + "/createWithList");
        return new HttpResponseUser(response);
    }

    @Step("Создание пользователей через массив (createWithArray)")
    public HttpResponseUser createUsersWithArray(List<User> users) {
        Response response = given(requestSpecification)
                .body(users)
                .post(USER_ENDPOINT + "/createWithArray");
        return new HttpResponseUser(response);
    }

    @Step("Получение пользователя по username: {username}")
    public HttpResponseUser getUserByUsername(String username) {
        Response response = given(requestSpecification)
                .get(USER_ENDPOINT + "/" + username);
        return new HttpResponseUser(response);
    }

    @Step("Получение пользователя из объекта User: {user.username}")
    public HttpResponseUser getUser(User user) {
        return getUserByUsername(user.getUsername());
    }

    @Step("Обновление пользователя по username: {username}")
    public HttpResponseUser updateUser(String username, User updatedUser) {
        Response response = given(requestSpecification)
                .body(updatedUser)
                .put(USER_ENDPOINT + "/" + username);
        return new HttpResponseUser(response);
    }

    @Step("Удаление пользователя")
    public HttpResponseUser deleteUser(String username) {
        Response response = given(requestSpecification)
                .delete(USER_ENDPOINT + "/" + username);
        return new HttpResponseUser(response);
    }

    @Step("Логин пользователя")
    public HttpResponseUser loginUser(String username, String password) {
        Response response = given(requestSpecification)
                .queryParam("username", username)
                .queryParam("password", password)
                .get(USER_ENDPOINT + "/login");
        return new HttpResponseUser(response);
    }

    @Step("Логаут пользователя")
    public HttpResponseUser logoutUser() {
        Response response = given(requestSpecification)
                .get(USER_ENDPOINT + "/logout");
        return new HttpResponseUser(response);
    }
}
