package com.example.trainup.controller;

import com.example.trainup.dto.sport.SportDto;
import com.example.trainup.dto.sport.SportRequestDto;
import com.example.trainup.service.PhotoService;
import com.example.trainup.service.SportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/sport")
@Validated
@RequiredArgsConstructor
@Log4j2
@Tag(
        name = "Sport Management",
        description = "Endpoints for managing sports categories. Restricted to ADMIN users."
)
public class SportController {
    private final SportService sportService;
    private final PhotoService photoService;

    @GetMapping
    @Operation(
            summary = "Retrieve All Sports",
            description = "Allows ADMIN users to retrieve a paginated list of all sports, "
                    + "optionally filtered by ID or name.")
    public List<SportDto> getAllSports(
            @RequestParam(required = false) @Positive Long id,
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        SportDto sportFilterDto = new SportDto(id, name, null);
        log.info("Attempting to fetch all sports with filter: {} and pageable: {}",
                sportFilterDto, pageable);

        List<SportDto> responseDtos = sportService.getAllSports(sportFilterDto, pageable);

        log.info("Successfully retrieved {} sports with specified criteria.", responseDtos.size());
        return responseDtos;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create New Sport",
            description = "Allows an ADMIN user to create a new sport category."
    )
    public SportDto createSport(@RequestBody @Valid SportRequestDto requestDto) {
        log.info("Attempting to create a new sport with request: {}", requestDto);
        SportDto sportDto = sportService.createSport(requestDto);

        log.info("Sport '{}' successfully created with ID: {}",
                sportDto.sportName(), sportDto.id());
        return sportDto;
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update Sport by ID",
            description = "Allows an ADMIN user to update an existing sport category by its ID."
    )
    public SportDto updateSport(@PathVariable @Positive Long id,
                         @RequestBody SportRequestDto requestDto) {
        log.info("Attempting to update sport with ID: {} with request: {}", id, requestDto);
        SportDto sportDto = sportService.updateSport(id, requestDto);

        log.info("Sport with ID: {} successfully updated.", id);
        return sportDto;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Delete Sport by ID",
            description = "Allows an ADMIN user to delete a sport category by its ID."
    )
    public ResponseEntity<Void> deleteSport(@PathVariable @Positive Long id) {
        log.info("Attempting to delete sport with ID: {}", id);
        sportService.deleteSport(id);

        log.info("Sport with ID: {} successfully deleted.", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/icon/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Upload sport icon by ID",
            description = "Allows an ADMIN user to upload sport icon."
    )
    public ResponseEntity<String> uploadIcon(
            @PathVariable @Positive Long id,
            @RequestParam("file")
            @Parameter(
                    description = "Image file to upload (JPEG, PNG, SVG)",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            ) MultipartFile file
    ) {
        log.info("Attempting to upload icon for sport ID: {}", id);
        if (file.isEmpty()) {
            log.warn("Upload attempt with empty file for sport ID: {}", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        if (!Arrays.asList("image/jpeg", "image/png", "image/svg+xml")
                .contains(file.getContentType())) {
            log.warn("Invalid file format uploaded by sport ID: {}. Content type: {}",
                    id, file.getContentType());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid file format. Only JPEG and PNG are supported.");
        }

        try {
            String imageUrl = photoService.uploadSportIcon(id, file);
            log.info("Icon uploaded successfully for sport ID: {}. URL: {}", id, imageUrl);
            return ResponseEntity.ok(imageUrl);
        } catch (EntityNotFoundException e) {
            log.error("Entity not found during icon upload for sport ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SecurityException e) {
            log.error("Security exception during icon upload for sport ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during photo upload for sport ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }
}
