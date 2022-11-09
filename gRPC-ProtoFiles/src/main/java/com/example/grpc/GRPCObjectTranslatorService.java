package com.example.grpc;

import com.example.model.*;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class GRPCObjectTranslatorService {

	private GRPCObjectTranslatorService(){}
	public static AnimalEntity toAnimalEntity(Animal animal) {
		AnimalEntity animalEntityToSave = new AnimalEntity(
				AnimalTypeEnum.valueOf(animal.getAnimalType().name()),
				animal.getWeight(),
				animal.getOrigin());
		animalEntityToSave.setRegistrationNo(animal.getRegistrationNo());
		animalEntityToSave.longSetArrival(animal.getArrival());
		return animalEntityToSave;
	}

	public static Animal toGRPCAnimal(AnimalEntity animalEntity) {

		Animal.Builder animalBuilder = Animal.newBuilder()
				.setWeight(animalEntity.getWeight());

		if (animalEntity.getRegistrationNo() != null) {
			animalBuilder.setRegistrationNo(animalEntity.getRegistrationNo());
		}

		if (animalEntity.getOrigin() != null) {
			animalBuilder.setOrigin(animalEntity.getOrigin());
		}

		if (animalEntity.getArrival() != null) {
			animalBuilder.setArrival(animalEntity.getArrival().toEpochSecond(ZoneOffset.UTC));
		}

		return animalBuilder.build();
	}

	public static AnimalAllResponse toGRPCAnimalAllResponse(List<AnimalEntity> animals) {
		AnimalAllResponse.Builder animalAllResponseBuilder = AnimalAllResponse.newBuilder();
		for (AnimalEntity animal : animals)
		{
		    animalAllResponseBuilder.addAnimals(toGRPCAnimal(animal));
		}
		AnimalAllResponse animalAllResponse = animalAllResponseBuilder.build();
		return animalAllResponse;
	}

	public static List<AnimalEntity> toAnimalEntities(List<Animal> animals){
		List<AnimalEntity> animalEntities = new ArrayList<>();
		for (Animal animal : animals)
		{
		    animalEntities.add(toAnimalEntity(animal));
		}
		return animalEntities;
	}

	public static AnimalPartEntity toAnimalPartEntity(AnimalPart animalPart) {

		AnimalPartEntity animalPartEntityToSave = new AnimalPartEntity(
				toAnimalEntity(animalPart.getAnimal()),
				AnimalPartTypeEnum.valueOf(animalPart.getAnimalPartType().name()),
				animalPart.getWeight()
		);
		animalPartEntityToSave.setId(animalPart.getId());
		return animalPartEntityToSave;
	}

	public static AnimalPart toGRPCAnimalPart(AnimalPartEntity animalPartEntity) {
		AnimalPart.Builder builder = AnimalPart.newBuilder()
				.setWeight(animalPartEntity.getWeight());

		if (animalPartEntity.getAnimal() == null){
			return AnimalPart.newBuilder().build();
		}
		builder.setAnimal(toGRPCAnimal(animalPartEntity.getAnimal()));

		if (animalPartEntity.getAnimalPartType() != null) {
			builder.setAnimalPartTypeValue(animalPartEntity.getAnimalPartType().ordinal());
		}

		if (animalPartEntity.getId() != null){
			builder.setId(animalPartEntity.getId());
		}

		return builder.build();
	}

	public static AnimalPartAllResponse toGRPCAnimalPartAllResponse(
			List<AnimalPartEntity> animalParts) {
		AnimalPartAllResponse.Builder animalPartAllResponseBuilder = AnimalPartAllResponse.newBuilder();
		for (AnimalPartEntity animalPart : animalParts)
		{
			animalPartAllResponseBuilder.addAnimalParts(toGRPCAnimalPart(animalPart));
		}
		AnimalPartAllResponse animalPartAllResponse = animalPartAllResponseBuilder.build();
		return animalPartAllResponse;
	}

	public static List<AnimalPartEntity> toAnimalPartEntities(
			List<AnimalPart> animalPartsList) {
		List<AnimalPartEntity> animalPartEntities = new ArrayList<>();
		for (AnimalPart animalPart : animalPartsList)
		{
			animalPartEntities.add(toAnimalPartEntity(animalPart));
		}
		return animalPartEntities;
	}

	public static TrayEntity toTrayEntity(Tray request) {
		TrayEntity trayEntityToSave = new TrayEntity(
				request.getMaxWeight(),
				AnimalPartTypeEnum.valueOf(request.getAnimalPartType().name()),
				toAnimalPartEntities(request.getAnimalPartsList())
		);
		trayEntityToSave.setId(request.getId());
		return trayEntityToSave;
	}

	public static Tray toGRPCTray(TrayEntity trayEntity) {
		Tray.Builder builder = Tray.newBuilder()
				.setMaxWeight(trayEntity.getMaxWeight());

		if (!trayEntity.getAnimalParts().isEmpty()){
			for (AnimalPartEntity animalPartEntity: trayEntity.getAnimalParts())
			{
				builder.addAnimalParts(toGRPCAnimalPart(animalPartEntity));
			}
		}

		if (trayEntity.getAnimalPartType() != null) {
			builder.setAnimalPartTypeValue(trayEntity.getAnimalPartType().ordinal());
		}

		if (trayEntity.getId() != null){
			builder.setId(trayEntity.getId());
		}

		return builder.build();
	}

	public static TrayAllResponse toGRPCTrayAllResponse(List<TrayEntity> trays) {
		TrayAllResponse.Builder trayAllResponseBuilder = TrayAllResponse.newBuilder();
		for (TrayEntity tray : trays)
		{
			trayAllResponseBuilder.addTrays(toGRPCTray(tray));
		}
		TrayAllResponse trayAllResponse = trayAllResponseBuilder.build();
		return trayAllResponse;
	}

	public static List<TrayEntity> toTrayEntities(List<Tray> fromTraysList) {
		List<TrayEntity> trayEntities = new ArrayList<>();
		for (Tray tray : fromTraysList)
		{
			trayEntities.add(toTrayEntity(tray));
		}
		return trayEntities;
	}

	public static PackageEntity toPackageEntity(Package request) {
		PackageEntity packageEntityToSave = new PackageEntity(
				toTrayEntities(request.getFromTraysList())
		);
		packageEntityToSave.setPackageId(request.getPackageId());
		return packageEntityToSave;
	}

	public static Package toGRPCPackage(PackageEntity packageEntity) {
		Package.Builder builder = Package.newBuilder();

		if (!packageEntity.getTrays().isEmpty()){
			for (TrayEntity trayEntity: packageEntity.getTrays())
			{
				builder.addFromTrays(toGRPCTray(trayEntity));
			}
		}

		if (packageEntity.getPackageId() != null){
			builder.setPackageId(packageEntity.getPackageId());
		}

		return builder.build();
	}

	public static PackageAllResponse toGRPCPackageAllResponse(
			List<PackageEntity> packageEntities) {
		PackageAllResponse.Builder packageAllResponseBuilder = PackageAllResponse.newBuilder();
		for (PackageEntity packageEntity : packageEntities)
		{
			packageAllResponseBuilder.addPackage(toGRPCPackage(packageEntity));
		}
		PackageAllResponse packageAllResponse = packageAllResponseBuilder.build();
		return packageAllResponse;
	}

	public static List<PackageEntity> toPackageEntities(
			List<Package> packages) {
		List<PackageEntity> packageEntities = new ArrayList<>();
		for (Package _package : packages)
		{
			packageEntities.add(toPackageEntity(_package));
		}
		return packageEntities;
	}
}
