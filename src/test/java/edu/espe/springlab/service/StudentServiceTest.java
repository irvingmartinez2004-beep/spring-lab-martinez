package edu.espe.springlab.service;

import edu.espe.springlab.domain.Student;
import edu.espe.springlab.dto.StudentRequestData;
import edu.espe.springlab.repository.StudentRepository;
import edu.espe.springlab.service.impl.StudentServiceImpl;
import edu.espe.springlab.web.advice.ConflictException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(StudentServiceImpl.class)
public class StudentServiceTest {

    @Autowired
    private StudentServiceImpl service;

    @Autowired
    private StudentRepository repository;


    // =============================================================
    //  TEST OFICIAL QUE YA TENÍAS (EMAIL DUPLICADO)
    // =============================================================
    @Test
    void shouldNotAllowDuplicateEmail() {
        // Crear y guardar un estudiante existente
        Student existing = new Student();
        existing.setFullName("Existing User");
        existing.setEmail("duplicate@example.com");
        existing.setBirthDate(LocalDate.of(2000, 10, 10));
        existing.setActive(true);

        repository.save(existing);

        // Crear la solicitud con el mismo email
        StudentRequestData req = new StudentRequestData();
        req.setFullName("Another User");
        req.setEmail("duplicate@example.com");
        req.setBirthDate(LocalDate.of(1999, 5, 15));

        // Verificar excepción por email duplicado
        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ConflictException.class);
    }


    // =============================================================
    //  TEST QUE FALLA A PROPÓSITO (PARA PROBAR EL ROLLBACK)
    // =============================================================

    /*
    @Test
    void shouldFailOnPurpose() {
        assertThat(1).isEqualTo(2);
    }
    */


    /*
    // =============================================================
    // TEST 1 — Test que siempre pasa
    // =============================================================
    @Test
    void shouldAlwaysPass() {
        assertThat(true).isTrue();
    }
    */


    /*
    // =============================================================
    // TEST 2 — Validar que Spring carga el contexto
    // =============================================================
    @Test
    void contextLoads() {
        // Si llega aquí, el contexto levantó correctamente
    }
    */



    // =============================================================
    // TEST 3 — Validar fecha futura
    // =============================================================
    @Test
    void shouldRejectFutureBirthDate() {
        StudentRequestData req = new StudentRequestData();
        req.setFullName("Future User");
        req.setEmail("future@mail.com");
        req.setBirthDate(LocalDate.now()); // fecha futura

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(RuntimeException.class); // depende de tu excepción
    }



    /*
    // =============================================================
    // TEST 4 — Verificar stats()
    // =============================================================
    @Test
    void shouldReturnCorrectStats() {

        Student a = new Student("A User", "a@mail.com", LocalDate.now(), true);
        Student b = new Student("B User", "b@mail.com", LocalDate.now(), false);

        repository.save(a);
        repository.save(b);

        Map<String, Long> stats = service.stats();

        assertThat(stats.get("total")).isEqualTo(2);
        assertThat(stats.get("active")).isEqualTo(1);
        assertThat(stats.get("inactive")).isEqualTo(1);
    }
    */


    /*
    // =============================================================
    // TEST 5 — Test de reactivación
    // =============================================================
    @Test
    void shouldReactivateStudent() {

        Student s = new Student();
        s.setFullName("Inactive User");
        s.setEmail("inactive@mail.com");
        s.setBirthDate(LocalDate.of(2000, 1, 1));
        s.setActive(false);

        repository.save(s);

        var response = service.reactivate(s.getId());

        assertThat(response.getActive()).isTrue();
    }
    */


    /*
    // =============================================================
    // TEST 6 — Test simple de suma (unit test básico)
    // =============================================================
    @Test
    void simpleMathShouldPass() {
        int result = 2 + 3;
        assertThat(result).isEqualTo(5);
    }
    */


    /*
    // =============================================================
    // TEST 7 — Validación de DTO (email inválido)
    // =============================================================
    @Test
    void shouldInvalidateWrongEmail() {
        StudentRequestData req = new StudentRequestData();
        req.setFullName("User");
        req.setEmail("not-an-email");

        var validator = jakarta.validation.Validation
                .buildDefaultValidatorFactory()
                .getValidator();

        var violations = validator.validate(req);

        assertThat(violations).isNotEmpty();
    }
    */


    /*
    // =============================================================
    // TEST 8 — Crear estudiante correctamente
    // =============================================================
    @Test
    void shouldCreateStudentCorrectly() {
        StudentRequestData req = new StudentRequestData();
        req.setFullName("Carlos Perez");
        req.setEmail("carlos@mail.com");
        req.setBirthDate(LocalDate.of(1999, 12, 1));

        var response = service.create(req);

        assertThat(response.getEmail()).isEqualTo("carlos@mail.com");
        assertThat(response.getFullName()).isEqualTo("Carlos Perez");
    }
    */


    /*
    // =============================================================
    // TEST 9 — Desactivar estudiante
    // =============================================================
    @Test
    void shouldDeactivateStudent() {
        Student s = new Student("User", "user@mail.com", LocalDate.now(), true);
        s = repository.save(s);

        var response = service.deactivate(s.getId());

        assertThat(response.getActive()).isFalse();
    }
    */


    /*
    // =============================================================
    // TEST 10 — test de findByEmail desde el repo
    // =============================================================
    @Test
    void shouldFindByEmailInRepository() {
        Student s = new Student("User", "search@mail.com", LocalDate.now(), true);
        repository.save(s);

        var found = repository.findByEmail("search@mail.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("search@mail.com");
    }
    */

        /*
    // =============================================================
    // TEST 11 — Buscar ID inexistente debe lanzar NotFoundException
    // =============================================================
    @Test
    void shouldThrowNotFoundWhenIdDoesNotExist() {
        assertThatThrownBy(() -> service.getById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Estudiante no encontrado");
    }
    */


    /*
    // =============================================================
    // TEST 12 — Desactivar estudiante inexistente
    // =============================================================
    @Test
    void shouldThrowWhenDeactivatingNonExistingStudent() {
        assertThatThrownBy(() -> service.deactivate(500L))
                .isInstanceOf(NotFoundException.class);
    }
    */


    /*
    // =============================================================
    // TEST 13 — Reactivar estudiante inexistente
    // =============================================================
    @Test
    void shouldThrowWhenReactivatingNonExistingStudent() {
        assertThatThrownBy(() -> service.reactivate(222L))
                .isInstanceOf(NotFoundException.class);
    }
    */


    /*
    // =============================================================
    // TEST 14 — Crear estudiante activa por defecto
    // =============================================================
    @Test
    void createdStudentShouldBeActiveByDefault() {
        StudentRequestData req = new StudentRequestData();
        req.setFullName("Juan Perez");
        req.setEmail("juan@mail.com");
        req.setBirthDate(LocalDate.of(2000, 1, 1));

        var result = service.create(req);

        assertThat(result.getActive()).isTrue();
    }
    */


    /*
    // =============================================================
    // TEST 15 — Actualizar estudiante correctamente
    // =============================================================
    @Test
    void shouldUpdateStudentCorrectly() {

        // Crear estudiante inicial
        Student s = new Student("Carlos", "a@mail.com",
                LocalDate.of(1999, 5, 20), true);
        s = repository.save(s);

        // Datos nuevos para actualizar
        StudentRequestData req = new StudentRequestData();
        req.setFullName("Carlos Updated");
        req.setEmail("nuevo@mail.com");
        req.setBirthDate(LocalDate.of(2001, 3, 10));
        req.setActive(false);

        var updated = service.update(s.getId(), req);

        assertThat(updated.getFullName()).isEqualTo("Carlos Updated");
        assertThat(updated.getEmail()).isEqualTo("nuevo@mail.com");
        assertThat(updated.getBirthDate()).isEqualTo(LocalDate.of(2001, 3, 10));
        assertThat(updated.getActive()).isFalse();
    }
    */


    /*
    // =============================================================
    // TEST 16 — Actualizar estudiante pero email ya está tomado por otro
    // =============================================================
    @Test
    void shouldFailUpdateIfEmailAlreadyExists() {

        // Usuario 1
        Student s1 = new Student("A", "mail1@mail.com",
                LocalDate.of(2000, 1, 1), true);
        repository.save(s1);

        // Usuario 2
        Student s2 = new Student("B", "mail2@mail.com",
                LocalDate.of(2000, 2, 2), true);
        s2 = repository.save(s2);

        // Intento actualizar a s2 con email de s1
        StudentRequestData req = new StudentRequestData();
        req.setFullName("B Updated");
        req.setEmail("mail1@mail.com"); // correo duplicado
        req.setBirthDate(LocalDate.of(2000, 3, 3));
        req.setActive(true);

        assertThatThrownBy(() -> service.update(s2.getId(), req))
                .isInstanceOf(ConflictException.class);
    }
    */


    /*
    // =============================================================
    // TEST 17 — Lista vacía de estudiantes
    // =============================================================
    @Test
    void listShouldReturnEmptyIfNoStudents() {
        var list = service.list();
        assertThat(list).isEmpty();
    }
    */


    /*
    // =============================================================
    // TEST 18 — Listar varios estudiantes
    // =============================================================
    @Test
    void shouldReturnMultipleStudents() {

        repository.save(new Student("A", "a@mail.com", LocalDate.now(), true));
        repository.save(new Student("B", "b@mail.com", LocalDate.now(), false));
        repository.save(new Student("C", "c@mail.com", LocalDate.now(), true));

        var list = service.list();

        assertThat(list).hasSize(3);
    }
    */


    /*
    // =============================================================
    // TEST 19 — Stats con valores reales
    // =============================================================
    @Test
    void statsShouldReturnCorrectValues() {

        repository.save(new Student("A", "a@mail.com", LocalDate.now(), true));
        repository.save(new Student("B", "b@mail.com", LocalDate.now(), false));
        repository.save(new Student("C", "c@mail.com", LocalDate.now(), true));

        var stats = service.stats();

        assertThat(stats.get("total")).isEqualTo(3);
        assertThat(stats.get("active")).isEqualTo(2);
        assertThat(stats.get("inactive")).isEqualTo(1);
    }
    */


    /*
    // =============================================================
    // TEST 20 — toResponse convierte correctamente
    // =============================================================
    @Test
    void toResponseShouldMapCorrectValues() {
        Student s = new Student("User Test", "test@mail.com",
                LocalDate.of(1999, 9, 9), true);
        s = repository.save(s);

        var r = service.getById(s.getId());

        assertThat(r.getId()).isEqualTo(s.getId());
        assertThat(r.getEmail()).isEqualTo("test@mail.com");
        assertThat(r.getFullName()).isEqualTo("User Test");
        assertThat(r.getBirthDate()).isEqualTo(LocalDate.of(1999, 9, 9));
    }
    */


    /*
    // =============================================================
    // TEST 21 — No permite email nulo
    // (para ver cómo se comporta el servicio sin validación)
    // =============================================================
    @Test
    void shouldFailIfEmailIsNull() {
        StudentRequestData req = new StudentRequestData();
        req.setFullName("No Email");
        req.setEmail(null);
        req.setBirthDate(LocalDate.of(2000, 10, 10));

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(NullPointerException.class);
    }
    */


    /*
    // =============================================================
    // TEST 22 — Desactivar → Reactivar flujo completo
    // =============================================================
    @Test
    void shouldDeactivateThenReactivate() {

        Student s = new Student("User Flow", "flow@mail.com",
                LocalDate.now(), true);
        s = repository.save(s);

        var d = service.deactivate(s.getId());
        assertThat(d.getActive()).isFalse();

        var r = service.reactivate(s.getId());
        assertThat(r.getActive()).isTrue();
    }
    */


}
