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
    public static final int SIZE = 200;

    public BitmapWrapper(Bitmap bitmap) {
        this.bitmap = Bitmap.createScaledBitmap(bitmap, SIZE, SIZE, false);
        // this.bitmap = bitmap;
    }

    public WeakReference<Bitmap> getBitmap() {
        return new WeakReference<>(bitmap);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = Bitmap.createScaledBitmap(bitmap, SIZE, SIZE, false);
        // this.bitmap = bitmap;
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
        bitmap = Bitmap.createScaledBitmap(bitmap, SIZE, SIZE, false);
        byteStream.close();
    }
}
