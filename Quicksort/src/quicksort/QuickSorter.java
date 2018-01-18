/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quicksort;

/**
 *
 * @author Kraken
 */
public class QuickSorter {
    
    
    public  void quicksort(int[] array){
        quicksort(0,array.length-1,array);
        for(int i = 0; i < array.length-1; i++){
            System.out.print(array[i] + " ");
        }
    }
    
    
    
    private void quicksort(int low, int high, int[] array){
        int i = low, j = high;
        
        int middle = array[low + (high-low)/2];
        
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
    
    
    
    
    private void exchange(int[] array, int first, int second){
        int temp = array[first];
        array[first] = array[second];
        array[second] = temp;
    }
    
}
