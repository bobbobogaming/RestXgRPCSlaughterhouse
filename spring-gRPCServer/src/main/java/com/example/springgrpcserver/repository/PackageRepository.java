package com.example.springgrpcserver.repository;

import com.example.model.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PackageRepository extends JpaRepository<PackageEntity, Long> {
	@Query(value = "select * from package_entity where package_id ="
			+ "(select package_entity_package_id from package_entity_from_tray_entities where from_tray_entities_id = "
			+ "(select tray_entity_id from tray_entity_animal_part_entities where animal_part_entities_id = "
			+ "(select id from animal_part_entity where animal_entity_registration_no = :animalId)))",nativeQuery = true)
	List<PackageEntity> findAllWithAnimal(@Param("animalId") long animalId);

	@Query(value = "select * from package_entity where package_id = "
			+ "(select package_entity_package_id from package_entity_from_tray_entities where from_tray_entities_id = "
			+ "(select tray_entity_id from tray_entity_animal_part_entities where animal_part_entities_id = :animalPartId))",
	nativeQuery = true) List<PackageEntity> findAllWithAnimalPart(@Param("animalPartId") long animalPartId);

	@Query(value = "select * from package_entity where package_id = "
			+ "(select package_entity_package_id from package_entity_from_tray_entities where from_tray_entities_id = :trayId)",
	nativeQuery = true) List<PackageEntity> findAllWithTray(@Param("trayId") long trayId);
}
