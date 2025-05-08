package com.example.trainup.mapper;

import com.example.trainup.config.MapperConfig;
import com.example.trainup.dto.users.GymOwnerRegistrationRequestDto;
import com.example.trainup.dto.users.GymOwnerResponseDto;
import com.example.trainup.model.Gym;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.GymRepository;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(config = MapperConfig.class, uses = {GymRepository.class})
public interface GymOwnerMapper {
    @Mapping(source = "userCredentials.email", target = "email")
    @Mapping(source = "userCredentials.userType", target = "userType")
    @Mapping(source = "ownedGyms", target = "ownedGymIds", qualifiedByName = "mapGymsToGymIds")
    GymOwnerResponseDto toDto(GymOwner gymOwner);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownedGyms", source = "ownedGymIds", qualifiedByName = "mapGymIdsToGyms")
    @Mapping(target = "userCredentials", source = ".", qualifiedByName = "mapToUserCredentials")
    GymOwner toModel(GymOwnerRegistrationRequestDto requestDto,
                     @Context PasswordEncoder passwordEncoder);

    default String encodePassword(String rawPassword, PasswordEncoder encoder) {
        return encoder.encode(rawPassword);
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
    default UserCredentials mapToUserCredentials(GymOwnerRegistrationRequestDto requestDto,
                                                 @Context PasswordEncoder passwordEncoder) {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setEmail(requestDto.email());
        userCredentials.setPassword(encodePassword(requestDto.password(), passwordEncoder));
        userCredentials.setUserType(UserCredentials.UserType.GYM_OWNER);
        return userCredentials;
    }
}
