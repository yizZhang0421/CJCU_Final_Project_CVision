package com.cjcu.ima105.cvision;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.camerakit.CameraKitView;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class CreateImageCamera extends AppCompatActivity {
    public CameraKitView cameraKitView;
    private FrameLayout pnlFlash;
    private CropWindowView cropWindowView;
    private LinearLayout control_area;
    private ImageView capture_button;
    private View.OnClickListener onTouchListener;
    private ImageView crop_mode_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_image_camera);

        cameraKitView = findViewById(R.id.camera);
        control_area=findViewById(R.id.control_area);
        capture_button=findViewById(R.id.capture_button);
        crop_mode_button=findViewById(R.id.crop_mode_button);
        cropWindowView = findViewById(R.id.crop_window);
        pnlFlash = findViewById(R.id.pnlFlash);

        onTouchListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("asd", "click");
                final View the_touch_view = view;
                the_touch_view.setOnClickListener(null);
                cameraKitView.captureImage(new  CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(final CameraKitView cameraKitView, final byte[] capturedImage) {
                        pnlFlash.setVisibility(View.VISIBLE);
                        AlphaAnimation fade = new AlphaAnimation(1, 0);
                        fade.setDuration(50);
                        fade.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                pnlFlash.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        pnlFlash.startAnimation(fade);

                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    String imageStr = Base64.encodeToString(capturedImage, Base64.DEFAULT);
                                    if(cropWindowView.getVisibility()==View.VISIBLE) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length);
                                        bitmap=cropBitmapCenter(bitmap, cameraKitView.getWidth(), cameraKitView.getHeight(), control_area.getHeight());
                                        Rectangle rectangle = cropWindowView.getCropLocationWithScaleToBitmapLocation(BigDecimal.ONE);
                                        float x = (float)rectangle.x/(float)cropWindowView.getWidth();
                                        float y = (float)rectangle.y/(float)cropWindowView.getHeight();
                                        float width = (float)rectangle.width/(float)cropWindowView.getWidth();
                                        float height = (float)rectangle.height/(float)cropWindowView.getHeight();
                                        bitmap = Bitmap.createBitmap(bitmap,
                                                Math.round((float)bitmap.getWidth()*x),
                                                Math.round((float)bitmap.getHeight()*y),
                                                Math.round((float)bitmap.getWidth()*width),
                                                Math.round((float)bitmap.getHeight()*height));
                                        ByteArrayOutputStream crop_captureImage_byte = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, crop_captureImage_byte);
                                        imageStr = Base64.encodeToString(crop_captureImage_byte.toByteArray(), Base64.DEFAULT);
                                    }

                                    MessageServerResponse write_image_response = ServerOperate.write_image(CurrentPosition.key, CurrentPosition.label_id, imageStr);

                                    the_touch_view.setOnClickListener(onTouchListener);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        t.start();
                    }
                });
            }
        };
        capture_button.setOnClickListener(onTouchListener);
        crop_mode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cropWindowView.getVisibility()==View.VISIBLE){
                    cropWindowView.setVisibility(View.GONE);
                }
                else{
                    cropWindowView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private Bitmap cropBitmapCenter(Bitmap bitmap, int viewWidth, int viewHeight, int buttonViewHeight){
        Bitmap croppedBitmap = null;

        int bitmapWidth     = bitmap.getWidth();
        int bitmapHeight    = bitmap.getHeight();
        if(bitmapWidth>viewWidth && bitmapHeight>viewHeight){
            int newX = Math.round((float) bitmapWidth / (float) 2) - Math.round((float) viewWidth / (float) 2);
            int newY = Math.round((float) bitmapHeight / (float) 2) - Math.round((float) viewHeight / (float) 2);
            croppedBitmap = Bitmap.createBitmap(bitmap, newX, newY, viewWidth, viewHeight-buttonViewHeight);
        }
        else {
            BigDecimal scale = new BigDecimal(bitmapWidth).divide(new BigDecimal(viewWidth), 10, RoundingMode.HALF_UP);
            int suitViewWidth = new BigDecimal(viewWidth).multiply(scale).intValue();
            int suitViewHeight = new BigDecimal(viewHeight).multiply(scale).intValue();
            int suitButtonViewHeight = new BigDecimal(buttonViewHeight).multiply(scale).intValue();
            if (suitViewHeight > bitmapHeight) {
                scale = new BigDecimal(bitmapHeight).divide(new BigDecimal(viewHeight), 10, RoundingMode.HALF_UP);
                suitViewWidth = new BigDecimal(viewWidth).multiply(scale).intValue();
                suitViewHeight = new BigDecimal(viewHeight).multiply(scale).intValue();
                suitButtonViewHeight = new BigDecimal(buttonViewHeight).multiply(scale).intValue();
                int newX = Math.round((float) bitmapWidth / (float) 2) - Math.round((float) suitViewWidth / (float) 2);
                croppedBitmap = Bitmap.createBitmap(bitmap, newX, 0, suitViewWidth, suitViewHeight - suitButtonViewHeight);
            } else {
                int newY = Math.round((float) bitmapHeight / (float) 2) - Math.round((float) suitViewHeight / (float) 2);
                croppedBitmap = Bitmap.createBitmap(bitmap, 0, newY, suitViewWidth, suitViewHeight - suitButtonViewHeight);
            }
        }
        return croppedBitmap;
    }








    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
