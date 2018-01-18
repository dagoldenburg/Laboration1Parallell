package ForkJoin;

import java.util.concurrent.RecursiveAction;

public class SortingTask extends RecursiveAction {

    private float[] array;
    private int low,high;

    public SortingTask(float[] array, int low, int high) {
        this.array = array;
        this.low = low;
        this.high = high;
    }

    @Override
    protected void compute() {

    }

}
