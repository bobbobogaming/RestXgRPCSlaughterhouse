package com.example.springgrpcserver.repository;

import com.example.model.TrayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrayRepository extends JpaRepository<TrayEntity, Long> {
		@Query(value = "select * from tray_entity where id = "
				+ "(select tray_entity_id from tray_entity_animal_part_entities where animal_part_entities_id = "
				+ "(select id from animal_part_entity where animal_entity_registration_no = :animalId))",
		nativeQuery = true) List<TrayEntity> findAllWithAnimal(@Param("animalId") long animalId);

		@Query(value = "select * from tray_entity where id = "
				+ "(select tray_entity_id from tray_entity_animal_part_entities where animal_part_entities_id = :animalPartId)",
		nativeQuery = true) List<TrayEntity> findAllWithAnimalPart(@Param("animalPartId") long animalPartId);

		@Query(value = "select * from tray_entity where id = "
				+ "(select from_tray_entities_id from package_entity_from_tray_entities where package_entity_package_id = :packageId)",
		nativeQuery = true) List<TrayEntity> findAllFromPackage(@Param("packageId") long packageId);
}
