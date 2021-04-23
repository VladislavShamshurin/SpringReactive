package ru.vlad.springApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.vlad.springApplication.validation.annotation.CheckName;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("car")
@CheckName
public class CarDto {
    @Id
    private Long id;

    @Column(value = "brand")
    private String brand;

    @Column("wheels_id")
    private Long wheelsId;

    @Column("transmission_id")
    private Long transmissionId;

    @Column("engine_id")
    private Long engineId;

    @Column("other_option_id")
    private Long otherOptionId;
}
