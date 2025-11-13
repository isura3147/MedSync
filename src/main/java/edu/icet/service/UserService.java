package edu.icet.service;

import edu.icet.model.User;
import edu.icet.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Authenticates a user.
     *
     * @param username The username to check.
     * @param password The plain-text password to check.
     * @return An Optional containing the User if authentication is successful,
     * or an empty Optional if it fails.
     */
    public Optional<User> authenticateUser(String username, String password) {
        // Find the user by their username
        Optional<User> userOpt = userRepository.findByUsername(username); //

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Check if the provided password matches the stored hash
            if (BCrypt.checkpw(password, user.getPasswordHash())) {
                // Password matches
                return userOpt;
            }
        }
        // User not found or password incorrect
        return Optional.empty();
    }

    /**
     * Registers a new user.
     *
     * @param username The username for the new account.
     * @param password The plain-text password for the new account.
     * @param role The role (e.g., "Admin", "Pharmacist").
     * @return The newly created User object.
     * @throws RuntimeException if the username already exists.
     */
    public User registerUser(String username, String password, String role) {
        // Check if username is already taken
        if (userRepository.findByUsername(username).isPresent()) { //
            throw new RuntimeException("Username already exists: " + username);
        }

        // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User newUser = new User(); //
        newUser.setUsername(username);
        newUser.setPasswordHash(hashedPassword);
        newUser.setRole(role);

        return userRepository.save(newUser);
    }
}