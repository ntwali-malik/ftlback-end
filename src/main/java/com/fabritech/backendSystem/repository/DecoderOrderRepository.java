package com.fabritech.backendSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fabritech.backendSystem.model.DecoderOrder;

public interface DecoderOrderRepository extends JpaRepository<DecoderOrder, Long> {
}

