package com.hw.frsecurity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.InputMismatchException;

public class GenericListAdapterParser {
    protected static void parseView(View view, Object object) {
        if (view instanceof TextView) {
            handleTextView((TextView) view, object);
        } else if (view instanceof ImageView) {
            handleImageView((ImageView) view, object);
        } else { // view is not recognized, ignore
            System.out.println("Error");
        }
    }

    private static void handleTextView(TextView textView, Object object) {
        if (object instanceof CharSequence) {
            textView.setText((CharSequence) object);
        } else {
            throw new InputMismatchException("The object is not compatible with a TextView.");
        }
    }

    private static void handleImageView(ImageView imageView, Object object) {
        if (object instanceof Drawable) {
            imageView.setImageDrawable((Drawable) object);
        } else if (object instanceof Bitmap) {
            imageView.setImageBitmap((Bitmap) object);
        } else { // view is not recognized, ignore
            throw new InputMismatchException("The object is not compatible with an ImageView.");
        }
    }
}
