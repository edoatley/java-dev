package uk.org.edoatley.config;

import java.io.IOException;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.Populator;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ClasspathDescriptorFileFinder;
import org.glassfish.hk2.utilities.DuplicatePostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;

public class AutoScanFeature implements Feature {

    private static final Logger log = LoggerFactory.getLogger(AutoScanFeature.class);

    @Inject
    ServiceLocator serviceLocator;

    @Override
    public boolean configure(FeatureContext context) {
        log.info("AutoScanFeature configure");
        DynamicConfigurationService dcs =
                serviceLocator.getService(DynamicConfigurationService.class);
        Populator populator = dcs.getPopulator();

        try {
            populator.populate(new ClasspathDescriptorFileFinder(this.getClass().getClassLoader()),
                    new DuplicatePostProcessor());
        } catch (MultiException | IOException e) {
            log.error("Error populating HK2 service locators", e);
            throw new RuntimeException(e);
        }
        return true;
    }

}
