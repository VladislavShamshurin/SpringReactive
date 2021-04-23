package ru.vlad.springApplication.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wheels implements Model<Long> {
    @Id
    private Long id;
    private String description;
    private BigDecimal price;
    private int radius;
}
