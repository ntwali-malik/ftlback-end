package com.fabritech.backendSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.fabritech.backendSystem.dto.DecoderOrderDTO;
import com.fabritech.backendSystem.model.DecoderOrder;
import com.fabritech.backendSystem.repository.DecoderOrderRepository;
import com.fabritech.backendSystem.service.EmailService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/decoder-orders")
public class DecoderOrderController {

    @Autowired
    private DecoderOrderRepository decoderOrderRepository;

    @Autowired
    private EmailService emailService;

    // Get all orders
    @GetMapping
    public List<DecoderOrder> getAllOrders() {
        return decoderOrderRepository.findAll();
    }

    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<DecoderOrder> getOrderById(@PathVariable Long id) {
        return decoderOrderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update order status
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest status) {
        return decoderOrderRepository.findById(id)
                .map(order -> {
                    order.setStatus(status.getStatus());
                    DecoderOrder updatedOrder = decoderOrderRepository.save(order);
                    
                    // Send status update email
                    sendStatusUpdateEmail(updatedOrder);
                    
                    return ResponseEntity.ok(updatedOrder);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createDecoderOrder(@RequestBody DecoderOrderDTO dto) {
        // Create new order
        DecoderOrder order = new DecoderOrder();
        order.setDecoderType(dto.getDecoderType());
        order.setInstallationType(dto.getInstallationType());
        order.setName(dto.getName());
        order.setPhoneNumber(dto.getPhoneNumber());
        order.setEmail(dto.getEmail());
        order.setLocation(dto.getLocation());
        order.setStatus("Pending"); // Set default status

        decoderOrderRepository.save(order);

        // Send confirmation email
        sendConfirmationEmail(order);

        return ResponseEntity.ok("Decoder order submitted successfully and confirmation email sent");
    }

    private void sendConfirmationEmail(DecoderOrder order) {
        String subject = "Fabritech Decoder Order Confirmation";
        String body = String.format("""
            Dear %s,

            Thank you for placing your decoder order with Fabritech. We have received your request, and it is being processed.

            ORDER DETAILS:
            ==============
            Decoder Type: %s
            Installation Type: %s
            Contact Number: %s
            Email: %s
            Location: %s
            Status: %s

            NEXT STEPS:
            ===========
            1. Our team will review your order and contact you within 1-2 hours.
            2. You will receive a follow-up email or call with further updates.

            CONTACT INFORMATION:
            ====================
            If you need immediate assistance, please contact us:
            Email: info@fabritech.rw
            Phone: +250788601280

            Best regards,
            Fabritech Decoder Order Team

            ----------------------------------------
            Fabritech Ltd
            KG 220 Street, Kigali
            Rwanda

            Note: This email was sent automatically. Please do not reply to this email.
            """,
            order.getName(),
            order.getDecoderType(),
            order.getInstallationType(),
            order.getPhoneNumber(),
            order.getEmail(),
            order.getLocation(),
            order.getStatus()
        );

        emailService.sendEmail(order.getEmail(), subject, body);
    }

    private void sendStatusUpdateEmail(DecoderOrder order) {
        String subject = "Fabritech Decoder Order Status Update";
        String body = String.format("""
            Dear %s,

            Your decoder order status has been updated.

            ORDER DETAILS:
            ==============
            Order ID: %d
            New Status: %s
            
            If you have any questions, please contact us:
            Email: info@fabritech.rw
            Phone: +250788601280

            Best regards,
            Fabritech Team
            """,
            order.getName(),
            order.getId(),
            order.getStatus()
        );

        emailService.sendEmail(order.getEmail(), subject, body);
    }
}

// Add this class at the bottom of the file or in a separate file
class StatusUpdateRequest {
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}