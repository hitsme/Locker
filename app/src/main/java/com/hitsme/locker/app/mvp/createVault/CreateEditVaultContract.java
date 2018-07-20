package com.hitsme.locker.app.mvp.createVault;

import com.hitsme.locker.app.mvp.BasePresenter;
import com.hitsme.locker.app.mvp.BaseView;

/**
 * Created by 10093 on 2017/4/19.
 */

public interface CreateEditVaultContract {

    interface View extends BaseView<Presenter> {
        void showEmptyTaskError();

        void showErrorEmptyName();

        void showErrorEmptyPassword();

        void showErrorEmptyPassword2();

        void showErrorPasswordNotMatch();

        void showErrorVaultExists();

        void showTasksList();

        void setName(String title);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void saveVault(String fileName, String password, String password2);

        void populateVault();
    }
}
