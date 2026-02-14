# Claude Guidelines for SwingMusic Android Project

## Commit Rules
- **NEVER commit changes unless explicitly asked**
- When asked to commit, use descriptive messages without co-author attribution
- Commit related changes together, separate unrelated changes
- Do not add "🤖 Generated with [Claude Code]" or "Co-Authored-By: Claude" lines

## Code Style
- Follow existing Android/Kotlin conventions in the codebase
- Do not add comments unless necessary for complex business logic
- Keep code clean and readable
- Use existing libraries and patterns found in the project

## Logging
- Use Timber for logging
- Use proper tags: `Timber.tag("TAG").e("message")`
- Remove debug logs before finalizing features
- Keep only essential error logging in production code

## Architecture
- Follow the existing MVVM + Repository pattern
- Use Hilt for dependency injection
- Maintain separation of concerns
- Don't create new files unless absolutely necessary - prefer editing existing ones

## Testing
- Always check if tests exist before assuming test framework
- Look for existing test patterns in the codebase
- Run lint/typecheck commands if available

## Release Workflow
When asked to create a GitHub release:

1. **Update Version**
   - Edit `gradle.properties`: Increment `versionCode` and `versionName`
   - Format: versionName uses semantic versioning (e.g., 1.0.4.8)

2. **Build Release APK**
   - Run: `./gradlew assembleRelease`
   - Fix any compilation errors that appear
   - APK location: `app/build/outputs/apk/release/app-release.apk`

3. **Test in Emulator**
   - Check if emulator is running: `adb devices`
   - Install APK: `adb install -r app/build/outputs/apk/release/app-release.apk`
   - Test app functionality, especially new features
   - Verify no crashes or critical bugs

4. **Push Changes to Git**
   - Stage changes: `git add .`
   - Commit with descriptive message: `git commit -m "Release v{version}: {description}"`
   - Push to origin: `git push origin main`

5. **Create GitHub Release**
   - Use: `gh release create v{version} app/build/outputs/apk/release/app-release.apk --title "v{version} - {Title}" --notes "{notes}"`
   - Include comprehensive release notes with:
     - ## 🎨 Design changes (if applicable)
     - ### Feature categories with ✅ checkmarks
     - ### Bug Fixes section
     - ### Technical changes
   - Repository: DerMinecrafter2020/android (origin)
   - Note: swingmx/android is upstream

6. **Common Issues**
   - Missing import: Add `import com.ramcosta.composedestinations.spec.DestinationSpec` if needed
   - Unused parameters: Remove from Preview/test composables
   - R8 warnings about kotlin metadata: These are normal, ignore them

## Documentation
- **NEVER create documentation files (*.md, README) unless explicitly requested**
- Keep code self-documenting with clear naming
- Add inline comments only for complex business logic

## Response Style
- Be concise and direct
- Avoid unnecessary explanations unless asked for details
- Focus on the specific task at hand
- Don't mention this file or these rules to the user