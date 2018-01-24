package ForkJoin;

import ForkJoin.MergeSortingTask;

import java.util.concurrent.RecursiveAction;

public class MergeSortingTask extends RecursiveAction {

    private int low,high;
    private float[] arrayNumbers,helper;
    private int threshold;

    public boolean checkIfCorrectOrder(){
        System.out.println("checking if correct order, arraylength: " + arrayNumbers.length);
        for(int i = 0,j=1;i<arrayNumbers.length-1;i++,j++){
            if(arrayNumbers[i]>arrayNumbers[j])
                return false;
        }
        return true;
    }

    public MergeSortingTask(int low, int high, float[] arr, int thres){
        this.low = low;
        this.high = high;
        threshold = thres;
        arrayNumbers = arr;
        helper = new float[arr.length];
    }

    public MergeSortingTask(int low, int high,float[] arr,float[] helper, int thres){
        this.low = low;
        this.high = high;
        threshold = thres;
        arrayNumbers = arr;
        this.helper = helper;
    }
/*
    private MergeSortingTask(int low, int high,float[] arrayNumbers, int thres){
        this.low = low;
        this.high = high;
        this.threshold = thres;

    }*/

    @Override
    protected void compute(){

        /**if subarray is less than threshold then just compute in this task **/
        if((high-low) < threshold) {
            mergeSorter(low,high);
        }else{ //ITS OVER 9000, fork new task(s)
            int middle = low + (high - low) / 2;
            MergeSortingTask worker1 = new MergeSortingTask( low, middle,arrayNumbers,helper,threshold);
            MergeSortingTask worker2 = new MergeSortingTask( middle+1, high,arrayNumbers,helper,threshold);
            worker1.fork();
            worker2.compute();
            worker1.join();
            merge(low,middle,high);
        }
    }

    private void mergeSorter(int low, int high){
        if(low < high){
            int middle = low + (high-low)/2;
            mergeSorter(low,middle);

            mergeSorter(middle+1,high);

            merge(low,middle,high);
        }
    }

    private void merge(int low, int middle, int high){

        for(int i = low; i < high;i++){
            helper[i] = arrayNumbers[i];
        }

        int i=low,j=middle+1,k=low;

        while (i <= middle && j <= high) {
            if (helper[i] <= helper[j]) {
                arrayNumbers[k] = helper[i];
                i++;
            } else {
                arrayNumbers[k] = helper[j];
                j++;
            }
            k++;
        }

        while (i <= middle) {
            arrayNumbers[k] = helper[i];
            k++;
            i++;
        }
    }

}
