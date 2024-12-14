# task-management-system (Система управления задачами)

## Функциональные возможности

- **Управление пользователями**: Управление пользователями с ролями (Администратор, Клиент).
- **Управление задачами**: Создание, обновление и назначение задач с приоритетами и статусами.
- **Система комментариев**: Добавление комментариев к задачам для совместной работы.
- **REST API**: Полная документация API с использованием OpenAPI (Swagger).

## Используемые технологии

- **Backend**: Java, Spring Boot, Spring Security(JWT), Hibernate
- **База данных**: PostgreSQL
- **Документация API**: Swagger/OpenAPI
- **Тестирование**: Testcontainers для интеграционного тестирования
- **Система сборки**: Gradle
- **Контейнеризация**: Docker

## Начало работы

### Клонирование репозитория

```bash
git clone https://github.com/chotchipWeb/task-management-system.git
cd task-management-system
```

### Запуск приложения

#### С использованием Docker Compose

1. Сборка и запуск контейнеров:
   ```bash
   docker-compose up --build
   ```

### Документация API

После запуска приложения перейдите по адресу:

```
http://localhost:8080/swagger-ui/index.html
```

## Схема базы данных

### Таблицы

- **users**: Хранит информацию о пользователях.
- **tasks**: Хранит данные задач, такие как заголовок, приоритет и статус.
- **comments**: Хранит комментарии, связанные с задачами.

### Примеры SQL-запросов

```sql
-- Создание таблицы пользователей
CREATE TABLE users
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    VARCHAR(255) UNIQUE                              NOT NULL,
    password VARCHAR(255)                                     NOT NULL,
    role     VARCHAR(255) CHECK (role IN ('ADMIN', 'CLIENT')) NOT NULL
);

-- Создание таблицы задач
CREATE TABLE tasks
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    details     TEXT,
    priority    VARCHAR(255) CHECK (priority IN ('HIGH', 'MIDDLE', 'LOW')),
    status      VARCHAR(255) CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED')),
    author_id   BIGINT REFERENCES users (id),
    executor_id BIGINT REFERENCES users (id)
);

-- Создание таблицы комментариев
CREATE TABLE comments
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    details   TEXT NOT NULL,
    task_id   BIGINT REFERENCES tasks (id),
    author_id BIGINT REFERENCES users (id)
);
```

## Тестирование

Интеграционные тесты используют Testcontainers для запуска PostgreSQL в контейнере Docker. Убедитесь, что Docker запущен
перед выполнением тестов.

```bash
./gradlew test
```
