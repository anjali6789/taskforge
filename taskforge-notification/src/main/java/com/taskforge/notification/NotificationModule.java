package com.taskforge.notification;

/**
 * Package marker class.
 * 
 * This module handles all async communication:
 * - kafka/ - Kafka producers and consumers
 * - rabbitmq/ - RabbitMQ message handlers
 * - email/ - Email service and templates
 * - config/ - Messaging configuration
 */
public final class NotificationModule {
    
    private NotificationModule() {
        // Utility class - prevent instantiation
    }
    
    public static final String MODULE_NAME = "taskforge-notification";
}
