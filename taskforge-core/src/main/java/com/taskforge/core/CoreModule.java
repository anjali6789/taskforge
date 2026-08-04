package com.taskforge.core;

/**
 * Package marker class.
 * 
 * This module will contain the heart of TaskForge:
 * - entity/ - JPA entities (User, Task, Project, Sprint, Comment)
 * - repository/ - Spring Data repositories
 * - service/ - Business logic services
 * - event/ - Domain events
 * - config/ - Database and JPA configuration
 */
public final class CoreModule {
    
    private CoreModule() {
        // Utility class - prevent instantiation
    }
    
    public static final String MODULE_NAME = "taskforge-core";
}
