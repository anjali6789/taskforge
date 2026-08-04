-- ============================================================
-- V1__create_tasks_table.sql
-- Creates the tasks table to match the Task entity
--
-- Flyway runs this automatically on app startup.
-- Never edit this file after it has run — create a new V2__ file instead.
-- ============================================================

CREATE TABLE tasks (
    -- UUID stored as VARCHAR to match @GeneratedValue(strategy = UUID)
    id          VARCHAR(36)  PRIMARY KEY,

    -- @Column(nullable = false)
    title       VARCHAR(255) NOT NULL,

    -- @Column(columnDefinition = "TEXT")
    description TEXT,

    -- @Enumerated(EnumType.STRING) + @Column(nullable = false)
    -- Possible values: TODO, IN_PROGRESS, IN_REVIEW, DONE, CANCELED
    status      VARCHAR(50)  NOT NULL DEFAULT 'TODO',

    -- @Enumerated(EnumType.STRING)
    -- Possible values: LOW, MEDIUM, HIGH
    priority    VARCHAR(50)           DEFAULT 'MEDIUM',

    -- @CreatedDate + @Column(updatable = false)
    created_at  TIMESTAMP,

    -- @LastModifiedDate
    updated_at  TIMESTAMP
);
