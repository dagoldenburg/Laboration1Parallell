package Test;

import ForkJoin.MergeSortingTask;
import ForkJoin.QuickSorterTask;
import Sorts.MergeSorter;
import Sorts.QuickSorter;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class TestClass {

    private static int SIZE = (int) 1E8,CORES = 4;
    private static Random rand;

    public static void main(String[] args){
        rand = new Random();
        testQuickSort();
        testArraysParallelSort();
        testMergeSort();
        testMergeSortForkJoin();
        testQuickSortForkJoin();
        
    }
    
    private static void testQuickSort(){
        System.out.println("Quick sort \n ---------------");
        float[] daArray = new float[SIZE];

        for(int i = 0; i < SIZE; i++){
            daArray[i] = rand.nextFloat();
        }

        new QuickSorter().quicksort(daArray);
        System.out.println("Our quicksort algo sort time with " + SIZE + " length " + ": " + QuickSorter.getTime() + " ms");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Is correctly sorted: "+checkIfCorrect(daArray)+"\n ---------------\n");

    }

    private static void testArraysParallelSort(){
        System.out.println("java.utils.Arrays.parallelSort \n ---------------");

        float[] daArray = new float[SIZE];
        for(int i = 0; i < SIZE; i++){
            daArray[i] = rand.nextFloat();
        }
        long start = System.currentTimeMillis();
        Arrays.parallelSort(daArray);
        long stop = System.currentTimeMillis();
        System.out.println("java.utils.Arrays.parallelSort " + SIZE + " length " + ": " + (stop - start) + " ms");
        System.out.println("Is correctly sorted: "+checkIfCorrect(daArray)+"\n ---------------\n");
    }

    private static void testMergeSort(){
        System.out.println("Merge sort \n ---------------");
        float[] arrayNumbers = new float[SIZE];

        for(int i = 0;i<arrayNumbers.length-1;i++){
            arrayNumbers[i] = rand.nextFloat();
        }

        new MergeSorter().mergeSorter(arrayNumbers);
        System.out.println("Merge Sort " + SIZE + " length " + ": " + MergeSorter.getTime() + " ms");

        System.out.println("Is correctly sorted: " + checkIfCorrect(arrayNumbers) +"\n ---------------\n");
    }

    private static void testMergeSortForkJoin(){
        System.out.println("Merge sort - ForkJoin\n ---------------");
        float[] arrayNumbers = new float[SIZE];

        System.gc();

        ForkJoinPool pool = new ForkJoinPool(CORES);

        for(int i = 0;i<arrayNumbers.length-1;i++){
            arrayNumbers[i] = rand.nextFloat();
        }

        MergeSortingTask.setArrayNumbers(arrayNumbers);
        MergeSortingTask.setHelper(new float[SIZE]);
        MergeSortingTask rootTaskWarmUp = new MergeSortingTask(0,arrayNumbers.length-1);
        pool.invoke(rootTaskWarmUp);

        for(int i = 0;i<arrayNumbers.length-1;i++){
            arrayNumbers[i] = rand.nextFloat();
        }

        System.gc();

        MergeSortingTask.setArrayNumbers(arrayNumbers);
        MergeSortingTask.setHelper(new float[SIZE]);
        long start = System.currentTimeMillis();
        MergeSortingTask rootTask = new MergeSortingTask(0,arrayNumbers.length-1);
        pool.invoke(rootTask);
        long stop = System.currentTimeMillis();
        System.out.println("Merge Sort - ForkJoin: " + SIZE + " length " + ": " + (stop-start) + " ms");

        System.out.println("Is correctly sorted: " + checkIfCorrect(arrayNumbers) +"\n ---------------\n");
    }

    private static void testQuickSortForkJoin(){
        System.out.println("Quick sort - ForkJoin\n ---------------");
        float[] arrayNumbers = new float[SIZE];

        System.gc();

        ForkJoinPool pool = new ForkJoinPool(CORES);

        for(int i = 0;i<arrayNumbers.length-1;i++){
            arrayNumbers[i] = rand.nextFloat();
        }

        QuickSorterTask.setArray(arrayNumbers);
        QuickSorterTask rootTaskWarmUp = new QuickSorterTask(0,arrayNumbers.length-1,arrayNumbers);
        pool.invoke(rootTaskWarmUp);

        for(int i = 0;i<arrayNumbers.length-1;i++){
            arrayNumbers[i] = rand.nextFloat();
        }

        System.gc();

        long start = System.currentTimeMillis();
        QuickSorterTask.setArray(arrayNumbers);
        QuickSorterTask rootTask = new QuickSorterTask(0,arrayNumbers.length-1,arrayNumbers);
        pool.invoke(rootTask);
        long stop = System.currentTimeMillis();
        System.out.println("Quick Sort - ForkJoin: " + SIZE + " length " + ": " + (stop-start) + " ms");

        System.out.println("Is correctly sorted: " + checkIfCorrect(arrayNumbers) +"\n ---------------\n");
    }
    
    
    


    private static boolean checkIfCorrect(float[] array){
        for(int i = 0,j=1;i<array.length-1;i++,j++){
            if(array[i]>array[j])
                return false;
        }
        return true;
    }

}
