package com.example.chen.location;

import java.util.Arrays;

/**
 * Created by chen on 19-4-1.
 */

public class Test {

    public static void main(String[] args){
        BpDeep bp = new BpDeep(new int[]{2,10,2}, 0.15, 0.8);
        //设置样本数据，对应上面的4个二维坐标数据
        double[][] data = new double[][]{{1,2},{2,2},{1,1},{2,1}};
        //设置目标数据，对应4个坐标数据的分类
        double[][] target = new double[][]{{1,0},{0,1},{0,1},{1,0}};

//        bp.load();

        for(int j=0;j<data.length;j++){
            double[] result = bp.computeOut(data[j]);
            for(int i=0;i<result.length;i++){
                result[i]=Math.round(result[i]);
            }
            System.out.println(Arrays.toString(data[j])+":"+Arrays.toString(result));
        }

        //根据训练结果来预测一条新数据的分类
        double[] x = new double[]{3,1};
        double[] result = bp.computeOut(x);
        for(int i=0;i<result.length;i++){
            result[i]=Math.round(result[i]);
        }
        System.out.println(Arrays.toString(x)+":"+Arrays.toString(result));
    }

}
