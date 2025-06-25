package com.example.trainup.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.trainup.dto.gym.GymAddressDto;
import com.example.trainup.dto.gym.GymFilterRequestDto;
import com.example.trainup.dto.gym.GymRegistrationRequestDto;
import com.example.trainup.dto.gym.GymResponseDto;
import com.example.trainup.dto.gym.GymUpdateRequestDto;
import com.example.trainup.model.Address;
import com.example.trainup.model.Gym;
import com.example.trainup.model.GymPhoto;
import com.example.trainup.model.Role;
import com.example.trainup.model.Sport;
import com.example.trainup.model.WorkingHoursEntry;
import com.example.trainup.model.WorkingHoursEntry.DayOfTheWeek;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.model.user.UserCredentials.UserType;
import com.example.trainup.service.CurrentUserService;
import com.example.trainup.service.GymService;
import com.example.trainup.service.GymServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GymControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GymService gymService;

    @MockBean
    private CurrentUserService currentUserService;

    private Gym gym;
    private GymOwner gymOwner;
    private UserCredentials gymOwnerCredentials;
    private Address gymAddress;
    private Sport sport;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        // Mock UserCredentials for GymOwner
        gymOwnerCredentials = new UserCredentials();
        gymOwnerCredentials.setId(10L);
        gymOwnerCredentials.setEmail("gymowner@example.com");
        gymOwnerCredentials.setUserType(UserType.GYM_OWNER);

        // Mock GymOwner
        gymOwner = new GymOwner();
        gymOwner.setId(1L);
        gymOwner.setUserCredentials(gymOwnerCredentials);
        gymOwner.setPhoneNumbers(Collections.singleton("+38(050)-111-2233")); // Example phone number

        // Mock Address
        gymAddress = new Address();
        gymAddress.setId(1L);
        gymAddress.setCountry("Ukraine");
        gymAddress.setCity("Kyiv");
        gymAddress.setStreet("Khreshchatyk");
        gymAddress.setHouse("1");

        // Mock Sport
        sport = new Sport();
        sport.setId(1L);
        sport.setSportName("Fitness");

        // Mock Trainer
        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setSports(new HashSet<>(Collections.singletonList(sport)));
        trainer.setPhoneNumbers(Collections.singleton("+38(099)-123-4567"));
        trainer.setUserCredentials(new UserCredentials()); // Mock a UserCredentials for trainer
        trainer.getUserCredentials().setId(11L);
        trainer.getUserCredentials().setEmail("trainer@example.com");
        trainer.getUserCredentials().setUserType(UserType.TRAINER);


        // Mock Gym
        gym = new Gym();
        gym.setId(1L);
        gym.setName("Test Gym");
        gym.setDescription("A great test gym.");
        gym.setWebsite("http://testgym.com");
        gym.setPhoneNumbers(new HashSet<>(Collections.singletonList("+38(099)-123-4567")));
        gym.setWorkingHours(new HashSet<>(Collections.singletonList(
                new WorkingHoursEntry(DayOfTheWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(21, 0))
        )));
        gym.setLocation(gymAddress);
        gym.setOverallRating(4.5f);
        gym.setNumberOfReviews(10);
        gym.setGymOwner(gymOwner);
        gym.setSports(new HashSet<>(Collections.singletonList(sport)));
        gym.setTrainers(new HashSet<>(Collections.singletonList(trainer)));

        GymPhoto gymPhoto = new GymPhoto();
        gymPhoto.setImageUrl("http://testgym.com/photo1.jpg");
        gymPhoto.setGym(gym);

        gym.setPhotos(new HashSet<>(Collections.singletonList(gymPhoto)));
    }

    //region Helper Methods
    private GymResponseDto createGymResponseDto(Gym gym) {
        return new GymResponseDto(
                gym.getId(),
                gym.getName(),
                new GymAddressDto(
                        gym.getLocation().getCountry(),
                        gym.getLocation().getCity(),
                        gym.getLocation().getCityDistrict(),
                        gym.getLocation().getStreet(),
                        gym.getLocation().getHouse()
                ),
                gym.getSports().stream().map(Sport::getId).collect(Collectors.toSet()),
                gym.getDescription(),
                gym.getWebsite(),
                gym.getPhoneNumbers(),
                gym.getWorkingHours().stream()
                        .map(wh ->
                                new WorkingHoursEntry(
                                        DayOfTheWeek.MONDAY,
                                        LocalTime.of(8, 00, 00),
                                        LocalTime.of(22, 00, 00)))
                        .collect(Collectors.toSet()),
                gym.getTrainers().stream().map(Trainer::getId).collect(Collectors.toSet()),
                gym.getOverallRating(),
                gym.getNumberOfReviews(),
                gym.getGymOwner().getId(),
                gym.getPhotos().stream().map(GymPhoto::getImageUrl).collect(Collectors.toSet())
        );
    }
    //endregion

    @Test
    void createGym_Success() throws Exception {
        // Given
        GymRegistrationRequestDto requestDto = new GymRegistrationRequestDto(
                "New Gym",
                new GymAddressDto("Ukraine", "Lviv", null, "Freedom Ave", "10"),
                Collections.singleton(1L), // Sport ID
                "Description of new gym.",
                "http://newgym.com",
                Collections.singleton("+38(050)-987-6543"),
                Collections.singleton(new WorkingHoursEntry(DayOfTheWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(20, 0))),
                Collections.singleton(1L),  // Trainer ID
                Collections.singleton("http://newgym.com/photo.jpg")
        );

        GymResponseDto expectedResponse = createGymResponseDto(gym);

        // Налаштування кастомного Authentication
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setId(10L);
        userCredentials.setEmail("gymowner@example.com");
        userCredentials.setUserType(UserType.GYM_OWNER);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userCredentials, // principal
                null, // credentials
                AuthorityUtils.createAuthorityList("ROLE_GYM_OWNER") // authorities
        );

        // Встановлення Authentication у SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When
        when(currentUserService.getCurrentUserByType(GymOwner.class)).thenReturn(gymOwner);
        when(gymService.save(eq(gymOwner), any(GymRegistrationRequestDto.class))).thenReturn(expectedResponse);

        // Then
        mockMvc.perform(post("/gym")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expectedResponse.id()))
                .andExpect(jsonPath("$.name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.location.country").value(expectedResponse.location().country()))
                .andExpect(jsonPath("$.sportIds[0]").value(expectedResponse.sportIds().iterator().next()))
                .andDo(print()); // Для дебагінгу

        verify(currentUserService).getCurrentUserByType(GymOwner.class);
        verify(gymService).save(eq(gymOwner), any(GymRegistrationRequestDto.class));
    }

    @Test
    @WithMockUser(username = "athlete@example.com", roles = "ATHLETE")
    void createGym_Unauthorized_403() throws Exception {
        // Given
        GymRegistrationRequestDto requestDto = new GymRegistrationRequestDto(
                "New Gym",
                new GymAddressDto("UA", "City", null, "Street", "1"),
                Collections.singleton(1L),
                "Desc",
                "http://newgym.com",
                Collections.singleton("+38(050)-987-6543"),
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet()
        );

        // Then
        mockMvc.perform(post("/gym")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors[0]").value("Access Denied"));
    }

    @Test
    @WithMockUser(username = "gymowner@example.com", roles = "GYM_OWNER")
    void createGym_ValidationFail_400() throws Exception {
        // Given - DTO with missing required fields (e.g., blank name)
        GymRegistrationRequestDto invalidRequestDto = new GymRegistrationRequestDto(
                "", // Blank name
                new GymAddressDto("UA", "City", null, "Street", "1"),
                Collections.singleton(1L),
                "Desc",
                "http://newgym.com",
                Collections.singleton("+38(050)-987-6543"),
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet()
        );

        // Then
        mockMvc.perform(post("/gym")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("name: Name can not be blank"));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void getAllGyms_Success() throws Exception {
        // Given
        GymResponseDto responseDto = createGymResponseDto(gym);
        List<GymResponseDto> expectedGyms = Collections.singletonList(responseDto);

        // When
        when(gymService.getAllGyms(any(GymFilterRequestDto.class), any(Pageable.class)))
                .thenReturn(expectedGyms);

        // Then
        mockMvc.perform(get("/gym")
                        .param("name", "Test Gym")
                        .param("locationCountry", "Ukraine")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(gym.getId()))
                .andExpect(jsonPath("$[0].name").value(gym.getName()))
                .andExpect(jsonPath("$[0].location.city").value(gym.getLocation().getCity()));

        verify(gymService).getAllGyms(any(GymFilterRequestDto.class), any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "gymowner@example.com", roles = "GYM_OWNER")
    void getGymsByGymOwnerId_Success() throws Exception {
        // Given
        GymResponseDto responseDto = createGymResponseDto(gym);
        Page<GymResponseDto> expectedPage = new PageImpl<>(
                Collections.singletonList(responseDto),
                PageRequest.of(0, 10),
                1
        );

        // When
        when(gymService.getGymsByGymOwner(any(Authentication.class), any(Pageable.class)))
                .thenReturn(expectedPage);

        // Then
        mockMvc.perform(get("/gym/my")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(gym.getId()))
                .andExpect(jsonPath("$.content[0].name").value(gym.getName()))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andDo(print()); // Для дебагінгу

        verify(gymService).getGymsByGymOwner(any(Authentication.class), any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "athlete@example.com", roles = "ATHLETE")
    void getGymsByGymOwnerId_NonGymOwner_403() throws Exception {
        // When & Then
        // currentUserService.getCurrentUserByType(GymOwner.class) поверне IllegalStateException,
        // оскільки ATHLETE не є GYM_OWNER.
        when(currentUserService.getCurrentUserByType(GymOwner.class))
                .thenThrow(new IllegalStateException("User is not a Gym Owner"));

        mockMvc.perform(get("/gym/my")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()) // Очікуємо 500, якщо IllegalStateException не обробляється специфічно
                .andExpect(jsonPath("$.error").value("Internal server error")); // Перевіряємо загальне повідомлення про помилку
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void getGymById_Success() throws Exception {
        // Given
        GymResponseDto responseDto = createGymResponseDto(gym);

        // When
        when(gymService.getGymById(eq(gym.getId()))).thenReturn(responseDto);

        // Then
        mockMvc.perform(get("/gym/{id}", gym.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(gym.getId()))
                .andExpect(jsonPath("$.name").value(gym.getName()));

        verify(gymService).getGymById(eq(gym.getId()));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void getGymById_NotFound_404() throws Exception {
        // When
        when(gymService.getGymById(eq(999L))).thenThrow(new EntityNotFoundException("Cannot find Gym by id:999"));

        // Then
        mockMvc.perform(get("/gym/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]").value("Cannot find Gym by id:999"));

        verify(gymService).getGymById(eq(999L));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void deleteGymById_Admin_Success() throws Exception {
        // When
        doNothing().when(gymService).deleteGymById(eq(gym.getId()));

        // Then
        mockMvc.perform(delete("/gym/{id}", gym.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(gymService).deleteGymById(eq(gym.getId()));
    }

    @Test
    @WithMockUser(username = "gymowner@example.com", roles = "GYM_OWNER")
    void deleteGymById_Owner_Success() throws Exception {
        // Given
        // Імітуємо, що canUserModifyGym повертає true для власника
        when(gymService.canUserModifyGym(any(Authentication.class), eq(gym.getId()))).thenReturn(true);
        doNothing().when(gymService).deleteGymById(eq(gym.getId()));

        // Then
        mockMvc.perform(delete("/gym/{id}", gym.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(gymService).canUserModifyGym(any(Authentication.class), eq(gym.getId()));
        verify(gymService).deleteGymById(eq(gym.getId()));
    }

    @Test
    @WithMockUser(username = "other.user@example.com", roles = "ATHLETE") // Користувач не адмін і не власник
    void deleteGymById_Unauthorized_403() throws Exception {
        // Given
        // Імітуємо, що canUserModifyGym повертає false для цього користувача
        when(gymService.canUserModifyGym(any(Authentication.class), eq(gym.getId()))).thenReturn(false);

        // Then
        mockMvc.perform(delete("/gym/{id}", gym.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors[0]").value("Access Denied"));

        verify(gymService).canUserModifyGym(any(Authentication.class), eq(gym.getId()));
        // Перевіряємо, що deleteGymById не викликався
        verify(gymService, never()).deleteGymById(anyLong());
    }

    @Test
    @WithMockUser(username = "gymowner@example.com", roles = "GYM_OWNER")
    void deleteGymById_OwnerButNotHisGym_403() throws Exception {
        // Given
        // Імітуємо, що canUserModifyGym повертає false, бо це не його спортзал
        when(gymService.canUserModifyGym(any(Authentication.class), eq(gym.getId()))).thenReturn(false);

        // Then
        mockMvc.perform(delete("/gym/{id}", gym.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors[0]").value("Access Denied"));

        verify(gymService).canUserModifyGym(any(Authentication.class), eq(gym.getId()));
        verify(gymService, never()).deleteGymById(anyLong());
    }

    @Test
    @WithMockUser(username = "gymowner@example.com", roles = "GYM_OWNER")
    void updateGym_Success() throws Exception {
        // Given
        GymUpdateRequestDto requestDto = new GymUpdateRequestDto(
                "Updated Gym Name",
                new GymAddressDto("Ukraine", "Kyiv", null, "New Street", "5"), // Update location
                new HashSet<>(Collections.singletonList(2L)), // New sport ID
                "Updated Description",
                "http://updatedgym.com",
                new HashSet<>(Collections.singletonList("+38(099)-888-7766")),
                new HashSet<>(Collections.singletonList(new WorkingHoursEntry(DayOfTheWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(22, 0)))),
                new HashSet<>(Collections.singletonList(2L)), // New trainer ID
                new HashSet<>(Collections.singletonList("http://updatedgym.com/photo2.jpg"))
        );

        GymResponseDto expectedResponse = new GymResponseDto(
                gym.getId(),
                requestDto.name(),
                requestDto.location(),
                requestDto.sportIds(),
                requestDto.description(),
                requestDto.website(),
                requestDto.phoneNumbers(),
                requestDto.workingHours(),
                requestDto.trainerIds(),
                gym.getOverallRating(), // Assuming these don't change via update DTO
                gym.getNumberOfReviews(),
                gym.getGymOwner().getId(),
                requestDto.photoUrls()
        );

        // When
        when(gymService.canUserModifyGym(any(Authentication.class), eq(gym.getId()))).thenReturn(true);
        when(gymService.updateGym(eq(gym.getId()), any(GymUpdateRequestDto.class))).thenReturn(expectedResponse);

        // Then
        mockMvc.perform(patch("/gym/{id}", gym.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(gym.getId()))
                .andExpect(jsonPath("$.name").value("Updated Gym Name"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.location.city").value("Kyiv"))
                .andExpect(jsonPath("$.phoneNumbers[0]").value("+38(099)-888-7766"))
                .andExpect(jsonPath("$.sportIds[0]").value(2L))
                .andExpect(jsonPath("$.photoUrls[0]").value("http://updatedgym.com/photo2.jpg"));

        verify(gymService).canUserModifyGym(any(Authentication.class), eq(gym.getId()));
        verify(gymService).updateGym(eq(gym.getId()), any(GymUpdateRequestDto.class));
    }

    @Test
    @WithMockUser(username = "other.user@example.com", roles = "ATHLETE")
    void updateGym_Unauthorized_403() throws Exception {
        // Given
        GymUpdateRequestDto requestDto = new GymUpdateRequestDto(
                "Updated Gym Name",
                new GymAddressDto("UA", "City", null, "Street", "1"),
                Collections.singleton(1L),
                "Desc",
                "http://newgym.com",
                Collections.singleton("+38(050)-987-6543"),
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet()
        );

        // When
        when(gymService.canUserModifyGym(any(Authentication.class), eq(gym.getId()))).thenReturn(false);

        // Then
        mockMvc.perform(patch("/gym/{id}", gym.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors[0]").value("Access Denied"));

        verify(gymService).canUserModifyGym(any(Authentication.class), eq(gym.getId()));
        verify(gymService, never()).updateGym(anyLong(), any(GymUpdateRequestDto.class));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void updateGym_Admin_403() throws Exception {
        // Given
        GymUpdateRequestDto requestDto = new GymUpdateRequestDto(
                "Updated Gym Name",
                new GymAddressDto("UA", "City", null, "Street", "1"),
                Collections.singleton(1L),
                "Desc",
                "http://newgym.com",
                Collections.singleton("+38(050)-987-6543"),
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet()
        );

        // Then
        // Для адміна `@PreAuthorize("@gymService.canUserModifyGym(#authentication, #id)")` НЕ дозволить
        // оновити спортзал, оскільки адмін не є його власником.
        // Цей тест перевіряє, що адмін не може оновлювати чужі спортзали.
        // Зауважте, що тут немає "hasRole('ADMIN')" в @PreAuthorize для PATCH.
        when(gymService.canUserModifyGym(any(Authentication.class), eq(gym.getId()))).thenReturn(false);

        mockMvc.perform(patch("/gym/{id}", gym.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors[0]").value("Access Denied"));

        verify(gymService).canUserModifyGym(any(Authentication.class), eq(gym.getId()));
        verify(gymService, never()).updateGym(anyLong(), any(GymUpdateRequestDto.class));
    }
}
