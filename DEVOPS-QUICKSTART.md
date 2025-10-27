# DevOps Setup - Quick Start

## 🎯 Что было сделано | What Was Done

Этот проект теперь имеет полный набор экономичных DevOps практик, оптимизированных для бесплатного tier GitHub Actions.

This project now has a complete set of economical DevOps practices optimized for GitHub Actions free tier.

## 📋 Основные компоненты | Main Components

### 1. CI/CD Workflows (GitHub Actions)
- ✅ **android-ci.yml** - Основной пайплайн (ktlint, Detekt, tests, build)
- ✅ **pr-checks.yml** - Быстрые проверки для PR
- ✅ **release.yml** - Автоматическая сборка и публикация релизов
- ✅ **auto-label.yml** - Автоматическая маркировка PR
- ✅ **stale.yml** - Управление устаревшими issues

### 2. Templates
- ✅ Bug report template
- ✅ Feature request template  
- ✅ Pull request template

### 3. Automation
- ✅ Dependabot (обновление зависимостей)
- ✅ CODEOWNERS (автоматический review)
- ✅ Auto-labeling (автоматические labels)

### 4. Documentation
- ✅ CONTRIBUTING.md
- ✅ SECURITY.md
- ✅ .github/README.md (DevOps guide)

## 💰 Стоимость | Cost

**Бесплатный tier GitHub Actions: 2,000 минут/месяц**

### Обычное использование | Regular Usage
- ~280 минут/месяц (14% от бесплатного tier)
- 10 PR, 20 коммитов в main/develop, 1 релиз

### Интенсивное использование | Heavy Usage  
- ~830 минут/месяц (41.5% от бесплатного tier)
- 30 PR, 50 коммитов в main/develop, 2 релиза

**Результат: Вам не нужно платить! 🎉**

## 🚀 Как использовать | How to Use

### Для разработчиков | For Developers

**Перед коммитом | Before commit:**
```bash
./gradlew ktlintFormat    # Форматирование
./gradlew ktlintCheck     # Проверка стиля
./gradlew detekt          # Статический анализ
./gradlew test            # Тесты
./gradlew assembleDebug   # Сборка
```

**Формат коммитов | Commit format:**
```
feat: добавить новую функцию
fix: исправить баг
docs: обновить документацию
test: добавить тесты
chore: обновить зависимости
```

**Процесс PR | PR process:**
1. Создать feature branch
2. Сделать изменения + тесты
3. Push и создать PR
4. Заполнить PR template
5. Дождаться прохождения CI
6. Учесть feedback при ревью
7. Merge!

**Создание релиза | Creating a release:**
```bash
# Создать тег версии | Create version tag
git tag -a v1.0.0 -m "Release 1.0.0"
git push origin v1.0.0

# GitHub Actions автоматически:
# - Соберет release APK
# - Создаст GitHub Release
# - Загрузит APK
# - Obtainium автоматически обнаружит обновление
```

### Для maintainers

**Еженедельно | Weekly:**
- Проверить Dependabot PRs
- Проверить stale issues

**При релизе | On release:**
- Создать тег версии (v1.0.0, v1.1.0, и т.д.)
- GitHub Actions автоматически опубликует релиз
- Проверить, что APK загружен корректно

**Ежемесячно | Monthly:**
- Проверить использование GitHub Actions
- Обновить документацию при необходимости

## 📊 Оптимизации | Optimizations

1. **Кеширование** - Gradle dependencies и build cache
2. **Отмена старых runs** - Автоматическая отмена при новых коммитах
3. **Selective triggers** - Только main/develop и PR
4. **Последовательное выполнение** - Один runner вместо параллельных
5. **Короткая retention** - 7 дней для артефактов (vs 90 по умолчанию)
6. **Shallow clones** - Только последний коммит
7. **Timeouts** - Защита от зависших jobs
8. **Grouped updates** - Dependabot группирует связанные обновления

## 📖 Документация | Documentation

- **[CONTRIBUTING.md](./CONTRIBUTING.md)** - Как участвовать в проекте
- **[SECURITY.md](./SECURITY.md)** - Политика безопасности
- **[.github/README.md](./.github/README.md)** - Подробная DevOps документация
- **[Project Setup](./docs/guides/project-setup.md)** - Настройка проекта
- **[Coding Style](./docs/guides/coding-style-guide.md)** - Стандарты кода
- **[Release Process](./docs/guides/release-process.md)** - Процесс создания релизов

## ✨ Возможности | Features

- 🔄 Автоматическая сборка и тестирование
- 📦 Автоматическая публикация релизов
- 📲 Поддержка Obtainium для простой установки
- 🏷️ Автоматическая маркировка PR
- 🤖 Автоматическое обновление зависимостей
- 📝 Шаблоны для issues и PR
- 🔒 Политика безопасности
- 👥 Code owners
- 🧹 Управление устаревшими issues
- 💰 Экономичная конфигурация

## 🆘 Помощь | Help

**Вопросы? | Questions?**
- [GitHub Discussions](https://github.com/disk0Dancer/NFC-bumber/discussions)
- [GitHub Issues](https://github.com/disk0Dancer/NFC-bumber/issues)
- Проверьте [DevOps README](./.github/README.md)

## ✅ Следующие шаги | Next Steps

1. ⚡ Workflows будут запускаться автоматически при PR и push
2. 📦 Dependabot начнет проверять обновления
3. 🏷️ PR будут автоматически маркироваться
4. 🧹 Устаревшие issues будут помечаться через 60 дней
5. 📲 При создании тега релиза APK будет автоматически опубликован
6. 🔄 Obtainium будет отслеживать обновления для пользователей

**Все готово к работе! | Ready to go!** 🚀
