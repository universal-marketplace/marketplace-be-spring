# Universal Marketplace 🛒

## Opis projektu
Universal Marketplace to nowoczesna, pełnostosowa platforma typu marketplace dla produktów i usług. Umożliwia użytkownikom przeglądanie ofert, dodawanie przedmiotów do koszyka, zarządzanie profilem oraz wystawianie opinii. 

Projekt rozwiązuje problem braku elastycznej i zunifikowanej platformy, która w łatwy sposób pozwala na wymianę dóbr i usług (zarówno w modelu C2C, jak i B2B), łącząc w sobie bezpieczeństwo i nowoczesne podejście do architektury oprogramowania.

## Funkcjonalności
- **Uwierzytelnianie:** Bezpieczne logowanie i rejestracja oparta na tokenach JWT.
- **Marketplace:** Przeglądanie ofert z zaawansowanym filtrowaniem (po typie, tagach, wyszukiwanie tekstowe) oraz paginacją.
- **System koszyka:** Dodawanie przedmiotów do koszyka, zarządzanie ilością i proces realizacji zamówienia (checkout).
- **Zarządzanie profilem:** Podgląd i edycja profilu użytkownika, zarządzanie własnymi ofertami i opiniami.
- **System opinii:** Możliwość wystawiania ocen i komentarzy użytkownikom oraz dodawania odpowiedzi do opinii.

## Technologie

**Backend (repozytorium bieżące):**
- Java 25
- Spring Boot 4.0.4
- PostgreSQL (baza danych) + JPA/Hibernate
- MapStruct (konwersja Entity <-> DTO)
- Spring Security (JWT)
- OpenAPI/Swagger (SpringDoc) do dokumentacji API

**Frontend (architektura całego systemu):**
- Angular 21 (Standalone components, Signals)
- Tailwind CSS + Angular Material
- Vitest do testów

## Instalacja

### Wymagania wstępne
- Java 25
- Maven (zawarty w projekcie jako `mvnw`)
- Docker oraz Docker Compose (do uruchomienia bazy danych i środowiska)

### Krok po kroku

1. **Sklonuj repozytorium**
   ```bash
   git clone [<url-repozytorium>](https://github.com/universal-marketplace/marketplace-be-spring.git)
   cd universal-marketplace-be
   ```

2. **Uruchom infrastrukturę za pomocą Docker Compose**
   Projekt wykorzystuje bazę danych PostgreSQL. Uruchom ją poleceniem:
   ```bash
   docker-compose up -d
   ```

3. **Uruchom aplikację (Spring Boot)**
   Użyj wbudowanego wrappera Maven, aby uruchomić aplikację lokalnie:
   ```bash
   ./mvnw spring-boot:run
   ```
   *(W systemie Windows użyj: `mvnw.cmd spring-boot:run`)*

Aplikacja będzie domyślnie dostępna pod adresem `http://localhost:8080`.

## Użycie

Po poprawnym uruchomieniu aplikacji, możesz korzystać z udostępnionego REST API (bazowa ścieżka: `/api`). 

Dokumentacja interfejsu API w standardzie OpenAPI / Swagger jest generowana automatycznie.
Po uruchomieniu serwera, przejdź pod adres:
```
http://localhost:8080/swagger-ui.html
```
aby testować endpointy i czytać dokumentację interfejsów za pomocą przeglądarki.

Większość zapytań (oprócz logowania i rejestracji) wymaga nagłówka autoryzacyjnego w standardzie Bearer Token:
```http
Authorization: Bearer <twój-token-jwt>
```

## Struktura projektu

Główne pliki i foldery w repozytorium to:
- `src/main/java/` - główny kod aplikacji backendowej (kontrolery, serwisy, repozytoria)
- `src/main/resources/` - pliki konfiguracyjne aplikacji (np. `application.yml` lub `application.properties`)
- `src/test/` - testy jednostkowe i integracyjne
- `.mvn/` i pliki `mvnw` - pliki i konfiguracja wrappera narzędzia Maven
- `docker-compose.yml` i `Dockerfile` - pliki konfiguracyjne dla technologii Docker
- `.env` - plik ze zmiennymi środowiskowymi konfiguracyjnymi projekt
- `GEMINI.md` - założenia projektowe i reguły biznesowo-techniczne

## Konfiguracja

Główna konfiguracja projektu opiera się na pliku `.env` oraz plikach konfiguracyjnych Springa (`application.properties` lub `application.yml`).
W pliku `.env` znajdują się podstawowe zmienne środowiskowe, m.in. dane dostępowe do bazy PostgreSQL oraz sekretny klucz do generowania tokenów JWT. 

Przykładowy wycinek konfiguracji zmiennych (.env):
```env
DB_URL=jdbc:postgresql://localhost:5432/marketplace
DB_USERNAME=postgres
DB_PASSWORD=twoje-haslo
JWT_SECRET=super_tajny_klucz_jwt_o_odpowiedniej_dlugosci
```

## Testy

Projekt posiada automatyczne testy zbudowane w oparciu o framework testowania dla Spring Boot.
Aby uruchomić testy, wykonaj komendę:
```bash
./mvnw test
```
*(Windows: `mvnw.cmd test`)*
