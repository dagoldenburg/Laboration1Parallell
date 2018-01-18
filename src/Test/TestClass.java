package Test;

import MergeSort.MergeSorter;

import java.util.Random;

public class TestClass {

    public static void main(String[] args){
        
        float[] arrayNumbers = new float[100000];

        Random rand = new Random();

        for(int i = 0;i<arrayNumbers.length-1;i++){
            arrayNumbers[i] = rand.nextFloat();
        }
        long now = System.currentTimeMillis();
        arrayNumbers = new MergeSorter().mergeSorter(arrayNumbers);
        System.out.println((System.currentTimeMillis()-now)/1000.0+" seconds");

        System.out.println("Is correctly sorted: " + checkIfCorrect(arrayNumbers));
    }

    private static boolean checkIfCorrect(float[] array){
        for(int i = 0,j=1;i<array.length-1;i++,j++){
            if(array[i]>array[j])
                return false;
        }
        return true;
    }

}
