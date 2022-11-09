package com.example.springgrpcserver.service;

import com.example.grpc.Package;
import com.example.grpc.*;
import com.example.model.PackageEntity;
import com.example.model.TrayEntity;
import com.example.springgrpcserver.repository.PackageRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@EntityScan("com.example.model")
@GrpcService
public class PackageServiceImpl extends PackageServiceGrpc.PackageServiceImplBase {
	private final PackageRepository repository;

	public PackageServiceImpl(PackageRepository packageRepository) {
		this.repository = packageRepository;
	}

	@Override public void create(Package request,
			StreamObserver<Package> responseObserver) {
		PackageEntity packageEntityToSave = GRPCObjectTranslatorService.toPackageEntity(request);
		PackageEntity packageEntity = repository.save(packageEntityToSave);

		Package _package = GRPCObjectTranslatorService.toGRPCPackage(packageEntity);
		responseObserver.onNext(_package);
		responseObserver.onCompleted();
	}

	@Override public void getAll(PackageAllRequest request,
			StreamObserver<PackageAllResponse> responseObserver) {
		List<PackageEntity> packageEntities = repository.findAll();

		PackageAllResponse packageAllResponse = GRPCObjectTranslatorService.toGRPCPackageAllResponse(packageEntities);
		responseObserver.onNext(packageAllResponse);
		responseObserver.onCompleted();
	}

	@Override public void getOne(PackageOneRequest request,
			StreamObserver<Package> responseObserver) {
		PackageEntity packageEntity = repository.findById(request.getId())
				.orElse(new PackageEntity());
		Package _package = GRPCObjectTranslatorService.toGRPCPackage(packageEntity);
		responseObserver.onNext(_package);
		responseObserver.onCompleted();
	}

	@Override public void getAllWithAnimal(AllPackagesWithAnimalRequest request,
			StreamObserver<AllPackagesWithAnimalResponse> responseObserver) {
			List<PackageEntity> packageEntities = repository.findAllWithAnimal(request.getAnimalId());
			AllPackagesWithAnimalResponse.Builder builder = AllPackagesWithAnimalResponse.newBuilder();
			for (PackageEntity packageEntity : packageEntities)
			{
			    builder.addPackages(GRPCObjectTranslatorService.toGRPCPackage(packageEntity));
			}
			responseObserver.onNext(builder.build());
			responseObserver.onCompleted();
	}
}
