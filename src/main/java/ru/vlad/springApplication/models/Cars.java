package ru.vlad.springApplication.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cars implements Model<Long> {
    @Id
    private Long id;

    private String brand;

    private Wheels wheels;

    private Transmissions transmission;

    private Engines engine;

    private List<OtherOptions> otherOptions;
}
