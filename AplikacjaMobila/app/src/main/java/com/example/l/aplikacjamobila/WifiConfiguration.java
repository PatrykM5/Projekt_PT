package com.example.l.aplikacjamobila;

import android.annotation.SuppressLint;
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

public class WifiConfiguration extends AppCompatActivity {

    WifiManager wifiManager;
    WifiInfo wifiInfo;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WifiManagerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonSend = (Button) findViewById(R.id.button);
        TextView textViewSSID = (TextView) findViewById(R.id.tvSSID);
        TextView textViewFrequency = (TextView) findViewById(R.id.tvFrequency);
        TextView textViewMacAddress = (TextView) findViewById(R.id.tvMacAddress);
        TextView textViewBSSID = (TextView) findViewById(R.id.tvBSSID);

        wifiManager = (WifiManager) getSystemService(getApplicationContext().WIFI_SERVICE);

        wifiInfo = wifiManager.getConnectionInfo();

        textViewSSID.setText(getSSID(this));
        textViewFrequency.setText(getFrequency(getApplicationContext()));
        textViewMacAddress.setText(getMacAddress(getApplicationContext()));
        textViewBSSID.setText(getBSSID(this));

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //getWifiConfiguration(getApplicationContext());
            }
        });
    }

    // https://developer.android.com/reference/android/net/wifi/WifiManager.html <- Dokumentacja WifiMenager, compareSignalLevel(int rssiA, int rssib)

    String getSSID(Context context) {
        return wifiInfo.getSSID();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    String getFrequency(Context context) {
        return "" + wifiInfo.getFrequency();
    }

    String getMacAddress(Context context) {
        return wifiInfo.getMacAddress();
    }

    String getBSSID(Context context) {
        return wifiInfo.getBSSID();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getWifiConfiguration(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int numberOfLevels = 5;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
        Log.d("Moc po przeliczeniu: ", String.valueOf(level));
        Log.d("RSSI - wskaźnik mocy: ", String.valueOf(wifiInfo.getRssi()));
        Log.d("Częstotliwość: ", String.valueOf(wifiInfo.getFrequency()));
        Log.d("BSSID: ", wifiInfo.getBSSID());
        Log.d("Mac Address: ", wifiInfo.getMacAddress());
        Log.d("Szybkość łącza: ", String.valueOf(wifiInfo.getLinkSpeed()));
        Log.d("SSID: ", wifiInfo.getSSID());
    }
}
