/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sorts;

/**
 *
 * @author Kraken
 */
public class QuickSorter {
    private static long start;
    private static long stop;

    private float[] daArray;

    public static long getTime(){
        return stop-start;
    }
    
    public float[] quicksort(float[] array){
        if(array == null || array.length == 0){
            return array;
        }
        daArray = array;
        start = System.currentTimeMillis();
        quicksort(0,array.length-1);
        stop = System.currentTimeMillis();
        return array;
    }
    
    
    
    private void quicksort(int low, int high){
        int i = low, j = high;
        
        float middle = daArray[low + (high-low)/2];
        
        while(i <= j){
            while(daArray[i] < middle){
                i++;
            }
            while(daArray[j] > middle){
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
        float temp = daArray[first];
        daArray[first] = daArray[second];
        daArray[second] = temp;
    }
    
}
