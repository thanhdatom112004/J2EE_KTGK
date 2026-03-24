package com.example.KTGK.service;

import com.example.KTGK.dto.RegisterRequest;
import com.example.KTGK.model.Patient;
import com.example.KTGK.model.Role;
import com.example.KTGK.repository.PatientRepository;
import com.example.KTGK.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepository patientRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {
        if (patientRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại");
        }
        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        Role patientRole = roleRepository.findByName("PATIENT")
                .orElseThrow(() -> new IllegalStateException("Chưa có role PATIENT trong CSDL"));

        Patient patient = new Patient();
        patient.setUsername(request.getUsername());
        patient.setPassword(passwordEncoder.encode(request.getPassword()));
        patient.setEmail(request.getEmail());
        patient.setRoles(new HashSet<>());
        patient.getRoles().add(patientRole);

        patientRepository.save(patient);
    }
}
