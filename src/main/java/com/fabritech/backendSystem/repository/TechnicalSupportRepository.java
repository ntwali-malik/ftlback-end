package com.fabritech.backendSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fabritech.backendSystem.model.TechnicalSupport;

public interface TechnicalSupportRepository extends JpaRepository<TechnicalSupport, Long> {
}
