package oldTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

///AAA / arrange /act/assert pattern
class UserOldTests {
    private static final String BASE_URL = "https://petstore.swagger.io";

    @Test
    void createUserTest() {
        String body = """
                {
                    "id": 0,
                    "username": "string",
                    "firstName": "string",
                    "lastName": "string",
                    "email": "string",
                    "password": "string",
                    "phone": "string",
                    "userStatus": 0
                }
                """;

        Response response = given()
               .baseUri(BASE_URL)
               .accept("application/json")
               .contentType("application/json")
                .when()
                .body(body)
               .post("/v2/user")
                .andReturn();
        int actualCode = response.getStatusCode();
        Assertions.assertEquals(200,actualCode, "Статус код не 200");



    }

    @Test
    public void createUsersWithList() {
        String requestBody = """
                [
                  {
                    "id": 0,
                    "username": "string",
                    "firstName": "string",
                    "lastName": "string",
                    "email": "string",
                    "password": "string",
                    "phone": "string",
                    "userStatus": 0
                  }
                ]
                """;
        // 2. Выполним POST запрос с этим телом
        given()
                .baseUri("https://petstore.swagger.io/v2")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/user/createWithList")
                .then()
                .statusCode(200) // Проверяем, что код ответа 200
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", equalTo("ok"));

    }

    @Test
    public void getUserByUsername() {
        given()
                .baseUri("https://petstore.swagger.io/v2")
                .accept(ContentType.JSON)          // Заголовок Accept: application/json
                .when()
                .get("/user/{username}", "user1") // Вставляем имя пользователя user1 в путь
                .then()
                .statusCode(200)                  // Проверяем, что ответ 200 OK
                .contentType(ContentType.JSON)   // Проверяем Content-Type ответа

                .body("username", equalTo("user1"))
                .body("firstName", equalTo("John"))
                .body("lastName", equalTo("Doe"))
                .body("email", equalTo("john.doe@email.com"))
                .body("password", equalTo("pass123"))
                .body("phone", equalTo("1234567890"))
                .body("userStatus", equalTo(1));
    }

    @Test
    public void updateUserByUsername() {

        String updatedUserJson = """
                {
                  "id": 0,
                  "username": "user1",
                  "firstName": "Updated",
                  "lastName": "User",
                  "email": "updated@example.com",
                  "password": "newpass123",
                  "phone": "2222222222",
                  "userStatus": 0
                }
                """;
        given()
                .baseUri("https://petstore.swagger.io/v2")
                .contentType(ContentType.JSON)       // Заголовок: Content-Type: application/json
                .accept(ContentType.JSON)            // Заголовок: Accept: application/json
                .body(updatedUserJson)               // Тело запроса
                .when()
                .put("/user/{username}", "user1")    // Обновляем пользователя с username=user1
                .then()
                .statusCode(200)                     // Проверка: статус ответа 200
                .contentType(ContentType.JSON)       // Проверка: тип ответа JSON
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", notNullValue())  // message = id, может быть разным, но не null
                .log().all();
    }
    @Test
    public void loginUserTest() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

        given()
                .accept(ContentType.JSON)
                .queryParam("username", "user1")   // Добавляем параметр username в URL запроса
                .queryParam("password", "123")     // Добавляем параметр password в URL запроса
                .log().all()                       // Логируем весь запрос (метод, URL, заголовки и параметры)
                .when()
                .get("/user/login")
                .then()
                .log().all()                      // Логируем полный ответ (статус, заголовки, тело)
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", containsString("logged in user session:"))
                .header("X-Expires-After", notNullValue())
                .header("X-Rate-Limit", equalTo("5000"));

    }

    @Test
    public void deleteUser1() {
        // Устанавливаем базовый URL (можно один раз в @BeforeAll)
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

        // Выполняем DELETE-запрос на удаление пользователя по имени
        given()
                .accept(ContentType.JSON)           // Указываем, что хотим получить ответ в формате JSON
                .when()
                .delete("/user/{username}", "user1")

                .then()
                .statusCode(200)                     // Ожидаем HTTP-статус 200
                .body("code", equalTo(200))          // Проверяем, что поле code = 200
                .body("type", equalTo("unknown"))    // Проверяем, что type = unknown
                .body("message", equalTo("user1"))  // Проверяем, что message = user1
                .log().all();  // Логирует ответ
    }
    @Test
    public void logoutUserTest() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

        given()
                .accept(ContentType.JSON)
                .log().all()
                .when()
                .get("/user/logout")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", equalTo("ok"))
                .header("content-type", containsString("application/json"))
                .header("server", equalTo("Jetty(9.2.9.v20150224)"));
    }

}