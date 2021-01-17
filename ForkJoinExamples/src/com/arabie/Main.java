package com.arabie;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
public class Main {
    public static void main(String [] args){
        long start,end;
        ForkJoinPool fjp = new ForkJoinPool();
        double[] nums = new double[100000];
        for(int i=0; i<nums.length; i++)
            nums[i] = i;

        System.out.println("A portion of the original sequence");
        for(int i=0; i<10; i++)
            System.out.print(nums[i]+" ");

        System.out.println("\n");

        SqrtTransform task = new SqrtTransform(nums, 0, nums.length);
        start=System.currentTimeMillis();
        //fjp.invoke(task);
        task.invoke();
        end=System.currentTimeMillis();
        System.out.println("USing Fork/Join takes:= "+(end-start));
        System.out.println("A portion of the transformed sequence" + " (to four decimal places): ");
        for(int i=0; i<10; i++)
            System.out.format("%.4f ", nums[i]);
        System.out.println();
        ///////////////////////////////////////////
        for(int i=0; i<nums.length; i++)
            nums[i] = i;
        start=System.currentTimeMillis();
        for(int i = 0; i < nums.length; i++){
            nums[i] = Math.sqrt(nums[i]);
        }
        end=System.currentTimeMillis();
        System.out.println("USing sequential takes:= "+(end-start));
        for(int i=0; i<10; i++)
            System.out.format("%.4f ", nums[i]);
        System.out.println();
    }
}
class SqrtTransform extends RecursiveAction {
    final int seqThreshold = 1000;
    double[] data; int start, end;
    SqrtTransform(double[] vals, int s, int e){
        data = vals; start = s; end = e;
    }
    protected void compute(){
        if((end - start) < seqThreshold){
            for(int i = start; i < end; i++){
                data[i] = Math.sqrt(data[i]);
            }
        }else{
            int middle = (end + start) / 2;
            invokeAll(new SqrtTransform(data, start, middle),
                    new SqrtTransform(data , middle, end));
        }
    }
}