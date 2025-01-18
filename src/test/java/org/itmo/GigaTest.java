package org.itmo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GigaTest {
    private GigaExecutor executor;

    @BeforeEach
    public void setUp() {
        executor = new GigaExecutor();
    }

    @Test
    public void factorialTest() {
        String code = """
                def factorial(n) {
                  if (n < 2) {
                    return 1;
                  }
                  return n * factorial(n-1);
                }
                
                print factorial(20);
                """;

        executor.executeCode(code);
    }

    @Test
    public void quickSortTest() {
        String code = """
                ar = [5, 8, 4, 0, 6];
                
                def swap(a, b) {
                  tmp = ar[b];
                  ar[b] = ar[a];
                  ar[a] = tmp;
                }
                
                def quickSort(start, stop) {
                
                  if (start < stop) {
                    pivot = partition(start, stop);
                    quickSort(start, pivot)
                    quickSort(pivot+1, stop)
                  }
                }
                
                def partition(start, stop) {
                  pivot = ar[(start + stop) / 2];
                  left = start - 1;
                  right = stop + 1;

                  while (1 > 0) {

                    left = left + 1;

                    while (ar[left] < pivot) {
                      left = left + 1;
                    }

                    right = right - 1;

                    while (ar[right] > pivot) {
                      right = right - 1;
                    }

                    if (left >= right) {
                      return right;
                    }
                    swap(left, right)
                  }
                }

                quickSort(0, 4)
                i = 0;
                while (i < 5) {
                 print ar[i];
                 i = i + 1;
                }
                """;

        executor.executeCode(code);
    }


}
