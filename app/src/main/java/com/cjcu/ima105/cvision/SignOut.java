package com.cjcu.ima105.cvision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class SignOut extends AppCompatActivity {
    public static Home home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_out);

        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        getWindow().setEnterTransition(explode);

        Toolbar toolbar = new Toolbar(this);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        LinearLayout.LayoutParams toolBarParams = new LinearLayout.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT,height/4);
        toolBarParams.gravity=Gravity.CENTER;
        toolbar.setLayoutParams(toolBarParams);
        toolbar.setBackground(getDrawable(R.drawable.homepage_action_bar));
        LinearLayout toolbar_content = (LinearLayout) getLayoutInflater().inflate(R.layout.home_page_toolbar,null);
        for(int i=0;i<toolbar_content.getChildCount();i++) {
            View child = toolbar_content.getChildAt(i);
            if(child.getId()==R.id.googleSignOutPage){
                child.setVisibility(View.INVISIBLE);
                break;
            }
        }
        Toolbar.LayoutParams contentLayout=new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        contentLayout.gravity=Gravity.CENTER;
        toolbar_content.setLayoutParams(contentLayout);
        toolbar.addView(toolbar_content);
        final RelativeLayout toolbar_contenter = findViewById(R.id.home_page_toolbar);
        toolbar_contenter.addView(toolbar);
        final ImageView userPhoto = findViewById(R.id.googleUserPhoto);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URLConnection conn = new URL(CurrentPosition.photoUri).openConnection();
                    conn.connect();
                    InputStream isCover = conn.getInputStream();
                    Bitmap bmpCover = BitmapFactory.decodeStream(isCover);
                    isCover.close();
                    userPhoto.setImageBitmap(bmpCover);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TextView userEmail = findViewById(R.id.googleUserEmail);
        userEmail.setText(CurrentPosition.email);
        SignInButton googleSignOutButton = findViewById(R.id.googleSignOutButton);
        for (int i = 0; i < googleSignOutButton.getChildCount(); i++) {
            View v = googleSignOutButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText("Sign out");
                break;
            }
        }
        googleSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.google_sign_in_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(SignOut.this, gso);
                googleSignInClient.signOut().addOnCompleteListener(SignOut.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SignOut.this.startActivity(new Intent(SignOut.this, Login.class));
                        SignOut.this.finish();
                        home.finish();
                    }
                });
            }
        });
    }
}
