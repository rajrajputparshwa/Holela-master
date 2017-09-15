package com.holela.Controller;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import java.lang.reflect.Field;

/**
 * Created by admin on 5/15/2017.
 */

public class EditText_OpenSans_Regular extends EditText {

    private final static String CONDENSED_FONT = "fonts/OpenSans-Regular.ttf";


    public EditText_OpenSans_Regular(Context context) {
        super(context);
        init();
    }

    public EditText_OpenSans_Regular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditText_OpenSans_Regular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), CONDENSED_FONT);

        setTypeface(tf);
        Field f = null;
    }

}
