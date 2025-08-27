package com.example.trainup.config;

import com.example.trainup.model.InitializationLog;
import com.example.trainup.model.Sport;
import com.example.trainup.repository.InitializationLogRepository;
import com.example.trainup.repository.SportRepository;
import com.example.trainup.service.PhotoService;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@Profile({"!test"})
@Log4j2
@RequiredArgsConstructor
public class SportIconInitializer implements CommandLineRunner {
    private static final String ICONS_PATH = "classpath:icons/";
    private static final String INITIALIZATION_TYPE = "SPORT_ICONS";
    private static final List<String> SPORT_NAMES = Arrays.asList(
            "TRX",
            "Аква аеробіка",
            "Біг",
            "Бокс",
            "Дитяче плавання",
            "Жіночий бокс",
            "Йога",
            "Кроссфіт",
            "Плавання для немовлят",
            "Плавання",
            "Сайклінг",
            "Сквош",
            "Стретчинг",
            "Тренажерний зал"
    );

    private final SportRepository sportRepository;
    private final PhotoService photoService;
    private final InitializationLogRepository initializationLogRepository;
    private final ResourceLoader resourceLoader;

    @Override
    public void run(String... args) throws Exception {
        if (initializationLogRepository.findByInitializationType(INITIALIZATION_TYPE).isPresent()) {
            log.info("Sport icons initialization already completed. Skipping.");
            return;
        }

        log.info("Starting sport icons initialization...");

        InitializationLog logEntry = new InitializationLog();
        logEntry.setInitializationType(INITIALIZATION_TYPE);
        logEntry.setExecutedAt(LocalDateTime.now());
        logEntry.setStatus("STARTED");
        initializationLogRepository.save(logEntry);

        try {
            for (String sportName : SPORT_NAMES) {
                Sport sport = sportRepository.findBySportName(sportName).orElse(null);
                if (sport == null) {
                    log.warn("Sport not found: {}", sportName);
                    continue;
                }

                if (!ObjectUtils.isEmpty(sport.getSportIconUrl())) {
                    log.info("Icon already uploaded for sport: {}", sportName);
                    continue;
                }

                String fileName = sportName.replace(" ", "_") + ".svg";
                Resource resource = resourceLoader.getResource(ICONS_PATH + fileName);

                if (!resource.exists()) {
                    log.warn("Icon file not found: {}", fileName);
                    continue;
                }

                try (InputStream inputStream = resource.getInputStream()) {
                    String imageUrl = photoService
                            .uploadSportIcon(sport.getId(), inputStream, fileName);
                    sport.setSportIconUrl(imageUrl);
                    sportRepository.save(sport);
                    log.info("Uploaded icon for sport {}: {}", sportName, imageUrl);
                }
            }

            logEntry.setStatus("SUCCESS");
            logEntry.setExecutedAt(LocalDateTime.now());
            initializationLogRepository.save(logEntry);
            log.info("Sport icons initialization completed successfully.");
        } catch (Exception e) {
            logEntry.setStatus("FAILED");
            logEntry.setExecutedAt(LocalDateTime.now());
            initializationLogRepository.save(logEntry);
            log.error("Sport icons initialization failed: {}", e.getMessage());
            throw e;
        }
    }
}
