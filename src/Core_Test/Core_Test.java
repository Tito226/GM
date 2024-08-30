package Core_Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Phaser;

// Simple JscrollPane 

// Driver Class 
public class Core_Test {
	static Random r=new Random();
	static int a=100, b=50;
	static ExecutorService pool=Executors.newFixedThreadPool(3);
	static Phaser phaser=new Phaser(1);
	static Runnable task=() -> {
			someJob();
			phaser.arriveAndDeregister();
	};
	
	// main function
	public static void main(String[] args) {
		
		long t1=System.currentTimeMillis();
		singleThreadJob();
		long t2=System.currentTimeMillis();
		System.out.println("single thread: "+(t2-t1));
		ArrayList<Future> fList=new ArrayList();

		long t3=System.currentTimeMillis();
		multiThreadJob();
		long t4=System.currentTimeMillis();
		System.out.println("multi thread: "+(t4-t3));
		
		pool.shutdown();
	}

	static void singleThreadJob() {
		for (int i=0; i<a; i++) {
			for (int j=0; j<b; j++) {
				someJob();
			}
		}
	}
	
	static void multiThreadJob() {
		for (int i=0; i<a; i++) {
			phaser.bulkRegister(b);
			for (int j=0; j<b; j++) {
				pool.submit(task);
			}
		}
	}
	
	static void someJob() {
		for (int j=0; j<1000; j++) {
			//System.out.println((r.nextInt()+41223)*3/2);
			int b=(r.nextInt()+41223)*3/2;
		}
	}
}
