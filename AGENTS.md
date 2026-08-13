# AGENTS.md — Android App Widget Project Rules & Guidelines

## 1. Project Goal & Core Philosophy
This project focuses on building a **lightweight, highly optimized Android App Widget**.
* **Zero Bloat:** Strictly avoid unnecessary dependencies, heavy third-party libraries, or complex visual frameworks.
* **Modern Android Stack:** Utilize modern Jetpack libraries optimized for system widgets.
* **Performance First:** Minimize UI recompositions, battery drain, memory usage, and Inter-Process Communication (IPC) overhead.

---

## 2. Tech Stack Constraints (Non-Negotiable)
When generating code or suggesting implementations, stick strictly to the following stack:

* **UI Framework:** `androidx.glance:glance-appwidget` and `androidx.glance:glance-material3` (Jetpack Glance).
    * ❌ **STRICTLY FORBIDDEN:** Legacy XML `RemoteViews` layouts (`AppWidgetProvider` with `RemoteViews`).
    * ❌ **STRICTLY FORBIDDEN:** Inserting standard Jetpack Compose desktop/app components (`androidx.compose.material3.*`) directly inside Widget UI files. Use Glance composables only (`androidx.glance.layout.*`, `androidx.glance.text.*`).
* **State Management & Storage:** `GlanceStateDefinition<Preferences>` (Preferences DataStore).
* **Background Tasks:** `WorkManager` (`androidx.work:work-runtime-ktx`).
* **Asynchronous Code:** Kotlin Coroutines and `Flow`.
* **Language:** 100% Idiomatic Kotlin.

---

## 3. Frontend / UI Optimization Rules

1. **Keep Component Hierarchy Flat:**
    * Limit nesting to maximum 3 levels deep (`GlanceNode`). Deeply nested `Column`/`Row` layouts increase IPC load and launcher render time.
    * Prefer single `Row` or `Column` layouts using `Alignment` and `Spacer` over nested container wrappers.

2. **Asset & Image Efficiency:**
    * Use vector drawables (`ImageVector` / SVG) instead of large PNG/JPG Bitmaps.
    * Do NOT perform bitmap scaling, canvas drawing, or image rendering inside `provideGlance`.
    * For dynamic images, pre-scale bitmaps to match exact target widget dimensions before passing them to `Image()`.

3. **Size & Mode Management:**
    * Explicitly define `sizeMode` in your `GlanceAppWidget`:
        * Use `SizeMode.Single` for fixed-size widgets.
        * Use `SizeMode.Responsive(setOf(SMALL, MEDIUM, LARGE))` for multi-size responsiveness. Avoid `SizeMode.Exact` unless strictly necessary (it triggers complete widget re-renders on minor resizes).

4. **Theming:**
    * Use `GlanceTheme` for automatic dark and light mode adaptation.

---

## 4. Backend, Data & Lifecycle Rules

1. **Keep `provideGlance` Non-Blocking:**
    * `GlanceAppWidget.provideGlance` is a suspend function—do NOT execute network calls, complex database queries, or heavy file IO directly inside it.
    * Always read pre-cached values from `Preferences` or DataStore inside `provideGlance` for fast instant rendering.

2. **Background Updates & Syncing:**
    * Do NOT rely on `updatePeriodMillis` in `appwidget-provider` XML for updates frequent than 30 minutes (Android throttles this).
    * For periodic data fetching, use `WorkManager` with a `PeriodicWorkRequest` (minimum interval 15 minutes) or `OneTimeWorkRequest` triggered by user interaction or push notifications.
    * Update the widget UI from the background worker via `GlanceAppWidget.update(context, glanceId)` or `updateAll(context)`.

3. **State Updates:**
    * Mutate widget state using `updateAppWidgetState(context, glanceId) { prefs -> ... }` followed by `GlanceWidget.update()`.
    * Keep state objects minimal. Store primitive data types in DataStore Preferences; do not store giant JSON payloads.

---

## 5. Code Structure & Style Guidelines

When creating or modifying files, keep responsibilities separated into these logical components:

* `*Widget.kt`: Contains ONLY `GlanceAppWidget` UI composables and state definitions.
* `*WidgetReceiver.kt`: Extends `GlanceAppWidgetReceiver` for widget entry points.
* `*Worker.kt`: Extends `CoroutineWorker` for background data fetching and DataStore state updates.
* `*State.kt` or `*Keys.kt`: Contains DataStore key definitions (`stringPreferencesKey`, `booleanPreferencesKey`, etc.).

### Quality Standard:
* Generate concise, clean Kotlin code without verbose explanatory comments for self-evident code.
* Handle nullability and loading states explicitly (e.g., render a lightweight loading state if DataStore keys are uninitialized).