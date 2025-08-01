package com.example.trainup.mapper;

import com.example.trainup.dto.users.gymowner.GymOwnerRegistrationRequestDto;
import com.example.trainup.dto.users.gymowner.GymOwnerResponseDto;
import com.example.trainup.model.enums.Gender;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.UserCredentials;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-01T19:20:49+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class GymOwnerMapperImpl implements GymOwnerMapper {

    @Override
    public GymOwnerResponseDto toDto(GymOwner gymOwner) {
        if ( gymOwner == null ) {
            return null;
        }

        String email = null;
        String userType = null;
        Set<Long> ownedGymIds = null;
        Long id = null;
        String firstName = null;
        String lastName = null;
        Gender gender = null;
        LocalDate dateOfBirth = null;
        String profileImageUrl = null;
        Set<String> phoneNumbers = null;

        email = gymOwnerUserCredentialsEmail( gymOwner );
        UserCredentials.UserType userType1 = gymOwnerUserCredentialsUserType( gymOwner );
        if ( userType1 != null ) {
            userType = userType1.name();
        }
        ownedGymIds = mapGymsToGymIds( gymOwner.getOwnedGyms() );
        id = gymOwner.getId();
        firstName = gymOwner.getFirstName();
        lastName = gymOwner.getLastName();
        gender = gymOwner.getGender();
        dateOfBirth = gymOwner.getDateOfBirth();
        profileImageUrl = gymOwner.getProfileImageUrl();
        Set<String> set1 = gymOwner.getPhoneNumbers();
        if ( set1 != null ) {
            phoneNumbers = new LinkedHashSet<String>( set1 );
        }

        GymOwnerResponseDto gymOwnerResponseDto = new GymOwnerResponseDto( id, firstName, lastName, gender, dateOfBirth, profileImageUrl, email, userType, phoneNumbers, ownedGymIds );

        return gymOwnerResponseDto;
    }

    @Override
    public GymOwner toModel(GymOwnerRegistrationRequestDto requestDto, PasswordEncoder passwordEncoder) {
        if ( requestDto == null ) {
            return null;
        }

        GymOwner gymOwner = new GymOwner();

        gymOwner.setUserCredentials( mapToUserCredentials( requestDto, passwordEncoder ) );
        gymOwner.setFirstName( requestDto.firstName() );
        gymOwner.setLastName( requestDto.lastName() );
        gymOwner.setGender( requestDto.gender() );
        gymOwner.setDateOfBirth( requestDto.dateOfBirth() );
        gymOwner.setProfileImageUrl( requestDto.profileImageUrl() );
        Set<String> set = requestDto.phoneNumbers();
        if ( set != null ) {
            gymOwner.setPhoneNumbers( new LinkedHashSet<String>( set ) );
        }

        return gymOwner;
    }

    private String gymOwnerUserCredentialsEmail(GymOwner gymOwner) {
        if ( gymOwner == null ) {
            return null;
        }
        UserCredentials userCredentials = gymOwner.getUserCredentials();
        if ( userCredentials == null ) {
            return null;
        }
        String email = userCredentials.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }

    private UserCredentials.UserType gymOwnerUserCredentialsUserType(GymOwner gymOwner) {
        if ( gymOwner == null ) {
            return null;
        }
        UserCredentials userCredentials = gymOwner.getUserCredentials();
        if ( userCredentials == null ) {
            return null;
        }
        UserCredentials.UserType userType = userCredentials.getUserType();
        if ( userType == null ) {
            return null;
        }
        return userType;
    }
}
