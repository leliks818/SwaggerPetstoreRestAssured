package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserBuilder {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private int userStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userStatus == user.getUserStatus() &&
                Objects.equals(username, user.getUsername()) &&
                Objects.equals(firstName, user.getFirstName()) &&
                Objects.equals(lastName, user.getLastName()) &&
                Objects.equals(email, user.getEmail()) &&
                Objects.equals(password, user.getPassword()) &&
                Objects.equals(phone, user.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, email, password, phone, userStatus);
    }

    @Override
    public String toString() {
        return "User {" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", userStatus=" + userStatus +
                '}';
    }
}