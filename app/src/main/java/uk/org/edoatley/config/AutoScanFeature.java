package uk.org.edoatley.config;

import java.io.IOException;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.Populator;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ClasspathDescriptorFileFinder;
import org.glassfish.hk2.utilities.DuplicatePostProcessor;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;

public class AutoScanFeature implements Feature {

    @Inject
    ServiceLocator serviceLocator;

    @Override
    public boolean configure(FeatureContext context) {

        DynamicConfigurationService dcs =
                serviceLocator.getService(DynamicConfigurationService.class);
        Populator populator = dcs.getPopulator();
        // Populator - populate HK2 service locators from inhabitants files
        // ClasspathDescriptorFileFinder - find files from META-INF/hk2-locator/default
        try {
            populator.populate(new ClasspathDescriptorFileFinder(this.getClass().getClassLoader()),
                    new DuplicatePostProcessor());
        } catch (MultiException | IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
