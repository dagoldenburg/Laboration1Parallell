/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ForkJoin;

import Sorts.QuickSorter;

import java.util.concurrent.RecursiveAction;

/**
 *
 * @author Kraken
 */
public class QuickSorterTask extends RecursiveAction{

    private float[] array;
    private int low,high,threshold;

    public QuickSorterTask(int low, int high,float[] array,int threshold) {
        this.array = array;
        this.low = low;
        this.high = high;
        this.threshold = threshold;
    }

  //  public static void setArray(float[] array) {
  //      QuickSorterTask.array = array;
  //  }

    @Override
    protected void compute(){
        int i = low, j = high;
        float middle = array[low + (high-low)/2];

        /**first exchange any elements that are in the wrong place **/
        /** (if element from left subarray is larger than element from right subarray **/
        while(i <= j){
            while(array[i] < middle){
                i++;
            }
            while(array[j] > middle){
                j--;
            }

            if(i <= j){
                exchange(i,j);
                i++;
                j--;
            }
        }


        /** if subarray is less than x elements then dont fork just compute **/
        if((high-low) < threshold) {
            if(low < j){
                quicksort(low, j);
            }
            if(i < high){
                quicksort(i,high);
            }
        }else{ //fork to new tasks
            boolean fork = false;
            QuickSorterTask worker1 = new QuickSorterTask(low, j,array);
            QuickSorterTask worker2 = new QuickSorterTask(i, high,array);
            if (low < j) {
                worker1.fork();
                fork = true;
            }
            if (i < high) {
                worker2.compute();
            }
            if(fork){
                worker1.join();
            }
        }
    }

    private void quicksort(int low, int high){
        int i = low, j = high;

        float middle = array[low + (high-low)/2];

        while(i <= j){
            while(array[i] < middle){
                i++;
            }
            while(array[j] > middle){
                j--;
            }

            if(i <= j){
                exchange(i,j);
                i++;
                j--;
            }
        }

        if(low < j){
            quicksort(low, j);
        }
        if(i < high){
            quicksort(i,high);
        }
    }

    private void exchange(int first, int second){
        float temp = array[first];
        array[first] = array[second];
        array[second] = temp;
    }

}
