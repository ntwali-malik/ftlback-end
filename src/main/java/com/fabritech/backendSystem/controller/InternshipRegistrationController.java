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
        registration.setStatus("Pending"); // Set default status

        repository.save(registration);

        // Send confirmation email
        sendConfirmationEmail(registration);

        return ResponseEntity.ok("Internship registration successful and confirmation email sent");
    }

    @GetMapping
    public List<InternshipRegistration> getAllRegistrations() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InternshipRegistration> getRegistrationById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRegistration(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok("Internship registration deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest status) {
        return repository.findById(id)
                .map(registration -> {
                    registration.setStatus(status.getStatus());
                    repository.save(registration);
                    
                    // Send status update email
                    sendStatusUpdateEmail(registration);
                    
                    return ResponseEntity.ok("Status updated successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private void sendConfirmationEmail(InternshipRegistration registration) {
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

        emailService.sendEmail(registration.getEmail(), subject, body);
    }

    private void sendStatusUpdateEmail(InternshipRegistration registration) {
        String subject = "Fabritech Internship Application Update";
        String body;

        if ("Approved".equals(registration.getStatus())) {
            body = String.format("""
                Dear %s,

                Congratulations! Your application for the Fabritech Internship Program has been APPROVED.

                INTERNSHIP DETAILS:
                ==================
                Program: %s
                Start Date: %s
                
                NEXT STEPS:
                ===========
                1. Orientation Session: You will be contacted shortly with orientation details
                2. Start Date: %s
                3. Location: YYUSSA Plaza, Kisimenti, Remera

                IMPORTANT REQUIREMENTS:
                =====================
                - Please arrive 15 minutes early on your first day
                - Bring a valid ID
                - Dress code: Business casual
                - Bring your laptop (if you have one)

                If you have any questions, please contact us:
                Email: info@fabritech.rw
                Phone: +250788601280

                Welcome to the Fabritech team!

                Best regards,
                The Fabritech Team
                """,
                registration.getFullName(),
                registration.getProgram(),
                registration.getStartDate(),
                registration.getStartDate()
            );
        } else if ("Rejected".equals(registration.getStatus())) {
            body = String.format("""
                Dear %s,

                Thank you for your interest in the Fabritech Internship Program.

                After careful consideration of your application, we regret to inform you that we are unable to offer you an internship position at this time.

                We encourage you to:
                - Continue developing your skills
                - Apply for future opportunities
                - Keep following our company for future openings

                We wish you the best in your future endeavors.

                Best regards,
                The Fabritech Team
                """,
                registration.getFullName()
            );
        } else {
            body = String.format("""
                Dear %s,

                Your internship application status has been updated to: %s

                If you have any questions, please contact us:
                Email: info@fabritech.rw
                Phone: +250788601280

                Best regards,
                The Fabritech Team
                """,
                registration.getFullName(),
                registration.getStatus()
            );
        }

        emailService.sendEmail(registration.getEmail(), subject, body);
    }
}

//class StatusUpdateRequest {
//    private String status;
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//}