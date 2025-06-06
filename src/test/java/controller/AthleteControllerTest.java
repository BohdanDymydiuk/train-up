package controller;

//import static org.mockito.Mockito.when;
//
//import com.example.trainup.controller.AthleteController;
//import com.example.trainup.dto.users.athlete.AthleteResponseDto;
//import com.example.trainup.dto.users.athlete.AthleteUpdateRequestDto;
//import com.example.trainup.model.enums.Gender;
//import com.example.trainup.service.users.AthleteService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import java.time.LocalDate;
//import java.util.Set;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//
//@WebMvcTest(AthleteController.class)
//@DisplayName("AthleteController Integration Test")
//public class AthleteControllerTest {
//        @Autowired
//        private MockMvc mockMvc;
//
//        @MockBean
//        private AthleteService athleteService;
//
//        private ObjectMapper objectMapper;
//        private AthleteResponseDto testAthleteResponseDto;
//        private AthleteUpdateRequestDto testAthleteUpdateRequestDto;
//
//        @BeforeEach
//        void setUp() {
//            objectMapper = new ObjectMapper();
//            objectMapper.registerModule(new JavaTimeModule());
//
//            testAthleteResponseDto = new AthleteResponseDto(
//                    1L,
//                    "John",
//                    "Doe",
//                    Gender.MALE,
//                    LocalDate.of(1990, 1, 1),
//                    "http://example.com/profile.jpg",
//                    "john.doe@example.com",
//                    "ATHLETE",
//                    Set.of("+38(050)-123-4567"),
//                    Set.of(1L, 2L),
//                    true,
//                    false
//            );
//
//            testAthleteUpdateRequestDto = new AthleteUpdateRequestDto(
//                    "Johnny",
//                    "Doe",
//                    Gender.MALE,
//                    LocalDate.of(1990, 1, 1),
//                    "http://example.com/new_profile.jpg",
//                    Set.of("+38(050)-765-4321"),
//                    Set.of(3L),
//                    false,
//                    true
//            );
//        }
//
//        @Test
//        @WithMockUser(roles = "ADMIN")
//        @DisplayName("should get athlete by ID for ADMIN role")
//        void getAthleteById_AdminAccess_Success() throws Exception {
//            when(athleteService.getAthleteById(1L)).thenReturn(testAthleteResponseDto);
//
//            mockMvc.perform(get("/athlete/{id}", 1L))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(1L))
//                    .andExpect(jsonPath("$.firstName").value("John"))
//                    .andExpect(jsonPath("$.email").value("john.doe@example.com"));
//
//            verify(athleteService, times(1)).getAthleteById(1L);
//        }
//
//        @Test
//        @WithMockUser(username = "john.doe@example.com", roles = "ATHLETE")
//        @DisplayName("should get athlete by ID for owner (athlete themselves)")
//        void getAthleteById_OwnerAccess_Success() throws Exception {
//            // canUserModifyAthlete тепер перевіряє email
//            when(athleteService.canUserModifyAthlete(any(Authentication.class), eq(1L)))
//                    .thenReturn(true);
//            when(athleteService.getAthleteById(1L)).thenReturn(testAthleteResponseDto);
//
//            mockMvc.perform(get("/athlete/{id}", 1L))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(1L))
//                    .andExpect(jsonPath("$.firstName").value("John"));
//
//            verify(athleteService, times(1)).getAthleteById(1L);
//            verify(athleteService, times(1)).canUserModifyAthlete(any(Authentication.class),
//            eq(1L));
//        }
//
//        @Test
//        @WithMockUser(username = "other@example.com", roles = "ATHLETE")
//        @DisplayName("should forbid getting athlete by ID for non-owner athlete")
//        void getAthleteById_NonOwnerAccess_Forbidden() throws Exception {
//            when(athleteService.canUserModifyAthlete(any(Authentication.class), eq(1L)))
//                    .thenReturn(false);
//
//            mockMvc.perform(get("/athlete/{id}", 1L))
//                    .andExpect(status().isForbidden());
//
//            verify(athleteService, never()).getAthleteById(anyLong());
//            verify(athleteService, times(1)).canUserModifyAthlete(any(Authentication.class),
//            eq(1L));
//        }
//
//        @Test
//        @DisplayName("should require authentication for getting athlete by ID")
//        void getAthleteById_NoAuthentication_Unauthorized() throws Exception {
//            mockMvc.perform(get("/athlete/{id}", 1L))
//                    .andExpect(status().isUnauthorized());
//
//            verify(athleteService, never()).getAthleteById(anyLong());
//            verify(athleteService, never()).canUserModifyAthlete(any(), anyLong());
//        }
//
//        @Test
//        @WithMockUser(roles = "ADMIN")
//        @DisplayName("should get all athletes with ADMIN role and filters")
//        void getAllAthlete_AdminAccess_WithFilters_Success() throws Exception {
//            List<AthleteResponseDto> athletes = List.of(testAthleteResponseDto);
//            Pageable pageable = PageRequest.of(0, 10);
//
//            when(athleteService.getAllAthlete(any(AthleteFilterRequestDto.class), eq(pageable)))
//                    .thenReturn(athletes);
//
//            mockMvc.perform(get("/athlete")
//                            .param("firstName", "John")
//                            .param("gender", "MALE")
//                            .param("dateOfBirth", "1990-01-01")
//                            .param("sportIds", "1", "2")
//                            .param("emailPermission", "true")
//                            .param("phonePermission", "false")
//                            .param("page", "0")
//                            .param("size", "10")
//                            .param("sort", "id,asc"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$[0].firstName").value("John"))
//                    .andExpect(jsonPath("$.length()").value(1));
//
//            // Створюємо очікуваний AthleteFilterRequestDto для перевірки
//            AthleteFilterRequestDto expectedFilter = new AthleteFilterRequestDto(
//                    "John", null, Gender.MALE, LocalDate.of(1990, 1, 1),
//                    Set.of(1L, 2L), true, false
//            );
//
//            verify(athleteService, times(1)).getAllAthlete(
//                    argThat(filter ->
//                            filter.firstName().equals(expectedFilter.firstName())
//                                    && filter.gender().equals(expectedFilter.gender())
//                                    && filter.dateOfBirth().equals(expectedFilter.dateOfBirth())
//                                    && filter.sportIds().equals(expectedFilter.sportIds())
//                                    && filter.emailPermission()
//                                    .equals(expectedFilter.emailPermission())
//                                    && filter.phonePermission()
//                                    .equals(expectedFilter.phonePermission())
//                    ),
//                    eq(pageable)
//            );
//        }
//
//        @Test
//        @WithMockUser(roles = "ATHLETE")
//        @DisplayName("should forbid getting all athletes for ATHLETE role")
//        void getAllAthlete_AthleteAccess_Forbidden() throws Exception {
//            mockMvc.perform(get("/athlete"))
//                    .andExpect(status().isForbidden());
//
//            verify(athleteService, never()).getAllAthlete(any(), any());
//        }
//
//        @Test
//        @DisplayName("should require authentication for getting all athletes")
//        void getAllAthlete_NoAuthentication_Unauthorized() throws Exception {
//            mockMvc.perform(get("/athlete"))
//                    .andExpect(status().isUnauthorized());
//
//            verify(athleteService, never()).getAllAthlete(any(), any());
//        }
//
//
//        @Test
//        @WithMockUser(username = "john.doe@example.com", roles = "ATHLETE")
//        @DisplayName("should update athlete by ID for owner (athlete themselves)")
//        void updateAthleteById_OwnerAccess_Success() throws Exception {
//            when(athleteService.canUserModifyAthlete(any(Authentication.class), eq(1L)))
//                    .thenReturn(true);
//            when(athleteService.updateAthleteByAuth(any(Authentication.class),
//                    eq(testAthleteUpdateRequestDto))).thenReturn(testAthleteResponseDto);
//
//            mockMvc.perform(patch("/athlete/{id}", 1L)
//                            .with(csrf())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper
//                            .writeValueAsString(testAthleteUpdateRequestDto)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.firstName").value("John"));
//
//            verify(athleteService, times(1)).updateAthleteByAuth(any(Authentication.class),
//                    eq(testAthleteUpdateRequestDto));
//            verify(athleteService, times(1)).canUserModifyAthlete(any(Authentication.class),
//            eq(1L));
//        }
//
//        @Test
//        @WithMockUser(roles = "ADMIN")
//        @DisplayName("should update athlete by ID for ADMIN role (via canUserModifyAthlete)")
//        void updateAthleteById_AdminAccess_Success() throws Exception {
//            when(athleteService.canUserModifyAthlete(any(Authentication.class), eq(1L)))
//                    .thenReturn(true);
//            when(athleteService.updateAthleteByAuth(any(Authentication.class),
//                    eq(testAthleteUpdateRequestDto)))
//                    .thenReturn(testAthleteResponseDto);
//
//            mockMvc.perform(patch("/athlete/{id}", 1L)
//                            .with(csrf())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper
//                            .writeValueAsString(testAthleteUpdateRequestDto)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.firstName").value("John"));
//
//            verify(athleteService, times(1)).updateAthleteByAuth(any(Authentication.class),
//                    eq(testAthleteUpdateRequestDto));
//            verify(athleteService, times(1)).canUserModifyAthlete(any(Authentication.class),
//            eq(1L));
//        }
//
//        @Test
//        @WithMockUser(username = "other@example.com", roles = "ATHLETE")
//        @DisplayName("should forbid updating athlete by ID for non-owner athlete")
//        void updateAthleteById_NonOwnerAccess_Forbidden() throws Exception {
//            when(athleteService.canUserModifyAthlete(any(Authentication.class), eq(1L)))
//                    .thenReturn(false);
//
//            mockMvc.perform(patch("/athlete/{id}", 1L)
//                            .with(csrf())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper
//                            .writeValueAsString(testAthleteUpdateRequestDto)))
//                    .andExpect(status().isForbidden());
//
//            verify(athleteService, never()).updateAthleteByAuth(any(), any());
//            verify(athleteService, times(1)).canUserModifyAthlete(any(Authentication.class),
//            eq(1L));
//        }
//
//        @Test
//        @DisplayName("should require authentication for updating athlete by ID")
//        void updateAthleteById_NoAuthentication_Unauthorized() throws Exception {
//            mockMvc.perform(patch("/athlete/{id}", 1L)
//                            .with(csrf())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper
//                            .writeValueAsString(testAthleteUpdateRequestDto)))
//                    .andExpect(status().isUnauthorized());
//
//            verify(athleteService, never()).updateAthleteByAuth(any(), any());
//            verify(athleteService, never()).canUserModifyAthlete(any(), anyLong());
//        }
//
//        @Test
//        @WithMockUser(roles = "ADMIN")
//        @DisplayName("should delete athlete by ID for ADMIN role")
//        void deleteAthleteById_AdminAccess_Success() throws Exception {
//            doNothing().when(athleteService).deleteAthleteById(1L);
//
//            mockMvc.perform(delete("/athlete/{id}", 1L).with(csrf()))
//                    .andExpect(status().isNoContent());
//
//            verify(athleteService, times(1)).deleteAthleteById(1L);
//        }
//
//        @Test
//        @WithMockUser(username = "john.doe@example.com", roles = "ATHLETE")
//        @DisplayName("should delete athlete by ID for owner (athlete themselves)")
//        void deleteAthleteById_OwnerAccess_Success() throws Exception {
//            when(athleteService.canUserModifyAthlete(any(Authentication.class), eq(1L)))
//                    .thenReturn(true);
//            doNothing().when(athleteService).deleteAthleteById(1L);
//
//            mockMvc.perform(delete("/athlete/{id}", 1L).with(csrf()))
//                    .andExpect(status().isNoContent());
//
//            verify(athleteService, times(1)).deleteAthleteById(1L);
//            verify(athleteService, times(1)).canUserModifyAthlete(any(Authentication.class),
//            eq(1L));
//        }
//
//        @Test
//        @WithMockUser(username = "other@example.com", roles = "ATHLETE")
//        @DisplayName("should forbid deleting athlete by ID for non-owner athlete")
//        void deleteAthleteById_NonOwnerAccess_Forbidden() throws Exception {
//            when(athleteService.canUserModifyAthlete(any(Authentication.class), eq(1L)))
//                    .thenReturn(false);
//
//            mockMvc.perform(delete("/athlete/{id}", 1L).with(csrf()))
//                    .andExpect(status().isForbidden());
//
//            verify(athleteService, never()).deleteAthleteById(anyLong());
//            verify(athleteService, times(1)).canUserModifyAthlete(any(Authentication.class),
//            eq(1L));
//        }
//
//        @Test
//        @DisplayName("should require authentication for deleting athlete by ID")
//        void deleteAthleteById_NoAuthentication_Unauthorized() throws Exception {
//            mockMvc.perform(delete("/athlete/{id}", 1L).with(csrf()))
//                    .andExpect(status().isUnauthorized());
//
//            verify(athleteService, never()).deleteAthleteById(anyLong());
//            verify(athleteService, never()).canUserModifyAthlete(any(), anyLong());
//        }
//}
