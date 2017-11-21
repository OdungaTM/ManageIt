package io.futurebound.manageit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by HP on 11/16/2017.
 */

public class LinedEditText extends android.support.v7.widget.AppCompatEditText {
    private Rect mRect;
    private Paint mPaint;

    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRect = new Rect();
        mPaint = new Paint();
        // define the style of line
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // define the color of line
        mPaint.setColor(Color.BLACK);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int lHeight = getLineHeight();
        // the number of line
        int count = height / lHeight;
        if (getLineCount() > count) {
            // for long text with scrolling
            count = getLineCount();
        }
        Rect r = mRect;
        r.contains(height,height,height,height);
        Paint paint = mPaint;

        // first line
        int baseline = getLineBounds(0,r);

        // draw the remaining lines.

        for (int i = 0; i < count; i++) {
        //for (int i = 0; i < getLineCount(); i++) {
            //canvas.drawLine(r.left, baseline, r.right, baseline, paint);
            canvas.drawLine(35*i, 150, 35*i+10, 160, paint);

            // next line
            baseline += getLineHeight();
            //canvas.translate(0,100);
        }
        super.onDraw(canvas);
    }

}
