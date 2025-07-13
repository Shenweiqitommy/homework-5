package use_case.login;

import entity.User;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface loginInputDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = loginInputDataAccessInterface;
        this.loginPresenter       = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        if (!userDataAccessObject.existsByName(username)) {
            loginPresenter.prepareFailView(username + ": Account does not exist.");
        } else {
            final String pwd = userDataAccessObject.get(username).getPassword();
            if (!password.equals(pwd)) {
                loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            } else {
                // fetch the full User entity
                final User user = userDataAccessObject.get(username);

                // ← Task 2.1: record who just logged in
                userDataAccessObject.setCurrentUser(user.getName());

                // prepare and present the successful login view
                final LoginOutputData loginOutputData =
                        new LoginOutputData(user.getName(), false);
                loginPresenter.prepareSuccessView(loginOutputData);
            }
        }
    }
}
