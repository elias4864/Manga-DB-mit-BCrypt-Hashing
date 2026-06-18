package manga.service;

import manga.model.User;
import org.mindrot.jbcrypt.BCrypt;
import manga.model.Role; // Nutze deine eigene Role-Klasse statt javax.management
import manga.model.User;

public class AuthService {


    private static final int SALT_ROUNDS = 12;

    public User registerUser(String username, String email, String rawInputPassword, Role role) {
        // HINWEIS: Hier solltest du rawInputPassword nutzen statt dem festen String "eljourka12345"
        String hashedPassword = BCrypt.hashpw(rawInputPassword, BCrypt.gensalt(SALT_ROUNDS));

        User newUser = new User(username, email, hashedPassword, role == null ? Role.USER : role);
        return newUser;
    }

    // REIHENFOLGE KORRIGIERT: Zuerst das Klartext-Passwort, dann der Hash
    public boolean loginUser(String rawInputPassword, String storedHash) {
        if (storedHash == null || !storedHash.startsWith("$2")) {
            throw new IllegalArgumentException("Der übergebene Passwort-Hash ist ungültig!");
        }

        // BCrypt erwartet: checkpw(Klartext, Hash)
        return BCrypt.checkpw(rawInputPassword, storedHash);
    }
}
