package com.fabritech.backendSystem.controller;

import com.fabritech.backendSystem.model.OurUsers;
import com.fabritech.backendSystem.repository.OurUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class OurUserController {

    @Autowired
    private OurUsersRepository ourUsersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Get all users (ADMIN only)
    @GetMapping
    public List<OurUsers> getAllUsers() {
        return ourUsersRepository.findAll();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<OurUsers> getUserById(@PathVariable Integer id) {
        Optional<OurUsers> user = ourUsersRepository.findById(id);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<OurUsers> createUser(@RequestBody OurUsers user) {
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new ResponseEntity<>(ourUsersRepository.save(user), HttpStatus.CREATED);
    }

    // Update user by ID
    @PutMapping("/{id}")
    public ResponseEntity<OurUsers> updateUser(@PathVariable Integer id, @RequestBody OurUsers userDetails) {
        Optional<OurUsers> optionalUser = ourUsersRepository.findById(id);
        if (optionalUser.isPresent()) {
            OurUsers user = optionalUser.get();
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setCity(userDetails.getCity());
            user.setRoles(userDetails.getRoles());
            if (userDetails.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }
            ourUsersRepository.save(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        if (ourUsersRepository.existsById(id)) {
            ourUsersRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
