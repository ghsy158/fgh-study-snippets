package com.fgh.alg.sort;

/**
 * ѡ������
 *
 * @author fgh
 * @since 2019/4/9 22:37
 */
public class SelectSort {

    /**
     * ��������������λ�õ�ֵ
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
            System.out.println("��" + i + "��ѭ��֮��minPos=" + minPos + ",���������");
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
