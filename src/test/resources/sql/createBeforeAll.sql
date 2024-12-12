CREATE TABLE users
(
    id       bigint generated always as identity primary key,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL
);


CREATE TABLE task
(
    id          bigint generated always as identity primary key,
    title       VARCHAR(255) NOT NULL,
    details     TEXT,
    status      VARCHAR(255) NOT NULL,
    priority    VARCHAR(255) NOT NULL,
    author_id   BIGINT,
    executor_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE SET NULL,
    FOREIGN KEY (executor_id) REFERENCES users (id) ON DELETE SET NULL
);


CREATE TABLE comment
(
    id        bigint generated always as identity primary key,
    details   TEXT   NOT NULL,
    task_id   BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    FOREIGN KEY (task_id) REFERENCES task (id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE
);