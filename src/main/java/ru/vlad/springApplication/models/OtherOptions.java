package ru.vlad.springApplication.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherOptions {
    @Id
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
}
