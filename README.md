# NFC Card Emulator (NFC-bumber)

[![Latest Release](https://img.shields.io/github/v/release/disk0Dancer/NFC-bumber?label=Latest%20Release&color=brightgreen)](https://github.com/disk0Dancer/NFC-bumber/releases/latest)
[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](LICENSE)
[![Android](https://img.shields.io/badge/Android-8.0%2B-green.svg)](https://developer.android.com)

## 📲 Установка | Installation

### Через Obtainium (Рекомендуется | Recommended) 🚀

[<img src="https://raw.githubusercontent.com/ImranR98/Obtainium/main/assets/graphics/badge_obtainium.png" alt="Get it on Obtainium" height="80">](https://apps.obtainium.imranr.dev/redirect?r=obtainium://add/https://github.com/disk0Dancer/NFC-bumber)

**Что такое Obtainium? | What is Obtainium?**

Obtainium - это приложение для автоматического отслеживания и установки обновлений open-source приложений напрямую из GitHub. | Obtainium is an app for automatically tracking and installing updates of open-source apps directly from GitHub.

**Как установить | How to install:**
1. Установите [Obtainium](https://github.com/ImranR98/Obtainium/releases/latest)
2. Нажмите на значок выше или добавьте вручную: `https://github.com/disk0Dancer/NFC-bumber`
3. Obtainium автоматически установит приложение и будет следить за обновлениями

### Прямая установка APK | Direct APK Installation

[**📥 Скачать последнюю версию | Download Latest Version**](https://github.com/disk0Dancer/NFC-bumber/releases/latest)

1. Скачайте `.apk` файл из [Releases](https://github.com/disk0Dancer/NFC-bumber/releases)
2. Включите "Установка из неизвестных источников" в настройках Android
3. Установите APK файл

---

## Обзор | Overview

**Русский:**
NFC Card Emulator - это современное Android приложение для эмуляции NFC карт. Приложение позволяет считывать физические NFC карты и оцифровывать их, чтобы терминалы не видели разницы между физической и виртуальной картой.

**English:**
NFC Card Emulator is a modern Android application for emulating NFC cards. The app allows you to read physical NFC cards and digitize them so terminals cannot detect the difference between physical and virtual cards.

## ✨ Основные возможности | Key Features

- 📱 **Простой UI** | Simple, intuitive user interface
- 🔄 **Слайдер карт** | Card slider for quick selection
- 📡 **HCE эмуляция** | Host Card Emulation for terminal compatibility
- 🔒 **Безопасность** | Encrypted storage with biometric protection
- 🎨 **Material Design 3** | Modern Android design with dynamic theming
- ⚡ **Быстро и удобно** | Fast, convenient, and easy to use

## 🏗️ Архитектура | Architecture

Приложение построено на современном Android стеке с использованием лучших практик:

- **Язык | Language**: Kotlin 1.9+
- **UI**: Jetpack Compose
- **Архитектура | Architecture**: MVVM (Model-View-ViewModel)
- **DI**: Hilt
- **Database**: Room
- **Async**: Kotlin Coroutines + Flow
- **Min SDK**: Android 8.0 (API 26)
- **Target SDK**: Android 14 (API 34)

## 📚 Документация | Documentation

Полная документация проекта доступна в директории [`/docs`](./docs/):

### Архитектура | Architecture
- [**C4 Model**](./docs/architecture/c4-model.md) - Архитектурная модель системы (Context, Container, Component, Code)

### Требования | Requirements
- [**Functional Requirements**](./docs/requirements/functional-requirements.md) - Функциональные требования к приложению
- [**Non-Functional Requirements**](./docs/requirements/non-functional-requirements.md) - Нефункциональные требования (производительность, безопасность, надежность)
- [**User Scenarios**](./docs/requirements/user-scenarios.md) - Сценарии использования и пользовательские истории

### Руководства | Guides
- [**Project Setup**](./docs/guides/project-setup.md) - Инструкция по настройке проекта для разработчиков
- [**Coding Style Guide**](./docs/guides/coding-style-guide.md) - Стандарты кодирования и best practices

### Планирование | Planning
- [**Development Roadmap**](./docs/ROADMAP.md) - Детальный план разработки по спринтам и задачам

## 🚀 Быстрый старт | Quick Start

### Требования | Prerequisites

- Android Studio Hedgehog (2023.1.1) или новее
- JDK 17
- Android устройство с NFC (для тестирования)

### Установка | Installation

```bash
# Клонировать репозиторий | Clone repository
git clone https://github.com/disk0Dancer/NFC-bumber.git
cd NFC-bumber

# Открыть в Android Studio | Open in Android Studio
# File > Open > Select project directory

# Собрать проект | Build project
./gradlew build

# Запустить на устройстве | Run on device
./gradlew installDebug
```

Подробная инструкция: [Project Setup Guide](./docs/guides/project-setup.md)

## 🎯 Сценарии использования | Use Cases

1. **Сканирование карты** | **Card Scanning**
   - Откройте приложение и нажмите "Добавить карту"
   - Поднесите физическую NFC карту к телефону
   - Назовите карту и сохраните

2. **Эмуляция карты** | **Card Emulation**
   - Выберите карту в слайдере
   - Поднесите телефон к терминалу
   - Терминал прочитает виртуальную карту

3. **Управление картами** | **Card Management**
   - Просматривайте все сохраненные карты
   - Переименовывайте и настраивайте карты
   - Удаляйте ненужные карты

Больше сценариев: [User Scenarios](./docs/requirements/user-scenarios.md)

## 🔐 Безопасность | Security

- ✅ Шифрование всех данных карт (AES-256)
- ✅ Хранение ключей в Android Keystore
- ✅ Биометрическая аутентификация (опционально)
- ✅ Работает без доступа к интернету
- ✅ Открытый исходный код

## 🛠️ Технологический стек | Tech Stack

### Core
- **Kotlin** 1.9.20+
- **Gradle** 8.2+ с Kotlin DSL
- **Android SDK** 26-34

### UI
- **Jetpack Compose** - Современный декларативный UI
- **Material Design 3** - Актуальные компоненты дизайна
- **Navigation Component** - Навигация между экранами

### Architecture
- **Hilt** - Dependency Injection
- **Room** - Локальная база данных
- **Coroutines & Flow** - Асинхронность
- **ViewModel** - Управление состоянием UI

### Security
- **EncryptedSharedPreferences** - Безопасное хранение настроек
- **Biometric API** - Биометрическая аутентификация
- **ProGuard/R8** - Обфускация кода

### Testing
- **JUnit 5** - Unit тесты
- **MockK** - Мокирование
- **Espresso** - UI тесты
- **Compose UI Test** - Тесты Compose

### Code Quality
- **ktlint** - Форматирование кода
- **Detekt** - Статический анализ
- **JaCoCo** - Покрытие кода

### DevOps & CI/CD
- **GitHub Actions** - Автоматическая сборка и тестирование
- **Dependabot** - Автоматическое обновление зависимостей
- **Auto-labeling** - Автоматическая маркировка PR
- **Экономичная конфигурация** - Оптимизировано для бесплатного tier GitHub Actions
- См. [DevOps Documentation](./.github/README.md) для деталей

## 🤝 Вклад в проект | Contributing

Мы приветствуем вклад в проект! Перед началом работы:

1. Прочитайте [Contributing Guide](./CONTRIBUTING.md) - полное руководство по участию
2. Ознакомьтесь с [Coding Style Guide](./docs/guides/coding-style-guide.md)
3. Изучите [Architecture](./docs/architecture/c4-model.md)
4. Создайте feature branch
5. Напишите тесты для ваших изменений
6. Отправьте Pull Request

### Стандарты | Standards

- Все коммиты следуют [Conventional Commits](https://www.conventionalcommits.org/)
- Код должен проходить ktlint проверку
- Unit тесты с покрытием ≥80%
- Документация для публичных API
- CI/CD проверки должны пройти успешно

## 📋 Roadmap

### v1.0 (MVP)
- [x] Архитектура и документация
- [ ] Сканирование NFC карт
- [ ] HCE эмуляция
- [ ] Управление картами
- [ ] Базовый UI с Material Design 3
- [ ] Шифрование данных

### v1.1
- [ ] Биометрическая аутентификация
- [ ] История транзакций
- [ ] Расширенная кастомизация карт
- [ ] Улучшенная обработка ошибок

### v2.0
- [ ] Локальное резервное копирование (экспорт/импорт файлов)
- [ ] Расширенная фильтрация и поиск карт
- [ ] Виджеты на главный экран
- [ ] Темы оформления
- [ ] Поддержка планшетов

## 📄 Лицензия | License

Этот проект распространяется под лицензией BSD 3-Clause License. См. [LICENSE](LICENSE) для деталей.

Copyright (c) 2025, Grigorii Churakov

## ⚠️ Дисклеймер | Disclaimer

**Важно | Important:**

Это приложение предназначено для легального использования с вашими собственными картами. Пользователи несут ответственность за соблюдение местных законов и правил использования NFC карт.

**НЕ используйте для:**
- Клонирования платежных карт (кредитных, дебетовых)
- Несанкционированного доступа
- Мошенничества

**DO NOT use for:**
- Cloning payment cards (credit, debit)
- Unauthorized access
- Fraud

Разработчики не несут ответственности за неправомерное использование приложения.

## 🔗 Ссылки | Links

- [GitHub Repository](https://github.com/disk0Dancer/NFC-bumber)
- [Issue Tracker](https://github.com/disk0Dancer/NFC-bumber/issues)
- [Documentation](./docs/)
- [Android NFC Guide](https://developer.android.com/guide/topics/connectivity/nfc)
- [Host Card Emulation](https://developer.android.com/guide/topics/connectivity/nfc/hce)

## 📧 Контакты | Contact

- **Issues**: [GitHub Issues](https://github.com/disk0Dancer/NFC-bumber/issues)
- **Discussions**: [GitHub Discussions](https://github.com/disk0Dancer/NFC-bumber/discussions)

---

**Сделано с ❤️ для удобства использования NFC карт | Made with ❤️ for convenient NFC card usage**