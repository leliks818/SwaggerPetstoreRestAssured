package controller;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.response.Response;

import java.io.File;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class HttpResponse {
    private final ValidatableResponse validatableResponse;

    public HttpResponse(ValidatableResponse response) {
        this.validatableResponse = response;
    }

    public HttpResponse(Response response) {
        this.validatableResponse = response.then();
    }

    @Step("Проверка, что статус-код равен {status}")
    public HttpResponse statusCodeIs(int status) {
        validatableResponse.statusCode(status);
        return this;
    }

    public String getJsonValue(String path) {
        return validatableResponse.extract().jsonPath().getString(path);
    }

    @Step("Проверка, что значение JSON поля '{path}' содержит подстроку '{expectedSubstring}'")
    public HttpResponse jsonValueContains(String path, String expectedSubstring) {
        String actual = validatableResponse.extract().jsonPath().getString(path);
        assertNotNull(actual, String.format("Значение поля '%s' равно null", path));
        assertTrue(actual.contains(expectedSubstring), String.format("Значение поля '%s' не содержит подстроку '%s'", path, expectedSubstring));
        return this;
    }

    @Step("Проверка, что значение JSON поля '{path}' равно '{expectedValue}'")
    public HttpResponse jsonValueIs(String path, String expectedValue) {
        String actualValue = validatableResponse.extract().jsonPath().getString(path);
        assertEquals(expectedValue, actualValue, String.format(
                "Фактическое значение '%s' не совпадает с ожидаемым '%s' для пути '%s'", actualValue, expectedValue, path));
        return this;
    }

    @Step("Проверка, что список JSON по пути '{path}' соответствует ожидаемому")
    public <T> HttpResponse jsonValueIs(String path, List<T> expectedValue, Class<T> actualClass) {
        List<T> actualValue = validatableResponse.extract().jsonPath().getList(path, actualClass);
        assertEquals(expectedValue, actualValue, String.format(
                "Фактический список '%s' не совпадает с ожидаемым '%s' для пути '%s'", actualValue, expectedValue, path));
        return this;
    }

    @Step("Проверка, что JSON поле '{path}' не равно null")
    public HttpResponse jsonValueNotNull(String path) {
        assertNotNull(validatableResponse.extract().jsonPath().get(path), String.format("Поле '%s' равно null", path));
        return this;
    }

    @Step("Проверка, что значение JSON поля '{path}' больше {number}")
    public HttpResponse jsonValueBiggerThan(String path, long number) {
        long actual = validatableResponse.extract().jsonPath().getLong(path);
        assertTrue(actual > number, String.format("Ожидалось, что значение поля '%s' будет больше %d, но было %d", path, number, actual));
        return this;
    }

    @Step("Проверка, что значение JSON поля '{path}' равно null")
    public HttpResponse jsonValueIsNull(String path) {
        Object value = validatableResponse.extract().jsonPath().get(path);
        assertNull(value, String.format("Ожидалось, что значение поля '%s' будет null, но было '%s'", path, value));
        return this;
    }

    @Step("Проверка, что значение JSON поля '{path}' равно '{expectedValue}'")
    public HttpResponse jsonValueIs(String path, Object expectedValue) {
        Object actualValue = validatableResponse.extract().jsonPath().get(path);

        if (expectedValue instanceof Number && actualValue instanceof Number) {
            double expected = ((Number) expectedValue).doubleValue();
            double actual = ((Number) actualValue).doubleValue();
            assertEquals(expected, actual, String.format(
                    "Ожидалось, что число по пути '%s' будет '%s', но было '%s'", path, expected, actual));
        } else {
            assertEquals(expectedValue, actualValue, String.format(
                    "Ожидалось, что значение по пути '%s' будет '%s', но было '%s'", path, expectedValue, actualValue));
        }

        return this;
    }

    @Step("Проверка, что значение JSON поля '{path}' больше {number}")
    public HttpResponse jsonValueIsGreaterThan(String path, long number) {
        Long actualValue = validatableResponse.extract().jsonPath().getLong(path);
        assertNotNull(actualValue, "Значение поля '" + path + "' равно null");
        assertTrue(actualValue > number, "Ожидалось, что значение поля '" + path + "' будет больше " + number + ", но было " + actualValue);
        return this;
    }

    @Step("Вывод тела ответа в консоль")
    public HttpResponse logBody() {
        System.out.println(validatableResponse.extract().response().getBody().asPrettyString());
        return this;
    }

    @Step("Получение списка JSON объектов по пути: {path}")
    public List<Map<String, Object>> getJsonList(String path) {
        return validatableResponse.extract().jsonPath().getList(path);
    }

    @Step("Проверка, что значение поля '{fieldName}' равно {expectedValue}")
    public HttpResponse bodyFieldEquals(String fieldName, long expectedValue) {
        Number actualValue = validatableResponse.extract().jsonPath().get(fieldName);
        assertEquals(expectedValue, actualValue.longValue(), String.format("Ожидалось, что значение поля '%s' будет %d, но было %d", fieldName, expectedValue, actualValue.longValue()));
        return this;
    }

    @Step("Загрузка изображения на сервер")
    public static HttpResponse uploadImage(File file) {
        Response response = given()
                .header("accept", "application/json")
                .contentType("multipart/form-data")
                .multiPart("file", file, "image/jpeg")
                .when()
                .post("https://petstore.swagger.io/v2/pet/1/uploadImage")
                .then()
                .extract()
                .response();

        return new HttpResponse(response);
    }
}
