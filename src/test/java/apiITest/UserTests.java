package apiITest;
import org.junit.jupiter.api.Test;

import controller.user.UserController;
import io.restassured.response.Response;
import models.ApiResponse;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;


import static testData.TestData.*;

class UserTests {

    private UserController userController;

    @BeforeEach
    void setup() {
        userController = new UserController();
    }

    @Test
    void createUserTest() {
        Response response = userController.createUser(DEFAULT_USER);

        Assertions.assertEquals(200, response.statusCode());
        ApiResponse apiResponse = response.as(ApiResponse.class);// десериализует (преобразует) JSON-ответ
        // от сервера в объект указанного класса (ApiResponse).

        Assertions.assertEquals(200, apiResponse.getCode());
        Assertions.assertEquals("unknown", apiResponse.getType());
        Assertions.assertTrue(apiResponse.getMessage().matches("\\d+"));
    }

    @Test
    void updateUserTest() {
        // Создаём пользователя, чтобы его обновить
        userController.createUser(UPDATE_USER);

        Response response = userController.updateUser(UPDATE_USER.getUsername(), UPDATE_USER);

        Assertions.assertEquals(200, response.statusCode());
        ApiResponse apiResponse = response.as(ApiResponse.class);
        Assertions.assertEquals(200, apiResponse.getCode());
        Assertions.assertEquals("unknown", apiResponse.getType());
        // В ответе приходит ID пользователя в виде строки
        Assertions.assertTrue(apiResponse.getMessage().matches("\\d+"));
    }

    @Test
    void getUserTest() {
        Response response = userController.getUser(DEFAULT_USER);
        Assertions.assertEquals(200, response.statusCode(), "Incorrect response code");

        User userFromResponse = response.getBody().as(User.class);
        Assertions.assertTrue(userFromResponse.getId() > 0, "User ID should be greater than 0");

        Assertions.assertEquals(DEFAULT_USER.getUsername(), userFromResponse.getUsername(), "Username is wrong");
        Assertions.assertEquals(DEFAULT_USER.getFirstName(), userFromResponse.getFirstName(), "First name is wrong");
        Assertions.assertEquals(DEFAULT_USER.getLastName(), userFromResponse.getLastName(), "Last name is wrong");
        Assertions.assertEquals(DEFAULT_USER.getEmail(), userFromResponse.getEmail(), "Email is wrong");
        Assertions.assertEquals(DEFAULT_USER.getPassword(), userFromResponse.getPassword(), "Password is wrong");
        Assertions.assertEquals(DEFAULT_USER.getPhone(), userFromResponse.getPhone(), "Phone is wrong");
        Assertions.assertEquals(DEFAULT_USER.getUserStatus(), userFromResponse.getUserStatus(), "User status is wrong");
    }

    @Test
    void createUsersWithArrayTest() {
        Response response = userController.createUsersWithArray((TEST_USERS));

        Assertions.assertEquals(200, response.statusCode());

        ApiResponse apiResponse = response.as(ApiResponse.class);
        Assertions.assertEquals(200, apiResponse.getCode());
        Assertions.assertEquals("unknown", apiResponse.getType());
        Assertions.assertEquals("ok", apiResponse.getMessage());
    }


    @Test
    void deleteUserTest() {
        userController.createUser(DEFAULT_USER);

        Response response = userController.deleteUser(DEFAULT_USER.getUsername());

        Assertions.assertEquals(200, response.statusCode());
        ApiResponse apiResponse = response.as(ApiResponse.class);
        Assertions.assertEquals(200, apiResponse.getCode());
        Assertions.assertEquals("unknown", apiResponse.getType());
        Assertions.assertEquals(DEFAULT_USER.getUsername(), apiResponse.getMessage());
    }

    @Test
    void loginUserTest() {
        userController.createUser(DEFAULT_USER);

        Response response = userController.loginUser(DEFAULT_USER.getUsername(), DEFAULT_USER.getPassword());

        Assertions.assertEquals(200, response.statusCode());
        ApiResponse apiResponse = response.as(ApiResponse.class);
        Assertions.assertEquals(200, apiResponse.getCode());
        Assertions.assertEquals("unknown", apiResponse.getType());
        Assertions.assertFalse(apiResponse.getMessage().isEmpty());
    }

    @Test
    void logoutUserTest() {
        Response response = userController.logoutUser();

        Assertions.assertEquals(200, response.statusCode());
        ApiResponse apiResponse = response.as(ApiResponse.class);
        Assertions.assertEquals(200, apiResponse.getCode());
        Assertions.assertEquals("unknown", apiResponse.getType());
        Assertions.assertEquals("ok", apiResponse.getMessage());
    }
}