package com.hitsme.locker.app.mvp.editVault;

import com.hitsme.locker.app.mvp.BasePresenter;
import com.hitsme.locker.app.mvp.BaseView;

/**
 * Created by 10093 on 2017/4/23.
 */

public interface EditVaultContract {

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

        void showErrorEmptyActualPassword();
    }

    interface Presenter extends BasePresenter {

        void editar(String fileName/*, String actualPassword, String newPassword1, String newPassword2*/);

        void populateVault();
    }
}
