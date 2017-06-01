package com.example.l.aplikacjamobila;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private WifiManager mainWifi;
    ListAdapter adapter;
    ListView lvWifiDetails;
    List<ScanResult> wifiList;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvWifiDetails = (ListView) findViewById(R.id.lvWifiDetails);
        mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        WifiReceiver receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        unregisterReceiver(receiverWifi);
        scanWifiList();

        button = (Button) findViewById(R.id.button);

        new SendFirstString().execute();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timer t = new Timer();
                t.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        process();
                        Log.d("Odświeżanie: ", DateFormat.getDateTimeInstance().format(new Date()));
                    }
                }, 0, 10000); // czas w milisekundach. 20sek = 20000
            }
        });

        lvWifiDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScanResult selectedWiFi = wifiList.get(position);
            }
        });
    }

    private void process() {

        new Thread(new Runnable() {

            public void run() {

                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void run() {
                        try {
                            DataOutputStream dataOutputStream = new DataOutputStream(SplashScreen.socket.getOutputStream());

                            scanWifiList();
                            String POST = "";

                            for (int i = 0; i < wifiList.size(); i++) {
                                if (Objects.equals(wifiList.get(i).SSID, "ap1") || Objects.equals(wifiList.get(i).SSID, "ap2") || Objects.equals(wifiList.get(i).SSID, "ap3")) {
                                    POST = POST + ":" + adapter.calculateDistanceInCm(wifiList.get(i).level, wifiList.get(1).frequency);
                                }
                            }
                            Log.d("POST: ", POST);
                            dataOutputStream.writeByte(1);
                            dataOutputStream.writeUTF(POST);
                            dataOutputStream.flush();

                        } catch (Exception e) {
                            Log.d("coś sie popsuło:", String.valueOf(e));
                        }
                    }
                });
            }
        }).start();
    }

    private void setAdapter() {
        int index = lvWifiDetails.getFirstVisiblePosition();
        View v = lvWifiDetails.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();
        adapter = new ListAdapter(getApplicationContext(), wifiList);
        lvWifiDetails.setAdapter(adapter);
        lvWifiDetails.setSelectionFromTop(index, top);
    }

    private void scanWifiList() {
        mainWifi.startScan();
        wifiList = mainWifi.getScanResults();
        //Toast.makeText(getApplicationContext(), "Odświeżanie: " + DateFormat.getDateTimeInstance().format(new Date()), Toast.LENGTH_SHORT).show();
        setAdapter();
    }

    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
        }
    }

    private class SendFirstString extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(SplashScreen.socket.getOutputStream());

                scanWifiList();
                String POST = "";

                for (int i = 0; i < wifiList.size(); i++) {
                    if (Objects.equals(wifiList.get(i).SSID, "abae0a") || Objects.equals(wifiList.get(i).SSID, "Orange-6F92") || Objects.equals(wifiList.get(i).SSID, "Marcin")) {
                        POST = POST + ":" + wifiList.get(i).SSID + " " + adapter.calculateDistanceInCm(wifiList.get(i).level, wifiList.get(1).frequency);
                    }
                }
                Log.d("POST: ", POST);
                dataOutputStream.writeByte(1);
                dataOutputStream.writeUTF(POST);
                dataOutputStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}