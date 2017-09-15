package com.holela.Controller;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by admin on 5/16/2017.
 */

public class MyRiad_Pro_Regular extends Textview {

    private final static String CONDENSED_FONT = "fonts/Myriad Pro Regular.ttf";

    public MyRiad_Pro_Regular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyRiad_Pro_Regular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyRiad_Pro_Regular(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), CONDENSED_FONT);
        setTypeface(tf);
    }


}
