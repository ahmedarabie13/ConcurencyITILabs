package com.company;
import javax.swing.*;

public class Main extends JFrame
{
    Thread th1;
    Thread th2;
    JLabel ball1 = new JLabel(new ImageIcon("D:\\Soccer-Ball-icon.png"));
    JLabel ball2 = new JLabel(new ImageIcon("D:\\Soccer-Ball-icon.png"));
    int ball1XPos, ball1YPos, ball1XChange, ball1YChange;
    int ball2XPos, ball2YPos, ball2XChange, ball2YChange;

    Main(){
        this.setTitle("Moving Label App");
//        ball1.setBorder(BorderFactory.createLineBorder(Color.BLUE,1));
//        ball2.setBorder(BorderFactory.createLineBorder(Color.RED,1));
        this.add(ball1);
        this.add(ball2);
        th1 =new Thread(this::bounceBall1);
        th1.start();
        th2 =new Thread(this::bounceBall2);
        th2.start();
    }
    public static void main(String args[])
    {
        Main f= new Main();
        f.setSize(900,600);
        f.setVisible(true);

    }


    public synchronized void bounceBall1() {

        ball1XPos =200;
        ball1YPos =230;
        ball1XChange =3;
        ball1YChange =3;
        while (true)
        {
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
        while (true)
        {
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