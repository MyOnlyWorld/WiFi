package com.example.chen.location;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public void writeTxtFile(String context, String filePath){
        BufferedWriter bw=null;
        File file=new File(filePath);
        try {
            if (!file.exists()){
                file.createNewFile();
            }
            FileWriter fw=new FileWriter(file.getAbsoluteFile(),true);
            bw=new BufferedWriter(fw);
            bw.write(context);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List readLayer(String filePath){
        List<Double> list=new ArrayList();
        String encoding="UTF-8";
        File file=new File(filePath);
        if (file.isFile() && file.exists()){
            try {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt=null;
                while((lineTxt=bufferedReader.readLine())!=null){
                    double a=Double.parseDouble(lineTxt.trim());
                    list.add(a);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}
