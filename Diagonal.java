import java.util.Scanner;

public class Diagonal {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System. in);
        System.out.println("Введите число 1 <= n <= 50");
        int n = scanner.nextInt();
        if (n < 1 || n >50) {
            System.out.println("n должно быть от 1 до 50");
            return;
        }
            int[][] a = new int[n][n];
            int num = 1;

            for (int k = 0; k < n * 2 - 1; k++) {
                if (k % 2 == 0) {

                    for (int j = Math.max(0, k - n + 1); j <= Math.min(k, n - 1); j++) {
                        int i = k - j;
                        a[i][j] = num++;
                    }
                }
                else {
                    for (int i = Math.max(0, k - n + 1); i <= Math.min(k, n - 1); i++) {
                        int j = k - i;
                        a[i][j] = num++;
                    }
                }
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.printf("%4d", a[i][j]);
                }
                System.out.println();
        }

    }
}
