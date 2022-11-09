package com.example.springrestapi.controller;

import com.example.grpc.*;
import com.example.grpc.TrayServiceGrpc;
import com.example.springrestapi.exceptions.TrayNotFoundException;
import com.example.springrestapi.modelAssembler.TrayModelAssembler;
import com.example.model.TrayEntity;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class TrayController {
  @GrpcClient("GrpcConnection")
  private TrayServiceGrpc.TrayServiceBlockingStub trayStub;
  private final TrayModelAssembler assembler;

  public TrayController(TrayModelAssembler assembler) {
    this.assembler = assembler;
  }

  @PostMapping("/trays") ResponseEntity<?> newTray(@RequestBody TrayEntity newTray){
    Tray trayToSend = GRPCObjectTranslatorService.toGRPCTray(newTray);
    Tray trayReceived = trayStub.create(trayToSend);
    TrayEntity trayEntity = GRPCObjectTranslatorService.toTrayEntity(trayReceived);
    EntityModel<TrayEntity> entityModel = assembler.toModel(trayEntity);

    return  ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  @GetMapping("/trays")
  public CollectionModel<EntityModel<TrayEntity>> all() {
    TrayAllRequest trayAllRequest = TrayAllRequest.newBuilder().build();

    TrayAllResponse traysReceived = trayStub.getAll(trayAllRequest);
    List<TrayEntity> trayEntities = GRPCObjectTranslatorService.toTrayEntities(traysReceived.getTraysList());
    List<EntityModel<TrayEntity>> trays = trayEntities.stream()
        .map(assembler::toModel)
        .collect(Collectors.toList());

    return CollectionModel.of(trays,
        linkTo(methodOn(TrayController.class).all()).withSelfRel());
  }

  @GetMapping("/trays/{id}")
  public EntityModel<TrayEntity> one(@PathVariable Long id) {
    TrayOneRequest trayOneRequest = TrayOneRequest.newBuilder()
        .setId(id)
        .build();
    Tray tray = trayStub.getOne(trayOneRequest);

    if (tray.getId() == 0L) throw new TrayNotFoundException(id);

    return assembler.toModel(GRPCObjectTranslatorService.toTrayEntity(tray));
  }
}
