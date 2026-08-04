package com.taskforge.common;

/**
 * Package marker class.
 * 
 * In Java, empty packages can cause issues with some tools.
 * This class serves as a placeholder and will be replaced with 
 * actual DTOs, utilities, and shared code as we build the application.
 * 
 * Coming soon in this package:
 * - dto/ - Data Transfer Objects (using Java Records)
 * - exception/ - Custom exceptions
 * - util/ - Utility classes
 * - constants/ - Application constants
 */
public final class CommonModule {
    
    private CommonModule() {
        // Utility class - prevent instantiation
    }
    
    public static final String MODULE_NAME = "taskforge-common";
}
