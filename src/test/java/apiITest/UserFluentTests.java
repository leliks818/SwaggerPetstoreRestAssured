package apiITest;

import controller.FluentUserController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static testData.TestData.*;

public class UserFluentTests {

    private FluentUserController userController;

    @BeforeEach
    public void setUp() {
        userController = new FluentUserController();
    }

    @AfterEach
    public void tearDown() {
        userController.deleteUser(DEFAULT_USER.getUsername());
        userController.deleteUser(UPDATE_USER.getUsername());
}

    @Test
    @DisplayName("Создание пользователя с сохранением ID и проверкой как числа")
    public void createUserTest() {
        // Создание пользователя и получение ID из ответа
        String expectedId = userController.createUser(DEFAULT_USER)
                .statusCodeIs(200)
                .jsonValueIs("code", 200)
                .jsonValueIs("type", "unknown")
                .getJsonValue("message");

        userController.getUser(DEFAULT_USER)
                .statusCodeIs(200)
                .jsonValueIs("id", expectedId)                // сравниваем с message
                .jsonValueIs("username", DEFAULT_USER.getUsername())
                .jsonValueIs("email", DEFAULT_USER.getEmail());
    }


    @Test
    void createUsersWithListTest() {
        userController.createUsersWithList(TEST_USERS)
                .statusCodeIs(200)
                .jsonValueIs("code", "200")
                .jsonValueIs("type", "unknown")
                .logBody();
    }

    @Test
    void createUsersWithArrayTest() {
        userController.createUsersWithArray(TEST_USERS)
                .statusCodeIs(200)
                .jsonValueIs("code", "200")
                .jsonValueIs("type", "unknown")
                .logBody();
    }

    @Test
    void getUserByUsernameTest() {
        userController.createUser(DEFAULT_USER);

        userController.getUserByUsername(DEFAULT_USER.getUsername())
                .statusCodeIs(200)
                .jsonValueIs("username", DEFAULT_USER.getUsername())
                .logBody();
    }


@Test
void updateUserTest() {
    userController.createUser(DEFAULT_USER);

    userController.updateUser(DEFAULT_USER.getUsername(), UPDATE_USER)
            .statusCodeIs(200)
            .jsonValueIs("code", 200)
            .jsonValueIs("type", "unknown")
            .jsonValueIsGreaterThan("message", 0)
            .logBody();

    userController.getUserByUsername(UPDATE_USER.getUsername())
            .statusCodeIs(200)
            .jsonValueIs("firstName", UPDATE_USER.getFirstName())
            .jsonValueIs("email", UPDATE_USER.getEmail())
            .logBody();
}


    @Test
    void deleteUserTest() {
        userController.createUser(DEFAULT_USER);

        userController.deleteUser(DEFAULT_USER.getUsername())
                .statusCodeIs(200)
                .jsonValueIs("code", "200")
                .jsonValueIs("type", "unknown")
                .jsonValueIs("message", DEFAULT_USER.getUsername())
                .logBody();
    }

    @Test
    void loginUserTest() {
        userController.createUser(DEFAULT_USER);

        userController.loginUser(DEFAULT_USER.getUsername(), DEFAULT_USER.getPassword())
                .statusCodeIs(200)
                .jsonValueContains("message", "logged in")
                .logBody();
    }

    @Test
    void logoutUserTest() {
        userController.logoutUser()
                .statusCodeIs(200)
                .jsonValueIs("code", "200")
                .jsonValueIs("type", "unknown")
                .logBody();
    }
}
