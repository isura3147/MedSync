package edu.icet.repository;

import edu.icet.config.AppConfig;
import edu.icet.model.User;
import edu.icet.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testSaveAndFindByUsername() {
        // 1. Create a new test user
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPasswordHash("testpasswordhash");
        testUser.setRole("Tester");

        // 2. Save the user
        User savedUser = userRepository.save(testUser);

        // 3. Check that it was saved and has an ID
        Assertions.assertNotNull(savedUser, "Saved user should not be null");
        Assertions.assertNotNull(savedUser.getId(), "Saved user should have an ID");

        // 4. Retrieve the user by its username
        Optional<User> foundUserOpt = userRepository.findByUsername("testuser");

        // 5. Verify the user was found and is the correct one
        Assertions.assertTrue(foundUserOpt.isPresent(), "User should be found by username");
        User foundUser = foundUserOpt.get();
        Assertions.assertEquals("testuser", foundUser.getUsername());
        Assertions.assertEquals("Tester", foundUser.getRole());

        System.out.println("Successfully saved and retrieved user: " + foundUser.getUsername());
    }

    @Test
    @Rollback(false)
    public void createAdminUser() {
        try {
            User admin = userService.registerUser("admin", "admin123", "Admin");
            Assertions.assertNotNull(admin);
            System.out.println("Admin user created or already exists.");
        } catch (RuntimeException e) {
            // This will catch if the user 'admin' already exists
            System.out.println(e.getMessage());
            System.out.println("Assuming admin user 'admin' already exists. Proceeding.");

            Optional<User> userOpt = userRepository.findByUsername("admin");
            if (userOpt.isPresent()) {
                User existingAdmin = userOpt.get();

                System.out.println("Forcing password reset for admin...");
                userRepository.delete(existingAdmin);
                User admin = userService.registerUser("admin", "admin123", "Admin");
                Assertions.assertNotNull(admin);
                System.out.println("Admin user password has been reset.");
            }
        }
    }
}