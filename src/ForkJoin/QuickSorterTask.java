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

    private static float[] array;
    private int low,high;

    public QuickSorterTask(int low, int high,float[] array) {
        this.array = array;
        this.low = low;
        this.high = high;
    }

    public static void setArray(float[] array) {
        QuickSorterTask.array = array;
    }

    @Override
    protected void compute(){
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
                exchange(array,i,j);
                i++;
                j--;
            }
        }
        if((high-low) < 9000) {
            if(low < j){
                quicksort(low, j, array);
            }
            if(i < high){
                quicksort(i,high,array);
            }
        }else{
            boolean fork = false;
            QuickSorterTask worker1 = new QuickSorterTask(low, j, array);
            QuickSorterTask worker2 = new QuickSorterTask(i, high, array);
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

    private void quicksort(int low, int high, float[] array){
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
                exchange(array,i,j);
                i++;
                j--;
            }
        }

        if(low < j){
            quicksort(low, j, array);
        }
        if(i < high){
            quicksort(i,high,array);
        }
    }

    private void exchange(float[] array, int first, int second){
        float temp = array[first];
        array[first] = array[second];
        array[second] = temp;
    }

}
