package testData;

import models.User;

import java.util.List;
import java.util.Map;

public class TestData {

    public static final User DEFAULT_USER = new User(
            0, "user1", "John", "Doe", "john.doe@email.com",
            "pass123", "1234567890", 1);

    public static final User UPDATE_USER = new User(
            0, "string2", "Updated", "User",
            "updated@example.com", "newpass123", "2222222222", 0);


    public static final List<User> TEST_USERS = List.of(
            new User(0, "userArray1", "First1", "Last1", "user1@example.com", "pass1", "1111111111", 1),
            new User(0, "userArray2", "First2", "Last2", "user2@example.com", "pass2", "2222222222", 0)
    );

    public class TestHeaders {
        public static final Map<String, String> DEFAULT_HEADERS = Map.of(
                "Content-Type", "application/json",
                "Accept", "application/json"
        );
    }

}
