package com.taskforge.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * TaskForge Application Entry Point
 * 
 * This is where Spring Boot starts. Let's understand each annotation:
 * 
 * @SpringBootApplication is a meta-annotation that combines:
 *   - @Configuration: This class can define @Bean methods
 *   - @EnableAutoConfiguration: Spring Boot guesses what you need based on dependencies
 *   - @ComponentScan: Scans this package and sub-packages for @Component, @Service, etc.
 * 
 * IMPORTANT: By default, @ComponentScan only scans THIS package (com.taskforge.api).
 * We need to tell it to also scan our other modules, hence scanBasePackages.
 */
@SpringBootApplication(scanBasePackages = "com.taskforge")
@ConfigurationPropertiesScan("com.taskforge")
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.taskforge")
@EntityScan(basePackages = "com.taskforge")  // Tells JPA to find @Entity classes in core module
public class TaskForgeApplication {

    /**
     * The main() method - where everything begins.
     * 
     * SpringApplication.run() does a LOT:
     * 1. Creates ApplicationContext (the Spring container)
     * 2. Scans for components and creates beans
     * 3. Starts embedded Tomcat server
     * 4. Runs any CommandLineRunner or ApplicationRunner beans
     * 
     * @param args Command line arguments, passed to Spring
     */
    public static void main(String[] args) {
        SpringApplication.run(TaskForgeApplication.class, args);
    }
}
