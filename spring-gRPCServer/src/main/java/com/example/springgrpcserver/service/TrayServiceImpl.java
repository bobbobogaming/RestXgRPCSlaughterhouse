package com.example.springgrpcserver.service;

import com.example.grpc.*;
import com.example.model.AnimalEntity;
import com.example.model.AnimalPartEntity;
import com.example.model.TrayEntity;
import com.example.springgrpcserver.repository.TrayRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.List;

@EntityScan("com.example.model")
@GrpcService
public class TrayServiceImpl extends TrayServiceGrpc.TrayServiceImplBase {
	private final TrayRepository repository;

	public TrayServiceImpl(TrayRepository trayRepository) {
		this.repository = trayRepository;
	}

	@Override public void create(Tray request,
			StreamObserver<Tray> responseObserver) {
		TrayEntity trayEntityToSave = GRPCObjectTranslatorService.toTrayEntity(request);
		TrayEntity trayEntity = repository.save(trayEntityToSave);

		Tray tray = GRPCObjectTranslatorService.toGRPCTray(trayEntity);
		responseObserver.onNext(tray);
		responseObserver.onCompleted();
	}

	@Override public void getAll(TrayAllRequest request,
			StreamObserver<TrayAllResponse> responseObserver) {
		List<TrayEntity> trays = repository.findAll();

		TrayAllResponse trayAllResponse = GRPCObjectTranslatorService.toGRPCTrayAllResponse(trays);
		responseObserver.onNext(trayAllResponse);
		responseObserver.onCompleted();
	}

	@Override public void getOne(TrayOneRequest request,
			StreamObserver<Tray> responseObserver) {
		TrayEntity trayEntity = repository.findById(request.getId())
				.orElse(new TrayEntity());
		Tray tray = GRPCObjectTranslatorService.toGRPCTray(trayEntity);
		responseObserver.onNext(tray);
		responseObserver.onCompleted();
	}
}
