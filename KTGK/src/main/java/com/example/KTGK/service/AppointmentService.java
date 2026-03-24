package com.example.KTGK.service;

import com.example.KTGK.model.Appointment;
import com.example.KTGK.model.Doctor;
import com.example.KTGK.model.Patient;
import com.example.KTGK.repository.AppointmentRepository;
import com.example.KTGK.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorService doctorService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
    }

    public void bookAppointment(String username, Long doctorId, LocalDate date) {
        Patient patient = patientRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bệnh nhân"));
        Doctor doctor = doctorService.getDoctorById(doctorId);

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(date);
        appointmentRepository.save(appointment);
    }

    public List<Appointment> getMyAppointments(String username) {
        Patient patient = patientRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bệnh nhân"));
        return appointmentRepository.findByPatientOrderByAppointmentDateDesc(patient);
    }
}
