package com.nibin.libray_1.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "book")
public class Book  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @NotBlank(message = "must not be blank")
    private String name;

    @NotBlank(message = "must not be blank")
    private String author;

    @Min(value = 0, message = "must not be negative")
    private int copies;
}
