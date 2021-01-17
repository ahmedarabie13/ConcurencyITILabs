package com.company;

public class SimpleThreads {

    static  void threadMessage(String message){
        String threadName=Thread.currentThread().getName();
        System.out.println(threadName+" "+message);
    }
    public static class MessageLoop implements Runnable{

        @Override
        public void run() {
            String importantInfo[]={
                    "Waking up at 5:00",
                    "Taking a shower at 5:15",
                    "Taking breakfast at 6:00",
                    "Going to work at 6:30"
            };
            try {
                for(String message:importantInfo){
                    Thread.sleep(4000);
                    threadMessage(message);
                }
            }catch (InterruptedException e){
                threadMessage("I wasn't done");
            }
        }
    }
    public static void main(String[] args) throws InterruptedException{
        long patience=1000*60*60;
        threadMessage("starting message thread");
        long startTime =System.currentTimeMillis();
        Thread t=new Thread(new MessageLoop());
        t.start();
        threadMessage("Waiting for message loop thread to finish");
        while(t.isAlive()){
            threadMessage("still waiting...");
            t.join(1000);
            if((System.currentTimeMillis()-startTime)>patience&&t.isAlive()){

                threadMessage("Tired of waiting!");
                t.interrupt();

                t.join();
            }

        }
        threadMessage("finally");
    }
}
