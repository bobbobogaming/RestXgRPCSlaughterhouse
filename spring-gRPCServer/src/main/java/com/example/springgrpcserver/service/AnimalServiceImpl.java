package com.example.springgrpcserver.service;

import com.example.grpc.*;
import com.example.model.PackageEntity;
import com.example.springgrpcserver.repository.AnimalRepository;
import com.example.model.AnimalEntity;
import com.example.grpc.GRPCObjectTranslatorService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

@EntityScan("com.example.model")
@GrpcService
public class AnimalServiceImpl extends AnimalServiceGrpc.AnimalServiceImplBase {

	private final AnimalRepository animalRepository;
	public AnimalServiceImpl(AnimalRepository animalRepository) {
		this.animalRepository = animalRepository;
	}

	@Override public void create(Animal request,
			StreamObserver<Animal> responseObserver) {
		AnimalEntity animalEntityToSave = GRPCObjectTranslatorService.toAnimalEntity(request);
		AnimalEntity animalEntity = animalRepository.save(animalEntityToSave);

		Animal animal = GRPCObjectTranslatorService.toGRPCAnimal(animalEntity);
		responseObserver.onNext(animal);
		responseObserver.onCompleted();
	}

	@Override public void getAll(AnimalAllRequest request,
			StreamObserver<AnimalAllResponse> responseObserver) {
		List<AnimalEntity> animals = animalRepository.findAll();

		String origin = request.getOrigin();
		String date = request.getDate();

		if (!origin.equals("")) {
			for (int i = animals.size()-1; i >= 0; i--) {
				if (!origin.equalsIgnoreCase(animals.get(i).getOrigin()))
					animals.remove(i);
			}
		}

			if (!date.equals("")) {
				date += " 00:00";

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				LocalDateTime parsed = LocalDateTime.parse(date,formatter);

				for (int i = animals.size()-1;i>=0; i--) {
					LocalDateTime localDate = animals.get(i).getArrival();
					if (localDate == null || !(localDate.getYear() == parsed.getYear()
							&& localDate.getMonth().equals(parsed.getMonth())
							&& localDate.getDayOfMonth() == parsed.getDayOfMonth()))
						animals.remove(i);
				}
			}
			AnimalAllResponse animalAllResponse = GRPCObjectTranslatorService.toGRPCAnimalAllResponse(animals);
			responseObserver.onNext(animalAllResponse);
			responseObserver.onCompleted();
	}

	@Override public void getOne(AnimalOneRequest request,
			StreamObserver<Animal> responseObserver) {
		AnimalEntity animalEntity = animalRepository.findById(request.getId())
				.orElse(new AnimalEntity());
		Animal animal = GRPCObjectTranslatorService.toGRPCAnimal(animalEntity);
		responseObserver.onNext(animal);
		responseObserver.onCompleted();
	}

	@Override public void getAllFromPackage(AllAnimalsFromPackageRequest request,
			StreamObserver<AllAnimalsFromPackageResponse> responseObserver) {
		List<AnimalEntity> packageEntities = animalRepository.findAllFromPackage(request.getPackageId());
		AllAnimalsFromPackageResponse.Builder builder = AllAnimalsFromPackageResponse.newBuilder();
		for (AnimalEntity animalEntity : packageEntities)
		{
			builder.addAnimals(GRPCObjectTranslatorService.toGRPCAnimal(animalEntity));
		}
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}
}
