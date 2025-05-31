package models.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   // Создаёт геттеры, сеттеры, toString, equals, hashCode
@NoArgsConstructor      // Конструктор без параметров
@AllArgsConstructor     // Конструктор со всеми параметрами
@Builder(toBuilder = true) // Позволяет создавать объект через builder
public class Tags {
    private long id;
    private String name;
}