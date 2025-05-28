package controller;

import io.qameta.allure.Step;
import io.restassured.response.Response;


import static org.junit.jupiter.api.Assertions.*;

public class HttpResponseUser {
    private final Response response;

    public HttpResponseUser(Response response) {
        this.response = response;
    }

    @Step("Проверка кода ответа: {expectedCode}")
    public HttpResponseUser statusCodeIs(int expectedCode) {
        int actualCode = response.getStatusCode();
        System.out.println("Status Code: " + actualCode);
        assertEquals(expectedCode, actualCode);
        return this;
    }
    public String getJsonValue(String path) {
        return response.jsonPath().getString(path);
    }

    @Step("Проверка значения JSON поля '{path}' на равенство '{expectedValue}'")
    public HttpResponseUser jsonValueIs(String path, Object expectedValue) {
        Object actual = response.jsonPath().get(path);
        System.out.println("RESPONSE: " + response.asString());
        System.out.println("Extracted [" + path + "] = " + actual);

        assertEquals(String.valueOf(expectedValue), String.valueOf(actual));
        return this;
    }

//    @Step("Проверка значения JSON поля '{path}' на соответствие регулярному выражению '{regex}'")
//    public HttpResponseUser jsonValueMatches(String path, String regex) {
//        String actual = response.jsonPath().getString(path);
//        System.out.println("RESPONSE: " + response.asString());
//        System.out.println("Extracted [" + path + "] = " + actual);
//        assertNotNull(actual, "Значение поля '" + path + "' равно null");
//        boolean matches = Pattern.matches(regex, actual);
//        assertTrue(matches, "Значение поля '" + path + "' не соответствует регулярному выражению '" + regex + "': " + actual);
//        return this;
//    }

    @Step("Проверка, что JSON поле '{path}' содержит подстроку '{expectedSubstring}'")
    public HttpResponseUser jsonValueContains(String path, String expectedSubstring) {
        String actual = response.jsonPath().getString(path);
        assertNotNull(actual);
        assertTrue(actual.contains(expectedSubstring));
        return this;
    }

    @Step("Проверка, что JSON поле '{path}' не null")
    public HttpResponseUser jsonValueNotNull(String path) {
        assertNotNull(response.jsonPath().get(path));
        return this;
    }

    @Step("Проверка, что значение JSON поля '{path}' больше {number}")
    public HttpResponseUser jsonValueBiggerThan(String path, int number) {
        int value = response.jsonPath().getInt(path);
        assertTrue(value > number);
        return this;
    }
    @Step("Проверка, что значение JSON поля '{path}' больше {number}")
    public HttpResponseUser jsonValueIsGreaterThan(String path, long number) {
        // Получаем значение как Long (поддержка больших чисел)
        Long actualValue = response.jsonPath().getLong(path);
        System.out.println("Extracted [" + path + "] = " + actualValue);
        assertNotNull(actualValue, "Значение поля '" + path + "' равно null");
        assertTrue(actualValue > number,
                "Ожидалось, что значение поля '" + path + "' будет больше " + number + ", но было " + actualValue);
        return this;
    }

    @Step("Лог тела ответа")
    public HttpResponseUser logBody() {
        System.out.println(response.getBody().asPrettyString());
        return this;
    }

}
