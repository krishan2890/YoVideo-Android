package com.inspius.yo_video.widget;

/**
 * Created by Billy on 11/7/16.
 */


import android.content.Context;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Billy on 6/5/2016.
 */
public class Hashtag extends ClickableSpan {
    Context context;
    TextPaint textPaint;

    public Hashtag(Context ctx) {
        super();
        context = ctx;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        textPaint = ds;
        ds.setColor(ds.linkColor);
        ds.setARGB(255, 30, 144, 255);
    }

    @Override
    public void onClick(View widget) {
//        TextView tv = (TextView) widget;
//        Spanned s = (Spanned) tv.getText();
//        int start = s.getSpanStart(this);
//        int end = s.getSpanEnd(this);
//        String theWord = s.subSequence(start + 1, end).toString();
//        Toast.makeText(context, String.format("Tags for tags: %s", theWord), 10).show();
    }
}