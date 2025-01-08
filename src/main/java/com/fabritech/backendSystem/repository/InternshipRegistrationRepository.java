package com.fabritech.backendSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabritech.backendSystem.model.InternshipRegistration;

@Repository
public interface InternshipRegistrationRepository extends JpaRepository<InternshipRegistration, Long> {
}
