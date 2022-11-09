package com.example.springgrpcserver.service;

import com.example.grpc.*;
import com.example.model.AnimalEntity;
import com.example.model.AnimalPartEntity;
import com.example.springgrpcserver.repository.AnimalPartRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.List;

@EntityScan("com.example.model")
@GrpcService
public class AnimalPartServiceImpl extends
		AnimalPartServiceGrpc.AnimalPartServiceImplBase {

	private final AnimalPartRepository repository;

	public AnimalPartServiceImpl(AnimalPartRepository repository) {
		this.repository = repository;
	}

	@Override public void create(AnimalPart request,
			StreamObserver<AnimalPart> responseObserver) {
		AnimalPartEntity animalPartEntityToSave = GRPCObjectTranslatorService.toAnimalPartEntity(request);
		AnimalPartEntity animalPartEntity = repository.save(animalPartEntityToSave);

		AnimalPart animalPart = GRPCObjectTranslatorService.toGRPCAnimalPart(animalPartEntity);
		responseObserver.onNext(animalPart);
		responseObserver.onCompleted();
	}

	@Override public void getAll(AnimalPartAllRequest request,
			StreamObserver<AnimalPartAllResponse> responseObserver) {
		List<AnimalPartEntity> animalParts = repository.findAll();

		AnimalPartAllResponse animalPartAllResponse = GRPCObjectTranslatorService.toGRPCAnimalPartAllResponse(animalParts);
		responseObserver.onNext(animalPartAllResponse);
		responseObserver.onCompleted();
	}

	@Override public void getOne(AnimalPartOneRequest request,
			StreamObserver<AnimalPart> responseObserver) {
		AnimalPartEntity animalPartEntity = repository.findById(request.getId())
				.orElse(new AnimalPartEntity());
		AnimalPart animalPart = GRPCObjectTranslatorService.toGRPCAnimalPart(animalPartEntity);
		responseObserver.onNext(animalPart);
		responseObserver.onCompleted();
	}
}
