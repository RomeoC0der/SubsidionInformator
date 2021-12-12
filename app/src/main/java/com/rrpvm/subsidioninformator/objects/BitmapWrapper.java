package com.rrpvm.subsidioninformator.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;

public class BitmapWrapper implements Serializable {
    private Bitmap bitmap;
    private boolean normalized = false;
    public static final double SIZE = 200;

    public BitmapWrapper(Bitmap bitmap) {
        this.bitmap = bitmap;
        normalize();
    }

    public BitmapWrapper(BitmapWrapper wrapper) {
        this.bitmap = wrapper.bitmap;
        this.normalized = wrapper.normalized;
    }


    public void scaleTo(int width, int height) {
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
    }
    public void scaleWithAspectRatio(int maxHeight){
        double aspect = aspectRatio(bitmap);
        int height = maxHeight;
        int width = (int)(height*aspect);
        scaleTo(width,height);
    }

    public static double aspectRatio(Bitmap bitmap) {
        return (double) bitmap.getWidth() / (double) bitmap.getHeight();
    }

    public void normalize() {
        if (!normalized) {
            double aspectRatio = aspectRatio(this.bitmap);
            this.bitmap = Bitmap.createScaledBitmap(bitmap, (int) (SIZE * aspectRatio), (int) SIZE, false);
            this.normalized = true;
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        boolean success = bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteStream);
        byte bitmapBytes[] = byteStream.toByteArray();
        if (success)
            out.write(bitmapBytes, 0, bitmapBytes.length);
        byteStream.close();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1)
            byteStream.write(b);
        byte bitmapBytes[] = byteStream.toByteArray();
        bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
        byteStream.close();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
