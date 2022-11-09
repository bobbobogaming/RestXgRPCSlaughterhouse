package com.example.springgrpcserver.repository;

import com.example.model.AnimalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnimalRepository extends JpaRepository<AnimalEntity, Long> {
		@Query(value = "select * from animal_entity where animal_entity.registration_no = "
				+ "(select animal_entity_registration_no from animal_part_entity where animal_part_entity.id = "
				+ "(select animal_part_entities_id from tray_entity_animal_part_entities where tray_entity_id = "
				+ "(select from_tray_entities_id from package_entity_from_tray_entities where package_entity_package_id = :packageId)))",
		nativeQuery = true) List<AnimalEntity> findAllFromPackage(@Param("packageId") long packageId);
}
