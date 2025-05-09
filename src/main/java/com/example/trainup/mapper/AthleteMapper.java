package com.example.trainup.mapper;

import com.example.trainup.config.MapperConfig;
import com.example.trainup.dto.users.AthleteRegistrationRequestDto;
import com.example.trainup.dto.users.AthleteResponseDto;
import com.example.trainup.model.Sport;
import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.model.user.UserCredentials.UserType;
import com.example.trainup.repository.SportRepository;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(config = MapperConfig.class, uses = {SportRepository.class})
public interface AthleteMapper {
    @Mapping(source = "userCredentials.email", target = "email")
    @Mapping(source = "userCredentials.userType", target = "userType")
    @Mapping(source = "sports", target = "sportIds", qualifiedByName = "mapSportsToSportIds")
    AthleteResponseDto toDto(Athlete athlete);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sports", source = "sportIds", qualifiedByName = "mapSportIdsToSports")
    @Mapping(target = "userCredentials", source = ".", qualifiedByName = "mapToUserCredentials")
    Athlete toModel(AthleteRegistrationRequestDto requestDto,
                    @Context PasswordEncoder passwordEncoder,
                    @Context SportRepository sportRepository);

    default String encodePassword(String rawPassword, PasswordEncoder encoder) {
        return encoder.encode(rawPassword);
    }

    @Named("mapSportsToSportIds")
    default Set<Long> mapSportToSportIds(Set<Sport> sports) {
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
                        .orElseThrow(() -> new IllegalArgumentException("Sport with id: "
                                + id + "not found")))
                .collect(Collectors.toSet());
    }

    @Named("mapToUserCredentials")
    default UserCredentials mapToUserCredentials(AthleteRegistrationRequestDto requestDto,
                                                 @Context PasswordEncoder passwordEncoder) {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setEmail(requestDto.email());
        userCredentials.setPassword(encodePassword(requestDto.password(), passwordEncoder));
        userCredentials.setUserType(UserType.ATHLETE);
        return userCredentials;
    }
}
