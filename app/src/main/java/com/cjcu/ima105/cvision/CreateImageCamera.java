package com.cjcu.ima105.cvision;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
    private ImageView capture_button;
    private ImageView crop_mode_button;
    private FrameLayout pnlFlash;
    private View.OnTouchListener onTouchListener;
    private CropWindowView cropWindowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_image_camera);

        cameraKitView = findViewById(R.id.camera);
        capture_button=findViewById(R.id.capture_button);
        crop_mode_button=findViewById(R.id.crop_mode_button);
        cropWindowView = findViewById(R.id.crop_window);
        pnlFlash = findViewById(R.id.pnlFlash);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screen_width = size.x;
        cameraKitView.setLayoutParams(new RelativeLayout.LayoutParams(screen_width, (int)((4*1.f)*((screen_width*1.f)/(3*1.f)))));
        cropWindowView.setLayoutParams(new RelativeLayout.LayoutParams(screen_width, (int)((4*1.f)*((screen_width*1.f)/(3*1.f)))));

        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final View the_touch_view = view;
                the_touch_view.setOnTouchListener(null);
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
                                        BigDecimal bitmap_width = new BigDecimal(bitmap.getWidth() + "");
                                        BigDecimal camera_view_width = new BigDecimal(cameraKitView.getWidth() + "");
                                        BigDecimal scale = bitmap_width.divide(camera_view_width, 10, RoundingMode.HALF_UP);
                                        Rectangle rectangle = cropWindowView.getCropLocationWithScaleToBitmapLocation(scale);
                                        bitmap = Bitmap.createBitmap(bitmap, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                                        ByteArrayOutputStream crop_captureImage_byte = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, crop_captureImage_byte);
                                        imageStr = Base64.encodeToString(crop_captureImage_byte.toByteArray(), Base64.DEFAULT);
                                    }

                                    MessageServerResponse write_image_response = ServerOperate.write_image(CurrentPosition.key, CurrentPosition.label_id, imageStr);

                                    the_touch_view.setOnTouchListener(onTouchListener);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        t.start();
                    }
                });
                return true;
            }
        };
        capture_button.setOnTouchListener(onTouchListener);
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
    private String map2json_dictionary(HashMap<String,String> map){
        String result="{";
        for(String key : map.keySet()){
            result+="\""+key+"\":\""+map.get(key)+"\",";
        }
        result=result.substring(0,result.length()-1);
        result+="}";
        return result;
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
