package AndersExempel;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinMain {

	public static void main(String[] args) throws Exception {
		
		if(args.length < 1) {
			System.out.println("Usagae: java ForkJoinMain <no-of-kernels>");
			System.exit(0);
		}

		
		final int n = 8; // no of threads/kernels
		ForkJoinPool pool = new ForkJoinPool(n);
		System.out.println("ForkJoin, threads = " + n);

		
		// Generate data
		final int SIZE = (int) 1E8;
		int[] arr = new int[SIZE];
		Random rand = new Random();
		for (int i = 0; i < arr.length; i++) {
			arr[i] = rand.nextInt(10);
		}

		
		// Warm up, to get the JIT compiler going
		SumTask rootTask = new SumTask(arr, 0, arr.length - 1);
		pool.invoke(rootTask);
		long sum = rootTask.get();
		System.out.println("Warm up completed, sum = " + sum);

		
		// Measuring
		for(int i = 0; i < 10; i++) {
			System.gc();
			Thread.sleep(5000);

			long start = System.nanoTime();
			rootTask = new SumTask(arr, 0, arr.length - 1);
			pool.invoke(rootTask);
			sum = rootTask.get();
			long elapsed = System.nanoTime() - start;

			System.out.println(elapsed/1.0E9 + " s, (sum " + sum + ")");
		}
	}
}