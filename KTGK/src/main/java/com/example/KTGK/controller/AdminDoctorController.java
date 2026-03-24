package com.example.KTGK.controller;

import com.example.KTGK.dto.DoctorForm;
import com.example.KTGK.model.Department;
import com.example.KTGK.model.Doctor;
import com.example.KTGK.repository.DepartmentRepository;
import com.example.KTGK.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/doctors")
public class AdminDoctorController {
    private final DoctorService doctorService;
    private final DepartmentRepository departmentRepository;

    public AdminDoctorController(DoctorService doctorService, DepartmentRepository departmentRepository) {
        this.doctorService = doctorService;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public String list(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "admin/doctor-list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("doctorForm", new DoctorForm());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("action", "create");
        return "admin/doctor-form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute DoctorForm doctorForm,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            loadFormData(model, "create");
            return "admin/doctor-form";
        }
        doctorService.createDoctor(doctorForm);
        return "redirect:/admin/doctors";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id);
        DoctorForm form = new DoctorForm();
        form.setName(doctor.getName());
        form.setSpecialty(doctor.getSpecialty());
        form.setImage(doctor.getImage());
        form.setDepartmentId(doctor.getDepartment() != null ? doctor.getDepartment().getId() : null);

        model.addAttribute("doctorForm", form);
        model.addAttribute("doctorId", id);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("action", "edit");
        return "admin/doctor-form";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute DoctorForm doctorForm,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctorId", id);
            loadFormData(model, "edit");
            return "admin/doctor-form";
        }
        doctorService.updateDoctor(id, doctorForm);
        return "redirect:/admin/doctors";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return "redirect:/admin/doctors";
    }

    private void loadFormData(Model model, String action) {
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("action", action);
    }
}
