package com.holela.Controller;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Admin on 12/1/2016.
 */

public class Textview_open_regular extends TextView {

    private final static String CONDENSED_FONT = "fonts/OpenSans-Regular.ttf";

    public Textview_open_regular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Textview_open_regular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Textview_open_regular(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), CONDENSED_FONT);
        setTypeface(tf);
    }


}