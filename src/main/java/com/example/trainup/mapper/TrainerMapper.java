package com.example.trainup.mapper;

import com.example.trainup.config.MapperConfig;
import com.example.trainup.dto.users.trainer.TrainerAddressDto;
import com.example.trainup.dto.users.trainer.TrainerRegistrationRequestDto;
import com.example.trainup.dto.users.trainer.TrainerResponseDto;
import com.example.trainup.model.Address;
import com.example.trainup.model.Gym;
import com.example.trainup.model.Sport;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.AddressRepository;
import com.example.trainup.repository.GymRepository;
import com.example.trainup.repository.SportRepository;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(config = MapperConfig.class, uses = {
        SportRepository.class,
        GymRepository.class,
        AddressRepository.class}
)
public interface TrainerMapper {
    @Mapping(source = "userCredentials.email", target = "email")
    @Mapping(source = "userCredentials.userType", target = "userType")
    @Mapping(source = "sports", target = "sportIds", qualifiedByName = "mapSportsToSportIds")
    @Mapping(source = "gyms", target = "gymIds", qualifiedByName = "mapGymsToGymIds")
    @Mapping(source = "location", target = "location", qualifiedByName = "mapAddressToAddressDto")
    TrainerResponseDto toDto(Trainer trainer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sports", source = "sportIds", qualifiedByName = "mapSportIdsToSports")
    @Mapping(target = "gyms", source = "gymIds", qualifiedByName = "mapGymIdsToGyms")
    @Mapping(target = "location", source = "location", qualifiedByName = "mapAddressDtoToAddress")
    @Mapping(target = "userCredentials", source = ".", qualifiedByName = "mapToUserCredentials")
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "phoneNumbers", source = "phoneNumbers")
    @Mapping(target = "certificates", source = "certificates")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "socialMediaLinks", source = "socialMediaLinks")
    @Mapping(target = "overallRating", ignore = true)
    Trainer toModel(TrainerRegistrationRequestDto requestDto,
                    @Context PasswordEncoder passwordEncoder,
                    @Context SportRepository sportRepository,
                    @Context GymRepository gymRepository,
                    @Context AddressRepository addressRepository);

    default String encodePassword(String rawPassword, PasswordEncoder encoder) {
        return encoder.encode(rawPassword);
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

    @Named("mapSportIdsToSports")
    default Set<Sport> mapSportIdsToSports(Set<Long> sportIds,
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

    @Named("mapGymsToGymIds")
    default Set<Long> mapGymsToGymIds(Set<Gym> gyms) {
        if (gyms == null || gyms.isEmpty()) {
            return Set.of();
        }
        return gyms.stream()
                .map(Gym::getId)
                .collect(Collectors.toSet());
    }

    @Named("mapGymIdsToGyms")
    default Set<Gym> mapGymIdsToGyms(Set<Long> gymIds, @Context GymRepository gymRepository) {
        if (gymIds == null || gymIds.isEmpty()) {
            return Set.of();
        }
        return gymIds.stream()
                .map(id -> gymRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Gym with id "
                                + id + " not found")))
                .collect(Collectors.toSet());
    }

    @Named("mapToUserCredentials")
    default UserCredentials mapToUserCredentials(TrainerRegistrationRequestDto requestDto,
                                                 @Context PasswordEncoder passwordEncoder) {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setEmail(requestDto.email());
        userCredentials.setPassword(encodePassword(requestDto.password(), passwordEncoder));
        userCredentials.setUserType(UserCredentials.UserType.TRAINER);
        return userCredentials;
    }

    @Named("mapAddressDtoToAddress")
    default Address mapAddressDtoToAddress(TrainerAddressDto addressDto,
                                           @Context AddressRepository addressRepository) {
        if (addressDto == null) {
            return null;
        }

        Address address = addressRepository.findByCountryAndCityAndStreetAndHouse(
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
        return address;
    }

    @Named("mapAddressToAddressDto")
    default TrainerAddressDto mapAddressToAddressDto(Address address) {
        if (address == null) {
            return null;
        }
        return new TrainerAddressDto(
                address.getCountry(),
                address.getCity(),
                address.getCityDistrict(),
                address.getStreet(),
                address.getHouse()
        );
    }
}
