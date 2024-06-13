import java.util.HashMap;

public class assignment4 {
    public static int[] findSubarrayWithSumZero(int[] arr) {
        HashMap<Integer, Integer> sumMap = new HashMap<>();
        int sum = 0;

        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            // System.out.println(sum);

            if (sum == 0) {
                return new int[]{0, i};
            }

            if (sumMap.containsKey(sum)) {
                return new int[]{sumMap.get(sum) + 1, i};
            }
        }
        System.out.println("No possible subarray, returning [-1,-1]");
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, -6, 5, 4, 0};
        int[] result = findSubarrayWithSumZero(arr);
        System.out.println("[" + result[0] + ", " + result[1] + "]");
    }
}