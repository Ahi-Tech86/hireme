package com.schizoscrypt.storage.repositories;

import com.schizoscrypt.storage.entities.EmployerAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployerAccountRepository extends JpaRepository<EmployerAccountRepository, Long> {
    Optional<EmployerAccountEntity> findByEmail(String email);
}
