package org.itmo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class GigaTest {
    private GigaExecutor executor;

    private static String sortArr;

    private static String boolArr;

    private static String generateArray(Integer size, Boolean shuffle, Boolean ones) {
        List<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            if (ones) {
                numbers.add(1);
            } else {
                numbers.add(i);
            }
        }

        if (shuffle) {
            Collections.shuffle(numbers);
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < numbers.size(); i++) {
            sb.append(numbers.get(i));
            if (i < numbers.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");

        return sb.toString();
    }

    @BeforeAll
    public static void init() {
        sortArr = generateArray(10000, true, false);
        boolArr = generateArray(100001, false, true);
    }

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
        String arrayInit = "ar = " + sortArr + ";";

        String code = arrayInit + """
                size = 10000;
                
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

                quickSort(0, size - 1)
                i = 0;
                while (i < size) {
                 print ar[i];
                 i = i + 1;
                }
                """;

        executor.executeCode(code);
    }

    @Test
    public void primeTest() {
        String arrayInit = "boolPrime = " + boolArr + ";";

        String code = arrayInit + """
                n = 100000;
                boolPrime[0] = 0;
                boolPrime[1] = 0;
                
                it = 2;
                while (it < √n + 1) {
                  if (boolPrime[it] == 1) {
                    notPrime = it * it;
                    while (notPrime < n + 1) {
                      boolPrime[notPrime] = 0;
                      notPrime = notPrime + it;
                    }
                  }
                  it = it + 1;
                }
                
                it = 0;
                while (it < n + 1) {
                  if (boolPrime[it] == 1) {
                    print it;
                  }
                  it = it + 1;
                }
                """;
        executor.executeCode(code);
    }

}
