package com.example.KTGK.config;

import com.example.KTGK.model.Patient;
import com.example.KTGK.model.Role;
import com.example.KTGK.repository.PatientRepository;
import com.example.KTGK.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initData(RoleRepository roleRepository,
                               PatientRepository patientRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ADMIN");
                        return roleRepository.save(role);
                    });

            roleRepository.findByName("PATIENT")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("PATIENT");
                        return roleRepository.save(role);
                    });

            Optional<Patient> adminOpt = patientRepository.findByUsername("admin");
            Patient admin = adminOpt.orElseGet(Patient::new);

            if (admin.getId() == null) {
                admin.setUsername("admin");
                admin.setEmail("admin@qlbv.vn");
                admin.setRoles(new HashSet<>());
            }

            // Ensure default admin credentials are always valid for demo/login testing.
            admin.setPassword(passwordEncoder.encode("admin123"));

            boolean hasAdminRole = admin.getRoles().stream()
                    .anyMatch(role -> "ADMIN".equals(role.getName()));
            if (!hasAdminRole) {
                admin.getRoles().add(adminRole);
            }

            patientRepository.save(admin);
        };
    }
}
