# PantryWise Smart Pantry Manager

Prepared for Hlayisa Mathavathe, student number 402413919, Mobile App Development 700.

Java Android app with five Activities, SQLite CRUD, 20 seeded recipes, strict quantity matching, expiry warnings and a persistent setting. PantryWise implements the assignment's Smart Pantry Manager concept. No location, maps, payments or network permissions are used.

## Open and run

Open this directory in Android Studio and install SDK 36. Gradle wrapper files are included. Use `gradlew.bat assembleDebug` on Windows, or `./gradlew assembleDebug` elsewhere. For terminal builds set ANDROID_HOME to the SDK directory. Minimum Android API is 23. A full build passed with Gradle 9.1.0, Android Gradle Plugin 9.0.1, SDK 36 and Eclipse Adoptium JDK 25.0.2 on Windows. The debug APK is generated at app/build/outputs/apk/debug/app-debug.apk.

SQLiteOpenHelper provides offline persistence and relational recipe data. SharedPreferences saves the expiry warning toggle. List screens refresh in onResume after edits. The app uses custom BaseAdapters and Intents carrying record IDs.

The optional `build-local.ps1` SDK-tool build runs 13 matching checks. Supply -Sdk and -Jdk paths if they differ from this machine. Matching sums duplicate pantry entries and repeated recipe requirements; handles common aliases; converts kg/g and l/ml/tsp/tbsp; and rejects incompatible unit families. One tablespoon means 15 ml and one teaspoon means 5 ml. Mass is never converted into volume.

Public project repository: https://github.com/hlayisapeace/PantryWise. The Android source, build files, Gradle wrapper and matching checks are published on the main branch. The commits record the actual source upload process; they do not establish a development timeline before this upload. The assignment's minimum 10 meaningful development commits must not be simulated by copying history or backdating commits. Hlayisa must review the report, complete the signed declaration and record a 5-7 minute narrated walkthrough of his understanding. The package remains a handover until those items are completed.

## Implementation and verification checklist

1. Start a new Git repository and record genuine development steps as they happen. Create the GitHub repository early.
2. Design SQLite tables for pantry items, recipes, and recipe requirements.
3. Implement a pantry ListView with a custom adapter and database reads.
4. Add an ingredient form Activity with validation and insert/update operations.
5. Add deletion with confirmation and verify persistence after relaunch.
6. Seed 20 recipes with clearly specified raw/cooked ingredient names and quantities.
7. Implement strict matching with compatible unit conversions. Sum duplicate pantry quantities and group repeated recipe requirements before comparing. Never convert mass to volume without ingredient-specific information.
8. Implement recipe details and pass the recipe identifier through an Intent.
9. Implement a working setting, such as expiry warnings shown on the pantry screen.
10. Test missing ingredients, insufficient quantities, plural names, duplicate records, unit conversions, validation, and persistence.
11. Capture screenshots of the actual running implementation and write the report from the work completed.
12. Record the required narrated video and package the source, report, and video under 50 MB.

## Design direction

White background, teal headings, coral expiry warnings and compact ingredient rows. Five Activities provide Pantry, Ingredient Form, Suggested Recipes, Recipe Detail and Settings. Bottom navigation connects the main destinations.

## Report preparation

Use the institution's supplied cover page, Hlayisa's own student number, a signed originality declaration, Times New Roman 12 pt with 1.5 spacing, and Harvard references to sources actually consulted. Include screen-flow and database relationship diagrams, real screenshots, actual code excerpts, and a GitHub link. Describe only development and testing that actually took place.
