package com.colak.springtutorial.config;

import org.togglz.core.Feature;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum ProductCheckFeature implements Feature {

    @Label("Old Logging Feature")
    OLD_LOGGING,

    @EnabledByDefault
    @Label("Enhanced Logging Feature")
    ENHANCED_LOGGING;

    public static boolean isEnhancedLoggingActive() {
        return FeatureContext.getFeatureManager().isActive(ENHANCED_LOGGING);
    }

}
