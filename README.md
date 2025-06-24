# 🔐 jwt-service-loadtest

Проект для автоматического нагрузочного тестирования микросервисов `user-auth` и `verification-service` с помощью **Apache JMeter**, **Micrometer**, **Prometheus**, **Grafana** и **GitHub Actions**.

## 🚀 Возможности

- Автоматическая сборка и запуск микросервисов через `docker-compose`
- Генерация нагрузки с помощью JMeter (.jmx-сценарий)
- Сбор метрик в Prometheus с отображением в Grafana
- Автоматический запуск тестов при коммите (`CI`)
- Генерация и публикация HTML-отчётов JMeter

## 🩵 Стек технологий

- Java 17 + Spring Boot
- Redis
- Apache JMeter
- Prometheus + Grafana
- Micrometer (Prometheus registry)
- Docker / Docker Compose
- GitHub Actions

## 📦 Структура проекта

```
.
├── docker-compose.yml                 # Основной стек: redis, микросервисы, Prometheus, Grafana
├── jwt_mock_test.jmx                  # JMeter-сценарий нагрузки
├── tools/
│   └── jmeter/                        # JMeter и его бинарники
├── report/                            # Генерируемый HTML-отчет после теста
├── user-auth/                         # Auth-сервис (Spring Boot)
├── verification-service/             # Внешний мок-сервис (Spring Boot)
└── .github/
    └── workflows/
        └── ci.yml                     # CI-пайплайн с автозапуском тестов
```

## ⚙️ Как запустить локально

```bash
# Собрать и запустить все сервисы
docker-compose up --build

# Открыть Grafana: http://localhost:3000 (логин/пароль: admin/admin)

# Запустить JMeter тест вручную
tools/jmeter/bin/jmeter -n -t jwt_mock_test.jmx -l result.jtl -e -o report
```

## 📊 Метрики и мониторинг

- Prometheus собирает метрики по эндпоинту `/actuator/prometheus`
- Grafana подключается к Prometheus и отображает дашборд
- Micrometer интегрирован в оба микросервиса

## 🧪 CI / CD

При каждом `push` или `pull request` в `main`:

1. Собираются микросервисы
2. Поднимаются контейнеры
3. Запускается JMeter тест
4. Генерируется HTML-отчет
5. Загружаются артефакты (лог-файлы, отчет)
