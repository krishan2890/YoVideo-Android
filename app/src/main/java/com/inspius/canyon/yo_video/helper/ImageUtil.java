package com.inspius.canyon.yo_video.helper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.inspius.canyon.yo_video.model.ImageObj;

import java.io.File;
import java.io.IOException;

/**
 * Created by Admin on 15/4/2016.
 */
public class ImageUtil {
    public static ImageObj getByteImageAvatar(Context context, Uri selectedImage) {

        String mimeType = "image/jpeg";
        String picturePath = null;

        picturePath = getRealPathFromURI(context, selectedImage);
        File file = new File(picturePath);

        Bitmap orgBitmap = decodeSampledBitmapFromFile(picturePath);

        // rotate
        int ori = getExifOrientation(picturePath);
        Bitmap bitmap = rotate(ori, orgBitmap);

        // image info
        mimeType = getContentTypeFromFileString(file);
        String name = file.getName();

        return new ImageObj(mimeType, bitmap);
    }

    private static String getContentTypeFromFileString(File file) {
        String type = null;
        if (file != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    MimeTypeMap.getFileExtensionFromUrl((Uri.fromFile(file)
                            .toString())));
        }
        return type;
    }

    private static Bitmap rotate(int value, Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.setRotate(value);

        if (bm != null)
            return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                    matrix, true);
        else {
            Log.e("ImageProcess", "Cannot get image");
            return bm;
        }
    }

    private static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognise a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = ExifInterface.ORIENTATION_ROTATE_90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = ExifInterface.ORIENTATION_ROTATE_180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = ExifInterface.ORIENTATION_ROTATE_270;
                        break;
                }

            }
        }
        return degree;
    }

    private static Bitmap decodeSampledBitmapFromFile(String fileName) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);

        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options,
//                GlobalConstant.SIZE_LIMIT_AVATAR, GlobalConstant.SIZE_LIMIT_AVATAR);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(fileName, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    private static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
