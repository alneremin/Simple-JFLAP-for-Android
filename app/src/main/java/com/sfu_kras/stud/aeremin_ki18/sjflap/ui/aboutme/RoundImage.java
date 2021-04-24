package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.aboutme;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.sfu_kras.stud.aeremin_ki18.sjflap.R;


public class RoundImage extends View {


    public RoundImage(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dfa);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 3;


        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        //invalidate();
    }
}
