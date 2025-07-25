package use_case.login;

import entity.User;

/**
 * DAO for the Login Use Case.
 */
public interface LoginUserDataAccessInterface {

    /**
     * Checks if the given username exists.
     * @param username the username to look for
     * @return true if a user with the given username exists; false otherwise
     */
    boolean existsByName(String username);

    /**
     * Saves the user.
     * @param user the user to save
     */
    void save(User user);

    /**
     * Returns the user with the given username.
     * @param username the username to look up
     * @return the user with the given username
     */
    User get(String username);

    /**
     * Record that the given username is now the “current” (logged-in) user.
     * @param username the username to set as current
     */
    void setCurrentUser(String username);

    /**
     * Get the last username passed to setCurrentUser(), or null if none.
     * @return the current logged-in username, or null
     */
    String getCurrentUser();
}
