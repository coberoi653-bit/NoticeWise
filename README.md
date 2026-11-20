# NoticeWise — AI Notice Interpreter

NoticeWise is a student-focused web application that turns long notices into easier-to-understand information. It can summarize a notice, identify deadlines, explain eligibility and required documents, create an action checklist, and save analysis history.

## Technology
- Java 24
- Spring Boot 3.4.5
- Spring Web and Spring Data JPA
- H2 for the first local run; PostgreSQL profile included
- Gemini API integration (optional API key)
- HTML, CSS and vanilla JavaScript

## Run in IntelliJ IDEA
1. Extract this ZIP file.
2. In IntelliJ, choose **File → Open** and select the extracted `NoticeWise` folder (the folder containing `pom.xml`).
3. Trust the project if IntelliJ asks, and wait for Maven to import dependencies.
4. Set Project SDK and Maven importer JDK to Java 24 if needed.
5. Open `src/main/java/com/noticewise/NoticeWiseApplication.java` and click the green Run icon.
6. Open http://localhost:8080

IntelliJ includes Maven integration and can import the project from `pom.xml`. A separate Maven installation is not required to import/run it inside IntelliJ if its bundled Maven is available.

## Enable Gemini
Set an environment variable named `GEMINI_API_KEY` before starting IntelliJ, then restart IntelliJ and run the application. The key is not stored in source code. You can also set `GEMINI_MODEL` if you need a different model available to your API key. Without a key, the app displays an offline fallback and does not pretend to provide AI-generated analysis.

## Switch to PostgreSQL later
1. Install PostgreSQL and create a database named `noticewise`.
2. Configure `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` environment variables.
3. Run with the Spring profile `postgres` (IntelliJ Run Configuration → Active profiles: `postgres`).

## API endpoints
- `POST /api/notices/analyze` — body: `{"noticeText":"..."}`
- `GET /api/notices` — latest saved notices
- `GET /api/notices/{id}` — one saved notice
- `DELETE /api/notices/{id}` — delete a notice

## Important
This is an initial working project scaffold. Test it locally, configure your own Gemini API key, and verify AI outputs before using it for real decisions. Do not upload API keys or passwords to GitHub. Do not describe the project as completed or tested until you have run it successfully.
