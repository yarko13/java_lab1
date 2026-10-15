import java.util.Scanner;

public class Main {
    private static final Scanner in = new Scanner(System.in);

    private static ComplexMatrix last = null;

    public static void main(String[] args) {
        System.out.println("Калькулятор матриц с комплексными числами");
        System.out.println("Числа вводятся БЕЗ пробелов внутри, ОБЯЗАТЕЛЬНО - первым идёт рациональная часть, а затем мнимая: 3, -2.5, 2i, 1+2i, 3-4i");
        System.out.println("Элементы одной строки матрицы разделяются пробелами.");
        System.out.println();

        while (true) {
            System.out.print("Операция (+, *, /, T, det, inv, exit): ");
            String op = readLine().trim().toLowerCase();
            try {
                ComplexMatrix res = null;   // 1. объявлена ДО switch
                switch (op) {
                    case "+": {
                        ComplexMatrix a = readMatrix("A");
                        ComplexMatrix b = readMatrix("B");
                        res = a.add(b);               // 2. не println, а запись в res
                        break;
                    }
                    case "*": {
                        ComplexMatrix a = readMatrix("A");
                        ComplexMatrix b = readMatrix("B");
                        res = a.multiply(b);
                        break;
                    }
                    case "/": {
                        ComplexMatrix a = readMatrix("A");
                        ComplexMatrix b = readMatrix("B");
                        res = a.divide(b);
                        break;
                    }
                    case "t": {
                        ComplexMatrix a = readMatrix("A");
                        res = a.transpose();
                        break;
                    }
                    case "inv": {
                        ComplexMatrix a = readMatrix("A");
                        res = a.inverse();
                        break;
                    }
                    case "det": {
                        ComplexMatrix a = readMatrix("A");
                        Complex d = a.determinant();  // 3. число, а не матрица
                        System.out.println(d);
                        System.out.println();
                        last = new ComplexMatrix(1, 1);
                        last.set(0, 0, d);
                        break;
                    }
                    case "exit":
                        return;
                    default:
                        System.out.println("Неизвестная операция.");
                }
                if (res != null) {                    // 4. общий кусок ПОСЛЕ switch
                    System.out.println(res);
                    last = res;
                }
            } catch (IllegalArgumentException | ArithmeticException e) {
                System.out.println("Ошибка: " + e.getMessage());
                System.out.println();

            }
        }
    }
    private static String readLine() {
        if (!in.hasNextLine()) {
            System.out.println();
            System.exit(0);
        }
        return in.nextLine();
    }


    private static ComplexMatrix readMatrix(String name) {
        int r;
        while (true) {
            System.out.print("Матрица " + name + ", строк (или R): ");
            String line = readLine().trim();
            if (line.equalsIgnoreCase("r")) {
                if (last == null) {
                    throw new IllegalArgumentException("результата прошлой операции ещё нет");
                }
                return last;
            }
            try {
                r = Integer.parseInt(line);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Нужно целое число или R, попробуйте ещё раз.");
            }
        }
        if (r <= 0) {
            throw new IllegalArgumentException("Число строк должно быть положительным");
        }

        ComplexMatrix m = null;
        for (int i = 0; i < r; i++) {
            System.out.print(name + ", строка " + (i + 1) + ": ");
            String[] parts = readLine().trim().split("\\s+");

            if (m == null) {
                m = new ComplexMatrix(r, parts.length);
            } else if (parts.length != m.getCols()) {
                throw new IllegalArgumentException("в строке " + (i + 1) + " должно быть столько же элементов, сколько в первой ("
                        + m.getCols() + "), а введено " + parts.length);
            }

            for (int j = 0; j < parts.length; j++) {
                try {
                    m.set(i, j, Complex.parse(parts[j]));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("не удалось разобрать число \"" + parts[j] + "\"");
                }
            }
        }
        return m;
    }
}