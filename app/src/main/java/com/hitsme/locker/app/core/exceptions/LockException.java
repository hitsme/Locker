package com.hitsme.locker.app.core.exceptions;

public class LockException extends RuntimeException {
    public static final String error_general = "err_error_general";
    public static final String cant_create = "err_cant_create";
    public static final String el_archivo_ya_existe = "err_the_file_already_exists";
    public static final String metodo_cifrado_no_soportado_por_os = "err_unsupported_encryption_method";
    public static final String no_se_pudo_agregar_el_archivo = "err_failed_to_add_the_file";
    public static final String error_al_abrir_el_archivo_verifique_password = "err_unable_to_open_file_Verify_the_password";
    public static final String no_se_pudo_eliminar_el_archivo = "err_could_not_delete_the_file";
    public static final String no_se_pudo_abrir_el_archivo = "err_could_not_open_the_file";
    public static final String not_found = "err_not_found";
    public static final String error_version = "error_version";
    public static final String error_open = "error_open";
    public static final String error_nosecureview = "error_nosecureview";
    public static final String error_defeated = "error_defeated";
    public static final String empty_password = "empty_password";
    public static final String error_password = "error_password";
    public static final String error_unlock0 = "error_unlock";
    private String code;
    private String[] params;

    public LockException(String code, Exception e) {
        super(e);
        this.code = code;
    }

    public LockException(String code, String[] params, Exception e) {
        super(e);
        this.code = code;
        this.params = params;
    }

    public LockException(String code) {
        this.code = code;
    }

    public LockException(String code, String[] params) {
        this.code = code;
        this.params = params;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String[] getParams() {
        return this.params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }
}
