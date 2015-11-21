package com.transitiasi.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.transitiasi.enums.Status;
import com.transitiasi.model.ShareInfo;

/**
 * Created by Ionut Stirban on 22/11/15.
 */
public class CustomMarkerBuilder {
    private CustomMarkerBuilder() {
        throw new AssertionError("No instance allowed");
    }

    public static Bitmap buildMarkerForBus(ShareInfo shareInfo, Resources resources) {
        Paint backgroundPaint = new Paint();

        backgroundPaint.setColor(resources.getColor(Status.fromString(shareInfo.getStatus()).color));

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.CENTER);

        backgroundPaint.setStyle(Paint.Style.FILL);
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        final int size = 80;
        final int padding = 5;

        Bitmap bmp = Bitmap.createBitmap(size, size, conf);
        Canvas canvas = new Canvas(bmp);
        //canvas.drawColor(Color.RED);


        if (shareInfo.getType().equals(ShareInfo.TRAM)) {
            canvas.drawCircle(size / 2, size / 2, size / 2 - padding, backgroundPaint);
        } else {
            canvas.drawRect(0, 0, size, size, backgroundPaint);
        }

        canvas.drawText(shareInfo.getType() + shareInfo.getLabel(), size / 2, size / 2 + 10, textPaint); // paint defines the text color, stroke width, size
        return bmp;

    }
}
