package testData;

import models.pet.Category;
import models.pet.Pet;
import models.pet.Status;
import models.User;
import models.pet.Tags;


import java.util.List;

import java.util.Map;


public class TestData {

    public static final User DEFAULT_USER = new User(
            0, "user1", "John", "Doe", "john.doe@email.com",
            "pass123", "1234567890", 1);

    public static final User UPDATE_USER = new User(
            0, "string2", "Updated", "models/User",
            "updated@example.com", "newpass123", "2222222222", 0);


    public static final List<User> TEST_USERS = List.of(
            new User(0, "userArray1", "First1", "Last1", "user1@example.com", "pass1", "1111111111", 1),
            new User(0, "userArray2", "First2", "Last2", "user2@example.com", "pass2", "2222222222", 0)
    );

/// без использования lombok
//    public static final Pet DEFAULT_PET = new Pet(
//            133,
//            new Category(2, "cat"),
//            "dog",
//            List.of("Url"),
//            List.of(new models.pet.Tag(2, "cat")),
//            Status.AVAILABLE
//    );
//    public static final Pet PET_TO_UPDATE;
//
//    static {
//        PET_TO_UPDATE = new Pet();
//        PET_TO_UPDATE.setId(134);
//        PET_TO_UPDATE.setStatus(Status.AVAILABLE);

    public static final Pet DEFAULT_PET = Pet.builder()
            .id(132)
            .category(Category.builder().id(2).name("cat").build())
            .name("dog")
            .photoUrls(List.of("Url"))
            .tags(List.of(Tags.builder().id(2).name("cat").build()))
            .status(Status.AVAILABLE)
            .build();

    public static final Pet PET_UPDATE = Pet.builder()
            .id(1349)
            .status(Status.AVAILABLE)
            .build();
}