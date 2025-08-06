package com.example.trainup.mapper;

import com.example.trainup.dto.users.athlete.AthleteRegistrationRequestDto;
import com.example.trainup.dto.users.athlete.AthleteResponseDto;
import com.example.trainup.model.enums.Gender;
import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.SportRepository;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-06T14:18:24+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class AthleteMapperImpl implements AthleteMapper {

    @Override
    public AthleteResponseDto toDto(Athlete athlete) {
        if ( athlete == null ) {
            return null;
        }

        String email = null;
        String userType = null;
        Set<Long> sportIds = null;
        Long id = null;
        String firstName = null;
        String lastName = null;
        Gender gender = null;
        LocalDate dateOfBirth = null;
        String profileImageUrl = null;
        Set<String> phoneNumbers = null;
        Boolean emailPermission = null;
        Boolean phonePermission = null;

        email = athleteUserCredentialsEmail( athlete );
        UserCredentials.UserType userType1 = athleteUserCredentialsUserType( athlete );
        if ( userType1 != null ) {
            userType = userType1.name();
        }
        sportIds = mapSportToSportIds( athlete.getSports() );
        id = athlete.getId();
        firstName = athlete.getFirstName();
        lastName = athlete.getLastName();
        gender = athlete.getGender();
        dateOfBirth = athlete.getDateOfBirth();
        profileImageUrl = athlete.getProfileImageUrl();
        Set<String> set1 = athlete.getPhoneNumbers();
        if ( set1 != null ) {
            phoneNumbers = new LinkedHashSet<String>( set1 );
        }
        emailPermission = athlete.getEmailPermission();
        phonePermission = athlete.getPhonePermission();

        AthleteResponseDto athleteResponseDto = new AthleteResponseDto( id, firstName, lastName, gender, dateOfBirth, profileImageUrl, email, userType, phoneNumbers, sportIds, emailPermission, phonePermission );

        return athleteResponseDto;
    }

    @Override
    public Athlete toModel(AthleteRegistrationRequestDto requestDto, PasswordEncoder passwordEncoder, SportRepository sportRepository) {
        if ( requestDto == null ) {
            return null;
        }

        Athlete athlete = new Athlete();

        athlete.setSports( mapSportIdsToSports( requestDto.sportIds(), sportRepository ) );
        athlete.setUserCredentials( mapToUserCredentials( requestDto, passwordEncoder ) );
        athlete.setFirstName( requestDto.firstName() );
        athlete.setLastName( requestDto.lastName() );
        athlete.setGender( requestDto.gender() );
        athlete.setDateOfBirth( requestDto.dateOfBirth() );
        athlete.setProfileImageUrl( requestDto.profileImageUrl() );
        Set<String> set1 = requestDto.phoneNumbers();
        if ( set1 != null ) {
            athlete.setPhoneNumbers( new LinkedHashSet<String>( set1 ) );
        }
        athlete.setEmailPermission( requestDto.emailPermission() );
        athlete.setPhonePermission( requestDto.phonePermission() );

        return athlete;
    }

    private String athleteUserCredentialsEmail(Athlete athlete) {
        if ( athlete == null ) {
            return null;
        }
        UserCredentials userCredentials = athlete.getUserCredentials();
        if ( userCredentials == null ) {
            return null;
        }
        String email = userCredentials.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }

    private UserCredentials.UserType athleteUserCredentialsUserType(Athlete athlete) {
        if ( athlete == null ) {
            return null;
        }
        UserCredentials userCredentials = athlete.getUserCredentials();
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
