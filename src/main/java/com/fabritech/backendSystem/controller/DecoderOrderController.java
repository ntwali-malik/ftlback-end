package com.fabritech.backendSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fabritech.backendSystem.dto.DecoderOrderDTO;
import com.fabritech.backendSystem.model.DecoderOrder;
import com.fabritech.backendSystem.repository.DecoderOrderRepository;

@RestController
@RequestMapping("/api/decoder-orders")
public class DecoderOrderController {

    @Autowired
    private DecoderOrderRepository decoderOrderRepository;

    @PostMapping
    public ResponseEntity<String> createDecoderOrder(@RequestBody DecoderOrderDTO dto) {
        DecoderOrder order = new DecoderOrder();
        order.setDecoderType(dto.getDecoderType());
        order.setInstallationType(dto.getInstallationType());
        order.setName(dto.getName());
        order.setPhoneNumber(dto.getPhoneNumber());
        order.setEmail(dto.getEmail());
        order.setLocation(dto.getLocation());

        decoderOrderRepository.save(order);
        return ResponseEntity.ok("Decoder order submitted successfully");
    }
}