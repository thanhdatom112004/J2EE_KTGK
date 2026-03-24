package com.example.KTGK.service;

import com.example.KTGK.dto.DoctorForm;
import com.example.KTGK.model.Department;
import com.example.KTGK.model.Doctor;
import com.example.KTGK.repository.DepartmentRepository;
import com.example.KTGK.repository.DoctorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    public DoctorService(DoctorRepository doctorRepository, DepartmentRepository departmentRepository) {
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
    }

    public Page<Doctor> getDoctorsPage(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword == null || keyword.isBlank()) {
            return doctorRepository.findAll(pageable);
        }
        return doctorRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ"));
    }

    public void createDoctor(DoctorForm form) {
        Department department = departmentRepository.findById(form.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Khoa không tồn tại"));

        Doctor doctor = new Doctor();
        doctor.setName(form.getName());
        doctor.setSpecialty(form.getSpecialty());
        doctor.setImage(form.getImage());
        doctor.setDepartment(department);
        doctorRepository.save(doctor);
    }

    public void updateDoctor(Long id, DoctorForm form) {
        Department department = departmentRepository.findById(form.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Khoa không tồn tại"));

        Doctor doctor = getDoctorById(id);
        doctor.setName(form.getName());
        doctor.setSpecialty(form.getSpecialty());
        doctor.setImage(form.getImage());
        doctor.setDepartment(department);
        doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}
