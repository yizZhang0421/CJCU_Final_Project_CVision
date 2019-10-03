package com.cjcu.ima105.cvision;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import pl.droidsonroids.gif.GifImageView;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SocketAddress sockaddr = new InetSocketAddress(ServerOperate.SERVER_ADDRESS, ServerOperate.SERVER_PORT);
                    Socket sock = new Socket();
                    int timeoutMs = 5000;
                    sock.connect(sockaddr, timeoutMs);
                    sock.close();
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                            .requestIdToken(getString(R.string.google_sign_in_client_id))
                            .requestServerAuthCode(getString(R.string.google_sign_in_client_id))
                            .requestEmail()
                            .build();
                    final GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(Login.this, gso);
                    googleSignInClient.silentSignIn().addOnCompleteListener(Login.this, new OnCompleteListener<GoogleSignInAccount>() {
                        @Override
                        public void onComplete(Task<GoogleSignInAccount> task) {
                            handleSilentSignInResult(task);
                        }
                    }).addOnFailureListener(Login.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Intent signInIntent = googleSignInClient.getSignInIntent();
                            startActivityForResult(signInIntent, 1);
                        }
                    });
                } catch(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GifImageView gif = findViewById(R.id.login_ani);
                            gif.setVisibility(View.GONE);
                            findViewById(R.id.textview_appname).setVisibility(View.GONE);
                            ImageView png = findViewById(R.id.server_fail);
                            png.setVisibility(View.VISIBLE);
                            Toast.makeText(Login.this, "cannot reach CVision server.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        t.start();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            CurrentPosition.photoUri=account.getPhotoUrl().toString();
            CurrentPosition.email=account.getEmail();
            MessageServerResponse get_key_response = ServerOperate.get_key(account.getIdToken(), getString(R.string.google_sign_in_client_id));
            if(get_key_response.status_code==0){
                CurrentPosition.key=get_key_response.other_information;
                Login.this.startActivity(new Intent(Login.this, Home.class));
                Login.this.finish();
            }
            else {
                GifImageView gif = findViewById(R.id.login_ani);
                gif.setVisibility(View.GONE);
                findViewById(R.id.textview_appname).setVisibility(View.GONE);
                ImageView png = findViewById(R.id.server_fail);
                png.setVisibility(View.VISIBLE);
                Toast.makeText(this, get_key_response.message, Toast.LENGTH_SHORT).show();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                        .requestIdToken(getString(R.string.google_sign_in_client_id))
                        .requestServerAuthCode(getString(R.string.google_sign_in_client_id))
                        .requestEmail()
                        .build();
                final GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(Login.this, gso);
                googleSignInClient.signOut();
                googleSignInClient.silentSignIn().addOnCompleteListener(Login.this, new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(Task<GoogleSignInAccount> task) {
                        handleSilentSignInResult(task);
                    }
                }).addOnFailureListener(Login.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Intent signInIntent = googleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, 1);
                    }
                });
            }
        } catch (ApiException e) {
            GifImageView gif = findViewById(R.id.login_ani);
            gif.setVisibility(View.GONE);
            findViewById(R.id.textview_appname).setVisibility(View.GONE);
            ImageView png = findViewById(R.id.server_fail);
            png.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Login Fail", Toast.LENGTH_SHORT).show();
        }
    }
    private void handleSilentSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            CurrentPosition.photoUri=account.getPhotoUrl().toString();
            CurrentPosition.email=account.getEmail();
            MessageServerResponse get_key_response = ServerOperate.get_key(account.getIdToken(), getString(R.string.google_sign_in_client_id));
            if(get_key_response.status_code==0){
                CurrentPosition.key=get_key_response.other_information;
                Login.this.startActivity(new Intent(Login.this, Home.class));
                Login.this.finish();
            }
            else {
                GifImageView gif = findViewById(R.id.login_ani);
                gif.setVisibility(View.GONE);
                ImageView png = findViewById(R.id.server_fail);
                png.setVisibility(View.VISIBLE);
                Toast.makeText(this, get_key_response.message, Toast.LENGTH_SHORT).show();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                        .requestIdToken(getString(R.string.google_sign_in_client_id))
                        .requestServerAuthCode(getString(R.string.google_sign_in_client_id))
                        .requestEmail()
                        .build();
                final GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(Login.this, gso);
                googleSignInClient.signOut();
                googleSignInClient.silentSignIn().addOnCompleteListener(Login.this, new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(Task<GoogleSignInAccount> task) {
                        handleSilentSignInResult(task);
                    }
                }).addOnFailureListener(Login.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Intent signInIntent = googleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, 1);
                    }
                });
            }
        } catch (ApiException e) {
        }
    }
}
