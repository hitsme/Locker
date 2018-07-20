package com.hitsme.locker.app.mvp.addToVault;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.hitsme.locker.app.R;
import com.hitsme.locker.app.data.Clases.Vault;
import com.hitsme.locker.app.migracion.MigrarUtilsDeprecated;
import com.hitsme.locker.app.mvp.listVaults.VaultPresenetr;
import com.hitsme.locker.app.mvp.listVaults.VaultsFragment;
import com.hitsme.locker.app.mvp.migrar.MigrarActivity;
import com.hitsme.locker.app.mvp.migrar.MigrarFragment;
import com.hitsme.locker.app.mvp.openVault.OpenVaultActivity;
import com.hitsme.locker.app.utils.ActivityUtils;
import com.hitsme.locker.app.utils.Injection;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 10093 on 2017/4/18.
 */
public class AddToVaultActivity extends AppCompatActivity implements AddTovaultContract.irLuegoDeRecibir {


    protected VaultPresenetr mTasksPresenter;


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;


    private ArrayList<String> archivos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ResolverAccionUtils.resolverAccion(getIntent(), this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_vault_activity);

        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.z_ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);

        VaultsFragment tasksFragment = (VaultsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (tasksFragment == null) {
            tasksFragment = VaultsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), tasksFragment, R.id.contentFrame);
        }

        mTasksPresenter = new VaultPresenetr(
                tasksFragment,
                Injection.provideSchedulerProvider());

        tasksFragment.setMItemListener(clickListener);
        ab.setTitle(R.string.selecione_la_bobeda_a_aniadir);
    }

    private VaultsFragment.TaskItemListener clickListener = new VaultsFragment.TaskItemListener() {
        @Override
        public void onTaskClick(Vault clickedVault) {
            Intent intent = new Intent(getApplicationContext(), OpenVaultActivity.class);
            intent.putExtra(OpenVaultActivity.EXTRA_VAULT_PATH, clickedVault.getFullPath());
            intent.putStringArrayListExtra(OpenVaultActivity.EXTRA_FILES_TO_ADD, archivos);
            startActivity(intent);
            finish();
        }
    };


    @Override
    public void irADecrypt(String filePath) {
        Intent intent = new Intent(this, OpenVaultActivity.class);
        intent.putExtra(OpenVaultActivity.EXTRA_VAULT_PATH, filePath);
        startActivity(intent);
        finish();
    }

    @Override
    public void irAEncrypt(ArrayList<String> filesPath) {
        this.archivos = filesPath;
    }

    @Override
    public void irAMigrar(MigrarUtilsDeprecated obje) {
        Intent intent = new Intent(this, MigrarActivity.class);
        intent.putExtra(MigrarFragment.DATA_MIGRACION, obje);
        startActivity(intent);
        finish();
    }

    @Override
    public void mostrarArchivoNoEncontrado(String path) {
        Toast.makeText(this, getResources().getText(R.string.archivo_no_encontrado) + path, Toast.LENGTH_SHORT).show();
        finish();
    }
}
