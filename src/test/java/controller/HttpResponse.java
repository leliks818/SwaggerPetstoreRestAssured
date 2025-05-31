package controller;
import io.qameta.allure.Step;
import io.restassured.response.Response;


import java.io.File;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class HttpResponse {
    private final Response response;

    public HttpResponse(Response response) {
        this.response = response;
    }

    @Step("Проверка кода ответа: {expectedCode}")
    public HttpResponse statusCodeIs(int expectedCode) {
        int actualCode = response.getStatusCode();
        System.out.println("Status Code: " + actualCode);
        assertEquals(expectedCode, actualCode);
        return this;
    }
    public String getJsonValue(String path) {
        return response.jsonPath().getString(path);
    }

    @Step("Проверка значения JSON поля '{path}' на равенство '{expectedValue}'")
    public HttpResponse jsonValueIs(String path, Object expectedValue) {
        Object actual = response.jsonPath().get(path);
        System.out.println("RESPONSE: " + response.asString());
        System.out.println("Extracted [" + path + "] = " + actual);

        assertEquals(String.valueOf(expectedValue), String.valueOf(actual));
        return this;
    }

    @Step("Проверка, что JSON поле '{path}' содержит подстроку '{expectedSubstring}'")
    public HttpResponse jsonValueContains(String path, String expectedSubstring) {
        String actual = response.jsonPath().getString(path);
        assertNotNull(actual);
        assertTrue(actual.contains(expectedSubstring));
        return this;
    }

    @Step("Проверка, что JSON поле '{path}' не null")
    public HttpResponse jsonValueNotNull(String path) {
        assertNotNull(response.jsonPath().get(path));
        return this;
    }

    @Step("Проверка, что значение JSON поля '{path}' больше {number}")
        public HttpResponse jsonValueBiggerThan(String path, long number) {
            long actual = response.jsonPath().getLong(path);
            assertTrue(actual > number);
            return this;
        }

    @Step("Проверка, что значение JSON поля '{path}' больше {number}")
    public HttpResponse jsonValueIsGreaterThan(String path, long number) {
        // Получаем значение как Long (поддержка больших чисел)
        Long actualValue = response.jsonPath().getLong(path);
        System.out.println("Extracted [" + path + "] = " + actualValue);
        assertNotNull(actualValue, "Значение поля '" + path + "' равно null");
        assertTrue(actualValue > number,
                "Ожидалось, что значение поля '" + path + "' будет больше " + number + ", но было " + actualValue);
        return this;
    }

    @Step("Лог тела ответа")
    public HttpResponse logBody() {
        System.out.println(response.getBody().asPrettyString());
        return this;
    }

    @Step("Получение списка JSON по пути: {path}")
    public List<Map<String, Object>> getJsonList(String path) {
        return response.jsonPath().getList(path);
    }

    @Step("Проверка, что поле '{fieldName}' равно {expectedValue}")
    public HttpResponse bodyFieldEquals(String fieldName, long expectedValue) {
        Number actualValue = response.jsonPath().get(fieldName);
        assertEquals(expectedValue, actualValue.longValue());
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
