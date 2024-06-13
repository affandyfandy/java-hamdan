import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class assignment3{
    public static List<Integer> findSecondBiggest(int[] arr){
        int biggest = arr[0];
        int secondBiggest = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if(arr[i] > biggest){
                secondBiggest = biggest;
                biggest = arr[i];
            } else if(arr[i] > secondBiggest){
                secondBiggest = arr[i];
            }
        }
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < arr.length; i++){
            if (arr[i] == secondBiggest){
                list.add(i);
            }
        }
        return list;
    }
    public static void main(String[] args) {
        int[] arr = {1, 4, 3, -6, 5, 4, 0};
        List<Integer> result = findSecondBiggest(arr);
        System.out.println(Arrays.toString(result.toArray()));
            
    }
}