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






        String fabilianName = "fabilian";
        String fabilianPw = "fabian2024";

        String soraName = "SoraReads";
        String soraPw = "mangaReader2026";

        String leoName = "leo1234_user";
        String leoPw = "leo1234";

        String eliasName = "eljourka";
        String eliasPw = "momo9010"; // Steht im Klartext in der DB!


        // NEUER USER HINZUGEFÜGT
        String newUserName = "elias_dev";
        String newUserPw = "securePass2026";


        // --- SCHRITT 1: VERSUCH DER REGISTRIERUNG ---




        System.out.println("\n-> Versuche Login für: " + leoName);
        if (userService.loginUser(leoName, leoPw)) {
            System.out.println("   => STATUS: ERFOLG! Der BCrypt-Hash in Reihe 59 ist korrekt."+leoName);
        } else {
            System.out.println("   => STATUS: FEHLGESCHLAGEN! Passwort oder Hash falsch.");
        }


        System.out.println("\n-> Versuche Login für: " + eliasName);
        if (userService.loginUser(eliasName, eliasPw)) {
            System.out.println("   => STATUS: ERFOLG!"+eliasName);
        } else {
            System.out.println("   => STATUS: FEHLGESCHLAGEN!");
            System.out.println("   => GRUND: '" + eliasName + "' hat das Klartext-Passwort '" + eliasPw + "' in der DB. BCrypt kann das nicht lesen.");

            // Generiere den sicheren Hash für euer Modul-Protokoll:
            try {
                String sichererHash = userService.encodePassword(eliasPw);
                System.out.println("   => SCHUTZ-MASSNAHME: Ersetze das Passwort in der DB-Reihe 12 mit diesem Hash:");
                System.out.println("      " + sichererHash);
            } catch (Exception e) {
                System.out.println("Password wurd enich gehasht!");
            }
                // Falls die Methode im Service anders heisst
            }
        try {
            userService.registerUser(fabilianName, "fabia.imhasly@gmx.ch", fabilianPw, Role.USER);
            System.out.println("[INFO] " + fabilianName + " wurde erfolgreich frisch gehasht registriert!");
        } catch (Exception e) {
            System.out.println("[HINWEIS] " + fabilianName + " existiert bereits in der DB oder Registrierung übersprungen.");
        }

        // Registrierung für SoraReads
        try {
            userService.registerUser(soraName, "sora@manga.ch", soraPw, Role.USER);
            System.out.println("[INFO] " + soraName + " wurde erfolgreich frisch gehasht registriert!");
        } catch (Exception e) {
            // Ignoriert Info-Meldung für kürzere Logs
        }


        // NEU: Registrierung für den neuen Test-User
        try {
            userService.registerUser(newUserName, "elias.kaiser@gmx.ch", newUserPw, Role.USER);
            System.out.println("[INFO] " + newUserName + " wurde erfolgreich frisch gehasht registriert!");
        } catch (Exception e) {
            System.out.println("[HINWEIS] " + newUserName + " existiert bereits in der DB.");
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

        // TEST 2: fabilian
        System.out.println("\n-> Versuche Login für: " + fabilianName);
        boolean fabiErfolg = userService.loginUser(fabilianName, fabilianPw);
        if (fabiErfolg) {
            System.out.println("   => STATUS: ERFOLG! Login klappt.");
        } else {
            System.out.println("   => STATUS: FEHLGESCHLAGEN!");
            System.out.println("   => GRUND: '" + fabilianName + "' hat in der Datenbank eventuell kein gehashtes Passwort.");

            // BCrypt-Hash Generator als Fallback für die Doku
            try {
                String neuerHash = userService.encodePassword(fabilianPw);
                System.out.println("   => TIPP: So müsste der sichere Hash für das Passwort '" + fabilianPw + "' in der DB aussehen:");
                System.out.println("      " + neuerHash);
            } catch (Exception e) {
                System.out.println("   => [HINWEIS] encodePassword-Methode im Service nicht verfügbar.");
            }
        }

        // TEST 3: SoraReads
        System.out.println("\n-> Versuche Login für: " + soraName);
        boolean soraErfolg = userService.loginUser(soraName, soraPw);
        if (soraErfolg) {
            System.out.println("   => STATUS: ERFOLG!");
        } else {
            System.out.println("   => STATUS: FEHLGESCHLAGEN!");
            try {
                String soraHash = userService.encodePassword(soraPw);
                System.out.println("   => TIPP: Generierter BCrypt-Hash für '" + soraPw + "':");
                System.out.println("      " + soraHash);
            } catch (Exception e) {
                // Ignoriert falls Methode fehlt
            }
        }

        // TEST 4: NEUER TEST-USER (Frisch mit BCrypt angelegt)
        System.out.println("\n-> Versuche Login für den neuen User: " + newUserName);
        boolean newErfolg = userService.loginUser(newUserName, newUserPw);
        if (newErfolg) {
            System.out.println("   => STATUS: ERFOLG! Die Neuanlage inklusive BCrypt-Hashing funktioniert einwandfrei.");
        } else {
            System.out.println("   => STATUS: FEHLGESCHLAGEN! Überprüfe die Validierungslogik im UserService.");
        }

        System.out.println("\n=== TEST-REIHE BEENDET ===");
    }

    @GetMapping("/")
    public String index() {
        return "Willkommen bei der Mangadb";
    }
}