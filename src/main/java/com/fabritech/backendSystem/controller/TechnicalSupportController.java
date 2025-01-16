package com.fabritech.backendSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fabritech.backendSystem.dto.TechnicalSupportDTO;
import com.fabritech.backendSystem.model.TechnicalSupport;
import com.fabritech.backendSystem.repository.TechnicalSupportRepository;
import com.fabritech.backendSystem.service.EmailService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/technical-support")
public class TechnicalSupportController {

    @Autowired
    private TechnicalSupportRepository technicalSupportRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<String> createTechnicalSupportRequest(@RequestBody TechnicalSupportDTO dto) {
        // Save the request to the database
        TechnicalSupport support = new TechnicalSupport();
        support.setServiceProvider(dto.getServiceProvider());
        support.setIssueType(dto.getIssueType());
        support.setSmartCardNumber(dto.getSmartCardNumber());
        support.setIssueDescription(dto.getIssueDescription());
        support.setName(dto.getName());
        support.setPhoneNumber(dto.getPhoneNumber());
        support.setEmail(dto.getEmail());
        support.setStatus("Pending"); // Set default status

        technicalSupportRepository.save(support);

        // Prepare and send confirmation email
        String subject = "Fabritech Technical Support Request Received";
        String body = String.format("""
            Dear %s,

            Thank you for reaching out to Fabritech Technical Support. We have received your support request, and our team is reviewing it.

            SUPPORT REQUEST DETAILS:
            ========================
            Service Provider: %s
            Issue Type: %s
            Smart Card Number: %s
            Issue Description: %s
            Contact Number: %s
            Email: %s
            Status: %s

            NEXT STEPS:
            ===========
            1. Our team will review your issue within 1-2 business days.
            2. You will receive a follow-up email or call with the resolution or further assistance.

            CONTACT INFORMATION:
            ===================
            If you need immediate assistance, please contact us:
            Email: info@fabritech.rw
            Phone: +250788601280

            Best regards,
            Fabritech Technical Support Team

            ----------------------------------------
            Fabritech Ltd
            KG 220 Street, Kigali
            Rwanda

            Note: This email was sent automatically. Please do not reply to this email.
            """,
            support.getName(),
            support.getServiceProvider(),
            support.getIssueType(),
            support.getSmartCardNumber(),
            support.getIssueDescription(),
            support.getPhoneNumber(),
            support.getEmail(),
            support.getStatus()
        );

        // Send plain text email
        emailService.sendEmail(support.getEmail(), subject, body);

        return ResponseEntity.ok("Technical support request submitted successfully and confirmation email sent");
    }
    
    @GetMapping
    public ResponseEntity<List<TechnicalSupport>> getAllTechnicalSupport() {
        List<TechnicalSupport> supports = technicalSupportRepository.findAll();
        return ResponseEntity.ok(supports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnicalSupport> getTechnicalSupportById(@PathVariable Long id) {
        return technicalSupportRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateSupportStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest status) {
        return technicalSupportRepository.findById(id)
                .map(support -> {
                    support.setStatus(status.getStatus());
                    TechnicalSupport updatedSupport = technicalSupportRepository.save(support);
                    
                    // Send status update email
                    sendStatusUpdateEmail(updatedSupport);
                    
                    return ResponseEntity.ok(updatedSupport);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private void sendStatusUpdateEmail(TechnicalSupport support) {
        String subject = "Fabritech Technical Support Status Update";
        String body = String.format("""
            Dear %s,

            Your technical support request status has been updated.

            SUPPORT REQUEST DETAILS:
            =======================
            Request ID: %d
            Service Provider: %s
            Issue Type: %s
            New Status: %s
            
            If you have any questions, please contact us:
            Email: info@fabritech.rw
            Phone: +250788601280

            Best regards,
            Fabritech Technical Support Team
            """,
            support.getName(),
            support.getId(),
            support.getServiceProvider(),
            support.getIssueType(),
            support.getStatus()
        );

        emailService.sendEmail(support.getEmail(), subject, body);
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