package controller.user;

import constants.CommonConstants;
import controller.HttpResponse;
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
    public HttpResponse createUser(User user) {
        Response response = given(requestSpecification)
                .body(user)
                .post(CommonConstants.USER_ENDPOINT); // Лучше указывать явно откуда
        return new HttpResponse(response);
    }

    @Step("Создание пользователей через список (createWithList)")
    public HttpResponse createUsersWithList(List<User> users) {
        Response response = given(requestSpecification)
                .body(users)
                .post(USER_ENDPOINT + "/createWithList");
        return new HttpResponse(response);
    }

    @Step("Создание пользователей через массив (createWithArray)")
    public HttpResponse createUsersWithArray(List<User> users) {
        Response response = given(requestSpecification)
                .body(users)
                .post(USER_ENDPOINT + "/createWithArray");
        return new HttpResponse(response);
    }

    @Step("Получение пользователя по username: {username}")
    public HttpResponse getUserByUsername(String username) {
        Response response = given(requestSpecification)
                .get(USER_ENDPOINT + "/" + username);
        return new HttpResponse(response);
    }

    @Step("Получение пользователя из объекта User: {user.username}")
    public HttpResponse getUser(User user) {
        return getUserByUsername(user.getUsername());
    }

    @Step("Обновление пользователя по username: {username}")
    public HttpResponse updateUser(String username, User updatedUser) {
        Response response = given(requestSpecification)
                .body(updatedUser)
                .put(USER_ENDPOINT + "/" + username);
        return new HttpResponse(response);
    }

    @Step("Удаление пользователя")
    public HttpResponse deleteUser(String username) {
        Response response = given(requestSpecification)
                .delete(USER_ENDPOINT + "/" + username);
        return new HttpResponse(response);
    }

    @Step("Логин пользователя")
    public HttpResponse loginUser(String username, String password) {
        Response response = given(requestSpecification)
                .queryParam("username", username)
                .queryParam("password", password)
                .get(USER_ENDPOINT + "/login");
        return new HttpResponse(response);
    }

    @Step("Логаут пользователя")
    public HttpResponse logoutUser() {
        Response response = given(requestSpecification)
                .get(USER_ENDPOINT + "/logout");
        return new HttpResponse(response);
    }
}