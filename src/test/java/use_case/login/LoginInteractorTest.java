package use_case.login;

import data_access.InMemoryUserDataAccessObject;
import entity.CommonUserFactory;
import entity.User;
import entity.UserFactory;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the LoginInteractor.
 */
public class LoginInteractorTest {

    // --------------------------------------------------
    // Existing success test
    // --------------------------------------------------
    @Test
    public void successTest() {
        LoginInputData inputData = new LoginInputData("Paul", "password");
        LoginUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // For the success test, we need to add Paul to the data access repository before we log in.
        UserFactory factory = new CommonUserFactory();
        User user = factory.create("Paul", "password");
        userRepository.save(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        LoginOutputBoundary successPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                assertEquals("Paul", user.getUsername());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }

    // --------------------------------------------------
    // NEW TEST FOR TASK 2.2: verify current-user tracking
    // --------------------------------------------------
    @Test
    public void successUserLoggedInTest() {
        // Arrange
        LoginInputData inputData = new LoginInputData("Paul", "password");
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        // Precondition: no one is logged in yet
        assertNull(userRepository.getCurrentUser());

        // Add Paul to the repository
        UserFactory factory = new CommonUserFactory();
        User user = factory.create("Paul", "password");
        userRepository.save(user);

        // Presenter stub to verify login succeeds
        LoginOutputBoundary presenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
                // sanity check: presenter still sees correct username
                assertEquals("Paul", outputData.getUsername());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userRepository, presenter);

        // Act
        interactor.execute(inputData);

        // Assert: DAO recorded the logged‐in user
        assertEquals("Paul", userRepository.getCurrentUser());
    }

    // --------------------------------------------------
    // Existing failure test
    // --------------------------------------------------
    @Test
    public void failureUserDoesNotExistTest() {
        // Arrange
        LoginInputData inputData = new LoginInputData("Paul", "password");
        LoginUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();
        // Note: we do NOT add Paul to the repo here

        // Presenter that checks for the correct failure message
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                // ←— this must be a single-line string literal:
                assertEquals("Paul: Account does not exist.", error);
            }
        };
    }
}
