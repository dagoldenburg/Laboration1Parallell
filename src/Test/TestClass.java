package Test;

import ForkJoin.MergeSortingTask;
import ForkJoin.MergeSortingTaskStatic;
import ForkJoin.QuickSorterTask;
import Sorts.MergeSorter;
import Sorts.QuickSorter;
import com.sun.scenario.effect.Merge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class TestClass {

    private static int SIZE = (int) 1E6,MAXCORES = 8,QUICK_SORT = 1,MERGE_SORT = 2;
    private static Random rand;
    private static ArrayList<Long> timeArray = new ArrayList<>();

    public static void main(String[] args){
        rand = new Random();

        System.out.println("available cores: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Beginning warmup phase");
        /** WARM UP CODE (JIT-compilation) **/
        int x = 10000; //just create a small array
        float[] dummyArray = new float[x];
        for(int i = 0; i < x; i++){
            dummyArray[i] = rand.nextFloat();
        }
        for(int i = 0; i < 1000; i++){
            Arrays.sort(dummyArray.clone());
            Arrays.parallelSort(dummyArray.clone());
            ForkJoinPool pool = new ForkJoinPool(MAXCORES);
            MergeSortingTask rootTaskWarmUp = new MergeSortingTask(0,dummyArray.length-1,dummyArray.clone(),9000);
            pool.invoke(rootTaskWarmUp);
            QuickSorterTask quickRootTaskWarmUp = new QuickSorterTask(0,dummyArray.length-1,dummyArray.clone(),9000);
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
        testArraysParallelSort((float[]) daArray.clone());
        testForkJoinSort(MERGE_SORT,daArray.clone());
        testForkJoinSort(QUICK_SORT, daArray.clone());
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
     * Test of our implementation of merge sort fork join
     * @param daArray
     */

    private static long executeSort(int whatSort,float[] daArray, int threshold,int cores){

        if(whatSort == QUICK_SORT){

            ForkJoinPool pool = new ForkJoinPool(cores);
            QuickSorterTask rootTask = new QuickSorterTask(0,daArray.length-1,daArray,threshold);
            long start = System.currentTimeMillis();
            pool.invoke(rootTask);
            long stop = System.currentTimeMillis();
            if(!checkIfCorrect(daArray)){
                System.out.println("didnt sort correctly m8");
            }
            //System.out.println("Time for " + cores + " cores and " + threshold + "threshold: " + (stop-start));
            return (stop-start);

        }else if(whatSort == MERGE_SORT){
            ForkJoinPool pool = new ForkJoinPool(cores);
            MergeSortingTask rootTask = new MergeSortingTask(0, daArray.length - 1,daArray,threshold);


            long start = System.currentTimeMillis();
            pool.invoke(rootTask);
            long stop = System.currentTimeMillis();

            if(!checkIfCorrect(daArray)){
                System.out.println("didnt sort correctly m8");
            }
            return (stop-start);
        }else{
            return 0;
        }
    }

    private static int getBestThreshold(int whatSort, float[] daArray){
        int threshold = 1000;
        int incrementVal = 1000;
        double lowestAvg = Double.MAX_VALUE;
        int bestThreshold = 0;
        for(int i = 0; i < 10; i++) {//do tests with 10 different threshold vals
            long totalTime = 0;
            int nrTries = 20;
            for (int k = 0; k < nrTries; k++) {//get average out of nrTries tries
                System.gc();
                if(whatSort == MERGE_SORT){
                    totalTime += executeSort(MERGE_SORT,daArray.clone(),threshold,MAXCORES);
                }else if(whatSort == QUICK_SORT){
                    totalTime += executeSort(QUICK_SORT,daArray.clone(),threshold,MAXCORES);
                }

            }
            double avg = totalTime / nrTries;
            System.out.println("Avg time for threshold " + threshold + ": " + avg + " ms.");
            if (avg < lowestAvg) {
                lowestAvg = avg;
                bestThreshold = threshold;
            }
            threshold += incrementVal; //increment threshold for next loop
        }
        return bestThreshold;
    }

    private static void testForkJoinSort(int whatSort,float[] daArray){
        if(whatSort == MERGE_SORT){
            System.out.println("------ Merge Sort - ForkJoin -------");
        }else if(whatSort == QUICK_SORT){
            System.out.println("------ Quick Sort - ForkJoin -------");
        }else{
            System.out.println("Invalid sorting algorithm");
            return;
        }
        //find threshold
        System.out.println("Searching for best threshold");

        int bestThreshold = getBestThreshold(whatSort,daArray);

        //use bestThreshold for next step of the test
        System.out.println("Best threshold: " + bestThreshold);
        System.out.println();

        for(int nrCores = 1; nrCores <= MAXCORES; nrCores++){//test 1 to MAXCORES
            long totalTime = 0;
            int nrTries = 20;
            for(int k = 0; k < nrTries; k++){//do 20 tests and calculate average
                if(whatSort == MERGE_SORT){
                    totalTime += executeSort(MERGE_SORT,daArray.clone(),bestThreshold,nrCores);
                }else if(whatSort == QUICK_SORT) {
                    totalTime += executeSort(QUICK_SORT, daArray.clone(), bestThreshold, nrCores);
                }
            }
            double avg = totalTime/nrTries;
            System.out.println("Avg sort time with " + nrCores + " cores: " + avg + " ms.");
            System.out.println();
        }
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
