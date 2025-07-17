package com.example.trainup.mapper;

import com.example.trainup.dto.gym.GymAddressDto;
import com.example.trainup.dto.gym.GymRegistrationRequestDto;
import com.example.trainup.dto.gym.GymResponseDto;
import com.example.trainup.model.Gym;
import com.example.trainup.model.WorkingHoursEntry;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.repository.AddressRepository;
import com.example.trainup.repository.SportRepository;
import com.example.trainup.repository.TrainerRepository;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-17T07:08:06+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class GymMapperImpl implements GymMapper {

    @Override
    public Gym toModel(GymRegistrationRequestDto requestDto, GymOwner gymOwner, SportRepository sportRepository, TrainerRepository trainerRepository, AddressRepository addressRepository) {
        if ( requestDto == null && gymOwner == null ) {
            return null;
        }

        Gym gym = new Gym();

        if ( requestDto != null ) {
            gym.setSports( mapSportIdsToSport( requestDto.sportIds(), sportRepository ) );
            gym.setTrainers( mapTrainerIdsToTrainers( requestDto.trainerIds(), trainerRepository ) );
            gym.setLocation( mapAddressDtoToAddress( requestDto.location(), addressRepository ) );
            gym.setPhotos( mapPhotoUrlsToPhotos( requestDto.photoUrls() ) );
            Set<String> set3 = requestDto.phoneNumbers();
            if ( set3 != null ) {
                gym.setPhoneNumbers( new LinkedHashSet<String>( set3 ) );
            }
            gym.setName( requestDto.name() );
            gym.setDescription( requestDto.description() );
            gym.setWebsite( requestDto.website() );
            Set<WorkingHoursEntry> set4 = requestDto.workingHours();
            if ( set4 != null ) {
                gym.setWorkingHours( new LinkedHashSet<WorkingHoursEntry>( set4 ) );
            }
        }
        gym.setGymOwner( gymOwner );

        linkPhotosToGym( gym );

        return gym;
    }

    @Override
    public GymResponseDto toDto(Gym gym) {
        if ( gym == null ) {
            return null;
        }

        Set<Long> sportIds = null;
        Set<Long> trainerIds = null;
        GymAddressDto location = null;
        Long gymOwnerId = null;
        Set<String> photoUrls = null;
        Long id = null;
        String name = null;
        String description = null;
        String website = null;
        Set<String> phoneNumbers = null;
        Set<WorkingHoursEntry> workingHours = null;
        Float overallRating = null;
        Integer numberOfReviews = null;

        sportIds = mapSportsToSportIds( gym.getSports() );
        trainerIds = mapTrainersToTrainerIds( gym.getTrainers() );
        location = mapAddressToAddressDto( gym.getLocation() );
        gymOwnerId = gymGymOwnerId( gym );
        photoUrls = mapPhotosToPhotoUrls( gym.getPhotos() );
        id = gym.getId();
        name = gym.getName();
        description = gym.getDescription();
        website = gym.getWebsite();
        Set<String> set3 = gym.getPhoneNumbers();
        if ( set3 != null ) {
            phoneNumbers = new LinkedHashSet<String>( set3 );
        }
        Set<WorkingHoursEntry> set4 = gym.getWorkingHours();
        if ( set4 != null ) {
            workingHours = new LinkedHashSet<WorkingHoursEntry>( set4 );
        }
        overallRating = gym.getOverallRating();
        numberOfReviews = gym.getNumberOfReviews();

        GymResponseDto gymResponseDto = new GymResponseDto( id, name, location, sportIds, description, website, phoneNumbers, workingHours, trainerIds, overallRating, numberOfReviews, gymOwnerId, photoUrls );

        return gymResponseDto;
    }

    private Long gymGymOwnerId(Gym gym) {
        if ( gym == null ) {
            return null;
        }
        GymOwner gymOwner = gym.getGymOwner();
        if ( gymOwner == null ) {
            return null;
        }
        Long id = gymOwner.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
