package com.schizoscrypt.storage.repositories;

import com.schizoscrypt.storage.entities.WorkerAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkerAccountRepository extends JpaRepository<WorkerAccountEntity, Long> {
    Optional<WorkerAccountEntity> findByEmail(String email);
}
