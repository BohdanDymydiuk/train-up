package com.example.trainup.repository;

import com.example.trainup.model.Address;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByCountryAndCityAndStreetAndHouse(String country, String city,
                                                            String street, String house);
}
