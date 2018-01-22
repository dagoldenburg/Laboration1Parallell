package AndersExempel;

import java.util.concurrent.RecursiveTask;

class SumTask extends RecursiveTask<Long> {

	public static final int THRESHOLD = (int) 1E5;
	private int[] array;
	private final int start, stop;
	private long res;

	SumTask(int[] array, int start, int stop) {
		this.array = array;
		this.start = start;
		this.stop = stop;
	}

	@Override
	protected Long compute() {
		if (stop - start < THRESHOLD) {
			// Calculate directly
			res = 0;
			for (int i = start; i <= stop; i++) {
				res += array[i];
			}
		} else {
			// fork + join
			int mid = (start+stop)/2;
			SumTask worker1 = new SumTask(array, start, mid);
			SumTask worker2 = new SumTask(array, mid+1, stop);
			worker1.fork(); // compute in new worker thread
			res = worker2.compute() + worker1.join(); // wait until worker1 has finished
		}
		return res;
	}

	private static final long serialVersionUID = 1L;
}