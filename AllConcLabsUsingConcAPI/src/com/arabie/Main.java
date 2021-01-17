package com.arabie;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class Main extends JFrame
{
    Date d= new Date();


    JLabel timeLabel =new JLabel();
    JLabel bannerLabel =new JLabel("iTi-JETS");
    JLabel ball1 = new JLabel(new ImageIcon("D:\\Soccer-Ball-icon.png"));
    JLabel ball2 = new JLabel(new ImageIcon("D:\\Soccer-Ball-icon.png"));
    int ball1XPos, ball1YPos, ball1XChange, ball1YChange;
    int ball2XPos, ball2YPos, ball2XChange, ball2YChange;
    volatile boolean lock=true;
    int bannerIndex =0;
    Object syncObj=new Object();

    JButton resButton;
    JButton pauseButton;
    ScheduledExecutorService threadPool;



    Main(){

        this.setTitle("Controlling Ball App");
        this.setLayout(null);
        threadPool = Executors.newScheduledThreadPool(8);
//        threadPool = Executors.newFixedThreadPool(8);

// using Runnable

        threadPool.scheduleAtFixedRate(this::runClock,0,1,TimeUnit.SECONDS);
        threadPool.submit(this::bounceBall1);
        threadPool.scheduleAtFixedRate(this::runBanner,0,50,TimeUnit.MILLISECONDS);
        threadPool.submit(this::bounceBall2);

//  using Callable

//        List<Callable<Object>> tasks=new ArrayList<>();
//        tasks.add(this::runBannerCallable);
//        tasks.add(this::runClockCallable);
//        tasks.add(this::bounceBall1Callable);
//        tasks.add(this::bounceBall2Callable);
//        for(Callable<Object> task:tasks)
//            threadPool.submit(task);

//        List<Future<Object>> res;
//        try {
//            res=threadPool.invokeAll(tasks);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        ball1.setBorder(BorderFactory.createLineBorder(Color.BLUE,1));
//        ball2.setBorder(BorderFactory.createLineBorder(Color.RED,1));

        timeLabel.setText(d.toString());
        timeLabel.setBounds(0,0, 200,20);
        bannerLabel.setBounds(0,80, 50,20);

        resButton =new JButton("Resume");
        pauseButton =new JButton("Pause");

        pauseButton.setEnabled(lock);
        resButton.setEnabled(!lock);

        resButton.setBounds(50,30,100,40);
        pauseButton.setBounds(200,30,100,40);
        resButton.addActionListener((event)->{
            synchronized(syncObj){
                lock =true;
                syncObj.notifyAll();
            }
            pauseButton.setEnabled(lock);
            resButton.setEnabled(!lock);

            System.out.println(Thread.currentThread().getName());
        });
        pauseButton.addActionListener((event) ->{
            synchronized(syncObj) {
                lock = false;
                syncObj.notifyAll();
            }
            System.out.println(Thread.currentThread().getName());

            pauseButton.setEnabled(lock);
            resButton.setEnabled(!lock);

        });

        this.add(resButton);
        this.add(pauseButton);
        this.add(ball1);
        this.add(ball2);
        this.add(bannerLabel);
        this.add(timeLabel);


    }
    public static void main(String args[])
    {
        Main f= new Main();
        System.out.println(Thread.currentThread().getName());
        f.setSize(900,600);
        f.setVisible(true);

    }


    public void bounceBall1()  {

        ball1XPos =200;
        ball1YPos =230;
        ball1XChange =3;
        ball1YChange =3;

        System.out.println(Thread.currentThread().getName());
        while (true)
        {
            if (!lock){
                synchronized (syncObj){
                    try {
                        syncObj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(Math.abs(ball1XPos - ball2XPos)<40 && Math.abs(ball1YPos - ball2YPos)<40){
                ball1XChange *=-1;
                ball2XChange *=-1;
                ball1YChange *=-1;
                ball2YChange *=-1;
            }

            ball1XPos += ball1XChange;
            ball1YPos -= ball1YChange;
            if(ball1XPos +20>this.getWidth()-ball1.getWidth()|| ball1XPos <0) ball1XChange *=-1;
            if(ball1YPos +20>this.getHeight()-ball1.getHeight()-20|| ball1YPos <0) ball1YChange *=-1;
            ball1.setBounds(ball1XPos, ball1YPos, 60,60);

            try {
                Thread.sleep(8);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public void bounceBall2() {
        ball2XPos =40;
        ball2YPos =90;
        ball2XChange =3;
        ball2YChange =3;
        System.out.println(Thread.currentThread().getName());
        while (true)
        {
            if (!lock){
                synchronized (syncObj){
                    try {
                        syncObj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(Math.abs(ball1XPos - ball2XPos)<40 && Math.abs(ball1YPos - ball2YPos)<40){

                ball1XChange *=-1;
                ball2XChange *=-1;
                ball1YChange *=-1;
                ball2YChange *=-1;
            }

            ball2XPos += ball2XChange;
            ball2YPos -= ball2YChange;
            if(ball2XPos +20>this.getWidth()-ball2.getWidth()|| ball2XPos <0) ball2XChange *=-1;
            if(ball2YPos +20>this.getHeight()-ball2.getHeight()-20|| ball2YPos <0) ball2YChange *=-1;
            ball2.setBounds(ball2XPos, ball2YPos, 60,60);

            try {
                Thread.sleep(8);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
    public void runClock() {
        this.d =new Date();
        this.timeLabel.setText(d.toString());
    }
    public void runBanner() {

          if (!lock){
                synchronized (syncObj){
                    try {
                        syncObj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
          }
          else {
              bannerIndex += 10;
              bannerLabel.setLocation(bannerIndex, 80);

              if (bannerIndex > this.getBounds().getWidth())
                  bannerIndex = 0;
          }
    }
    public Object runBannerCallable() {
        int i=0;
        while (true){

            if (!lock){
                synchronized (syncObj){
                    try {
                        syncObj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            bannerLabel.setLocation(i,80);
            i+=10;

            if(i>this.getBounds().getWidth())
                i=0;
            try {
                Thread.sleep(50);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
    public Object runClockCallable() {
        while (true){
//        System.out.println("pla");
            this.d =new Date();
            this.timeLabel.setText(d.toString());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public Object bounceBall1Callable()  {

        ball1XPos =200;
        ball1YPos =230;
        ball1XChange =3;
        ball1YChange =3;

        System.out.println(Thread.currentThread().getName());
        while (true)
        {
            if (!lock){
                synchronized (syncObj){
                    try {
                        syncObj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(Math.abs(ball1XPos - ball2XPos)<40 && Math.abs(ball1YPos - ball2YPos)<40){
                ball1XChange *=-1;
                ball2XChange *=-1;
                ball1YChange *=-1;
                ball2YChange *=-1;
            }

            ball1XPos += ball1XChange;
            ball1YPos -= ball1YChange;
            if(ball1XPos +20>this.getWidth()-ball1.getWidth()|| ball1XPos <0) ball1XChange *=-1;
            if(ball1YPos +20>this.getHeight()-ball1.getHeight()-20|| ball1YPos <0) ball1YChange *=-1;
            ball1.setBounds(ball1XPos, ball1YPos, 60,60);

            try {
                Thread.sleep(8);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public Object bounceBall2Callable() {
        ball2XPos =40;
        ball2YPos =90;
        ball2XChange =3;
        ball2YChange =3;
        System.out.println(Thread.currentThread().getName());
        while (true)
        {
            if (!lock){
                synchronized (syncObj){
                    try {
                        syncObj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(Math.abs(ball1XPos - ball2XPos)<40 && Math.abs(ball1YPos - ball2YPos)<40){

                ball1XChange *=-1;
                ball2XChange *=-1;
                ball1YChange *=-1;
                ball2YChange *=-1;
            }

            ball2XPos += ball2XChange;
            ball2YPos -= ball2YChange;
            if(ball2XPos +20>this.getWidth()-ball2.getWidth()|| ball2XPos <0) ball2XChange *=-1;
            if(ball2YPos +20>this.getHeight()-ball2.getHeight()-20|| ball2YPos <0) ball2YChange *=-1;
            ball2.setBounds(ball2XPos, ball2YPos, 60,60);

            try {
                Thread.sleep(8);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}