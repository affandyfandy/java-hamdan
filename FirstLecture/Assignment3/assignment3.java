import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class assignment3 {
    public static List<Integer> findSecondBiggest(int[] arr) {
        int biggest = Integer.MIN_VALUE;
        int secondBiggest = Integer.MIN_VALUE;
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > biggest) {
                secondBiggest = biggest;
                biggest = arr[i];
                indices.clear(); // clear indices since new secondBiggest is found
            } else if (arr[i] > secondBiggest && arr[i] != biggest) {
                secondBiggest = arr[i];
                indices.clear(); // clear indices since new secondBiggest is found
                indices.add(i); // add current index as first occurrence of new secondBiggest
            } else if (arr[i] == secondBiggest) {
                indices.add(i); // add current index as another occurrence of secondBiggest
            }
        }

        return indices;
    }

    public static void main(String[] args) {
        int[] arr = {1, 4, 3, -6, 5, 4, 0};
        List<Integer> result = findSecondBiggest(arr);
        System.out.println(Arrays.toString(result.toArray()));
    }
}
