package org.vaadin.example.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Person implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "Informe o nome completo")
    private String fullName;

    @Column(nullable = false)
    @NotNull(message = "Informe a data de nascimento")
    @DateTimeFormat
    private LocalDate birthDate;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[0-9]{3}[.]?[0-9]{3}[.]?[0-9]{3}-[0-9]{2}$", message = "Informe o CPF no formato xxx.xxx.xxx-xx")
    @CPF(message = "Informe um CPF válido")
    private String cpf;

    @Column(nullable = false)
    @Email(
        regexp = "^" + "([a-zA-Z0-9_\\.\\-+])+" // local
                 + "@" + "[a-zA-Z0-9-.]+" // domain
                 + "\\." + "[a-zA-Z0-9-]{2,}" // tld
                 + "$",
        message = "Informe um endereço de e-mail válido"
    )
    private String email;

    @Column
    private String profession;

    public Person() { }

    public Person(String fullName,
                  LocalDate birthDate,
                  String cpf,
                  String email,
                  String profession) {
        
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.cpf = cpf;
        this.email = email;
        this.profession = profession;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getProfession() {
        return profession;
    }    

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPersisted() {
        return id != null;
    }

    @Override
    public String toString() {
        return "{ fullName=" + fullName +
               "\n birthDate=" + birthDate +
               "\n cpf=" + cpf +
               "\n email=" + email +
               "\n profession=" + profession + " }";
    }
}
