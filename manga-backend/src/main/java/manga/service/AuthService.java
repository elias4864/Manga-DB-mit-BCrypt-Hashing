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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Konstruktor für Dependency Injection
    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // DIESE METHODE MUSS GENAU SO AUSSEHEN:
    public User registerUser(String username, String email, String password, Role role) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password); // Hinweis: Später hier BCrypt nutzen!
        newUser.setRole(role);
        String hashedPassword = passwordEncoder.encode(password);
        newUser.setPassword(hashedPassword);
        newUser.setRole(role);
        return  userRepository.save(newUser);




    }
    public boolean loginUser(String usernameOrEmail, String inputPassword) {

        // Ekach input donhi jagi pathvava (Username ODER Email sathi)
        Optional<User> userOptional = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        // User sapadla ki nahi te check kara
        if (userOptional.isEmpty()) {
            System.out.println("Login fail: User '" + usernameOrEmail + "' sapadla nahi.");
            return false;
        }

        // User data baher kadha
        User user = userOptional.get();

        /**
         * Password-Check via Bcrypt Password Vergleich zu PasswordEncoder
         */

        boolean isPasswordCorrect = passwordEncoder.matches(inputPassword, user.getPassword());

        if (isPasswordCorrect) {
            System.out.println("Login successful! Welcome: " + user.getUsername());
        } else {
            System.out.println("Login fail: Password chukicha ahe.");
        }

        return isPasswordCorrect;
    }
}