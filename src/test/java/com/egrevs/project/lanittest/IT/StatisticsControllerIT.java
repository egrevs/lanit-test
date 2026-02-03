package com.egrevs.project.lanittest.IT;

import com.egrevs.project.lanittest.config.TestContainersConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("integration")
@Import(TestContainersConfig.class)
public class StatisticsControllerIT {
}
