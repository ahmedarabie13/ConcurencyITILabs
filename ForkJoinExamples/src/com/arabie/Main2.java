package com.arabie;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Main2 {
    public static void main(String [] args){
        long start,end;
        ForkJoinPool fjp = new ForkJoinPool();
        double[] nums = new double[5000];
        for(int i=0; i<nums.length; i++)
            nums[i]= (((i%2) == 0 ? i : -i));
        Sum task = new Sum(nums, 0, nums.length);
        double summation;
        start=System.nanoTime();
        summation=0;
        for(double num:nums){
            summation+=num;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        end =System.nanoTime();
        System.out.println("The Summation = " + summation+"\ntakes sequentially  = "+(end-start));
        start=System.nanoTime();
        summation = fjp.invoke(task);
        end =System.nanoTime();
        System.out.println("The Summation = " + summation+"\ntakes with fork/join = "+(end-start));

    }
}
class Sum extends RecursiveTask<Double> {
    final int seqThreshold = 500;
    double[] data; int start, end;
    Sum(double[] vals, int s, int e){
        data = vals; start = s; end = e;
    }
    @Override
    protected Double compute(){
        double sum = 0;
        if((end - start) < seqThreshold){
            for(int i = start; i < end; i++){
                sum+= data[i];
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else{
            int middle = (end + start) / 2;

            Sum subTaskA = new Sum(data, start, middle);
            Sum subTaskB = new Sum(data, middle, end);

            subTaskA.fork();
            subTaskB.fork();

            sum = subTaskA.join() + subTaskB.join();
        }

        return sum;
    }
}
