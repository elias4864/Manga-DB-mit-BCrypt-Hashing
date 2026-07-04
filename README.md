# MangaDB - Fullstackapplikation mit sicherer User-Authentifizierung

Dieses Repository enthält das Fullstack-Projekt **MangaDB**, welches im Rahmen des **Moduls 183 (Applikationssicherheit implementieren)** entwickelt wurde. Die Applikation bietet eine vollständige Mehrbenutzer-Verwaltung zur Organisation von Manga-Bibliotheken und setzt dabei auf moderne Sicherheitsstandards bei der Authentifizierung und Datenhaltung.

## 🚀  Tech Stack

Die Applikation ist als moderne Fullstack-Architektur aufgebaut:
* **Backend:** Java mit Spring Boot (Spring Security)
* **Frontend:** React+(Viteserver)
* **Datenbank:** SQL mit JPA/Hibernate und Tomcat Server
* **Sicherheit:** BCrypt-Passworthashing & Role-Based Access Control (RBAC)

---

## 🔒 Sicherheitsarchitektur: Login & Password Hashing

Um den Schutz von Benutzerdaten gemäss den OWASP-Top-10-Richtlinien zu gewährleisten, werden Passwörter niemals im Klartext verarbeitet oder persistiert sondern vor dem Speichern des Passowrd in der DB das eingegeben Password mit der DB verglichen und danach gehasht. Die Authentifizierungs-Pipeline basiert auf **Spring Security** und dem **BCrypt-Hashing-Algorithmus** erfolgt mittels Password-Encoder Methode

### Key Features der Implementierung
1. **Automatisches Salting:** Jeder Benutzer erhält bei der Registrierung einen kryptografisch zufälligen, einzigartigen Salt. Dies verhindert effektiv Angriffe über Rainbow Tables oder tabellenbasierte Lookups.
2. **Key Stretching (Work Factor):** Der Algorithmus nutzt einen konfigurierbaren Kostenfaktor (Default: `10`), um Brute-Force-Angriffe durch künstliche Rechenzeitverzögerung auf Hardware-Ebene massiv zu erschweren.
3. **Sichere Verifikation:** Beim Login extrahiert das Backend den Salt automatisch aus dem in der Datenbank hinterlegten String und verifiziert das eingegebene Passwort, ohne dass das Klartextpasswort temporär unsicher zwischengespeichert werden muss.

---

## 🛠️ Technische Umsetzung

### Backend (Spring Boot Configuration)

Die `PasswordEncoder`-Bean wird zentral in der Sicherheitskonfiguration registriert und steuert die Verschlüsselung im gesamten Service-Layer:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt mit einem Standard-Kostenfaktor von 10 Runden
        return new BCryptPasswordEncoder();
    }
}
