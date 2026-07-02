package manga.service;

import io.jsonwebtoken.security.Password;
import manga.model.User;
import manga.model.Role;
import manga.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // Wir injizieren hier den UserService statt des Repositories!
    public AuthService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Wenn deine Vorgabe sagt, die Methode MUSS hier so existieren,
    // leitet sie die Arbeit sauber an den UserService weiter:
    public User registerUser(String username, String email, String password, Role role) {
        return userService.registerUser(username, email, password, role);
    }

    public boolean loginUser(String usernameOrEmail, String inputPassword) {
        // Nutzen der findByLogin-Logik aus dem UserService
        Optional<User> userOptional = userService.findByLogin(usernameOrEmail);

        if (userOptional.isEmpty()) {
            System.out.println("Login fehlgeschlagen: User existiert nicht.");
            return false;
        }

        User user = userOptional.get();
        boolean isPasswordCorrect = passwordEncoder.matches(inputPassword, user.getPassword());

        if (isPasswordCorrect) {
            System.out.println("Login erfolgreich für: " + user.getUsername());
            // HIER kommt später deine JWT-Generierung hin!
        } else {
            System.out.println("Login fehlgeschlagen: Passwort falsch.");
        }

        return isPasswordCorrect;
    }
}