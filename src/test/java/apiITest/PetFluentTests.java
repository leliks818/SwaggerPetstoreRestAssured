package apiITest;

import controller.pet.FluentPetController;
import controller.HttpResponse;
import models.pet.Pet;
import models.pet.Status;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static testData.TestData.*;

public class PetFluentTests {

    private FluentPetController petController;

    @BeforeEach
    public void setUp() {
        petController = new FluentPetController();
    }

    @AfterEach
    public void tearDown() {
        petController.deletePet(DEFAULT_PET);
        petController.deletePet(PET_UPDATE);
    }

    @Test
    @DisplayName("Добавление нового питомца ")
    public void testAddPet() {
        HttpResponse response = petController.addPet(DEFAULT_PET);
        response.statusCodeIs(200)
                .jsonValueIs("name", DEFAULT_PET.getName())
                .jsonValueIs("status", DEFAULT_PET.getStatus());
    }

    @Test
    @DisplayName("Обновление существующего питомца")
    public void testUpdatePet() {
        HttpResponse addResponse = petController.addPet(DEFAULT_PET)
                .statusCodeIs(200);

        Long petId = Long.parseLong(addResponse.getJsonValue("id"));
        Pet petUpdate = Pet.builder()
                .id(petId)
                .status(Status.AVAILABLE)
                .build();

        HttpResponse updateResponse = petController.updatePet(petUpdate)
                .statusCodeIs(200)
                .jsonValueIs("status", Status.AVAILABLE.name());

        Long updatedId = Long.parseLong(updateResponse.getJsonValue("id"));
        assertEquals(petId, updatedId);
    }

    @Test
    @DisplayName("Поиск питомцев по статусу available с проверкой тела ответа")
    void testFindPetsByStatusAvailable() {
        petController.addPet(DEFAULT_PET)
                .statusCodeIs(200);

        HttpResponse response = petController.findPetsByStatus("available")
                .statusCodeIs(200);

        List<Map<String, Object>> pets = response.getJsonList("");
        assertFalse(pets.isEmpty(), "Список питомцев не должен быть пустым");

        for (Map<String, Object> pet : pets) {
            assertEquals("available", pet.get("status"), "Статус питомца должен быть 'available'");
        }
    }

    @Test
    @DisplayName("Получение питомца по ID ")
    void testGetPetById() {
        long petId = 1340;
        Pet petToAdd = DEFAULT_PET.toBuilder().id(petId).build();
        petController.addPet(petToAdd).statusCodeIs(200);

        petController.getPetById(petId)
                .statusCodeIs(200)
                .bodyFieldEquals("id", petId);
    }

    @Test
    void deletePetTest() {
        petController.addPet(DEFAULT_PET)
                .logBody()
                .statusCodeIs(200);
        petController.deletePet(DEFAULT_PET)
                .logBody()
                .statusCodeIs(200)
                .jsonValueIs("code", 200);
    }
    @Test
    void apiUploadTest() {
        // Путь к изображению
        File file = new File("src/test/resources/pet.jpg");

        HttpResponse.uploadImage(file)
                .statusCodeIs(200)
                .bodyFieldEquals("code", 200L)
                .jsonValueContains("message", "pet.jpg");
    }
}

//    @Test
//    void apiUploadTest() {
//        // URL API для загрузки изображения
//        String apiUrl = "https://petstore.swagger.io/v2/pet/1/uploadImage";
//
//        // Создаем объект File, который указывает на изображение для загрузки
//        // Путь к файлу
//        File file = new File("src/test/resources/pet.jpg");
//
//        // Отправляем POST-запрос на API с изображением
//        Response response =
//                given()
//                        .header("accept", "application/json")
//                        .contentType("multipart/form-data")
//                        .multiPart("file", file, "image/jpeg") // Указываем тип содержимого файла
//                        .when()
//                        .post(apiUrl)
//                        .then()
//                        .statusCode(200) // Проверяем, что запрос успешен
//                        .extract()
//                        .response();
//
//        // Выводим ответ сервера
//        System.out.println("Response: " + response.asString());
//    }

