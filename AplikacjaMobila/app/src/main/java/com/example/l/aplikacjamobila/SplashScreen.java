package com.example.l.aplikacjamobila;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class SplashScreen extends AppCompatActivity {

    static Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new ConnectToWifiTrack().execute();
    }

    private class ConnectToWifiTrack extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String s;
            try {
                socket = new Socket(AppConfig.SERVER_IP, AppConfig.SERVER_PORT);
                s = "t";
            } catch (IOException e) {
                s = "f";
            }
            return s;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String s) {
            if (Objects.equals(s, "t")) {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
            } else if (Objects.equals(s, "f")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                builder
                        .setTitle("ERROR")
                        .setMessage("Brak połączenia z serwerem.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        }
    }
}
