package controller.pet;

import controller.HttpResponse;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import models.pet.Pet;

import java.io.File;

import static constants.CommonConstants.*;
import static io.restassured.RestAssured.given;

public class FluentPetController {

    private final RequestSpecification requestSpecification;

    public FluentPetController() {
        requestSpecification = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .filter(new AllureRestAssured());
    }

    @Step("Создание Pet")
    public HttpResponse addPet(Pet pet) {
        ValidatableResponse response = given(requestSpecification)
                .body(pet)
                .post(PET_ENDPOINT)
                .then();
        return new HttpResponse(response);
    }

    @Step("Загрузка изображения на сервер")
    public static HttpResponse uploadImage(File file) {
        ValidatableResponse response = given()
                .header("accept", "application/json")
                .contentType("multipart/form-data")
                .multiPart("file", file, "image/jpeg")
                .when()
                .post("https://petstore.swagger.io/v2/pet/1/uploadImage")
                .then();  // вернула ValidatableResponse
        return new HttpResponse(response);
    }

    @Step("Обновление существующего питомца")
    public HttpResponse updatePet(Object pet) {
        ValidatableResponse response = given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(pet)
                .put(PET_ENDPOINT)
                .then();
        return new HttpResponse(response);
    }

    @Step("Поиск питомцев по статусам: {statuses}")
    public HttpResponse findPetsByStatus(String... statuses) {
        String statusParam = String.join(",", statuses);
        ValidatableResponse response = given(requestSpecification)
                .queryParam("status", statusParam)
                .get("/pet/findByStatus")
                .then();
        return new HttpResponse(response);
    }

    @Step("Получение питомца по ID: {petId}")
    public HttpResponse getPetById(long petId) {
        ValidatableResponse response = given(requestSpecification)
                .get("/pet/" + petId)
                .then();
        return new HttpResponse(response);
    }

    @Step("Получение питомца по объекту Pet")
    public HttpResponse getPet(Pet pet) {
        ValidatableResponse response = given(requestSpecification)
                .get(PET_ENDPOINT + pet.getId())
                .then();
        return new HttpResponse(response);
    }

    @Step("Обновление питомца через форму: petId={petId}, name={name}, status={status}")
    public HttpResponse updatePetWithFormData(int petId, String name, String status) {
        ValidatableResponse response = given(requestSpecification)
                .contentType("application/x-www-form-urlencoded")
                .formParam("name", name)
                .formParam("status", status)
                .post("/" + petId)
                .then();
        return new HttpResponse(response);
    }

    @Step("Удаление питомца")
    public HttpResponse deletePet(Pet pet) {
        ValidatableResponse response = given(requestSpecification)
                //.header("api_key", apiKey)
                .delete(PET_ENDPOINT + "/" + pet.getId())
                .then();
        return new HttpResponse(response);
    }
}
