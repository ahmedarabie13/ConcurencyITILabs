package com.arabie;


import static com.arabie.Main.kernel;
import static com.arabie.Main.outPut;
import static com.arabie.Main.pic;
import static com.arabie.Main.s;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {

    static byte[][] kernel = new byte[2][2];
    static byte[][] pic = new byte[20000][20000];
    static byte s = 2;
    static byte[][] outPut = new byte[20000 / s][20000 / s];

    public static void main(String[] args) throws Exception {

        System.out.println("sequential exec : ");
        intialize();
//        printInput();

        long startTime = System.currentTimeMillis();
        seqConvolve();
        System.out.println("no. of threads before parallel : " + Thread.activeCount());
        long endTime = System.currentTimeMillis();
//        printOutput();
        System.out.println("Time Duration : " + (endTime - startTime) + " ms");

        System.out.println("\n---------- parallel execution using ExecutorServices ----------");
        startTime = System.currentTimeMillis();
        System.out.println("no. of threads before parallel : " + Thread.activeCount());
        new Master().doRun(20000, 4);
        System.out.println("no. of threads during parallel: " + Thread.activeCount());
        endTime = System.currentTimeMillis();
//        printOutput();
        System.out.println("Time Duration: " + (endTime - startTime) + "ms");
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());

        ForkJoinPool fjp = new ForkJoinPool();
        Task task=new Task(0,20000);
        System.out.println("\n---------- parallel execution using ForkJoin ----------");
        startTime = System.currentTimeMillis();
        System.out.println("no. of threads before parallel : " + Thread.activeCount());
//        fjp.invoke(task);
        task.invoke();
        System.out.println("no. of threads during parallel: " + Thread.activeCount());
        endTime = System.currentTimeMillis();
//        printOutput();
        System.out.println("Time Duration: " + (endTime - startTime) + "ms");
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());


    }

    public static void intialize() {
        int temp;
        for (int i = 0; i < 20000; i++) {
            for (int j = 0; j < 20000; j++) {
                temp = i + j;
                pic[i][j] = (byte) temp;
                if (i < 2 && j < 2) {
                    kernel[i][j] = (byte) 1;
                }
            }
        }
    }

    public static void seqConvolve() {
        int temp;
        for (int i = 0; i < 20000; i += s) {
            for (int j = 0; j < 20000; j += s) {
                temp = pic[i][j] * kernel[0][0] + pic[i + 1][j] * kernel[1][0]
                        + pic[i][j + 1] * kernel[0][1] + pic[i + 1][j + 1] * kernel[1][1];
                outPut[i / 2][j / 2] = (byte) temp;
            }
        }
    }

    public static void printInput() {
        System.out.println("input : ");
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                System.out.print(pic[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    public static void printOutput() {
        System.out.println("output : ");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(outPut[i][j] + "\t");
            }
            System.out.println("");
        }
    }

}

class Master {

    public void doRun(int totalRows, int numWorkers) throws InterruptedException, ExecutionException {

        List<Callable<Long>> tasks = new ArrayList<>();

        for (int i = 0; i < numWorkers; ++i) {

            int begin = (totalRows / numWorkers) * i;
            int end = begin + (totalRows / numWorkers);
            tasks.add(new Worker(begin, end));

        }

        ExecutorService exec = Executors.newFixedThreadPool(numWorkers);

        exec.invokeAll(tasks);
        exec.shutdown();
    }
}
class Worker implements Callable<Long> {

    private int wBegin;
    private int wEnd;

    public Worker(int begin, int end) {
        this.wBegin = begin;
        this.wEnd = end;
    }

    @Override
    public Long call() {
        seqConvolve();
        return 0l;
    }

    public void seqConvolve() {
        int temp;
        for (int i = wBegin; i < wEnd; i += s) {
            for (int j = 0; j < 20000; j += s) {
                temp = pic[i][j] * kernel[0][0] + pic[i + 1][j] * kernel[1][0]
                        + pic[i][j + 1] * kernel[0][1] + pic[i + 1][j + 1] * kernel[1][1];
                outPut[i / 2][j / 2] = (byte) temp;
            }
        }
    }
}
class Task extends RecursiveAction{
    private int wBegin;
    private int wEnd;
    final int seqThreshold = 5000;

    public Task(int begin, int end) {
        this.wBegin = begin;
        this.wEnd = end;
    }
    @Override
    protected void compute() {
        if((wEnd - wBegin) < seqThreshold){
            seqConvolve();
        }else{
            int middle = (wEnd + wBegin) / 2;
            Task subTaskA = new Task(wBegin, middle);
            Task subTaskB = new Task(middle, wEnd);
            subTaskA.fork();
            subTaskB.fork();
            subTaskA.join() ;
            subTaskB.join();
        }
    }
    public void seqConvolve() {
        int temp;
        for (int i = wBegin; i < wEnd; i += s) {
            for (int j = 0; j < 20000; j += s) {
                temp = pic[i][j] * kernel[0][0] + pic[i + 1][j] * kernel[1][0]
                        + pic[i][j + 1] * kernel[0][1] + pic[i + 1][j + 1] * kernel[1][1];
                outPut[i / 2][j / 2] = (byte) temp;
            }
        }
    }
}
