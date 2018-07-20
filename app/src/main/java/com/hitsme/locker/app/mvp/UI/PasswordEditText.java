package com.hitsme.locker.app.mvp.UI;


import android.content.Context;
import android.graphics.Typeface;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by 10093 on 2017/5/4.
 */
public class PasswordEditText extends EditText {

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public PasswordEditText(Context context) {
        super(context);
        init();
    }

    private void init(){
        this.setTypeface(Typeface.DEFAULT);
        this.setTransformationMethod(new PasswordTransformationMethod());
    }


}
