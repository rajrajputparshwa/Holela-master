package com.holela.Controller;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Admin on 12/1/2016.
 */

public class Textview_open_semi_bold extends TextView {

    private final static String CONDENSED_FONT = "fonts/OpenSans-Semibold.ttf";

    public Textview_open_semi_bold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Textview_open_semi_bold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Textview_open_semi_bold(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), CONDENSED_FONT);
        setTypeface(tf);
    }


}