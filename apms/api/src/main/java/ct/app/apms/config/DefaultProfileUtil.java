package ct.app.apms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

// Adapted from JHipster
public final class DefaultProfileUtil {

    private static final String PROP_LOGGING_CONFIG = "logging.config";
    private static final String PROP_BANNER_LOCATION = "banner.location";
    private static final String PROP_SPRING_CONFIG_LOCATION = "spring.config.location";
    private static final String PROP_DERBY_ERROR_FIELD = "derby.stream.error.field";
    private static final String DERBY_ERROR_STREAM = "java.lang.System.err";

    // private ctor to prevent instantiation
    private DefaultProfileUtil() {
    }

    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties = new HashMap<>();
        defProperties.put(PROP_SPRING_CONFIG_LOCATION, Constants.SPRING_CONFIG_LOCATION);
        defProperties.put(PROP_LOGGING_CONFIG, Constants.LOGBACK_CONFIG_FILE);
        defProperties.put(PROP_BANNER_LOCATION, Constants.LOGBACK_BANNER_FILE);
        app.setDefaultProperties(defProperties);
        // Point Derby log to STDERR after configutation is initialized
        app.addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
            @Override
            public void initialize (ConfigurableApplicationContext ctx) {
                System.setProperty(PROP_DERBY_ERROR_FIELD, DERBY_ERROR_STREAM);
                final Logger logger = LoggerFactory.getLogger (DefaultProfileUtil.class);
                logger.trace ("set Derby log file to {}", DERBY_ERROR_STREAM);
            }
        });
        
    }

    public static String[] getActiveProfiles(Environment env) {
        String[] profiles = env.getActiveProfiles();

        return profiles.length == 0 ? env.getDefaultProfiles() : profiles;
    }

}
