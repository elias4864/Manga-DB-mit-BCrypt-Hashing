package manga.service;

import manga.model.User;
import manga.model.Role;
import manga.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    // Konstruktor für Dependency Injection
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // DIESE METHODE MUSS GENAU SO AUSSEHEN:
    public User registerUser(String username, String email, String password, Role role) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password); // Hinweis: Später hier BCrypt nutzen!
        newUser.setRole(role);

        return userRepository.save(newUser);
    }

    public User loginUser(String username, String password) {
        User newUser = userRepository.findByUsernameOrEmail(username, password);
        return newUser;
    }
}