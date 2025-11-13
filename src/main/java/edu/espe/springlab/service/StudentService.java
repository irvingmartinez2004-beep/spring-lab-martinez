package edu.espe.springlab.service;

import edu.espe.springlab.dto.StudentRequestData;
import edu.espe.springlab.dto.StudentResponse;

import java.util.List;
import java.util.Map;

public interface StudentService {

    //Crear un estudiante a partir del DTO validado
    StudentResponse create(StudentRequestData request);

    //Busqueda por ID
    StudentResponse getById(Long id);

    //Listar todos los estudiantes
    List<StudentResponse> list();

    //Cambiar estado del estudiante
    StudentResponse deactivate(Long id);


    StudentResponse reactivate(Long id);

    StudentResponse update(Long id, StudentRequestData request);

    Map<String, Long> stats();



}
