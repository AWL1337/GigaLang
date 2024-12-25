package org.itmo;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.itmo.antlr.MyLanguageLexer;



public class Main {

    //task 1
    public static Long fac(int n) {
        if (n < 2) {
            return 1L;
        }

        return fac(n - 1) * n;
    }

    //task 2 quicksort
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);


            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }


    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;


                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }


        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;


        return i + 1;
    }



//task 3 prime

    public static void prime(int limit) {
        int N = limit;
        boolean[] a = new boolean[N];
        for (int i = 2; i<N; i++) a[i] = true;
        for (int i = 2; i<N; i++)
            if (a[i] != false)
                for (int j = i; j*i < N; j++)
                    a[i*j] = false;
        for (int i = 2; i < N; i++)
            if (i > N - 100) // Зачем это проверять?
                if (a[i]) System.out.print(" " + i);
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}