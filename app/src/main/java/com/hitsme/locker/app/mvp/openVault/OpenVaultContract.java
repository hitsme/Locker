package com.hitsme.locker.app.mvp.openVault;

import com.hitsme.locker.app.mvp.BasePresenter;
import com.hitsme.locker.app.mvp.BaseView;

import java.util.ArrayList;

/**
 * Created by 10093 on 2017/4/29.
 */

public interface OpenVaultContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showErrorEmptyPassword();

        void showErrorPasswordIncorrect();

        void openVault(String password, String path, ArrayList<String> filesToAdd);

        boolean isActive();

        void setVaultName(String name);
    }

    interface Presenter extends BasePresenter {
        void openVault(String password);

        String getVaultPaht();

    }
}
