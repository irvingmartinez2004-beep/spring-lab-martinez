package edu.espe.springlab.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class StudentRequestData {

    @NotBlank
    @Size(min = 3, max = 120)
    private String fullName;

    @NotBlank
    @Email
    @Size(max = 120)
    private String email;

    @NotNull
    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
    private LocalDate birthDate;


    @NotNull(message = "El campo active es obligatorio")
    private Boolean active;




    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
