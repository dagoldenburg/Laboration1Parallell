package ForkJoin;

import ForkJoin.MergeSortingTask;

import java.util.concurrent.RecursiveAction;

public class MergeSortingTaskStatic extends RecursiveAction {

    private int low,high;
    private static float[] arrayNumbers, helper;
    private int threshold;

    public MergeSortingTaskStatic(int low, int high,float[] arr,int thres){
        this.low = low;
        this.high = high;
        threshold = thres;
        MergeSortingTaskStatic.arrayNumbers = arr;
        MergeSortingTaskStatic.helper = new float[arr.length];
    }

    private MergeSortingTaskStatic(int low, int high, int thres){
        this.low = low;
        this.high = high;
        this.threshold = thres;
    }

    @Override
    protected void compute(){

        /**if subarray is less than threshold then just compute in this task **/
        if((high-low) < threshold) {
            mergeSorter(arrayNumbers,low,high);
        }else{ //ITS OVER 9000, fork new task(s)
            int middle = low + (high - low) / 2;
            MergeSortingTaskStatic worker1 = new MergeSortingTaskStatic( low, middle,threshold);
            MergeSortingTaskStatic worker2 = new MergeSortingTaskStatic( middle + 1, high,threshold);
            worker1.fork();
            worker2.compute();
            worker1.join();
            merge(low,middle,high,arrayNumbers);
        }
    }

    private void mergeSorter(float arrayNumbers[], int low, int high){
        if(low < high){
            int middle = low + (high-low)/2;
            mergeSorter(arrayNumbers,low,middle);

            mergeSorter(arrayNumbers,middle+1,high);

            merge(low,middle,high,arrayNumbers);
        }
    }

    private void merge(int low, int middle, int high,float[] arrayNumbers){

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
