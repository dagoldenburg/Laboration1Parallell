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
public class Quicksort {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int[] daArray = {1,2,5,9,10,6,355,55,65,1024,19,27,69,1337,1312,88};
        
        QuickSorter sorter = new QuickSorter();
        sorter.quicksort(daArray);
        
    }
    
}
