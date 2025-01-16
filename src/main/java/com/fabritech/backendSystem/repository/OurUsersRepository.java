package com.fabritech.backendSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabritech.backendSystem.model.OurUsers;

@Repository

public interface OurUsersRepository extends JpaRepository<OurUsers, Integer> {
	Optional<OurUsers> findByEmail(String email);
}
