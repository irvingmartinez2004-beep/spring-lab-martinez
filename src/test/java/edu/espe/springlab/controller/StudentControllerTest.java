package edu.espe.springlab.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.espe.springlab.dto.StudentRequestData;
import edu.espe.springlab.dto.StudentResponse;
import edu.espe.springlab.service.StudentService;
import edu.espe.springlab.web.controller.StudentController;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;  // <----- ESTA ES LA CORRECTA

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SuppressWarnings("deprecation")
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    @SuppressWarnings("deprecation")
    private StudentService service;

    private StudentRequestData req(String name, String email) {
        StudentRequestData r = new StudentRequestData();
        r.setFullName(name);
        r.setEmail(email);
        r.setBirthDate(LocalDate.of(2000,1,1));
        r.setActive(true);
        return r;
    }

    private StudentResponse res(Long id, String name, String email, boolean active) {
        StudentResponse r = new StudentResponse();
        r.setId(id);
        r.setFullName(name);
        r.setEmail(email);
        r.setBirthDate(LocalDate.of(2000,1,1));
        r.setActive(active);
        return r;
    }

    private static final String BASE = "/api/martinezirving/students";

    // -------------------------------------------------------
    // 1. CREATE (POST)
    // -------------------------------------------------------
    @Disabled("Este test se desactiva temporalmente")

    @Test
    void shouldCreateStudent() throws Exception {
        StudentRequestData r = req("Juan", "juan@test.com");
        StudentResponse s = res(1L, "Juan", "juan@test.com", true);

        Mockito.when(service.create(any())).thenReturn(s);

        mvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan@test.com"));
    }

    /*
    @Test  // ❌ INCORRECTO: No envía body
    void wrongCreate() throws Exception {
        mvc.perform(post(BASE))
                .andExpect(status().isCreated());
    }
    */

    // -------------------------------------------------------
    // 2. GET BY ID
    // -------------------------------------------------------
    @Test
    void shouldGetById() throws Exception {
        Mockito.when(service.getById(1L))
                .thenReturn(res(1L, "Ana", "ana@test.com", true));

        mvc.perform(get(BASE + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana@test.com"));
    }

    /*
    @Test // ❌ INCORRECTO: ID inválido, pero espera OK
    void wrongGetById() throws Exception {
        mvc.perform(get(BASE + "/999"))
                .andExpect(status().isOk());
    }
    */

    // -------------------------------------------------------
    // 3. GET ALL
    // -------------------------------------------------------
    @Test
    void shouldReturnAllStudents() throws Exception {
        List<StudentResponse> list = List.of(
                res(1L, "A", "a@mail.com", true),
                res(2L, "B", "b@mail.com", true)
        );

        Mockito.when(service.list()).thenReturn(list);

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    /*
    @Test // ❌ INCORRECTO: No compara tamaño
    void wrongGetAll() throws Exception {
        mvc.perform(get(BASE))
                .andExpect(status().isOk());
    }
    */

    // -------------------------------------------------------
    // 4. DEACTIVATE
    // -------------------------------------------------------
    @Test
    void shouldDeactivateStudent() throws Exception {
        Mockito.when(service.deactivate(1L))
                .thenReturn(res(1L, "User", "u@test.com", false));

        mvc.perform(patch(BASE + "/1/deactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }

    /*
    @Test // ❌ INCORRECTO: Olvida el /deactivate
    void wrongDeactivate() throws Exception {
        mvc.perform(patch(BASE + "/1"))
                .andExpect(status().isOk());
    }
    */

    // -------------------------------------------------------
    // 5. REACTIVATE
    // -------------------------------------------------------
    @Test
    void shouldReactivateStudent() throws Exception {
        Mockito.when(service.reactivate(1L))
                .thenReturn(res(1L, "User", "u@test.com", true));

        mvc.perform(patch(BASE + "/1/reactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }

    /*
    @Test // ❌ INCORRECTO
    void wrongReactivate() throws Exception {
        mvc.perform(patch(BASE + "/reactivate"))
                .andExpect(status().isOk());
    }
    */

    // -------------------------------------------------------
    // 6. UPDATE (PUT)
    // -------------------------------------------------------
    @Test
    void shouldUpdateStudent() throws Exception {
        StudentRequestData r = req("Nuevo", "update@test.com");

        Mockito.when(service.update(eq(1L), any()))
                .thenReturn(res(1L, "Nuevo", "update@test.com", true));

        mvc.perform(put(BASE + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Nuevo"))
                .andExpect(jsonPath("$.email").value("update@test.com"));
    }

    /*
    @Test // ❌ INCORRECTO: Sin body
    void wrongUpdate() throws Exception {
        mvc.perform(put(BASE + "/1"))
                .andExpect(status().isOk());
    }
    */

    // -------------------------------------------------------
    // 7. STATS
    // -------------------------------------------------------
    @Test
    void shouldReturnStats() throws Exception {
        Map<String, Long> map = Map.of(
                "total", 10L,
                "active", 8L,
                "inactive", 2L
        );

        Mockito.when(service.stats()).thenReturn(map);

        mvc.perform(get(BASE + "/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(10))
                .andExpect(jsonPath("$.active").value(8))
                .andExpect(jsonPath("$.inactive").value(2));
    }

    /*
    @Test // ❌ INCORRECTO: Espera array en vez de objeto
    void wrongStats() throws Exception {
        mvc.perform(get(BASE + "/stats"))
                .andExpect(jsonPath("$.length()").value(3));
    }
    */
}
