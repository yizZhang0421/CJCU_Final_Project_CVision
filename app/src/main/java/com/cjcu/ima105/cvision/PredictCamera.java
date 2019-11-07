package com.cjcu.ima105.cvision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PredictCamera extends AppCompatActivity {
    private LinearLayout control_area;
    private TextView no_predict_model_banner;
    private static boolean is_model_exist;

    public CameraKitView cameraKitView;
    private ImageView capture_button;
    private ImageView crop_mode_button;
    private ImageView choose_model_button;
    private FrameLayout pnlFlash;
    private CropWindowView cropWindowView;
    private View.OnTouchListener onTouchListener;

    private String[] models_id;
    private String[] models_name;
    private String[] models_owner_email;

    private String owner_email;
    private String model;
    private String name;

    public Bitmap bitmap;
    private String result;
    Thread t;

    private PredictCamera predictCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.predict_camera);

        predictCamera=this;

        control_area = findViewById(R.id.control_area);
        no_predict_model_banner = findViewById(R.id.textview_no_predict_model_banner);

        cameraKitView = findViewById(R.id.camera);
        capture_button = findViewById(R.id.capture_button);
        crop_mode_button = findViewById(R.id.crop_mode_button);
        choose_model_button = findViewById(R.id.imageview_choose_predict_model_button);
        cropWindowView = findViewById(R.id.crop_window);
        pnlFlash = findViewById(R.id.pnlFlash);

        final ArrayList<HashMap<String, String>> predict_model_response = ServerOperate.predictable_model_list(CurrentPosition.key);
        models_id = new String[predict_model_response.size()];
        models_name = new String[predict_model_response.size()];
        models_owner_email = new String[predict_model_response.size()];
        for (int i = 0; i < predict_model_response.size(); i++) {
            HashMap<String, String> detail = predict_model_response.get(i);
            models_id[i] = detail.get("id");
            models_name[i] = detail.get("name");
            models_owner_email[i] = detail.get("email");
        }
        if (models_id.length == 0) {
            cameraKitView.setVisibility(View.GONE);
            pnlFlash.setVisibility(View.GONE);
            cropWindowView.setVisibility(View.GONE);
            control_area.setVisibility(View.GONE);
            no_predict_model_banner.setVisibility(View.VISIBLE);
            is_model_exist = false;
            return;
        } else {
            is_model_exist = true;
        }
        owner_email = models_owner_email[0];
        model = models_id[0];
        name = models_name[0];

        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                capture_button.setOnTouchListener(null);
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
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

                        Runnable runable = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String imageStr = Base64.encodeToString(capturedImage, Base64.DEFAULT);
                                    if (cropWindowView.getVisibility() == View.VISIBLE) {
                                        bitmap = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length);
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
                                    else{
                                        bitmap=BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length);
                                    }
                                    final MessageServerResponse predict_response = ServerOperate.predict(CurrentPosition.key, model, imageStr);
                                    result=predict_response.other_information;
                                    if(result==null || result.length()==0){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(PredictCamera.this, predict_response.message, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ResultPage.result_loading.setVisibility(View.GONE);
                                        ResultPage.capture_result.setVisibility(View.VISIBLE);
                                        ResultPage.capture_result.setImageBitmap(bitmap);
                                        ResultPage.predict_result.setText(result);
                                        result="";
                                        bitmap=null;
                                    }
                                });
                            }
                        };
                        t = new Thread(runable);
                        t.start();
                        startActivity(new Intent(PredictCamera.this, ResultPage.class));
                    }
                });
                return true;
            }
        };
//        capture_button.setOnTouchListener(onTouchListener);
        crop_mode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cropWindowView.getVisibility() == View.VISIBLE) {
                    cropWindowView.setVisibility(View.GONE);
                } else {
                    cropWindowView.setVisibility(View.VISIBLE);
                }
            }
        });
        choose_model_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PredictCamera.this);
                View mView = getLayoutInflater().inflate(R.layout.predictable_model_choose_popup, null);

                final TextView current_choose_model = mView.findViewById(R.id.current_choose_model);
                current_choose_model.setText("目前使用模型：\t\t" + name);
                MaterialSpinner spinner = (MaterialSpinner) mView.findViewById(R.id.spinner);
                String[] modelname_email = new String[models_name.length+1];
                modelname_email[0]="選擇模型";
                for(int i=1;i<modelname_email.length;i++){
//                    modelname_email[i]=models_name[i]+"("+models_owner_email[i]+")";
                    modelname_email[i]=models_name[i-1];
                }
                spinner.setItems(modelname_email);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.height += 800;
                dialog.getWindow().setAttributes(lp);
                spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                        position-=1;
                        name = models_name[position];
                        owner_email = models_owner_email[position];
                        model = models_id[position];
                        current_choose_model.setText(models_name[position]);
                        Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
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
        if (is_model_exist) {
            cameraKitView.onStart();
        }
        if(t!=null) {
            t.interrupt();
            t=null;
        }
        if(capture_button!=null) {
            capture_button.setOnTouchListener(onTouchListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (is_model_exist) {
            cameraKitView.onResume();
        }
    }

    @Override
    protected void onPause() {
        if (is_model_exist) {
            cameraKitView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (is_model_exist) {
            cameraKitView.onStop();
        }
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (is_model_exist) {
            cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && findViewById(R.id.home_fragment) == null) {
            PredictCamera.this.startActivity(new Intent(PredictCamera.this, Home.class));
            PredictCamera.this.finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
