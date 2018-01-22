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

    public static long getTime(){
        return stop-start;
    }
    
    public float[] quicksort(float[] array){
        start = System.currentTimeMillis();
        quicksort(0,array.length-1,array);
        stop = System.currentTimeMillis();
        return array;
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
