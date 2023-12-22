CREATE TABLE groups (
    group_id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(64) NOT NULL,
    PRIMARY KEY(group_id),
    UNIQUE(name)
);

CREATE TABLE students (
    student_id BIGINT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(64) NOT NULL,
    second_name VARCHAR(64) NOT NULL,
    middle_name VARCHAR(64) NOT NULL,
    birthday DATE NOT NULL,
    group_id BIGINT,
    PRIMARY KEY(student_id),
    UNIQUE(first_name, second_name, middle_name),
    FOREIGN KEY (group_id) REFERENCES groups(group_id) ON DELETE SET NULL
);