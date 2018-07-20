package com.hitsme.locker.app.mvp.migrar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hitsme.locker.app.R;
import com.hitsme.locker.app.mvp.UI.PasswordEditText;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 10093 on 2017/4/27.
 */

public class MigrarFragment extends Fragment implements MigrarContract.View {

    public static final String DATA_MIGRACION = "DATA_MIGRACION";

    private MigrarContract.Presenter mPresenter;

    @InjectView(R.id.nombre)    EditText name;
    @InjectView(R.id.password1)    PasswordEditText password1;

    @InjectView(R.id.fab_edit_task_done)  FloatingActionButton fab;

    public static MigrarFragment newInstance() {
        return new MigrarFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(@NonNull MigrarContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.migrar_fragment, container, false);
        ButterKnife.inject(this, root);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.migrarVault(name.getText().toString(),password1.getText().toString());
            }
        });

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return root;
    }


    @Override
    public void showErrorEmptyName() {
        name.setError(getResources().getString(R.string.empty_name));
        name.requestFocus();
    }

    @Override
    public void showErrorEmptyPassword() {
        password1.setError(getResources().getString(R.string.empty_password));
        password1.requestFocus();
    }

    @Override
    public void showErrorIncorrectPassword() {
        password1.setError(getResources().getString(R.string.error_open_file));
        password1.requestFocus();

    }

    @Override
    public void showErrorNoSeAhEcontradoVault() {

    }


    @Override
    public void showErrorVaultExists() {
        name.setError(getResources().getString(R.string.exists_vault_whith_name));
    }

    @Override
    public void showTasksList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }



    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void terminar() {
        getActivity().finish();
    }
}
