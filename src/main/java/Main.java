import java.util.Scanner;

import static java.lang.Math.min;

public class Main {
    private static int ele(int i, int j, int n) {
        int m, a, l;
        m = min(min(i, n - 1 - i), min(j, n - 1 - j));
        i -= m;
        j -= m;
        a = 1 + 4 * m * (n - m);
        l = n - 2 * m;
        if (i == 0)
            return a + j;
        else if (j == 0)
            return a + 4 * (l - 1) - i;
        else if (i == l - 1)
            return a + 4 * l - 3 - l - j;
        return a + l - 1 + i;
    }

    public static void main(String[] args) {
        int i, j, n;
        Scanner s = new Scanner(System.in);
        while (s.hasNext()) {
            n = s.nextInt();
            // 输出
            for (i = 0; i < n - 1; i++) {
                for (j = 0; j < n; j++) {
                    System.out.print(ele(i, j, n) + " ");
                }
            }
            for (j = 0; j < n - 1; j++) {
                System.out.print(ele(n - 1, j, n) + " ");
            }
            System.out.println(ele(n - 1, n - 1, n));
        }
    }
}
