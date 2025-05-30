package controller;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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
        Response response = given(requestSpecification)
                .body(pet)
                .post(PET_ENDPOINT);
        return new HttpResponse(response);
    }

    @Step("Загрузка изображения для питомца с ID = {petId}")
    public HttpResponse uploadPetImage(long petId, String additionalMetadata, File imageFile) {
        Response response = given(requestSpecification)
                .multiPart("file", imageFile)
                .multiPart("additionalMetadata", additionalMetadata != null ? additionalMetadata : "")
                .post("/" + petId + "/uploadImage");
        return new HttpResponse(response);
    }

    @Step("Обновление существующего питомца")
    public HttpResponse updatePet(Object pet) {
        Response response = given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(pet)
                .put(PET_ENDPOINT);
        return new HttpResponse(response);
    }


    @Step("Поиск питомцев по статусам: {statuses}")
    public HttpResponse findPetsByStatus(String... statuses) {
        String statusParam = String.join(",", statuses);
        Response response = given(requestSpecification)
                .queryParam("status", statusParam)
                .get("/pet/findByStatus"); // ← правильный путь!
        return new HttpResponse(response);
    }


    @Step("Получение питомца по ID: {petId}")
    public HttpResponse getPetById(long petId) {
        Response response = given(requestSpecification)
                .get("/pet/" + petId); // ← ДОБАВЛЕН /pet
        return new HttpResponse(response);
    }


    public HttpResponse getPet(Pet pet) {
        Response response = given(requestSpecification)
                .get(PET_ENDPOINT + pet.getId());
        return new HttpResponse(response);
    }

    @Step("Обновление питомца через форму: petId={petId}, name={name}, status={status}")
    public HttpResponse updatePetWithFormData(int petId, String name, String status) {
        Response response = given(requestSpecification)
                .contentType("application/x-www-form-urlencoded")
                .formParam("name", name)
                .formParam("status", status)
                .post("/" + petId);
        return new HttpResponse(response);
    }

    public HttpResponse deletePet(Pet pet) {
        Response response = given(requestSpecification)
                //.header("api_key", apiKey)
                .delete(PET_ENDPOINT + "/" + pet.getId()); // ✅ слэш добавлен
        return new HttpResponse(response);
    }
}