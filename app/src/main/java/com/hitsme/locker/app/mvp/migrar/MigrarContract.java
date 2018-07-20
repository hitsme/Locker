package com.hitsme.locker.app.mvp.migrar;

import com.hitsme.locker.app.mvp.BasePresenter;
import com.hitsme.locker.app.mvp.BaseView;

/**
 * Created by 10093 on 2017/4/26.
 */

public interface MigrarContract {

    interface View extends BaseView<Presenter> {

        void showErrorEmptyName();

        void showErrorVaultExists();

        void showErrorEmptyPassword();

        void showErrorIncorrectPassword();

        void showErrorNoSeAhEcontradoVault();

        public void showTasksList();

        boolean isActive();

        public void terminar();
    }

    interface Presenter extends BasePresenter {

        void migrarVault(String name, String password);

        void populateVault();
    }
}
