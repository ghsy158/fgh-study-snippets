package com.fgh.alg.sort;

/**
 * 选择排序
 *
 * @author fgh
 * @since 2019/4/9 22:37
 */
public class SelectSort {

    /**
     * 交换数组中两个位置的值
     *
     * @param arr
     * @param i
     * @param j
     */
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[j];//5
        arr[j] = arr[i]; //
        arr[i] = temp;
    }

    public static void sort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minPos = i;
            for (int j = i + 1; j < arr.length; j++) {
                minPos = arr[j] < arr[minPos] ? j : minPos;
            }
            swap(arr, i, minPos);
            System.out.println("第" + i + "次循环之后，minPos=" + minPos + ",数组的内容");
            print(arr);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[]{6, 3, 5, 2, 7, 1, 9, 8, 4};
        sort(arr);
//        swap(arr, 1, 2);
//        print(arr);
    }

    private static void print(int[] arr) {
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }

}
