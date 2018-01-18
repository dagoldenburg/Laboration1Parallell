/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quicksort;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Kraken
 */
public class Quicksort {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Random rand = new Random();
        int arrayLength = 1000000;
        float[] daArray = new float[arrayLength];
        
        for(int i = 0; i < arrayLength; i++){
            daArray[i] = rand.nextFloat();
        }

        long startTime = System.currentTimeMillis();
        QuickSorter sorter = new QuickSorter();
        sorter.quicksort(daArray);
        long finishTime = System.currentTimeMillis();
        System.out.println("Our quicksort algo sort time with " + arrayLength + " length " + ": " + (finishTime-startTime));
        
        
        startTime = System.currentTimeMillis();
        Arrays.parallelSort(daArray);
        finishTime = System.currentTimeMillis();
        System.out.println("Their quicksort algo sort time with " + arrayLength + " length " + ": " + (finishTime-startTime));
    }
    
}
