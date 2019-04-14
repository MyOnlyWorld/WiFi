package com.example.chen.location;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] PERMS={Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    ImageView image;
    Button init,location;
    WifiManager wifiManager;
    List<ScanResult> list;
    Handler handler;
    Runnable runnable,runnable1;
    BpDeep bp;
    List lists;
    Variance v;
    Type t;
    boolean flag,loc,scan;
    Canvas canvas;
    Paint paint;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions(PERMS,127);
        init(MainActivity.this);
        beginLocate();
    }

    private void beginLocate(){
        runnable1=new Runnable() {
            @Override
            public void run() {
                locate();
                handler.postDelayed(runnable1,1000*10);
            }
        };
        handler.postDelayed(runnable1,1000*10);
    }

    private void initView(){
        image=findViewById(R.id.image);
        init=findViewById(R.id.init);
        init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBP();
            }
        });
        location=findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t= (String) location.getText();
                if (t.equals("定位")){
                    start();
                    drawInfo("开始定位");
                    location.setText("暂停");
                }else{
                    end();
                    drawInfo("暂停定位");
                    location.setText("定位");
                }
            }
        });
        drawLocation(9);
    }

    private void init(Context context){
        wifiManager= (WifiManager) context
                .getSystemService(WIFI_SERVICE);
        lists=new ArrayList<>();
        v=new Variance();
        t=new Type();
        flag=false;
        loc=false;
        scan=false;
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                if (loc==false){
//                    scan=true;
                    wifiManager.startScan();
                    list=wifiManager.getScanResults();
                    lists.add(list);
//                    scan=false;
                }
                handler.postDelayed(this,500);
            }
        };
        initView();
        initBP();
    }

    private void initBP(){
        bp = new BpDeep(new int[]{3,5,3}, 0.15, 0.8);
        List<String> layer=Arrays.asList(getResources().getStringArray(R.array.layer));
        List<String> layer_weight=Arrays.asList(getResources().getStringArray(R.array.layer_weight));
        bp.load(layer,layer_weight);
        drawLocation(9);
        Toast.makeText(MainActivity.this,
                "初始化成功！",Toast.LENGTH_SHORT).show();
    }

    private void locate(){
        loc=true;
//        drawInfo(lists.size()+"");
        if (lists.size()>10){
            //取出RSSI
            List<double[]> RSSIList=new ArrayList<>(lists.size());
            for (int n=0;n<lists.size();n++){
                List<ScanResult> results= (List<ScanResult>) lists.get(n);
                double[] a=new double[results.size()];
                for (int i=0;i<results.size();i++){
                    a[i]=(results.get(i)).level;
                }
                RSSIList.add(a);
            }
            //清空数据，重新采集
            lists.clear();
            RSSIList=t.getArray(RSSIList);//取出最大的三个RSSI；
            RSSIList=v.gsFilter(RSSIList);//高斯过滤
            double[][] tar=new double[RSSIList.size()][];//结果数组
            //计算结果
            for (int i=0;i<RSSIList.size();i++) {
                double[] in=RSSIList.get(i);
                tar[i]=bp.computeOut(in);
            }
            //结果反归一化
            double[] d=new double[tar.length];
            for(int j=0;j<tar.length;j++){
                d[j]=4*tar[j][0]+2*tar[j][1]+tar[j][2];
            }
            //取出现最多的结果
            double result=v.getModalNums(d);
            //在屏幕上画出位置
            drawLocation((int) result);
        }else{
//            drawLocation(1);
//            drawInfo("对不起，无法定位！");
        }
        loc=false;
    }

    public void start(){
        if (flag==false){
            handler.postDelayed(runnable,500);
            flag=true;
        }else {
            Toast.makeText(MainActivity.this,
                    "您已开启定位",Toast.LENGTH_SHORT).show();
        }
    }

    public void end(){
        if (flag==true){
            handler.removeCallbacks(runnable);
            flag=false;
        }else {
            Toast.makeText(MainActivity.this,
                    "您已关闭定位",Toast.LENGTH_SHORT).show();
        }
    }

    public void drawLocation(int k){
        bitmap=Bitmap.createBitmap(500, 800,
                Bitmap.Config.ARGB_8888);
        canvas=new Canvas(bitmap);
        paint=new Paint();
        image.setImageBitmap(bitmap);
        for (int i=0; i<4; i++){
            canvas.drawLine(100,100+100*i,400,100+100*i,paint);
            canvas.drawLine(100+100*i,100,100+100*i,400,paint);
        }
        paint.setColor(Color.RED);
        switch (k){
            case 0:
                canvas.drawCircle(100,400,20,paint);
                break;
            case 1:
                canvas.drawCircle(250,400,20,paint);
                break;
            case 2:
                canvas.drawCircle(400,400,20,paint);
                break;
            case 3:
                canvas.drawCircle(100,250,20,paint);
                break;
            case 4:
                canvas.drawCircle(250,250,20,paint);
                break;
            case 5:
                canvas.drawCircle(400,250,20,paint);
                break;
            case 6:
                canvas.drawCircle(100,100,20,paint);
                break;
            case 7:
                canvas.drawCircle(250,100,20,paint);
                break;
            case 8:
                canvas.drawCircle(400,100,20,paint);
                break;
            default:
                canvas.drawCircle(250,500,20,paint);
                break;

        }

        paint.setColor(Color.GRAY);
        paint.setTextSize(50);
        canvas.drawText("您的位置是"+k+"号点",60,600,paint);
    }

    public void drawInfo(String content){
        Toast.makeText(MainActivity.this,content,Toast.LENGTH_SHORT).show();
    }

}