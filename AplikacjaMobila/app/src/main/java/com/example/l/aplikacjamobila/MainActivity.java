package com.example.l.aplikacjamobila;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
        }, 0, 20000);
    }

    private void process() {

        new Thread(new Runnable() {

            public void run() {
                try {

                } catch (Exception e) {

                }
                runOnUiThread(new Runnable() {

                    public void run() {
                        try {
                            //your list fill hear
                            adapter.notifyDataSetChanged();
                            scanWifiList();
                        } catch (Exception e) {

                        }
                    }
                });
            }

        }).start();
    }

    private void setAdapter() {
        adapter = new ListAdapter(getApplicationContext(), wifiList);
        lvWifiDetails.setAdapter(adapter);
    }

    private void scanWifiList() {
        mainWifi.startScan();
        wifiList = mainWifi.getScanResults();
        Toast.makeText(getApplicationContext(), "Odświeżanie: " + DateFormat.getDateTimeInstance().format(new Date()), Toast.LENGTH_SHORT).show();

        setAdapter();
    }

    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
        }
    }
}