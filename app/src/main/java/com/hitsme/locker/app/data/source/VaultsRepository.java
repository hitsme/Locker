package com.hitsme.locker.app.data.source;

import android.text.TextUtils;

import com.hitsme.locker.app.data.Clases.Vault;
import com.hitsme.locker.app.data.converters.Converter;
import com.hitsme.locker.app.core.utils.FileUtils;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by 10093 on 2017/4/17.
 */

public class VaultsRepository {

    public static Observable<List<Vault>> getVaults() {
        File baseFile = new File(Preferences.getDefaultVaultDirectory());
        if (!baseFile.exists()) {
            baseFile.mkdirs();
        }

        return Observable.from(baseFile.listFiles())
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File o) {
                        return FileUtils.esBobeda(o);
                    }
                })
                .map((new Func1<File, Vault>() {
                    @Override
                    public Vault call(File srcObj) {
                        return Converter.convertVault(srcObj);
                    }
                }))
                .toSortedList(new Func2<Vault, Vault, Integer>() {
                    @Override
                    public Integer call(Vault vault, Vault vault2) {
                        return vault.getName().compareTo(vault2.getName());
                    }
                });

    }


    public static Observable<Vault> getVault(String fullPath) {
        if (TextUtils.isEmpty(fullPath)) {
            return Observable.empty();
        }
        File file = new File(fullPath);
        if (!file.exists() || !FileUtils.esBobeda(file)) {
            return Observable.empty();
        }

        return Observable.just(file)
                .map((new Func1<File, Vault>() {
                    @Override
                    public Vault call(File srcObj) {
                        return Converter.convertVault(srcObj);
                    }
                }));
    }


}
