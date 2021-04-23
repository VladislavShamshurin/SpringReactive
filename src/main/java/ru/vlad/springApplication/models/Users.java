package ru.vlad.springApplication.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Model<Long> {
    private String name;
    private String email;
    private String phone;
    @Id
    private Long id;
}
