package edu.icet.service;

import edu.icet.model.User;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private User currentUser;

    public void setLoggedInUser(User user) {
        this.currentUser = user;
    }

    public User getLoggedInUser() {
        if (currentUser == null) {
            throw new IllegalStateException("No user is logged in.");
        }
        return currentUser;
    }

    public void clearSession() {
        this.currentUser = null;
    }
}