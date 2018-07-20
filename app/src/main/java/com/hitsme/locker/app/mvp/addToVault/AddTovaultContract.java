package com.hitsme.locker.app.mvp.addToVault;

import com.hitsme.locker.app.migracion.MigrarUtilsDeprecated;

import java.util.ArrayList;

/**
 * Created by 10093 on 2017/4/22.
 */

public interface AddTovaultContract {

    interface irLuegoDeRecibir  {
        void irADecrypt(String filePath);
        void irAEncrypt(ArrayList<String> filesPath);
        void irAMigrar(MigrarUtilsDeprecated mu);
        void mostrarArchivoNoEncontrado(String path);
    }
}
