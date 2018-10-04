package com.jay.translator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;

public class ViewSettings {

   public static Bitmap setImageBlurry(Context context, Drawable drawable){

       Bitmap fromDrawable = ((BitmapDrawable)drawable).getBitmap();

       int width = Math.round(fromDrawable.getWidth() * 0.2f);
       int height = Math.round(fromDrawable.getHeight() * 0.2f);

       Bitmap inBitmap = Bitmap.createScaledBitmap(fromDrawable, width, height, false);
       Bitmap outBitmap = Bitmap.createBitmap(inBitmap);

       RenderScript renderScript = RenderScript.create(context);
       ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

       Allocation in = Allocation.createFromBitmap(renderScript, inBitmap);
       Allocation out = Allocation.createFromBitmap(renderScript, outBitmap);

       blur.setRadius(21);
       blur.setInput(in);
       blur.forEach(out);

       out.copyTo(outBitmap);
       renderScript.destroy();

       return Bitmap.createBitmap(outBitmap);
   }


   public static void setBackgroundImage(String language, ImageView backgroundImage){

       switch (language) {
           case "en":
               backgroundImage.setImageResource(R.drawable.london);
               break;
           case "ru":
               backgroundImage.setImageResource(R.drawable.moscow);
               break;
           case "de":
               backgroundImage.setImageResource(R.drawable.berlin);
               break;
           case "fr":
               backgroundImage.setImageResource(R.drawable.paris);
               break;
           case "es":
               backgroundImage.setImageResource(R.drawable.madrid);
               break;
           case "it":
               backgroundImage.setImageResource(R.drawable.venice);
               break;
           case "pl":
               backgroundImage.setImageResource(R.drawable.warsaw);
               break;
       }
   }


}
