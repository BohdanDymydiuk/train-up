package com.example.trainup.mapper;

import com.example.trainup.config.MapperConfig;
import com.example.trainup.dto.AddressDto;
import com.example.trainup.dto.event.EventRegistrationRequestDto;
import com.example.trainup.dto.event.EventResponseDto;
import com.example.trainup.model.Address;
import com.example.trainup.model.Event;
import com.example.trainup.model.Sport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "requestDto.name", target = "name")
    @Mapping(source = "requestDto.description", target = "description")
    @Mapping(source = "requestDto.dateTime", target = "dateTime")
    @Mapping(source = "sport", target = "sport")
    @Mapping(source = "requestDto.location", target = "location")
    Event toModel(EventRegistrationRequestDto requestDto, Sport sport);

    default Address mapAddressDtoToAddress(AddressDto addressDto) {
        if (addressDto == null) {
            return null;
        }
        Address newAddress = new Address();
        newAddress.setCountry(addressDto.country());
        newAddress.setCity(addressDto.city());
        newAddress.setCityDistrict(addressDto.cityDistrict());
        newAddress.setStreet(addressDto.street());
        newAddress.setHouse(addressDto.house());
        return newAddress;
    }

    @Mapping(source = "sport.id", target = "sportId")
    @Mapping(source = "gym.id", target = "gymId")
    @Mapping(source = "trainer.id", target = "trainerId")
    @Mapping(source = "location", target = "location")
    EventResponseDto toDto(Event event);

    default AddressDto mapAddressToAddressDto(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressDto(
                address.getCountry(),
                address.getCity(),
                address.getCityDistrict(),
                address.getStreet(),
                address.getHouse()
        );
    }
}
