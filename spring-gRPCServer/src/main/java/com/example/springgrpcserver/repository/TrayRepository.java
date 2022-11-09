package com.example.springgrpcserver.repository;

import com.example.model.TrayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TrayRepository extends JpaRepository<TrayEntity, Long> {
}
