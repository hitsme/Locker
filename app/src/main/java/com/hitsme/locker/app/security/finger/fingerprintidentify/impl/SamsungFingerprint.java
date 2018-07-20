package com.hitsme.locker.app.security.finger.fingerprintidentify.impl;

import android.content.Context;

import com.hitsme.locker.app.security.finger.fingerprintidentify.base.BaseFingerprint;
import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;

public class SamsungFingerprint extends BaseFingerprint {

    private int mResultCode = -1;
    private SpassFingerprint mSpassFingerprint;

    public SamsungFingerprint(Context context, BaseFingerprint.FingerprintIdentifyExceptionListener exceptionListener) {
        super(context, exceptionListener);

        try {
            Spass spass = new Spass();
            spass.initialize(mContext);
            mSpassFingerprint = new SpassFingerprint(mContext);
            setHardwareEnable(spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT));
            setRegisteredFingerprint(mSpassFingerprint.hasRegisteredFinger());
        } catch (Throwable e) {
            onCatchException(e);
        }
    }

    @Override
    protected void doIdentify() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSpassFingerprint.startIdentify(new SpassFingerprint.IdentifyListener() {
                        @Override
                        public void onFinished(int i) {
                            mResultCode = i;
                        }

                        @Override
                        public void onReady() {

                        }

                        @Override
                        public void onStarted() {

                        }

                        @Override
                        public void onCompleted() {
                            switch (mResultCode) {
                                case SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS:
                                case SpassFingerprint.STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS:
                                    onSucceed();
                                    break;

                                case SpassFingerprint.STATUS_TIMEOUT_FAILED:
                                case SpassFingerprint.STATUS_BUTTON_PRESSED:
                                case SpassFingerprint.STATUS_QUALITY_FAILED:
                                case SpassFingerprint.STATUS_USER_CANCELLED_BY_TOUCH_OUTSIDE:
                                case SpassFingerprint.STATUS_AUTHENTIFICATION_FAILED:
                                    onNotMatch();
                                    break;

                                default:
                                    onFailed();
                                    break;
                            }
                        }
                    });
                } catch (Throwable e) {
                    onCatchException(e);
                    onFailed();
                }
            }
        });
    }

    @Override
    protected void doCancelIdentify() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mSpassFingerprint != null) {
                        mSpassFingerprint.cancelIdentify();
                    }
                } catch (Throwable e) {
                    onCatchException(e);
                }
            }
        });
    }
}
