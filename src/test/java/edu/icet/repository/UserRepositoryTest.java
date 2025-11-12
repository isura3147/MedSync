package edu.icet.repository;

import edu.icet.config.AppConfig;
import edu.icet.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ExtendWith(SpringExtension.class) // Integrates Spring with JUnit 5
@ContextConfiguration(classes = {AppConfig.class}) // Tells the test which config to load
@Transactional // Automatically rolls back database changes after each test
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

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
}