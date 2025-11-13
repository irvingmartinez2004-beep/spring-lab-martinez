package edu.espe.springlab.service.impl;

import edu.espe.springlab.domain.Student;
import edu.espe.springlab.dto.StudentRequestData;
import edu.espe.springlab.dto.StudentResponse;
import edu.espe.springlab.repository.StudentRepository;
import edu.espe.springlab.service.StudentService;
import edu.espe.springlab.web.advice.ConflictException;
import edu.espe.springlab.web.advice.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository repo;

    public StudentServiceImpl(StudentRepository repo) {this.repo = repo;}

    @Override
    public StudentResponse create(StudentRequestData request) {
        if(repo.existsByEmail(request.getEmail())) {
            throw new ConflictException("El email ya esta registrado");
        }
        Student student = new Student();
        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setBirthDate(request.getBirthDate());
        student.setActive(true);

        Student saved = repo.save(student);
        return toResponse(saved);
    }

    @Override
    public StudentResponse getById(Long id) {
        Student student = repo.findById(id).orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));
        return toResponse(student);
    }

    @Override
    public List<StudentResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public StudentResponse deactivate(Long id) {
        Student student = repo.findById(id).orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));
        student.setActive(false);
        return toResponse(repo.save(student));
    }

    @Override
    public StudentResponse update(Long id, StudentRequestData request) {
        Student student = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));

        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setBirthDate(request.getBirthDate());
        student.setActive(request.getActive()); // <-- IMPORTANTE

        Student saved = repo.save(student);
        return toResponse(saved);
    }


    @Override
    public StudentResponse reactivate(Long id) {
        Student student = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));

        student.setActive(true);
        Student saved = repo.save(student);

        return toResponse(saved);
    }

    @Override
    public Map<String, Long> stats() {
        long total = repo.count();
        long active = repo.countByActive(true);
        long inactive = repo.countByActive(false);

        Map<String, Long> result = new HashMap<>();
        result.put("total", total);
        result.put("active", active);
        result.put("inactive", inactive);
        return result;
    }



    private StudentResponse toResponse(Student student){
        StudentResponse r = new StudentResponse();
        r.setId(student.getId());
        r.setFullName(student.getFullName());
        r.setEmail(student.getEmail());
        r.setBirthDate(student.getBirthDate());
        r.setActive(student.getActive());
        return r;
    }
}
