# Documentation Index - NFC Card Emulator

## Навигация по документации | Documentation Navigation

Этот документ служит центральной точкой навигации по всей документации проекта NFC Card Emulator.

---

## 📖 Обзор проекта | Project Overview

**Название**: NFC Card Emulator (NFC-bumber)  
**Описание**: Современное Android приложение для эмуляции NFC карт  
**Лицензия**: BSD 3-Clause  
**Язык**: Kotlin  
**Платформа**: Android 8.0+ (API 26+)

### Основная цель | Main Goal

Создать удобное Android приложение, которое позволяет:
- Считывать физические NFC карты
- Оцифровывать и сохранять карты в зашифрованном виде
- Эмулировать виртуальные карты так, чтобы терминалы не видели разницы с физическими картами
- Предоставить простой и интуитивный пользовательский интерфейс

---

## 📚 Структура документации | Documentation Structure

### 1. Архитектура | Architecture

#### [C4 Model Documentation](./architecture/c4-model.md)
**Описание**: Полная архитектурная документация по модели C4

**Содержание**:
- **Level 1: System Context** - Контекст системы, взаимодействие с пользователями и внешними системами
- **Level 2: Container Diagram** - Архитектура приложения (UI, Business Logic, Data слои)
- **Level 3: Component Diagram** - Детальное описание компонентов каждого слоя
- **Level 4: Code Level** - Ключевые классы и интерфейсы с примерами кода

**Ключевые разделы**:
- Диаграммы архитектуры
- Технологический стек
- Соображения безопасности
- Масштабируемость и производительность
- Планы развития

**Когда читать**: Начните с этого документа, чтобы понять общую архитектуру системы

---

### 2. Требования | Requirements

#### [Functional Requirements](./requirements/functional-requirements.md)
**Описание**: Полный список функциональных требований к приложению

**Содержание** (50+ требований):
- **FR-1**: Чтение NFC карт (обнаружение, извлечение данных, определение типа)
- **FR-2**: Хранение и управление картами (сохранение, именование, кастомизация, удаление)
- **FR-3**: Эмуляция карт через HCE (Host Card Emulation service, выбор карты, логирование)
- **FR-4**: Пользовательский интерфейс (главный экран, сканирование, детали, настройки)
- **FR-5**: Безопасность и приватность (шифрование, биометрия, offline-first)
- **FR-6**: Разрешения и системная интеграция
- **FR-7**: Обработка ошибок и надежность
- **FR-8**: Помощь и поддержка

**Формат**: Каждое требование включает:
- Описание
- Критерии приемки
- Приоритет (P0-P3)
- Техническую реализацию

**Когда читать**: Для понимания того, что должно делать приложение

#### [Non-Functional Requirements](./requirements/non-functional-requirements.md)
**Описание**: Требования к качеству системы

**Содержание** (70+ требований):
- **NFR-1**: Производительность (время запуска, скорость сканирования, HCE response time)
- **NFR-2**: Масштабируемость (количество карт, история транзакций)
- **NFR-3**: Надежность (crash-free rate, целостность данных, восстановление после ошибок)
- **NFR-4**: Удобство использования (легкость использования, доступность, интернационализация)
- **NFR-5**: Безопасность (шифрование, аутентификация, защита кода, разрешения)
- **NFR-6**: Поддерживаемость (качество кода, документация, тестирование)
- **NFR-7**: Портативность (версии Android, совместимость устройств)
- **NFR-8**: Совместимость (NFC чипсеты, терминалы, типы карт)
- **NFR-9**: Операционные требования (развертывание, мониторинг, поддержка)
- **NFR-10**: Юридические требования (лицензии, приватность)

**Метрики**: Конкретные измеримые показатели для каждого требования

**Когда читать**: Для понимания качественных характеристик приложения

#### [User Scenarios](./requirements/user-scenarios.md)
**Описание**: Сценарии использования и пользовательские истории

**Содержание**:
- **3 User Personas**: Городской житель, бизнес-путешественник, студент
- **10+ Detailed Scenarios**: 
  1. Первая настройка приложения
  2. Сканирование и добавление карты
  3. Выбор карты для эмуляции
  4. Использование эмулированной карты у терминала
  5. Управление несколькими картами
  6. Просмотр и редактирование деталей карты
  7. Удаление карты
  8. Биометрическая блокировка
  9. Устранение неполадок
  10. Резервное копирование (будущее)
- **User Journey Map**: Первая неделя использования
- **Edge Cases**: Нестандартные ситуации
- **Success Metrics**: Метрики успеха пользователя
- **Accessibility**: Соображения доступности

**Когда читать**: Для понимания, как пользователи будут взаимодействовать с приложением

---

### 3. Руководства для разработчиков | Developer Guides

#### [Project Setup Guide](./guides/project-setup.md)
**Описание**: Полная инструкция по настройке проекта для разработки

**Содержание**:
- **Prerequisites**: Необходимое ПО (Android Studio, JDK, SDK, Gradle)
- **Initial Setup**: Клонирование, конфигурация IDE, настройка Gradle
- **Project Structure**: Организация файлов и директорий
- **Build Configuration**: Gradle конфигурация (project-level, app-level)
- **Development Workflow**: Стратегия ветвления, commit convention, PR process
- **Code Quality Tools**: ktlint, Detekt, JaCoCo
- **Running the Application**: Debug и release builds, signing
- **Testing**: Unit tests, instrumentation tests, структура тестов
- **CI/CD**: GitHub Actions настройка
- **Troubleshooting**: Решение типичных проблем

**Шаг за шагом**: Пошаговая инструкция от клонирования до запуска приложения

**Когда читать**: Первый документ для новых разработчиков

#### [Coding Style Guide](./guides/coding-style-guide.md)
**Описание**: Стандарты кодирования и лучшие практики

**Содержание**:
- **Language**: Kotlin conventions
- **Code Formatting**: Автоформатирование, отступы, длина строк, импорты
- **Naming Conventions**: Классы, функции, переменные, пакеты, XML ресурсы
- **Code Structure**: Организация файлов, порядок объявлений
- **Kotlin Best Practices**: 
  - Иммутабельность
  - Null safety
  - Data classes
  - Sealed classes
  - Extension functions
  - Scope functions
  - Coroutines
- **Android Best Practices**: 
  - MVVM архитектура
  - Dependency Injection (Hilt)
  - Resource management
  - Jetpack Compose
- **Documentation**: KDoc комментарии, inline комментарии
- **Testing**: Именование тестов, структура, моки
- **Performance**: Оптимизация производительности
- **Security**: Работа с чувствительными данными
- **Error Handling**: Result types, специфичные исключения

**Примеры**: Каждое правило с примерами правильного и неправильного кода

**Когда читать**: Перед началом написания кода

---

## 🗺️ Карта чтения | Reading Map

### Для новых разработчиков | For New Developers

**Рекомендуемый порядок чтения**:

1. **Начало** → [README.md](../README.md) - Общий обзор проекта
2. **Архитектура** → [C4 Model](./architecture/c4-model.md) - Понимание структуры системы
3. **Setup** → [Project Setup](./guides/project-setup.md) - Настройка окружения
4. **Coding** → [Coding Style Guide](./guides/coding-style-guide.md) - Стандарты кодирования
5. **Requirements** → [Functional Requirements](./requirements/functional-requirements.md) - Что должно работать
6. **Users** → [User Scenarios](./requirements/user-scenarios.md) - Как пользователи будут использовать

**Время на чтение**: ~2-3 часа

### Для Product Owners | For Product Owners

1. [README.md](../README.md) - Обзор
2. [User Scenarios](./requirements/user-scenarios.md) - Сценарии использования
3. [Functional Requirements](./requirements/functional-requirements.md) - Функциональность
4. [C4 Model - Level 1 & 2](./architecture/c4-model.md) - Высокоуровневая архитектура

**Время на чтение**: ~1 час

### Для архитекторов | For Architects

1. [C4 Model](./architecture/c4-model.md) - Полная архитектура
2. [Non-Functional Requirements](./requirements/non-functional-requirements.md) - Качественные характеристики
3. [Functional Requirements](./requirements/functional-requirements.md) - Функциональность
4. [Project Setup](./guides/project-setup.md) - Технологический стек

**Время на чтение**: ~2 часа

### Для QA Engineers | For QA Engineers

1. [Functional Requirements](./requirements/functional-requirements.md) - Что тестировать
2. [User Scenarios](./requirements/user-scenarios.md) - Тест-кейсы
3. [Non-Functional Requirements](./requirements/non-functional-requirements.md) - Performance, security testing
4. [C4 Model - Level 3](./architecture/c4-model.md) - Компоненты для integration testing

**Время на чтение**: ~1.5 часа

---

## 📊 Статистика документации | Documentation Statistics

### Объем документации | Documentation Volume

- **Всего документов**: 7 markdown файлов
- **Всего строк**: 4,230+ строк
- **Всего слов**: ~35,000 слов
- **Время чтения**: ~3-4 часа (вся документация)

### Разбивка по разделам | Breakdown by Section

| Раздел | Файлов | Строк | Основная тема |
|--------|--------|-------|---------------|
| **Architecture** | 1 | 363 | System design, C4 model |
| **Requirements** | 3 | 1,984 | Functional, non-functional, user scenarios |
| **Guides** | 2 | 1,669 | Setup, coding standards |
| **Root** | 1 | 214 | Project overview |

### Покрытие | Coverage

- ✅ System Architecture (C4 Model)
- ✅ Functional Requirements (50+ требований)
- ✅ Non-Functional Requirements (70+ требований)
- ✅ User Scenarios (10+ сценариев)
- ✅ Developer Setup Guide
- ✅ Coding Style Guide
- ✅ Technology Stack Documentation
- ✅ Security Guidelines
- ✅ Testing Strategy

---

## 🔄 Обновление документации | Documentation Updates

### Когда обновлять | When to Update

Документацию следует обновлять в следующих случаях:

1. **Новые функции**: Обновить functional requirements и user scenarios
2. **Изменения архитектуры**: Обновить C4 model
3. **Новые зависимости**: Обновить project setup guide
4. **Изменения в стандартах**: Обновить coding style guide
5. **Performance изменения**: Обновить non-functional requirements

### Процесс обновления | Update Process

1. Изменить соответствующий .md файл
2. Убедиться, что ссылки работают
3. Обновить дату в начале документа
4. Создать PR с описанием изменений
5. Запросить review от команды

---

## 🔗 Внешние ресурсы | External Resources

### Android Development
- [Android Developer Documentation](https://developer.android.com)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)

### NFC & HCE
- [Android NFC Guide](https://developer.android.com/guide/topics/connectivity/nfc)
- [Host Card Emulation](https://developer.android.com/guide/topics/connectivity/nfc/hce)
- [ISO 14443-4 Specification](https://www.iso.org/standard/73599.html)

### Architecture & Design
- [C4 Model](https://c4model.com/)
- [MVVM Pattern](https://developer.android.com/topic/architecture)
- [Material Design 3](https://m3.material.io/)

### Best Practices
- [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- [Android Best Practices](https://developer.android.com/topic/architecture/recommendations)
- [Effective Kotlin](https://kt.academy/book/effectivekotlin)

---

## 📝 Обратная связь | Feedback

Если вы нашли ошибки, неточности или у вас есть предложения по улучшению документации:

1. **Создайте Issue**: [GitHub Issues](https://github.com/disk0Dancer/NFC-bumber/issues)
2. **Начните Discussion**: [GitHub Discussions](https://github.com/disk0Dancer/NFC-bumber/discussions)
3. **Отправьте PR**: С исправлениями или дополнениями

---

## ✅ Checklist для новых разработчиков | New Developer Checklist

Перед началом работы убедитесь, что вы:

- [ ] Прочитали [README.md](../README.md)
- [ ] Изучили [C4 Model](./architecture/c4-model.md) (хотя бы Level 1-2)
- [ ] Настроили окружение по [Project Setup Guide](./guides/project-setup.md)
- [ ] Ознакомились с [Coding Style Guide](./guides/coding-style-guide.md)
- [ ] Прочитали основные [Functional Requirements](./requirements/functional-requirements.md)
- [ ] Понимаете основные [User Scenarios](./requirements/user-scenarios.md)
- [ ] Клонировали репозиторий и собрали проект
- [ ] Запустили проверки кода (ktlint, detekt)
- [ ] Присоединились к GitHub Discussions

---

## 📅 Версия документации | Documentation Version

- **Версия**: 1.0.0
- **Дата создания**: 27 октября 2025
- **Последнее обновление**: 27 октября 2025
- **Статус**: Complete (MVP documentation)

---

**Готовы начать? | Ready to start?** → [Project Setup Guide](./guides/project-setup.md)

**Есть вопросы? | Have questions?** → [GitHub Discussions](https://github.com/disk0Dancer/NFC-bumber/discussions)
