package com.jay.translator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

import java.util.logging.Handler;

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


   public static void startArrowAnimation(final Context context, final ImageView imageView){

       VectorChildFinder vector = new VectorChildFinder(context, R.drawable.ic_action_next, imageView);

       final VectorDrawableCompat.VFullPath path1 = vector.findPathByName("path1");
       path1.setFillColor(context.getResources().getColor(R.color.colorPrimaryDark));

       final VectorDrawableCompat.VFullPath path2 = vector.findPathByName("path2");
       path2.setFillColor(context.getResources().getColor(R.color.colorPrimaryDark));

       final VectorDrawableCompat.VFullPath path3 = vector.findPathByName("path3");
       path3.setFillColor(context.getResources().getColor(R.color.colorPrimaryDark));


       new android.os.Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               path1.setFillColor(context.getResources().getColor(R.color.colorAccent));
               path3.setFillColor(context.getResources().getColor(R.color.colorPrimaryDark));
               imageView.invalidate();

           }
       },300);

       new android.os.Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               path2.setFillColor(context.getResources().getColor(R.color.colorAccent));
               path1.setFillColor(context.getResources().getColor(R.color.colorPrimaryDark));
               imageView.invalidate();
           }
       },600);

       new android.os.Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               path3.setFillColor(context.getResources().getColor(R.color.colorAccent));
               path2.setFillColor(context.getResources().getColor(R.color.colorPrimaryDark));
               imageView.invalidate();
           }
       },900);


       new android.os.Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               startArrowAnimation(context,imageView);
           }
       },1200);


   }

}
