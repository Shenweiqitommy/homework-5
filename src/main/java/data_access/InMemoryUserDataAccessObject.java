package data_access;

import java.util.HashMap;
import java.util.Map;

import entity.User;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * In‐memory DAO for user data.
 * Supports signup, login, change‐password, and tracks the last‐logged‐in user.
 */
public class InMemoryUserDataAccessObject
        implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface {

    /** All registered users, keyed by username. */
    private final Map<String, User> users = new HashMap<>();

    /** The username most recently recorded via setCurrentUser(). */
    private String currentUser;

    // ─── SignupUserDataAccessInterface ────────────────────────────────────────
    @Override
    public boolean existsByName(String username) {
        return users.containsKey(username);
    }

    @Override
    public void save(User user) {
        users.put(user.getName(), user);
    }

    // ─── LoginUserDataAccessInterface ─────────────────────────────────────────
    @Override
    public User get(String username) {
        return users.get(username);
    }

    /**
     * Record that the given username has just logged in.
     */
    @Override
    public void setCurrentUser(String username) {
        this.currentUser = username;
    }

    /**
     * Return the last username passed to setCurrentUser(), or null if none.
     */
    @Override
    public String getCurrentUser() {
        return currentUser;
    }

    // ─── ChangePasswordUserDataAccessInterface ────────────────────────────────
    @Override
    public void changePassword(User user) {
        // overwrite existing entry with new password
        users.put(user.getName(), user);
    }
}
