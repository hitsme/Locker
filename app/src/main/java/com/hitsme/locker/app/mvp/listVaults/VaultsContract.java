package com.hitsme.locker.app.mvp.listVaults;

import com.hitsme.locker.app.data.Clases.Vault;
import com.hitsme.locker.app.mvp.BasePresenter;
import com.hitsme.locker.app.mvp.BaseView;

import java.util.List;

/**
 * Created by 10093 on 2017/4/24.
 */

public interface VaultsContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTasks(List<Vault> vaults);

        //void showAddVault();

        //void showTaskDetailsUi(String taskId);

        void showLoadingTasksError();

        void showNoVaults();

        void showSuccessfullySavedMessage();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadVaults();


    }
}
