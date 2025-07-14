package com.pragma.users.infrastructure.output.jpa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class JpaUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nombre")
    private String firstname;

    @Column(name = "apellido")
    private String lastname;

    @Column(name = "numero_documento")
    private String identityDocument;

    @Column(name = "celular")
    private String phoneNumber;

    @Column(name = "fecha_nacimiento")
    private LocalDate birthdate;

    @Column(name = "correo")
    private String email;

    @Column(name = "clave")
    private String password;

    @Column(name = "id_restaurante")
    private Long restaurantId;

    @ManyToOne
    @JoinColumn(name = "id_role") // Aquí defines la columna específica
    private JpaRoleEntity role;
}
