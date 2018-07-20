package com.hitsme.locker.app.security.finger;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hitsme.locker.app.R;
import com.hitsme.locker.app.mvp.intro.IntroActivity;
import com.hitsme.locker.app.security.finger.fingerprintidentify.FingerprintIdentify;
import com.hitsme.locker.app.security.finger.fingerprintidentify.aosp.FingerprintManagerCompat;
import com.hitsme.locker.app.security.finger.fingerprintidentify.base.BaseFingerprint;

public class FingerPrintActivity extends AppCompatActivity {

    private final String TAG = "FingerPrintActivity";

    private TextView mResultInfo = null;
    private Button mCancelBtn = null;
    private Button mStartBtn = null;
    private FingerprintManagerCompat fingerprintManager = null;
    private FingerprintManagerCompat.AuthenticationCallback myAuthCallback = null;
    private CancellationSignal cancellationSignal = null;
    private FingerprintIdentify mFingerprintIdentify;
    private Handler handler = null;
    public static final int MSG_AUTH_SUCCESS = 100;
    public static final int MSG_AUTH_FAILED = 101;
    public static final int MSG_AUTH_ERROR = 102;
    public static final int MSG_AUTH_HELP = 103;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean isProtecting = sp.getBoolean("isProtected", false);
        if (!isProtecting) {

            startNextActivity();
        }

        setContentView(R.layout.finger_activity);
//            mResultInfo = (TextView) this.findViewById(R.id.fingerprint_status);
        mCancelBtn = (Button) this.findViewById(R.id.cancel_button);
        mStartBtn = (Button) this.findViewById(R.id.start_button);
        mCancelBtn.setEnabled(true);
        mStartBtn.setEnabled(true);

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set button state
                android.os.Process.killProcess(android.os.Process.myPid());   //获取PID
                System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
            }
        });



        // reset result info.




                // start fingerprint auth here.
        int i=1000;
        while(i-->0) {
            try {
                //CryptoObjectHelper cryptoObjectHelper = new CryptoObjectHelper();
                if (cancellationSignal == null) {
                    cancellationSignal = new CancellationSignal();
                }
                fingerprintManager.authenticate(null, 0,
                        null, myAuthCallback, null);
//                    mFingerprintIdentify.startIdentify(3, null,myAuthCallback);
                // mFingerprintIdentify = new FingerprintIdentify(getApplicationContext(),null,handler);
                // set button state.
                mStartBtn.setEnabled(false);
                mCancelBtn.setEnabled(true);
            } catch (Exception e) {
                //  e.printStackTrace();
                // Toast.makeText(FingerPrintActivity.this, "Fingerprint init failed! Try again!", Toast.LENGTH_SHORT).show();
            }

        }


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Log.d(TAG, "msg: " + msg.what + " ,arg1: " + msg.arg1);
                switch (msg.what) {
                    case MSG_AUTH_SUCCESS:
                        setResultInfo(R.string.fingerprint_success);
                        Intent intent = new Intent(FingerPrintActivity.this, IntroActivity.class);
                        startActivity(intent);
//                            mCancelBtn.setEnabled(false);
                        mStartBtn.setEnabled(true);
                        cancellationSignal = null;
                        break;
                    case MSG_AUTH_FAILED:
                        setResultInfo(R.string.fingerprint_not_recognized);
                        mCancelBtn.setEnabled(false);
                        mStartBtn.setEnabled(true);
                        cancellationSignal = null;
                        break;
                    case MSG_AUTH_ERROR:
                        handleErrorCode(msg.arg1);
                        break;
                    case MSG_AUTH_HELP:
                        handleHelpCode(msg.arg1);
                        break;
                }
            }
        };

        // init fingerprint.
        fingerprintManager = FingerprintManagerCompat.from(this);

        if (!fingerprintManager.isHardwareDetected()) {
            // no fingerprint sensor is detected, show dialog to tell user.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_sensor_dialog_title);
            builder.setMessage(R.string.no_sensor_dialog_message);
            builder.setIcon(android.R.drawable.stat_sys_warning);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.cancel_btn_dialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            // show this dialog.
            builder.create().show();
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            // no fingerprint image has been enrolled.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_fingerprint_enrolled_dialog_title);
            builder.setMessage(R.string.no_fingerprint_enrolled_dialog_message);
            builder.setIcon(android.R.drawable.stat_sys_warning);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.cancel_btn_dialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            // show this dialog
            builder.create().show();
        } else {
            try {
                myAuthCallback = new FingerprintManagerCompat.AuthenticationCallback(handler) {
                    @Override
                    public void onAuthenticationError(int errMsgId, CharSequence errString) {
                        super.onAuthenticationError(errMsgId, errString);
                    }
                };
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startNextActivity(){
        Intent i = new Intent(this, IntroActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean isProtecting = sp.getBoolean("isProtected", false);
        Toast.makeText(FingerPrintActivity.this,"is"+isProtecting,Toast.LENGTH_SHORT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean isProtecting = sp.getBoolean("isProtected", false);
        Toast.makeText(FingerPrintActivity.this,"is"+isProtecting,Toast.LENGTH_SHORT);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean isProtecting = sp.getBoolean("isProtected", false);
        Toast.makeText(FingerPrintActivity.this,"is"+isProtecting,Toast.LENGTH_SHORT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean isProtecting = sp.getBoolean("isProtected", false);
        Toast.makeText(FingerPrintActivity.this,"is"+isProtecting,Toast.LENGTH_SHORT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mStartBtn.isEnabled() && cancellationSignal != null) {
            cancellationSignal.cancel();
        }
    }

    private void handleHelpCode(int code) {
        switch (code) {
            case FingerprintManager.FINGERPRINT_ACQUIRED_GOOD:
                setResultInfo(R.string.AcquiredGood_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY:
                setResultInfo(R.string.AcquiredImageDirty_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT:
                setResultInfo(R.string.AcquiredInsufficient_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL:
                setResultInfo(R.string.AcquiredPartial_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST:
                setResultInfo(R.string.AcquiredTooFast_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW:
                setResultInfo(R.string.AcquiredToSlow_warning);
                break;
        }
    }

    private void handleErrorCode(int code) {
        switch (code) {
            case FingerprintManager.FINGERPRINT_ERROR_CANCELED:
                setResultInfo(R.string.ErrorCanceled_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE:
                setResultInfo(R.string.ErrorHwUnavailable_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:
                setResultInfo(R.string.ErrorLockout_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_NO_SPACE:
                setResultInfo(R.string.ErrorNoSpace_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_TIMEOUT:
                setResultInfo(R.string.ErrorTimeout_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS:
                setResultInfo(R.string.ErrorUnableToProcess_warning);
                break;
        }
    }

    private void setResultInfo(int stringId) {
        if (mResultInfo != null) {
            if (stringId == R.string.fingerprint_success) {
                // mResultInfo.setTextColor(getColor(R.color.success_color));
            } else {
                // mResultInfo.setTextColor(getColor(R.color.warning_color));
            }
            mResultInfo.setText(stringId);
        }
    }
}