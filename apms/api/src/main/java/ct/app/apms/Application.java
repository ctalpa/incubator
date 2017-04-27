package ct.app.apms;

import ct.app.apms.config.ApmsProperties;
import ct.app.apms.config.DefaultProfileUtil;
import ct.app.apms.config.db.APMSJpaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by c.talpa on 25/01/2017.
 */
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ApmsProperties.class)
@EntityScan(basePackageClasses = {Application.class,Jsr310JpaConverters.class})
@EnableJpaRepositories(repositoryBaseClass = APMSJpaRepository.class)
public class Application {

    // Adapted from JHipster
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        DefaultProfileUtil.addDefaultProfile(app);
        app.run(args).getEnvironment();
    }
}
