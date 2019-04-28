package com.fgh.alg.sort;

/**
 * @author fgh
 * @since 2019/4/10 12:51
 */
public class QuickSort {

    public static void main(String[] args) {
//        int[] arr = {1, 4, 6, 9, 10, 2, 7, 3, 5, 8};
//        sort(arr, 0, arr.length - 1);
//        printArr(arr);

        int i = 0;
        int[] arr = new int[]{};
        arr[3] = 0;
        for (; i < 3; i++) {
            arr[i] = 0;
            System.out.println("hello world");
        }
    }

    public static void sort(int[] arr, int leftBound, int rightBound) {
        if (leftBound >= rightBound) return;
        int mid = partition(arr, leftBound, rightBound);
        sort(arr, leftBound, mid - 1);
        sort(arr, mid + 1, rightBound);

    }

    public static int partition(int[] arr, int leftBound, int rightBound) {
        int pivot = arr[rightBound];
        int left = leftBound;
        int right = rightBound - 1;

        while (left <= right) {
            while (left <= right && arr[left] <= pivot) left++;
            while (left <= right && arr[right] > pivot) right--;
            if (left < right) {
                swap(arr, left, right);
            }
        }
        swap(arr, left, rightBound);
        return left;
    }

//    private void partion

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }

    private static void printArr(int[] arr) {
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }

}
