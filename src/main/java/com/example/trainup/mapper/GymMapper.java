package com.example.trainup.mapper;

import com.example.trainup.config.MapperConfig;
import com.example.trainup.dto.gym.GymAddressDto;
import com.example.trainup.dto.gym.GymRegistrationRequestDto;
import com.example.trainup.dto.gym.GymResponseDto;
import com.example.trainup.model.Address;
import com.example.trainup.model.Gym;
import com.example.trainup.model.GymPhoto;
import com.example.trainup.model.Sport;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.repository.AddressRepository;
import com.example.trainup.repository.SportRepository;
import com.example.trainup.repository.TrainerRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = {SportRepository.class, TrainerRepository.class,
        AddressRepository.class})
public interface GymMapper {
    @Mapping(source = "requestDto.sportIds", target = "sports",
            qualifiedByName = "mapSportIdsToSports")
    @Mapping(source = "requestDto.trainerIds", target = "trainers",
            qualifiedByName = "mapTrainerIdsToTrainers")
    @Mapping(source = "requestDto.location", target = "location",
            qualifiedByName = "mapAddressDtoToAddress")
    @Mapping(source = "requestDto.photoUrls", target = "photos",
            qualifiedByName = "mapPhotoUrlsToPhotos")
    @Mapping(source = "gymOwner", target = "gymOwner")
    @Mapping(source = "requestDto.phoneNumbers", target = "phoneNumbers")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "overallRating", ignore = true)
    @Mapping(target = "numberOfReviews", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    Gym toModel(GymRegistrationRequestDto requestDto, GymOwner gymOwner,
                @Context SportRepository sportRepository,
                @Context TrainerRepository trainerRepository,
                @Context AddressRepository addressRepository);

    @AfterMapping
    default void linkPhotosToGym(@MappingTarget Gym gym) {
        if (gym.getPhotos() != null && !gym.getPhotos().isEmpty()) {
            Set<GymPhoto> photosCopy = new HashSet<>(gym.getPhotos());
            gym.getPhotos().clear();
            photosCopy.forEach(gym::addPhoto);
        }
    }

    @Mapping(source = "sports", target = "sportIds", qualifiedByName = "mapSportsToSportIds")
    @Mapping(source = "trainers", target = "trainerIds",
            qualifiedByName = "mapTrainersToTrainerIds")
    @Mapping(source = "location", target = "location", qualifiedByName = "mapAddressToAddressDto")
    @Mapping(source = "gymOwner.id", target = "gymOwnerId")
    @Mapping(source = "photos", target = "photoUrls", qualifiedByName = "mapPhotosToPhotoUrls")
    GymResponseDto toDto(Gym gym);

    @Named("mapSportIdsToSports")
    default Set<Sport> mapSportIdsToSport(Set<Long> sportIds,
                                          @Context SportRepository sportRepository) {
        if (sportIds == null || sportIds.isEmpty()) {
            return Set.of();
        }
        return sportIds.stream()
                .map(id -> sportRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Sport with id "
                                + id + " not found")))
                .collect(Collectors.toSet());
    }

    @Named("mapSportsToSportIds")
    default Set<Long> mapSportsToSportIds(Set<Sport> sports) {
        if (sports == null || sports.isEmpty()) {
            return Set.of();
        }
        return sports.stream()
                .map(Sport::getId)
                .collect(Collectors.toSet());
    }

    @Named("mapTrainerIdsToTrainers")
    default Set<Trainer> mapTrainerIdsToTrainers(Set<Long> trainerIds,
                                                 @Context TrainerRepository trainerRepository) {
        if (trainerIds == null || trainerIds.isEmpty()) {
            return Set.of();
        }
        return trainerIds.stream()
                .map(id -> trainerRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Trainer with id "
                                + id + " not found")))
                .collect(Collectors.toSet());
    }

    @Named("mapTrainersToTrainerIds")
    default Set<Long> mapTrainersToTrainerIds(Set<Trainer> trainers) {
        if (trainers == null || trainers.isEmpty()) {
            return Set.of();
        }
        return trainers.stream()
                .map(Trainer::getId)
                .collect(Collectors.toSet());
    }

    @Named("mapAddressDtoToAddress")
    default Address mapAddressDtoToAddress(GymAddressDto addressDto,
                                           @Context AddressRepository addressRepository) {
        if (addressDto == null) {
            return null;
        }
        return addressRepository.findByCountryAndCityAndStreetAndHouse(
                addressDto.country(), addressDto.city(), addressDto.street(), addressDto.house()
        ).orElseGet(() -> {
            Address newAddress = new Address();
            newAddress.setCountry(addressDto.country());
            newAddress.setCity(addressDto.city());
            newAddress.setCityDistrict(addressDto.cityDistrict());
            newAddress.setStreet(addressDto.street());
            newAddress.setHouse(addressDto.house());
            return addressRepository.save(newAddress);
        });
    }

    @Named("mapAddressToAddressDto")
    default GymAddressDto mapAddressToAddressDto(Address address) {
        if (address == null) {
            return null;
        }
        return new GymAddressDto(
                address.getCountry(),
                address.getCity(),
                address.getCityDistrict(),
                address.getStreet(),
                address.getHouse()
        );
    }

    @Named("mapPhotoUrlsToPhotos")
    default Set<GymPhoto> mapPhotoUrlsToPhotos(Set<String> photoUrls) {
        if (photoUrls == null || photoUrls.isEmpty()) {
            return Set.of();
        }
        return photoUrls.stream()
                .map(url -> {
                    GymPhoto gymPhoto = new GymPhoto();
                    gymPhoto.setImageUrl(url);
                    return gymPhoto;
                })
                .collect(Collectors.toSet());
    }

    @Named("mapPhotosToPhotoUrls")
    default Set<String> mapPhotosToPhotoUrls(Set<GymPhoto> photos) {
        if (photos == null || photos.isEmpty()) {
            return Set.of();
        }
        return photos.stream()
                .map(GymPhoto::getImageUrl)
                .collect(Collectors.toSet());
    }
}
