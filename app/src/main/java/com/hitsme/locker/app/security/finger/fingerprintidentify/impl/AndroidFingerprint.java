package com.hitsme.locker.app.security.finger.fingerprintidentify.impl;

import android.content.Context;
import android.os.Handler;
import android.support.v4.os.CancellationSignal;

import com.hitsme.locker.app.security.finger.fingerprintidentify.aosp.FingerprintManagerCompat;
import com.hitsme.locker.app.security.finger.fingerprintidentify.base.BaseFingerprint;


public class AndroidFingerprint extends BaseFingerprint {

    private CancellationSignal mCancellationSignal;
    private FingerprintManagerCompat mFingerprintManagerCompat;
    private Handler handler;
    public AndroidFingerprint(Context context, BaseFingerprint.FingerprintIdentifyExceptionListener exceptionListener,Handler handler) {
        super(context, exceptionListener);
       this.handler=handler;
        try {
            mFingerprintManagerCompat = FingerprintManagerCompat.from(mContext);
            setHardwareEnable(mFingerprintManagerCompat.isHardwareDetected());
            setRegisteredFingerprint(mFingerprintManagerCompat.hasEnrolledFingerprints());
        } catch (Throwable e) {
            onCatchException(e);
        }
    }

    @Override
    protected void doIdentify() {
        try {
            mCancellationSignal = new CancellationSignal();
            mFingerprintManagerCompat.authenticate(null, 0, mCancellationSignal, new FingerprintManagerCompat.AuthenticationCallback(handler) {
                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    onSucceed();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    onNotMatch();
                }

                @Override
                public void onAuthenticationError(int errMsgId, CharSequence errString) {
                    super.onAuthenticationError(errMsgId, errString);
                    onFailed();
                }
            }, null);
        } catch (Throwable e) {
            onCatchException(e);
            onFailed();
        }
    }

    @Override
    protected void doCancelIdentify() {
        try {
            if (mCancellationSignal != null) {
                mCancellationSignal.cancel();
            }
        } catch (Throwable e) {
            onCatchException(e);
        }
    }

    @Override
    protected boolean needToCallDoIdentifyAgainAfterNotMatch() {
        return false;
    }
}