package manga;

import manga.model.Role; // Falls deine Role-Klasse in manga.model liegt
import manga.model.User; // WICHTIG: Import für dein User-Modell
import manga.service.AuthService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Startklasse der Manga-API.
 * Initialisiert die Spring-Boot-Anwendung
 */
@SpringBootApplication
@RestController
public class MangaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MangaApiApplication.class, args);

        // 1. Instanz des AuthService erstellen
        AuthService authService = new AuthService();

        // 2. Den Benutzer zuerst über den Service registrieren (damit das Passwort gehasht wird)
        String loginPasswordInput = "meinSicheresPasswort123"; // Korrekt
        User neuerUser = authService.registerUser("Otaku99", "otaku@manga.de", loginPasswordInput, Role.USER);

        System.out.println("\n--- LOGIN SIMULATION ---");

        // Die Inputs des Benutzers beim Login-Versuch
        String falschesPasswordInput = "passwort123"; // Falsch

        // Holt jetzt den echten, funktionierenden Hash aus dem erstellten User
        String gespeicherterHashAusDb = neuerUser.getPassword();

        // Überprüfung 1: Erfolgreicher Login -> Liefert true
        boolean loginErfolgreich = authService.loginUser(loginPasswordInput, gespeicherterHashAusDb);
        System.out.println("Login mit korrektem Passwort: " + (loginErfolgreich ? "ERFOLGREICH (200 OK)" : "FEHLGESCHLAGEN"));
        System.out.println("Erfolgreich eingeloggt mit dem User: " + neuerUser.getUsername());

        // Überprüfung 2: Fehlgeschlagener Login -> Liefert false (ohne Exception!)
        boolean loginFehlgeschlagen = authService.loginUser(falschesPasswordInput, gespeicherterHashAusDb);
        System.out.println("Login mit falschem Passwort:  " + (loginFehlgeschlagen ? "ERFOLGREICH" : "FEHLGESCHLAGEN (401 Unauthorized)"));
    }

    /**
     * Liefert eine einfache Willkommensnachricht.
     *
     * @return Willkommensnachricht
     */
    @GetMapping("/")
    public String index() {
        return "Willkommen bei Mangadb";
    }
}