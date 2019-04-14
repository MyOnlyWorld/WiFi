package com.example.chen.myapp;

import android.Manifest;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.chen.myapp.util.FileUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] PERMS_INITIAL={Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    WifiManager wifiManager;
    TextView text;
    Button start, end;
    Spinner spinner;
    List<ScanResult> list;
    Handler handler;
    Runnable runnable;
    String[] files={"wifi01.txt","wifi02.txt","wifi03.txt",
            "wifi04.txt","wifi05.txt", "wifi06.txt",
            "wifi07.txt","wifi08.txt","wifi09.txt"};
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions(PERMS_INITIAL,127);
        init(MainActivity.this);
    }

    private void init(Context context){
        wifiManager= (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                wifiManager.startScan();
                list=wifiManager.getScanResults();
                diaplay(list);
                handler.postDelayed(this,500);
            }
        };
        spinner=findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filename=files[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        text=findViewById(R.id.text);
        start=findViewById(R.id.start);
        start.setEnabled(true);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
                start.setEnabled(false);
                end.setEnabled(true);
            }
        });
        end=findViewById(R.id.end);
        end.setEnabled(false);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end();
                start.setEnabled(true);
                end.setEnabled(false);
            }
        });
    }

    private void diaplay(List list){
        ScanResult result;
        StringBuilder sb=new StringBuilder("WiFi信息\n");
        StringBuilder wifi=new StringBuilder();
        for (int i=0;i<list.size();i++){
            result= (ScanResult) list.get(i);
            sb.append("\nWiFi名称:"+result.SSID+
                    "\nMAC地址:"+result.BSSID+
                    "\nRSSI:"+result.level+"\n");
            wifi.append(result.level+" ");
        }
        wifi.append("\n");
        text.setText(sb.toString());
        FileUtil.saveFile(wifi.toString(),filename);
    }

    public void start(){
        handler.postDelayed(runnable,500);
    }

    public void end(){
        handler.removeCallbacks(runnable);
    }

}
