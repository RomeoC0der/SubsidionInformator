package com.rrpvm.subsidioninformator.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;

public class BitmapWrapper implements Serializable {
    private Bitmap bitmap;
    public static final double SIZE = 200;
    private boolean normalized = false;

    public BitmapWrapper(Bitmap bitmap) {
        this.bitmap = bitmap;
        normalize();
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        double aspectRatio = aspectRatio(bitmap);
        //  this.bitmap = Bitmap.createScaledBitmap(bitmap, (int) (SIZE * aspectRatio), (int) SIZE, false);
        this.bitmap = bitmap;
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
        // double aspectRatio = aspectRatio(bitmap);
        // bitmap = Bitmap.createScaledBitmap(bitmap, (int) (SIZE / aspectRatio), (int) SIZE, false);
        byteStream.close();
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
}
