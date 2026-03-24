package com.example.KTGK.controller;

import com.example.KTGK.dto.AppointmentForm;
import com.example.KTGK.model.Doctor;
import com.example.KTGK.service.AppointmentService;
import com.example.KTGK.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/enroll")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public AppointmentController(AppointmentService appointmentService, DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    @GetMapping("/{doctorId}")
    public String bookForm(@PathVariable Long doctorId, Model model) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointmentForm", new AppointmentForm());
        return "appointment-form";
    }

    @PostMapping("/{doctorId}")
    public String book(@PathVariable Long doctorId,
                       @Valid @ModelAttribute AppointmentForm appointmentForm,
                       BindingResult bindingResult,
                       Authentication authentication,
                       Model model) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctor", doctor);
            return "appointment-form";
        }
        appointmentService.bookAppointment(authentication.getName(), doctorId, appointmentForm.getAppointmentDate());
        return "redirect:/enroll/my-appointments";
    }

    @GetMapping("/my-appointments")
    public String myAppointments(Authentication authentication, Model model) {
        model.addAttribute("appointments", appointmentService.getMyAppointments(authentication.getName()));
        return "my-appointments";
    }
}
