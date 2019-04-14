package com.example.chen.myapp.util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtil {

    static String filepath= Environment.getExternalStorageDirectory().getAbsoluteFile()+"/360/wifi/";

    public static void saveFile(String str, String filename){
        try {
            File file = new File(filepath,filename);
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream os=new FileOutputStream(file,true);
            os.write(str.getBytes());
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
