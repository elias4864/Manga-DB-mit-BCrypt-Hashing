package manga;

import manga.model.Role;
import manga.model.User;
import manga.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MangaApiApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MangaApiApplication.class, args);
        UserService userService = context.getBean(UserService.class);

        System.out.println("\n=== START TEST-REIHE MIT DEINEN DATENBANK-USERN ===");

        // --- DEFINITION DER ECHTEN DATEN AUS DEINER DB ---
        String leoName = "leo1234_user";
        String leoPw = "leo1234";

        String fabilianName = "fabilian";
        String fabilianPw = "fabian2024";

        String soraName = "SoraReads";
        String soraPw = "mangaReader2026";


        // --- SCHRITT 1: VERSUCH DER REGISTRIERUNG ---
        System.out.println("\n[SCHRITT 1] Versuche User zu registrieren (falls noch nicht vorhanden)...");

        try {
            userService.registerUser(fabilianName, "fabia.imhasly@gmx.ch", fabilianPw, Role.USER);
            System.out.println("[INFO] " + fabilianName + " wurde erfolgreich frisch gehasht registriert!");
        } catch (Exception e) {
            System.out.println("[HINWEIS] " + fabilianName + " existiert bereits in der DB. Verwende bestehenden Eintrag.");
        }

        try {
            userService.registerUser(soraName, "sora@manga.ch", soraPw, Role.USER);
        } catch (Exception e) {
            // Ignoriert Info-Meldung für kürzere Logs
        }


        // --- SCHRITT 2: LOGIN-SIMULATIONEN ---
        System.out.println("\n[SCHRITT 2] Simuliere Logins mit Benutzernamen und Passwort...");

        // TEST 1: leo1234_user (Bereits gehasht in DB)
        System.out.println("\n-> Versuche Login für: " + leoName);
        boolean leoErfolg = userService.loginUser(leoName, leoPw);
        if (leoErfolg) {
            System.out.println("   => STATUS: ERFOLG! Der BCrypt-Hash in der DB matched mit dem Passwort.");
        } else {
            System.out.println("   => STATUS: FEHLGESCHLAGEN!");
        }

        // TEST 2: fabilian (Steht im Klartext in deiner DB -> Schlägt fehl)
        System.out.println("\n-> Versuche Login für: " + fabilianName);
        boolean fabiErfolg = userService.loginUser(fabilianName, fabilianPw);
        if (fabiErfolg) {
            System.out.println("   => STATUS: ERFOLG!");
        } else {
            System.out.println("   => STATUS: FEHLGESCHLAGEN!");
            System.out.println("   => GRUND: '" + fabilianName + "' hat in der Datenbank kein gehashtes Passwort.");

            // HIER GENERIEREN WIR DEN PASSENDEN HASH FÜR DEINE DOKUMENTATION:
            String neuerHash = userService.encodePassword(fabilianPw);
            System.out.println("   => TIPP: So müsste der sichere Hash für das Passwort '" + fabilianPw + "' in der DB aussehen:");
            System.out.println("      " + neuerHash);
        }

        // TEST 3: SoraReads (Steht im Klartext in deiner DB -> Schlägt fehl)
        System.out.println("\n-> Versuche Login für: " + soraName);
        boolean soraErfolg = userService.loginUser(soraName, soraPw);
        if (!soraErfolg) {
            String soraHash = userService.encodePassword(soraPw);
            System.out.println("   => STATUS: FEHLGESCHLAGEN!");
            System.out.println("   => TIPP: Generierter BCrypt-Hash für '" + soraPw + "':");
            System.out.println("      " + soraHash);
        }

        System.out.println("\n=== TEST-REIHE BEENDET ===");
    }

    @GetMapping("/")
    public String index() {
        return "Willkommen bei der Mangadb";
    }
}