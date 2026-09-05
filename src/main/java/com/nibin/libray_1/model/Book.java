package com.nibin.libray_1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(name = "Book", description = "A title in the catalogue, with the number of copies the library holds")
@Table(name = "books")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Assigned by the server", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @NotBlank(message = "must not be blank")
    @Schema(description = "Title of the book", example = "The Hobbit", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "must not be blank")
    @Schema(example = "J.R.R. Tolkien", requiredMode = Schema.RequiredMode.REQUIRED)
    private String author;

    @Min(value = 0, message = "must not be negative")
    @Schema(description = "Copies currently available to borrow", example = "5")
    private int copies;
}
