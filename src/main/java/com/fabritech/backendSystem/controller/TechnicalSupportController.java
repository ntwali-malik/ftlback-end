package com.fabritech.backendSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fabritech.backendSystem.dto.TechnicalSupportDTO;
import com.fabritech.backendSystem.model.TechnicalSupport;
import com.fabritech.backendSystem.repository.TechnicalSupportRepository;

@RestController
@RequestMapping("/api/technical-support")
public class TechnicalSupportController {

    @Autowired
    private TechnicalSupportRepository technicalSupportRepository;

    @PostMapping
    public ResponseEntity<String> createTechnicalSupportRequest(@RequestBody TechnicalSupportDTO dto) {
        TechnicalSupport support = new TechnicalSupport();
        support.setServiceProvider(dto.getServiceProvider());
        support.setIssueType(dto.getIssueType());
        support.setSmartCardNumber(dto.getSmartCardNumber());
        support.setIssueDescription(dto.getIssueDescription());
        support.setName(dto.getName());
        support.setPhoneNumber(dto.getPhoneNumber());
        support.setEmail(dto.getEmail());

        technicalSupportRepository.save(support);
        return ResponseEntity.ok("Technical support request submitted successfully");
    }
}
