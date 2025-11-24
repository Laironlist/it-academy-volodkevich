import java.util.Scanner;

public class Factorial {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long N;
        while (true) {
            System.out.println("Введите число N (1 <= N <= 14): ");
            if (scanner.hasNextInt()) {
                N = scanner.nextInt();
                if (N >= 1 && N <= 14) {
                    break;
                } else {
                    System.out.println("Ошибка: число должно быть больше 1 и меньше 14!");
                }
            } else {
                System.out.println("Ошибка: введите целое число!");
                scanner.next();
            }
        }
        long factorial = 1;
        for (long i = 2; i <= N; i++) {
            factorial *= i;
        }
        System.out.println(N + " = " + factorial);

    }
}