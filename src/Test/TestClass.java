package Test;

import ForkJoin.MergeSortingTask;
import ForkJoin.QuickSorterTask;
import Sorts.MergeSorter;
import Sorts.QuickSorter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class TestClass {

    private static int SIZE = (int) 1E5,MAXCORES = 1,QUICK_SORT = 1,MERGE_SORT = 2;
    private static Random rand;
    private static ArrayList<Long> timeArray = new ArrayList<>();

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
            //new QuickSorter().quicksort(dummyArray.clone());
            Arrays.parallelSort(dummyArray.clone());
            //new MergeSorter().mergeSorter(dummyArray.clone());

            ForkJoinPool pool = new ForkJoinPool(MAXCORES);
            MergeSortingTask.setArrayNumbers(dummyArray.clone());
            //MergeSortingTask.setThreshold(9000);
            MergeSortingTask.setHelper(new float[dummyArray.length]);
            MergeSortingTask rootTaskWarmUp = new MergeSortingTask(0,dummyArray.length-1,9000);
            pool.invoke(rootTaskWarmUp);

           // QuickSorterTask.setArray(dummyArray.clone());
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

        //testArraysSort((float[]) daArray.clone());
       /// testQuickSort((float[]) daArray.clone());
       // testMergeSort((float[]) daArray.clone());
        //testArraysParallelSort((float[]) daArray.clone());
       // testMergeSortForkJoin((float[]) daArray.clone());
        //testQuickSortForkJoin((float[]) daArray.clone(),9000);
        testThreshold(QUICK_SORT,1000,12000,1000,5,daArray.clone());
        testThreshold(MERGE_SORT,1000,12000,1000,5,daArray.clone());


    }
    //whatSort 1 = quick 2 =
    private static int testThreshold(int whatSort,int fromRange, int toRange, int increase,int testPerThreshold,float[] daArray){
        switch(whatSort){
            case 1:
                System.out.println("QUICKSORT\n--------");break;
            case 2:
                System.out.println("MERGESORT\n--------");break;
            default:
                System.out.println("whatSort needs to be 1 or 2");return -1;
        }
        int count = 0;
        for(int i = whatSort;i<=toRange;i+=increase)
            count++;
        long[] iArray = new long[count];
        long[] jArray = new long[testPerThreshold];
        for(int i = fromRange,index=0;i<=toRange;i+=increase,index++){
            for(int j = 0;j<testPerThreshold;j++){
                long start = System.nanoTime();
                switch(whatSort){
                    case 1: testQuickSortForkJoin(daArray,i);break;
                    case 2: testMergeSortForkJoin(daArray,i);break;
                    default:
                        System.out.println("whatSort needs to be 1 or 2");
                        return -1;
                }
                jArray[j] = System.nanoTime() - start;
            }
            long sum=0;
            for(int x=0;x<jArray.length-1;x++){
                sum+=jArray[x];
            }
            System.out.println("Average for "+i+" as threshold is "+sum/jArray.length+" nano seconds");
            iArray[index]=sum/jArray.length;
        }
        long lowest=Long.MAX_VALUE;
        int lowestIndex = -1;
        for(int i = 0;i<=iArray.length-1;i++){
            if(iArray[i]<lowest){
                lowest = iArray[i];
                lowestIndex = i;
            }
        }
        System.out.println(lowestIndex);
        System.out.println(iArray[lowestIndex]);
        System.out.println("\n\nMost efficient threshold is "+((lowestIndex*increase)+fromRange));
        return (lowestIndex*increase)+fromRange;
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
    private static void testMergeSortForkJoin(float[] daArray,int threshold){
        //System.out.println("------ Merge sort - ForkJoin ------");
        System.gc();
/*

        //find threshold
        System.out.println("Searching for best threshold");
        int threshold = 500;
        int incrementVal = 500;
        double lowestAvg = Double.MAX_VALUE;
        int bestThreshold = 0;
        for(int i = 0; i < 20; i++) {//do tests with 20 different threshold vals
            long totalTime = 0;
            int nrTries = 20;
            for (int k = 0; k < nrTries; k++) {//get average out of nrTries tries
                ForkJoinPool pool = new ForkJoinPool(MAXCORES);
                MergeSortingTask.setArrayNumbers(daArray);
                MergeSortingTask.setHelper(new float[daArray.length]);
                long start = System.currentTimeMillis();
                MergeSortingTask rootTask = new MergeSortingTask(0, daArray.length - 1,threshold);
                pool.invoke(rootTask);
                long stop = System.currentTimeMillis();
                totalTime += (stop - start);
            }
            double avg = totalTime / nrTries;
            if (avg < lowestAvg) {
                lowestAvg = avg;
                bestThreshold = threshold;
            }
            threshold += incrementVal; //increment threshold for next loop
        }
        //use bestThreshold for next step of the test
        System.out.println("Best threshold: " + bestThreshold);
        System.out.println();

        for(int nrCores = 1; nrCores <= MAXCORES; nrCores++){//test 1 to MAXCORES
            System.out.println("Testing sort with " + nrCores + " cores");
            long totalTime = 0;
            int nrTries = 20;
            for(int k = 0; k < nrTries; k++){//do 20 tests and calculate average

                totalTime += executeMergeSortForkJoin(daArray.clone(),nrCores,bestThreshold);

            }
            double avg = totalTime/nrTries;
            System.out.println("Average sort time: " + avg + " ms.");
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }*/

        ForkJoinPool pool = new ForkJoinPool(MAXCORES);
        MergeSortingTask.setArrayNumbers(daArray);
        MergeSortingTask.setHelper(new float[daArray.length]);
        long start = System.currentTimeMillis();
        MergeSortingTask rootTask = new MergeSortingTask(0,daArray.length-1,threshold);
        pool.invoke(rootTask);
        long stop = System.currentTimeMillis();
        //System.out.println("Merge Sort - ForkJoin: " + daArray.length + " length " + ": " + (stop-start) + " ms");

        //System.out.println("Is correctly sorted: " + checkIfCorrect(daArray));
        //System.out.println();
        //System.out.println();
    }

    /**
     * Test of our implementation of quick sort fork join
     * @param daArray
     */

    private static void testQuickSortForkJoin(float[] daArray,int threshold){
        //System.out.println("------ Quick sort - ForkJoin ------");

        System.gc();

        ForkJoinPool pool = new ForkJoinPool(MAXCORES);
        QuickSorterTask rootTask = new QuickSorterTask(0,daArray.length-1,daArray,threshold);
        long start = System.currentTimeMillis();
        pool.invoke(rootTask);
        long stop = System.currentTimeMillis();
        //System.out.println("Quick Sort - ForkJoin: " + daArray.length + " length " + ": " + (stop-start) + " ms");

        //System.out.println("Is correctly sorted: " + checkIfCorrect(daArray));
        //System.out.println();
        //System.out.println();
    }
    
    
    


    private static boolean checkIfCorrect(float[] array){
        for(int i = 0,j=1;i<array.length-1;i++,j++){
            if(array[i]>array[j])
                return false;
        }
        return true;
    }

}
