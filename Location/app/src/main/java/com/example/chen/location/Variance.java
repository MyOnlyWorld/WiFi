package com.example.chen.location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Variance {

    public double mean(List<double[]> lists,int k){
        int m=lists.size();
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=lists.get(i)[k];
        }
        double dAve=sum/m;//求平均值
        return dAve;
    }

    public double variance(List<double[]> lists,int k) {
        int m=lists.size();
        double dAve=mean(lists,k);//求平均值
        double dVar=0;
        for(int i=0;i<m;i++){//求方差
            double a=lists.get(i)[k];
            dVar+=(a-dAve)*(a-dAve);
        }
        return dVar/m-1;
    }

    public double StandardDiviation(List<double[]> lists,int k) {
        return Math.sqrt(variance(lists,k));
    }

    public List<double[]> gsFilter(List<double[]> lists){
        double[][] a=new double[3][2];
        for (int i=0;i<3;i++){
            a[i]= new double[]{mean(lists, i),
                    StandardDiviation(lists, i),};
        }
        for (int i=0;i<lists.size();i++){
            double[] b=lists.get(i);
            if (b[0]<0.15*a[0][1]+a[0][0]|b[0]>3.09*a[0][1]+a[0][0]|
                    b[1]<0.15*a[1][1]+a[1][0]|b[1]>3.09*a[1][1]+a[1][0]|
                    b[2]<0.15*a[2][1]+a[2][0]|b[2]>3.09*a[2][1]+a[2][0]){
                lists.remove(i);
            }
        }
        return lists;
    }

    //求众数
    public Double getModalNums(double a[]){
        Map<Double,Integer> map=new HashMap<>();
        for (int i=0; i<a.length; i++){
            if (map.containsKey(a[i])){
                int j=map.get(a[i]);
                map.put(a[i], j+1);
            }else {
                map.put(a[i],1);
            }
        }
        double k=0;
        Integer c=0,n;
        for (Map.Entry entry:map.entrySet()){
            n = (Integer) entry.getValue();
            if (c<n){
                c = n;
                k = (double) entry.getKey();
            }
        }
        return k;
    }

}
