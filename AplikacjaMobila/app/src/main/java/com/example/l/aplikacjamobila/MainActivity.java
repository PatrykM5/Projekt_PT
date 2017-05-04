package com.example.l.aplikacjamobila;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private WifiManager mainWifi;
    ListAdapter adapter;
    ListView lvWifiDetails;
    List<ScanResult> wifiList;
    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvWifiDetails = (ListView) findViewById(R.id.lvWifiDetails);
        mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiReceiver receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        scanWifiList();
        lvWifiDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScanResult selectedWiFi = wifiList.get(position);
            }
        });

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                process();
                Log.d("Odświeżanie: ", DateFormat.getDateTimeInstance().format(new Date()));
            }
        }, 0, 10000); // czas w milisekundach. 20sek = 20000
    }

    private void process() {

        new Thread(new Runnable() {

            public void run() {
                try {
                    socket = new Socket(AppConfig.SERVER_IP, AppConfig.SERVER_PORT);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Brak połączenia z serwerem.", Toast.LENGTH_SHORT).show();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {

                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                            scanWifiList();
                            String POST = wifiList.get(0).SSID + " " + adapter.calculateDistanceInCm(wifiList.get(0).level, wifiList.get(0).frequency) + " "
                                    + wifiList.get(1).SSID + " " + adapter.calculateDistanceInCm(wifiList.get(1).level, wifiList.get(1).frequency) + " "
                                    + wifiList.get(2).SSID + " " + adapter.calculateDistanceInCm(wifiList.get(2).level, wifiList.get(2).frequency);
                            Log.d("POST: ", POST);

                            dataOutputStream.writeByte(1);
                            dataOutputStream.writeUTF(POST);

                        } catch (Exception e) {

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

    private boolean checkInternetConnection() {
        getBaseContext();
        ConnectivityManager connect = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (connect.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            new AlertDialog.Builder(this)
                    .setTitle("Ups... coś poszło nie tak")
                    .setMessage("Wysyłanie danych nie powiodło się.\nSprawdź połączenie z Internetem.")
                    .setPositiveButton("OK", null)
                    .show();
            return false;
        }
        return false;
    }
}