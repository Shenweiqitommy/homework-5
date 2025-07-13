package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.InMemoryUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupViewModel;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

/**
 * The version of Main using an in-memory DAO for user data.
 */
public class MainWithInMemory {

    /**
     * The main method for starting the program with an in-memory DAO.
     * @param args input to main
     */
    public static void main(String[] args) {
        // Build the main application window
        final JFrame application = new JFrame("Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final CardLayout cardLayout = new CardLayout();
        final JPanel views = new JPanel(cardLayout);
        application.add(views);

        // Manage switching between views
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        new ViewManager(views, cardLayout, viewManagerModel);

        // ViewModels for each screen
        final LoginViewModel loginViewModel = new LoginViewModel();
        final LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
        final SignupViewModel signupViewModel = new SignupViewModel();

        // --- Task 1 change: use in-memory DAO for offline/testing ---
        final InMemoryUserDataAccessObject userDataAccessObject = new InMemoryUserDataAccessObject();

        // Set up the Signup screen
        final SignupView signupView = SignupUseCaseFactory.create(
                viewManagerModel,
                loginViewModel,
                signupViewModel,
                userDataAccessObject
        );
        views.add(signupView, signupView.getViewName());

        // Set up the Login screen
        final LoginView loginView = LoginUseCaseFactory.create(
                viewManagerModel,
                loginViewModel,
                loggedInViewModel,
                userDataAccessObject
        );
        views.add(loginView, loginView.getViewName());

        // Set up the Logged-In / Change Password screen
        final LoggedInView loggedInView = ChangePasswordUseCaseFactory.create(
                viewManagerModel,
                loggedInViewModel,
                userDataAccessObject
        );
        views.add(loggedInView, loggedInView.getViewName());

        // Show the Signup screen first
        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChanged();

        application.pack();
        application.setVisible(true);
    }
}
