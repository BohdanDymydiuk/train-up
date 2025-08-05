package com.example.trainup.mapper;

import com.example.trainup.dto.users.trainer.TrainerAddressDto;
import com.example.trainup.dto.users.trainer.TrainerRegistrationRequestDto;
import com.example.trainup.dto.users.trainer.TrainerResponseDto;
import com.example.trainup.model.enums.Gender;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.AddressRepository;
import com.example.trainup.repository.GymRepository;
import com.example.trainup.repository.SportRepository;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-05T18:25:53+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class TrainerMapperImpl implements TrainerMapper {

    @Override
    public TrainerResponseDto toDto(Trainer trainer) {
        if ( trainer == null ) {
            return null;
        }

        String email = null;
        String userType = null;
        Set<Long> sportIds = null;
        Set<Long> gymIds = null;
        TrainerAddressDto location = null;
        Long id = null;
        String firstName = null;
        String lastName = null;
        Gender gender = null;
        LocalDate dateOfBirth = null;
        String profileImageUrl = null;
        Set<String> phoneNumbers = null;
        String description = null;
        String socialMediaLinks = null;
        Float overallRating = null;
        Integer numberOfReviews = null;

        email = trainerUserCredentialsEmail( trainer );
        UserCredentials.UserType userType1 = trainerUserCredentialsUserType( trainer );
        if ( userType1 != null ) {
            userType = userType1.name();
        }
        sportIds = mapSportsToSportIds( trainer.getSports() );
        gymIds = mapGymsToGymIds( trainer.getGyms() );
        location = mapAddressToAddressDto( trainer.getLocation() );
        id = trainer.getId();
        firstName = trainer.getFirstName();
        lastName = trainer.getLastName();
        gender = trainer.getGender();
        dateOfBirth = trainer.getDateOfBirth();
        profileImageUrl = trainer.getProfileImageUrl();
        Set<String> set2 = trainer.getPhoneNumbers();
        if ( set2 != null ) {
            phoneNumbers = new LinkedHashSet<String>( set2 );
        }
        description = trainer.getDescription();
        socialMediaLinks = trainer.getSocialMediaLinks();
        overallRating = trainer.getOverallRating();
        numberOfReviews = trainer.getNumberOfReviews();

        TrainerResponseDto trainerResponseDto = new TrainerResponseDto( id, firstName, lastName, gender, dateOfBirth, profileImageUrl, email, userType, phoneNumbers, sportIds, gymIds, location, description, socialMediaLinks, overallRating, numberOfReviews );

        return trainerResponseDto;
    }

    @Override
    public Trainer toModel(TrainerRegistrationRequestDto requestDto, PasswordEncoder passwordEncoder, SportRepository sportRepository, GymRepository gymRepository, AddressRepository addressRepository) {
        if ( requestDto == null ) {
            return null;
        }

        Trainer trainer = new Trainer();

        trainer.setSports( mapSportIdsToSports( requestDto.sportIds(), sportRepository ) );
        trainer.setGyms( mapGymIdsToGyms( requestDto.gymIds(), gymRepository ) );
        trainer.setLocation( mapAddressDtoToAddress( requestDto.location(), addressRepository ) );
        trainer.setUserCredentials( mapToUserCredentials( requestDto, passwordEncoder ) );
        Set<String> set2 = requestDto.phoneNumbers();
        if ( set2 != null ) {
            trainer.setPhoneNumbers( new LinkedHashSet<String>( set2 ) );
        }
        List<String> list = requestDto.certificates();
        if ( list != null ) {
            trainer.setCertificates( new LinkedHashSet<String>( list ) );
        }
        trainer.setDescription( requestDto.description() );
        trainer.setSocialMediaLinks( requestDto.socialMediaLinks() );
        trainer.setFirstName( requestDto.firstName() );
        trainer.setLastName( requestDto.lastName() );
        trainer.setGender( requestDto.gender() );
        trainer.setDateOfBirth( requestDto.dateOfBirth() );
        trainer.setProfileImageUrl( requestDto.profileImageUrl() );
        trainer.setOnlineTraining( requestDto.onlineTraining() );

        return trainer;
    }

    private String trainerUserCredentialsEmail(Trainer trainer) {
        if ( trainer == null ) {
            return null;
        }
        UserCredentials userCredentials = trainer.getUserCredentials();
        if ( userCredentials == null ) {
            return null;
        }
        String email = userCredentials.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }

    private UserCredentials.UserType trainerUserCredentialsUserType(Trainer trainer) {
        if ( trainer == null ) {
            return null;
        }
        UserCredentials userCredentials = trainer.getUserCredentials();
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
