package com.example.chen.location;

import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.List;

public class Type {

    public double[] chStr2Int(String[] str){
        int n=str.length;
        double[] a=new double[n];
        for (int i=0;i<n;i++){
            a[i]=Double.parseDouble(str[i]);
        }
        return a;
    }

    public double[] sort(double[] array){
        int len=array.length;
        for (int i=0;i<len-1;i++){
            for (int j=0;j<len-1-i;j++){
                if (array[j]<array[j+1]){
                    double temp=array[j];
                    array[j]=array[j+1];
                    array[j+1]=temp;
                }
            }
        }
        return array;
    }

    public double[] getArray(String[] str){
        double[] a=chStr2Int(str);
        a=sort(a);
        double[] b={a[0],a[1],a[2]};
        return b;
    }

    public List getArray(List<double[]> list){
        List list1=new ArrayList();
        double[] b=new double[]{0,0,0};
        for (double[] a:list){
            a=sort(a);
            if (a.length>=3){
                b[0]=a[0];
                b[1]=a[1];
                b[2]=a[2];
            }else if (a.length==2){
                b[0]=a[0];
                b[1]=a[1];
                b[2]=0;
            }else if (a.length==1){
                b[0]=a[0];
                b[1]=0;
                b[2]=0;
            }else if (a.length<=0){
                b[0]=0;
                b[1]=0;
                b[2]=0;
            }
            list1.add(b);
        }
        return list1;
    }

}
