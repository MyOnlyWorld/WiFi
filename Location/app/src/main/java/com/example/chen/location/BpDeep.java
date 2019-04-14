package com.example.chen.location;

import android.content.Context;
import android.os.Environment;

import java.util.List;
import java.util.Random;

public class BpDeep {
	public double[][] layer;//神经网络各层节点
	public double[][] layerErr;//神经网络各节点误差
	public double[][][] layer_weight;//各层节点权重
	public double[][][] layer_weight_delta;//各层节点权重动量
	public double mobp;//动量系数
    public double rate;//学习系数
    public FileUtil f=null;//文件操作

    public BpDeep(int[] layernum, double rate, double mobp){
        this.mobp = mobp;
        this.rate = rate;
        layer = new double[layernum.length][];
        layerErr = new double[layernum.length][];
        layer_weight = new double[layernum.length][][];
        layer_weight_delta = new double[layernum.length][][];
        Random random = new Random();
        for(int l=0;l<layernum.length;l++){
            layer[l]=new double[layernum[l]];
            layerErr[l]=new double[layernum[l]];
            if(l+1<layernum.length){
                layer_weight[l]=new double[layernum[l]+1][layernum[l+1]];
                layer_weight_delta[l]=new double[layernum[l]+1][layernum[l+1]];
                for(int j=0;j<layernum[l]+1;j++)
                    for(int i=0;i<layernum[l+1];i++)
                        layer_weight[l][j][i]=random.nextDouble();//随机初始化权重
            }   
        }
        f=new FileUtil();
    }
    
  //逐层向前计算输出
    public double[] computeOut(double[] in){
        for(int l=1;l<layer.length;l++){
            for(int j=0;j<layer[l].length;j++){
                double z=layer_weight[l-1][layer[l-1].length][j];
                for(int i=0;i<layer[l-1].length;i++){
                    layer[l-1][i]=l==1?in[i]:layer[l-1][i];
                    z+=layer_weight[l-1][i][j]*layer[l-1][i];
                }
                layer[l][j]=1/(1+Math.exp(-z));
            }
        }
        return layer[layer.length-1];
    }
    
  //逐层反向计算误差并修改权重
    public void updateWeight(double[] tar){
        int l=layer.length-1;
        for(int j=0;j<layerErr[l].length;j++)
            layerErr[l][j]=layer[l][j]*(1-layer[l][j])*(tar[j]-layer[l][j]);
 
        while(l-->0){
            for(int j=0;j<layerErr[l].length;j++){
                double z = 0.0;
                for(int i=0;i<layerErr[l+1].length;i++){
                    z=z+l>0?layerErr[l+1][i]*layer_weight[l][j][i]:0;
                    layer_weight_delta[l][j][i]= mobp*layer_weight_delta[l][j][i]+rate*layerErr[l+1][i]*layer[l][j];//隐含层动量调整
                    layer_weight[l][j][i]+=layer_weight_delta[l][j][i];//隐含层权重调整
                    if(j==layerErr[l].length-1){
                        layer_weight_delta[l][j+1][i]= mobp*layer_weight_delta[l][j+1][i]+rate*layerErr[l+1][i];//截距动量调整
                        layer_weight[l][j+1][i]+=layer_weight_delta[l][j+1][i];//截距权重调整
                    }
                }
                layerErr[l][j]=z*layer[l][j]*(1-layer[l][j]);//记录误差
            }
        }
    }
    
    public void train(double[] in, double[] tar){
        computeOut(in);
        updateWeight(tar);
    }

    //打印layer和layer_weight
    public void printLayer(){
        System.out.println(layer.length);
        System.out.println(layer_weight.length);
        for (int i=0;i<layer.length;i++){
            for (int j=0;j<layer[i].length;j++){
                System.out.print(1+" ");
            }
            System.out.println();
        }
        System.out.println();
        for (int i=0;i<layer_weight.length-1;i++){
            for (int j=0;j<layer_weight[i].length;j++){
                for (int k=0;k<layer_weight[i][j].length;k++){
                    System.out.print(1+" ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    //保存神经元
    public void save(String[] paths){
        for (int i=0;i<layer.length;i++){
            for (int j=0;j<layer[i].length;j++){
                f.writeTxtFile(layer[i][j]+"\n",paths[0]);
            }
        }
        for (int i=0;i<layer_weight.length-1;i++){
            int a=layer_weight[i].length;
            for (int j=0;j<layer_weight[i].length;j++){
                for (int k=0;k<layer_weight[i][j].length;k++){
                    f.writeTxtFile(layer_weight[i][j][k]+"\n",paths[1]);
                }
            }
        }
    }

    //加载神经元
    public void load(List<String> layerList,List<String> layer_weightList){
        int count=0;
        for (int i=0;i<layer.length;i++){
            for (int j=0;j<layer[i].length;j++){
                layer[i][j]= Double.parseDouble(layerList.get(count));
                count++;
            }
        }

        count=0;
        for (int i=0;i<layer_weight.length-1;i++){
            for (int j=0;j<layer_weight[i].length;j++){
                for (int k=0;k<layer_weight[i][j].length;k++){
                    layer_weight[i][j][k]= Double.parseDouble(layer_weightList.get(count));
                    count++;
                }
            }
        }
    }

}
