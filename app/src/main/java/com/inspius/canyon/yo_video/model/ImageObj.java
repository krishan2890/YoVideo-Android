package com.inspius.canyon.yo_video.model;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by Admin on 15/4/2016.
 */

public class ImageObj implements Serializable {
    private String mimeType;
    private String name;
    private Bitmap bitmap;

    public ImageObj(String name, String mimeType, Bitmap bitmap) {
        this.mimeType = mimeType;
        this.name = name;
        this.bitmap = bitmap;
    }

    public byte[] getImgBytes() {
        if (bitmap == null)
            return null;

        return getByteBitmap(bitmap);
    }

// public byte[] getImgBytes(int requiredSize) {
//  if (bitmap == null)
//   return null;
//
//  Bitmap bmp = CommonUtil.resize(bitmap, requiredSize);
//  return getByteBitmap(bmp);
// }

// /**
//  *
//  * @param bitmap
//  * @return
//  */
// public static Bitmap resize(Bitmap bitmap, int requiredSize) {
//
//  // Avatar size must be less than < 300px each side.
//  int newWidth = bitmap.getWidth();
//  int newHeight = bitmap.getHeight();
//  while (newWidth > requiredSize || newHeight > requiredSize) {
//   newWidth = newWidth / 2;
//   newHeight = newHeight / 2;
//  }
//
//  return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
// }

    private byte[] getByteBitmap(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (mimeType.equalsIgnoreCase(Bitmap.CompressFormat.JPEG.toString())) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        } else {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }
        return stream.toByteArray();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}