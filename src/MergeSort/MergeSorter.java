package MergeSort;

public class MergeSorter {

    private int number;


    private void mergeSorter(int arrayNumbers[], int low, int high){
        if(low < high){
            int middle = (low + (high-low))/2;
            mergeSorter(arrayNumbers,low,middle);

            mergeSorter(arrayNumbers,middle+1,high);

            merge(low,middle,high,arrayNumbers.length);
        }
    }

    private void merge(int low, int middle, int high,int[] arrayNumbers){
        int helper[] = new int[arrayNumbers.length];

        for(int i = 0; i < arrayNumbers.length;i++){
            helper[i] = arrayNumbers[i];
        }

        int i=low,j=middle+1,k=low;

        while (i <= middle && j <= high) {
            if (helper[i] <= helper[j]) {
                numbers[k] = helper[i];
                i++;
            } else {
                numbers[k] = helper[j];
                j++;
            }
            k++;
        }
        // Copy the rest of the left side of the array into the target array
        while (i <= middle) {
            numbers[k] = helper[i];
            k++;
            i++;
        }

    }

}
