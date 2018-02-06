package ru.arturvasilov.githubmvp.screen.repositories;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import ru.arturvasilov.githubmvp.screen.auth.AuthPresenterTest;
import ru.arturvasilov.githubmvp.test.MockLifecycleHandler;
import ru.arturvasilov.githubmvp.test.TestKeyValueStorage;
import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.gdgkazan.githubmvp.repository.KeyValueStorage;
import ru.gdgkazan.githubmvp.repository.RepositoryProvider;
import ru.gdgkazan.githubmvp.screen.repositories.RepositoriesPresenter;
import ru.gdgkazan.githubmvp.screen.repositories.RepositoriesView;

import static junit.framework.Assert.assertNotNull;

/**
 * @author Artur Vasilov
 */
@RunWith(JUnit4.class)
public class RepositoriesPresenterTest {

    /**
     * TODO : task
     *
     * Create tests for {@link ru.gdgkazan.githubmvp.screen.repositories.RepositoriesPresenter}
     *
     * Your test cases must have at least 3 tests
     */

    private RepositoriesView mRepositoriesView;
    private RepositoriesPresenter mRepositoriesPresenter;

    @Before
    public void setUp() throws Exception {
        LifecycleHandler lifecycleHandler = new MockLifecycleHandler();
        mRepositoriesView = Mockito.mock(RepositoriesView.class);

        mRepositoriesPresenter = new RepositoriesPresenter(lifecycleHandler, mRepositoriesView);
    }

    @Test
    public void testCreated() throws Exception {
        assertNotNull(mRepositoriesPresenter);
    }

    @Test
    public void testNoActionsWithView() throws Exception {
        Mockito.verifyNoMoreInteractions(mRepositoriesView);
    }

    @Test
    public void testShowHideRepositories(){
        KeyValueStorage storage = new TokenKeyValueStorage("ac781f9d0eb890d1e107d2898db9");
        RepositoryProvider.setKeyValueStorage(storage);

        mRepositoriesPresenter.init();

        Mockito.verify(mRepositoriesView).showLoading();
        Mockito.verify(mRepositoriesView).hideLoading();
    }

    @Test
    public void testErrorRepositories(){
        KeyValueStorage storage = new TokenKeyValueStorage("ac781f9d0eb890d1e107d2898db9");
        RepositoryProvider.setKeyValueStorage(storage);

        mRepositoriesPresenter.init();

        Mockito.verify(mRepositoriesView).showError();
    }

    private class TokenKeyValueStorage extends TestKeyValueStorage {

        private final String mToken;

        public TokenKeyValueStorage(@NonNull String token) {
            mToken = token;
        }

        @NonNull
        @Override
        public String getToken() {
            return mToken;
        }
    }
}
