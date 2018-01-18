package MergeSort;

public class MergeSorter {

    public float[] mergeSorter(float arrayNumbers[]){
        return mergeSorter(arrayNumbers,0,arrayNumbers.length-1);
    }

    private float[] mergeSorter(float arrayNumbers[], int low, int high){
        if(low < high){
            int middle = low + (high-low)/2;
            mergeSorter(arrayNumbers,low,middle);

            mergeSorter(arrayNumbers,middle+1,high);

           return merge(low,middle,high,arrayNumbers);
        }
        return null;
    }

    private float[] merge(int low, int middle, int high,float[] arrayNumbers){
        float helper[] = new float[arrayNumbers.length];

        for(int i = 0; i < arrayNumbers.length;i++){
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
        return arrayNumbers;
    }

}
