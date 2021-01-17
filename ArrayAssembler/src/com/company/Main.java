package com.company;


public class Main {
    Thread thread1;
    Thread thread2;
    Thread thread3;

    Thread thrd1;
    Thread thrd2;
    Thread thrd3;

    int[] totalArr1 =new int[0];
    int[] totalArr2=new int[20];

    int[] arr1={0,1,2,3,4,5,6,7,8,9};
    int[] arr2= {10,11,12,13};
    int[] arr3 = {14,15,16,17,18,19};
    int arrIndx=0;
    public Main() {

        thread1=new Thread(this::assembler1);
        thread2=new Thread(this::assembler2);
        thread3=new Thread(this::assembler3);
        thread1.start();
        thread2.start();
        thread3.start();


        thrd1=new Thread(this::task1);
        thrd2=new Thread(this::task2);
        thrd3=new Thread(this::task3);
        thrd1.start();
        thrd2.start();
        thrd3.start();

    }
    public static void main(String[] args) {

        Main m=new Main();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("First Approach using Copying");
        for(int num : m.totalArr1){
            System.out.print(num+"\t");
        }
        System.out.println("\nSecond Approach using index counter");
        for(int num : m.totalArr2){
            System.out.print(num+"\t");
        }
        //System.out.println(m.totalArr.length);
    }

    public  void assembler1(){
        synchronized (totalArr1) {
            genericAssembler(arr1);
        }

//        synchronized (totalArr){
//            if(totalArr==null){
//                totalArr=new int[arr1.length];
//                for(int i=0;i<arr1.length;i++){
//                    totalArr[i]=arr1[i];
//                }
//            }else{
//                int[] temp=new int[arr1.length+totalArr.length];
//                for(int i=0;i<arr1.length+totalArr.length;i++){
//                    if(i< totalArr.length){
//                        temp[i]=totalArr[i];
//                    }
//                    else {
//                        temp[i]=arr1[i- totalArr.length];
//                    }
//                }
//                totalArr=temp;
//            }
//
//        }
}
    public  void assembler2(){
        synchronized (totalArr1) {
            genericAssembler(arr2);
        }
    }
    public void assembler3(){
        synchronized (totalArr1){
            genericAssembler(arr3);
        }
    }
    private  void genericAssembler(int[] arr){

            int[] tempArr=new int[arr.length+ totalArr1.length];
            for(int i = 0; i<arr.length+ totalArr1.length; i++){
                if(i< totalArr1.length){
                    tempArr[i]= totalArr1[i];
                }
                else {
                    tempArr[i]=arr[i- totalArr1.length];
                }
            }
            totalArr1 =tempArr;


    }
    public void task1(){
        synchronized (totalArr2){

            commonTask(arr1);
        }
    }
    public void task2(){
        synchronized (totalArr2){

            commonTask(arr2);
        }
    }
    public void task3(){
        synchronized (totalArr2){

            commonTask(arr3);
        }
    }
    private void commonTask(int[] arr){
        for(int i=0;i<arr.length;i++,arrIndx++){
            totalArr2[arrIndx]=arr[i];
        }
    }
}
