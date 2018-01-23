package Test;

import ForkJoin.MergeSortingTask;
import ForkJoin.QuickSorterTask;
import Sorts.MergeSorter;
import Sorts.QuickSorter;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class TestClass {

    private static int SIZE = (int) 1E7,CORES = 1;
    private static Random rand;

    public static void main(String[] args){
        rand = new Random();

        System.out.println("Beginning warmup phase");
        /** WARM UP CODE (JIT-compilation) **/
        int x = 10000; //just create a small array
        float[] dummyArray = new float[x];
        for(int i = 0; i < x; i++){
            dummyArray[i] = rand.nextFloat();
        }
        for(int i = 0; i < 1000; i++){
            Arrays.sort(dummyArray.clone());
            new QuickSorter().quicksort(dummyArray.clone());
            Arrays.parallelSort(dummyArray.clone());
            new MergeSorter().mergeSorter(dummyArray.clone());

            ForkJoinPool pool = new ForkJoinPool(CORES);
            MergeSortingTask.setArrayNumbers(dummyArray.clone());
            MergeSortingTask.setHelper(new float[dummyArray.length]);
            MergeSortingTask rootTaskWarmUp = new MergeSortingTask(0,dummyArray.length-1);
            pool.invoke(rootTaskWarmUp);

           // QuickSorterTask.setArray(dummyArray.clone());
            QuickSorterTask quickRootTaskWarmUp = new QuickSorterTask(0,dummyArray.length-1,dummyArray.clone());
            pool.invoke(quickRootTaskWarmUp);
        }
        /** END OF WARMUP PHASE **/


        System.out.println("Starting real test phase");
        System.out.println();
        /** REAL TEST PHASE **/
        float[] daArray = new float[SIZE];

        for(int i = 0; i < SIZE; i++){
            daArray[i] = rand.nextFloat();
        }

        testArraysSort((float[]) daArray.clone());
        testQuickSort((float[]) daArray.clone());
        testMergeSort((float[]) daArray.clone());
        testArraysParallelSort((float[]) daArray.clone());
        testMergeSortForkJoin((float[]) daArray.clone());
        testQuickSortForkJoin((float[]) daArray.clone());

    }


    /**
     * Test of Java utils array sort
     * @param daArray
     */
    private static void testArraysSort(float[] daArray){
        System.out.println("------ java.utils.Arrays.sort ------");
        long start = System.currentTimeMillis();
        Arrays.sort(daArray);
        long stop = System.currentTimeMillis();
        System.out.println("Total time to sort: " + (stop-start) + " ms.");
        System.out.println();
        System.out.println();
    }

    /**
     * Test of our implementation of regular quick sort
     * @param daArray
     */
    private static void testQuickSort(float[] daArray){
        System.out.println("------ Our quick sort -------");

        new QuickSorter().quicksort(daArray);
        System.out.println("Our quicksort algo sort time with " + SIZE + " length " + ": " + QuickSorter.getTime() + " ms");

        System.out.println("Is correctly sorted: "+checkIfCorrect(daArray));
        System.out.println();
        System.out.println();

    }


    /**
     * Test of Java utils arrays parallel sort
     * @param daArray
     */
    private static void testArraysParallelSort(float[] daArray){
        System.out.println("------ java.utils.Arrays.parallelSort ------");

        long start = System.currentTimeMillis();
        Arrays.parallelSort(daArray);
        long stop = System.currentTimeMillis();
        System.out.println("java.utils.Arrays.parallelSort " + SIZE + " length " + ": " + (stop - start) + " ms");
        System.out.println("Is correctly sorted: "+checkIfCorrect(daArray));
        System.out.println();
        System.out.println();
    }


    /**
     * Test of our implementation of merge sort
     * @param daArray
     */
    private static void testMergeSort(float[] daArray){
        System.out.println("------ Our Merge sort ------");

        new MergeSorter().mergeSorter(daArray);
        System.out.println("Merge Sort " + SIZE + " length " + ": " + MergeSorter.getTime() + " ms");

        System.out.println("Is correctly sorted: " + checkIfCorrect(daArray));
        System.out.println();
        System.out.println();
    }


    /**
     * Test of our implementation of merge sort fork join
     * @param daArray
     */
    private static void testMergeSortForkJoin(float[] daArray){
        System.out.println("------ Merge sort - ForkJoin ------");

        System.gc();

        ForkJoinPool pool = new ForkJoinPool(CORES);
        MergeSortingTask.setArrayNumbers(daArray);
        MergeSortingTask.setHelper(new float[daArray.length]);
        long start = System.currentTimeMillis();
        MergeSortingTask rootTask = new MergeSortingTask(0,daArray.length-1);
        pool.invoke(rootTask);
        long stop = System.currentTimeMillis();
        System.out.println("Merge Sort - ForkJoin: " + daArray.length + " length " + ": " + (stop-start) + " ms");

        System.out.println("Is correctly sorted: " + checkIfCorrect(daArray));
        System.out.println();
        System.out.println();
    }


    /**
     * Test of our implementation of quick sort fork join
     * @param daArray
     */
    private static void testQuickSortForkJoin(float[] daArray){
        System.out.println("------ Quick sort - ForkJoin ------");

        System.gc();

        ForkJoinPool pool = new ForkJoinPool(CORES);
        long start = System.currentTimeMillis();
       // QuickSorterTask.setArray(daArray);
        QuickSorterTask rootTask = new QuickSorterTask(0,daArray.length-1,daArray);
        pool.invoke(rootTask);
        long stop = System.currentTimeMillis();
        System.out.println("Quick Sort - ForkJoin: " + daArray.length + " length " + ": " + (stop-start) + " ms");

        System.out.println("Is correctly sorted: " + checkIfCorrect(daArray));
        System.out.println();
        System.out.println();
    }
    
    
    


    private static boolean checkIfCorrect(float[] array){
        for(int i = 0,j=1;i<array.length-1;i++,j++){
            if(array[i]>array[j])
                return false;
        }
        return true;
    }

}
