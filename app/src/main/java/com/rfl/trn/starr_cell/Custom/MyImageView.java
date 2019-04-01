package com.rfl.trn.starr_cell.Custom;

import android.content.Context;
import android.widget.ImageView;

public class MyImageView extends android.support.v7.widget.AppCompatImageView {
    public MyImageView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        //force a 16:9 aspect ratio
        int height = Math.round(width * .5625f);
        setMeasuredDimension(width, height);
    }

}
