package com.nibin.libray_1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(name = "User", description = "Someone who can borrow books")
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Assigned by the server", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @NotBlank(message = "must not be blank")
    @Column(name = "name")
    @Schema(example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "Reserved for suspending a borrower; not enforced yet", example = "false")
    private boolean status;
}
