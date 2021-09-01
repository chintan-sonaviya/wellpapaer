package com.mcrealtime.wallpaperapp.AdUtills;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class AdForCustomtxt extends TextView {
    public AdForCustomtxt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AdForCustomtxt(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdForCustomtxt(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fontforad/Poppins-Regular.otf");
        setTypeface(tf, Typeface.BOLD);

    }
}
