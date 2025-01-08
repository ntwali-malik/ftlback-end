package com.fabritech.backendSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.fabritech.backendSystem.dto.InternshipRegistrationDTO;
import com.fabritech.backendSystem.model.InternshipRegistration;
import com.fabritech.backendSystem.repository.InternshipRegistrationRepository;
import com.fabritech.backendSystem.service.EmailService;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/internships")
public class InternshipRegistrationController {

    @Autowired
    private InternshipRegistrationRepository repository;
    
    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<String> registerIntern(@RequestBody InternshipRegistrationDTO dto) {
        InternshipRegistration registration = new InternshipRegistration();
        registration.setFullName(dto.getFullName());
        registration.setEmail(dto.getEmail());
        registration.setPhone(dto.getPhone());
        registration.setProgram(dto.getProgram());
        registration.setEducation(dto.getEducation());
        registration.setStartDate(dto.getStartDate());

        repository.save(registration);

        // Create a plain text email template
        String subject = "Welcome to Fabritech Internship Program";
        String body = String.format("""
            Dear %s,

            Thank you for applying to the Fabritech Internship Program. We are pleased to confirm that your registration has been successfully received.

            REGISTRATION DETAILS:
            ====================
            Program: %s
            Start Date: %s
            Contact Number: %s
            Email: %s
            Education Background: %s

            NEXT STEPS:
            ===========
            1. Our team will review your application within 2-3 business days.
            2. You will receive another email with the status of your application.
            3. If accepted, we will schedule an orientation session before your start date.

            IMPORTANT NOTE:
            ==============
            Please ensure to arrive 15 minutes early on your first day and bring a valid ID.

            CONTACT INFORMATION:
            ===================
            If you have any questions or need to update your information, please don't hesitate to contact us:
            Email: info@fabritech.rw
            Phone: +250788601280

            Best regards,
            The Fabritech Team

            ----------------------------------------
            Fabritech Ltd
            KN 78 St, Gasabo, Kigali
            Rwanda

            Note: This email was sent automatically. Please do not reply to this email.
            """,
            registration.getFullName(),
            registration.getProgram(),
            registration.getStartDate(),
            registration.getPhone(),
            registration.getEmail(),
            registration.getEducation()
        );

        // Send plain text email
        emailService.sendEmail(registration.getEmail(), subject, body);

        return ResponseEntity.ok("Internship registration successful and confirmation email sent");
    }

    // Get all internship registrations
    @GetMapping
    public List<InternshipRegistration> getAllRegistrations() {
        return repository.findAll();
    }

    // Get an internship registration by ID
    @GetMapping("/{id}")
    public ResponseEntity<InternshipRegistration> getRegistrationById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete an internship registration by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRegistration(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok("Internship registration deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}