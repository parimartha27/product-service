package com.training.product.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name can't be null, empty or blank")
    private String name;

    @Min(value = 1, message="Price must be greater than 0")
    private Double price;

    @NotBlank(message = "Description can't be null, empty or blank")
    private String description;

    @Min(value = 1, message="stock must be greater than 0")
    private int stock;

    @CreationTimestamp
    private LocalDateTime inputtedDate;

    @CreationTimestamp
    private LocalDateTime updatedDate;
}
