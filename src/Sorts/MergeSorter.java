package Sorts;

public class MergeSorter {


    private static long start;
    private static long stop;

    private float helper[];
    public static long getTime(){
        return stop-start;
    }

    public void mergeSorter(float arrayNumbers[]){
        helper = new float[arrayNumbers.length];
        start = System.currentTimeMillis();
        mergeSorter(arrayNumbers,0,arrayNumbers.length-1);
        stop = System.currentTimeMillis();
    }

    private void mergeSorter(float arrayNumbers[], int low, int high){
        if(low < high){
            int middle = low + (high-low)/2;
            mergeSorter(arrayNumbers,low,middle);

            mergeSorter(arrayNumbers,middle+1,high);

            merge(low,middle,high,arrayNumbers);
        }
    }

    private void merge(int low, int middle, int high,float[] arrayNumbers){

        for(int i = low; i < high;i++){
            helper[i] = arrayNumbers[i];
        }

        int i=low,j=middle+1,k=low;

        while (i <= middle && j <= high) {
            if (helper[i] <= helper[j]) {
                arrayNumbers[k] = helper[i];
                i++;
            } else {
                arrayNumbers[k] = helper[j];
                j++;
            }
            k++;
        }

        while (i <= middle) {
            arrayNumbers[k] = helper[i];
            k++;
            i++;
        }
    }

}