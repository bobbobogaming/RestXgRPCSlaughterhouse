package com.example.springgrpcserver.repository;


import com.example.model.AnimalPartEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnimalPartRepository extends JpaRepository<AnimalPartEntity, Long> {
}
