package com.example.l.aplikacjamobila;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        final TextView tv = (TextView) findViewById(R.id.textView);


        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                getWifiConfiguration(getApplicationContext());
            }
        });

        registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                int state = wifi.getWifiState();
                if (state == WifiManager.WIFI_STATE_ENABLED) {
                    List<ScanResult> results = wifi.getScanResults();

                    for (ScanResult result : results) {
                        if (result.BSSID.equals(wifi.getConnectionInfo().getBSSID())) {
                            int level = WifiManager.calculateSignalLevel(wifi.getConnectionInfo().getRssi(),
                                    result.level);
                            int difference = level * 100 / result.level;
                            int signalStrangth = 0;
                            if (difference >= 100)
                                signalStrangth = 4;
                            else if (difference >= 75)
                                signalStrangth = 3;
                            else if (difference >= 50)
                                signalStrangth = 2;
                            else if (difference >= 25)
                                signalStrangth = 1;
                            tv.setText(tv.getText() + "\nDifference : " + difference + " signal state:" + signalStrangth);

                        }

                    }
                }
            }
        }, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
    }

    // https://developer.android.com/reference/android/net/wifi/WifiManager.html <- Dokumentacja WifiMenager, compareSignalLevel(int rssiA, int rssib)

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getWifiConfiguration(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int numberOfLevels = 5;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
        Log.d("-------------------------", "");
        Log.d("Moc po przeliczeniu: ", String.valueOf(level));
        Log.d("RSSI - wskaźnik mocy: ", String.valueOf(wifiInfo.getRssi()));
        Log.d("Częstotliwość: ", String.valueOf(wifiInfo.getFrequency()));
        Log.d("BSSID: ", wifiInfo.getBSSID());
        Log.d("Mac Address: ", wifiInfo.getMacAddress());
        Log.d("Szybkość łącza: ", String.valueOf(wifiInfo.getLinkSpeed()));
        Log.d("SSID: ", wifiInfo.getSSID());
    }
}
