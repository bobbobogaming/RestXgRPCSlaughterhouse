package com.example.springgrpcserver;


import com.example.model.*;
import com.example.springgrpcserver.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
class LoadDatabase {
  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(AnimalRepository animalRepository, AnimalPartRepository animalPartRepository,
      TrayRepository trayRepository, PackageRepository packageRepository) {

    return args -> {
      AnimalEntity animalEntity1 = new AnimalEntity(AnimalTypeEnum.CHICKEN, 7000, "Horsens");
      animalRepository.save(animalEntity1);
      animalRepository.save(new AnimalEntity(AnimalTypeEnum.COW, 410200, "Vejle"));
      animalRepository.save(new AnimalEntity(AnimalTypeEnum.PIG, 50500, "Kolding"));
      animalRepository.save(new AnimalEntity(AnimalTypeEnum.COW, 470900, "Horsens"));
      animalRepository.save(new AnimalEntity(AnimalTypeEnum.PIG, 40900, "Vejle"));

      AnimalPartEntity animalPartEntity1 = new AnimalPartEntity(animalEntity1, AnimalPartTypeEnum.LIVER,30);
      animalPartRepository.save(animalPartEntity1);

      TrayEntity trayEntity = new TrayEntity(1000, AnimalPartTypeEnum.LIVER,List.of(new AnimalPartEntity[] {
          animalPartEntity1}));
      trayRepository.save(trayEntity);

      packageRepository.save(new PackageEntity(List.of(new TrayEntity[]{
          trayEntity})));

      animalRepository.findAll().forEach(animal -> log.info("Preloaded " + animal));

    };
  }
}
