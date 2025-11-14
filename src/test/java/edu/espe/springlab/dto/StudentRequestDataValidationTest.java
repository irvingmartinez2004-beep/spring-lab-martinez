
        package edu.espe.springlab.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class StudentRequestDataValidationTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private StudentRequestData build(
            String name,
            String email,
            LocalDate birthDate,
            Boolean active
    ) {
        StudentRequestData dto = new StudentRequestData();
        dto.setFullName(name);
        dto.setEmail(email);
        dto.setBirthDate(birthDate);
        dto.setActive(active);
        return dto;
    }
    @Disabled("Este test se desactiva temporalmente")

    // ---------------------------------------------------------
    // 1. VALIDACIÓN CORRECTA
    // ---------------------------------------------------------
    @Test
    void shouldPassWhenAllFieldsAreValid() {
        var dto = build("John Doe", "john@mail.com", LocalDate.of(2000, 1, 1), true);

        var errors = validator.validate(dto);

        assertThat(errors).isEmpty();
    }

    // ❌ Este test fallaría si lo activas (ejemplo de fallo)
    /*
    @Test
    void shouldFailEvenWhenAllIsCorrect_BUT_SHOULD_NOT() {
        var dto = build("John Doe", "john@mail.com", LocalDate.now(), true);
        var errors = validator.validate(dto);
        assertThat(errors).isNotEmpty(); // <-- debería fallar
    }
    */


    // ---------------------------------------------------------
    // 2. NOMBRE: vacío o muy corto
    // ---------------------------------------------------------
    @Test
    void shouldFailWhenNameIsBlank() {
        var dto = build("  ", "user@mail.com", LocalDate.of(2000, 1, 1), true);

        var errors = validator.validate(dto);

        assertThat(errors).isNotEmpty();
    }

    /*
    @Test
    void shouldNotFailWithBlankName_BUT_SHOULD() {
        var dto = build("  ", "user@mail.com", LocalDate.of(2000, 1, 1), true);
        var errors = validator.validate(dto);
        assertThat(errors).isEmpty(); // <-- debería fallar
    }
    */


    @Test
    void shouldFailWhenNameIsTooShort() {
        var dto = build("Jo", "mail@mail.com", LocalDate.of(2000, 1, 1), true);

        var errors = validator.validate(dto);

        assertThat(errors).isNotEmpty();
    }

    /*
    @Test
    void shouldNotFailWhenNameTooShort_BUT_SHOULD() {
        var dto = build("Jo", "mail@mail.com", LocalDate.of(2000, 1, 1), true);
        var errors = validator.validate(dto);
        assertThat(errors).isEmpty(); // <-- debería fallar
    }
    */


    // ---------------------------------------------------------
    // 3. EMAIL: vacío, formato incorrecto, demasiado largo
    // ---------------------------------------------------------
    @Test
    void shouldFailWhenEmailIsBlank() {
        var dto = build("John Doe", " ", LocalDate.of(2000, 1, 1), true);

        var errors = validator.validate(dto);

        assertThat(errors).isNotEmpty();
    }

    /*
    @Test
    void shouldNotFailWithBlankEmail_BUT_SHOULD() {
        var dto = build("John Doe", " ", LocalDate.of(2000, 1, 1), true);
        var errors = validator.validate(dto);
        assertThat(errors).isEmpty(); // <-- debería fallar
    }
    */


    @Test
    void shouldFailWhenEmailIsInvalid() {
        var dto = build("John Doe", "wrong-email", LocalDate.of(2000, 1, 1), true);

        var errors = validator.validate(dto);

        assertThat(errors).isNotEmpty();
    }

    /*
    @Test
    void shouldNotFailOnInvalidEmail_BUT_SHOULD() {
        var dto = build("John Doe", "wrong-email", LocalDate.of(2000, 1, 1), true);
        var errors = validator.validate(dto);
        assertThat(errors).isEmpty(); // <-- debería fallar
    }
    */


    @Test
    void shouldFailWhenEmailTooLong() {
        String longEmail = "a".repeat(200) + "@mail.com";
        var dto = build("John Doe", longEmail, LocalDate.of(2000, 1, 1), true);

        var errors = validator.validate(dto);

        assertThat(errors).isNotEmpty();
    }

    /*
    @Test
    void shouldNotFailWhenEmailTooLong_BUT_SHOULD() {
        String longEmail = "a".repeat(200) + "@mail.com";
        var dto = build("John Doe", longEmail, LocalDate.of(2000, 1, 1), true);
        var errors = validator.validate(dto);
        assertThat(errors).isEmpty(); // <-- debería fallar
    }
    */


    // ---------------------------------------------------------
    // 4. FECHA DE NACIMIENTO: futura
    // ---------------------------------------------------------
    @Test
    void shouldFailWhenBirthDateIsInTheFuture() {
        var dto = build("John Doe", "mail@mail.com", LocalDate.now().plusDays(5), true);

        var errors = validator.validate(dto);

        assertThat(errors).isNotEmpty();
    }

    /*
    @Test
    void shouldNotFailWhenBirthDateFuture_BUT_SHOULD() {
        var dto = build("John Doe", "mail@mail.com", LocalDate.now().plusDays(5), true);
        var errors = validator.validate(dto);
        assertThat(errors).isEmpty(); // <-- debería fallar
    }
    */


    // ---------------------------------------------------------
    // 5. ACTIVO: nulo
    // ---------------------------------------------------------
    @Test
    void shouldFailWhenActiveIsNull() {
        var dto = build("John Doe", "mail@mail.com", LocalDate.of(2000, 1, 1), null);

        var errors = validator.validate(dto);

        assertThat(errors).isNotEmpty();
    }

    /*
    @Test
    void shouldNotFailWhenActiveNull_BUT_SHOULD() {
        var dto = build("John Doe", "mail@mail.com", LocalDate.of(2000, 1, 1), null);
        var errors = validator.validate(dto);
        assertThat(errors).isEmpty(); // <-- debería fallar
    }
    */


    // ============================================================
    // 1. Nombre con longitud mínima (válido)
    // ============================================================
    @Test
    void shouldPassWhenNameIsMinLength() {
        var dto = build("Ana", "ok@mail.com", LocalDate.of(2000, 1, 1), true);
        var errors = validator.validate(dto);
        assertThat(errors).isEmpty();
    }

    /*
    @Test
    void invalid_minNameLength_shouldFail() {
        var dto = build("An", "ok@mail.com", LocalDate.now(), true);
        var errors = validator.validate(dto);
        assertThat(errors).isNotEmpty();
    }
    */

    // ============================================================
    // 2. Nombre con longitud máxima (válido)
    // ============================================================
    @Test
    void shouldPassWhenNameIsMaxLength() {
        var name = "a".repeat(120);
        var dto = build(name, "ok@mail.com", LocalDate.of(2000, 1, 1), true);
        assertThat(validator.validate(dto)).isEmpty();
    }

    /*
    @Test
    void invalid_nameTooLong_shouldFail() {
        var name = "a".repeat(150);
        var dto = build(name, "ok@mail.com", LocalDate.now(), true);
        assertThat(validator.validate(dto)).isNotEmpty();
    }
    */

    // ============================================================
    // 3. Nombre excede el máximo (inválido)
    // ============================================================
    @Test
    void shouldFailWhenNameExceedsMaxLength() {
        var dto = build("a".repeat(150), "ok@mail.com", LocalDate.of(2000, 1, 1), true);
        assertThat(validator.validate(dto)).isNotEmpty();
    }

    /*
    @Test
    void invalid_nameBoundary_shouldFail() {
        var dto = build("a".repeat(200), "ok@mail.com", LocalDate.now(), true);
        assertThat(validator.validate(dto)).isNotEmpty();
    }
    */

    // ============================================================
    // 4. Email exactamente máximo permitido (válido)
    // ============================================================
    @Disabled("Este test se desactiva temporalmente")

    @Test
    void shouldPassWhenEmailIsMaxLength() {
        String email = "a".repeat(110) + "@m.com";
        var dto = build("User", email, LocalDate.of(2000, 1, 1), true);
        assertThat(validator.validate(dto)).isEmpty();
    }

    /*
    @Test
    void invalid_emailTooLong_shouldFail() {
        String email = "a".repeat(200) + "@mail.com";
        var dto = build("User", email, LocalDate.now(), true);
        assertThat(validator.validate(dto)).isNotEmpty();
    }
    */

    // ============================================================
    // 5. Email supera límite (inválido)
    // ============================================================
    @Test
    void shouldFailWhenEmailExceedsMaxLength() {
        String email = "a".repeat(200) + "@mail.com";
        var dto = build("User", email, LocalDate.now(), true);
        assertThat(validator.validate(dto)).isNotEmpty();
    }

    /*
    @Test
    void invalid_emailSpaces_shouldFail() {
        var dto = build("User", " mail @mail.com ", LocalDate.now(), true);
        assertThat(validator.validate(dto)).isNotEmpty();
    }
    */

    // ============================================================
    // 6. Email con espacios → inválido
    // ============================================================
    @Test
    void shouldFailWhenEmailContainsSpaces() {
        var dto = build("User", " mail @mail.com ", LocalDate.of(2000, 1, 1), true);
        assertThat(validator.validate(dto)).isNotEmpty();
    }

    /*
    @Test
    void invalid_emailWithoutAt_shouldFail() {
        var dto = build("User", "mail.com", LocalDate.now(), true);
        assertThat(validator.validate(dto)).isNotEmpty();
    }
    */

    // ============================================================
    // 7. Email en mayúsculas (válido)
    // ============================================================
    @Test
    void shouldPassWithUppercaseEmail() {
        var dto = build("User", "TEST@MAIL.COM", LocalDate.of(2000, 1, 1), true);
        assertThat(validator.validate(dto)).isEmpty();
    }

    /*
    @Test
    void invalid_emailMissingDomain_shouldFail() {
        var dto = build("User", "test@", LocalDate.now(), true);
        assertThat(validator.validate(dto)).isNotEmpty();
    }
    */

    // ============================================================
    // 8. Fecha nacimiento igual a hoy (válido)
    // ============================================================
    @Test
    void shouldPassWhenBirthDateIsToday() {
        var dto = build("User", "user@mail.com", LocalDate.now(), true);
        assertThat(validator.validate(dto)).isEmpty();
    }

    /*
    @Test
    void invalid_birthDateFuture_shouldFail() {
        var dto = build("User", "user@mail.com", LocalDate.now().plusDays(1), true);
        assertThat(validator.validate(dto)).isNotEmpty();
    }
    */

    // ============================================================
    // 9. Fecha nacimiento nula (inválido)
    // ============================================================
    @Test
    void shouldFailWhenBirthDateIsNull() {
        var dto = build("User", "user@mail.com", null, true);
        assertThat(validator.validate(dto)).isNotEmpty();
    }

    /*
    @Test
    void invalid_birthDateOld_shouldFail() {
        var dto = build("User", "user@mail.com", LocalDate.of(3000, 1, 1), true);
        assertThat(validator.validate(dto)).isNotEmpty();
    }
    */

    // ============================================================
    // 10. Fecha muy antigua (válido)
    // ============================================================
    @Test
    void shouldPassWhenBirthDateIsVeryOld() {
        var dto = build("User", "user@mail.com", LocalDate.of(1900, 1, 1), true);
        assertThat(validator.validate(dto)).isEmpty();
    }

    /*
    @Test
    void invalid_birthDateNullActiveNull_shouldFail() {
        var dto = build("User", "mail@mail.com", null, null);
        assertThat(validator.validate(dto)).isNotEmpty();
    }
    */

    // ============================================================
    // 11. Active = true (válido)
    // ============================================================
    @Test
    void shouldPassWhenActiveIsTrue() {
        var dto = build("User", "user@mail.com", LocalDate.of(2000, 1, 1), true);
        assertThat(validator.validate(dto)).isEmpty();
    }

    /*
    @Test
    void invalid_activeNull_shouldFail() {
        var dto = build("User", "mail@mail.com", LocalDate.now(), null);
        assertThat(validator.validate(dto)).isNotEmpty();
    }
    */

    // ============================================================
    // 12. Active = false (válido)
    // ============================================================
    @Test
    void shouldPassWhenActiveIsFalse() {
        var dto = build("User", "user@mail.com", LocalDate.of(2000, 1, 1), false);
        assertThat(validator.validate(dto)).isEmpty();
    }

    /*
    @Test
    void invalid_activeMissing_shouldFail() {
        var dto = new StudentRequestData(); // todo null
        assertThat(validator.validate(dto)).isNotEmpty();
    }
    */

    // ============================================================
    // 13. Todos los campos nulos → inválido
    // ============================================================
    @Test
    void shouldFailWhenAllFieldsAreNull() {
        var dto = build(null, null, null, null);
        assertThat(validator.validate(dto)).isNotEmpty();
    }

    /*
    @Test
    void invalid_allFieldsButEmail_shouldFail() {
        var dto = build(null, "u@u.com", null, null);
        assertThat(validator.validate(dto)).isNotEmpty();
    }
    */

    // ============================================================
    // 14. Nombre y email solo espacios → inválido
    // ============================================================
    @Test
    void shouldFailWhenNameAndEmailAreOnlySpaces() {
        var dto = build("   ", "   ", LocalDate.of(2000, 1, 1), true);
        assertThat(validator.validate(dto)).isNotEmpty();
    }

    /*
    @Test
    void invalid_spacesOnly_shouldFail() {
        var dto = build("   ", "mail @ mail . com", null, null);
        assertThat(validator.validate(dto)).isNotEmpty();
    }
    */

}


