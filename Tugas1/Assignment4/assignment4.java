public class assignment4 {
    public static int[] findSubarrayWithSumZero(int[] arr) {
        for (int start = 0; start < arr.length; start++) {
            int sum = 0;
            // Iterate from the start index to the end of the array
            for (int end = start; end < arr.length; end++) {
                sum += arr[end];
                // Check if the sum of the current subarray is zero
                if (sum == 0) {
                    return new int[]{start, end};
                }
            }
        }
        // Return an empty array if no subarray with sum zero is found
        System.out.println("No possible subarray, returning [-1,-1]");
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, -6, 5, 4, 0};
        int[] result = findSubarrayWithSumZero(arr);
        System.out.println("[" + result[0] + ", " + result[1] + "]");
    }
}
