public class ComplexMatrix {

    private final int rows;
    private final int cols;
    private final Complex[][] data;

    public ComplexMatrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Вы что-то перепутали... Недопустимый размер матрицы");
        }
        this.rows = rows;
        this.cols = cols;
        this.data = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] = new Complex(0, 0);
            }
        }
     }

    public int getRows(){
        return rows;
    }
    public int getCols() {
        return cols;
    }
    public Complex get(int i, int j) {
        if (i < 0 || i >= rows || j < 0 || j >= cols) {
            throw new IndexOutOfBoundsException("Ох-ох-ох... Выход за пределы матрицы");
        }
        return data[i][j];
    }
    public void set(int i, int j, Complex value) {
        if (i < 0 || i >= rows || j < 0 || j >= cols) {
            throw new IndexOutOfBoundsException("Ох-ох-ох... Выход за пределы матрицы");
        }
        if (value == null) {
            throw new IllegalArgumentException("Тревога! Значение не может быть null");
        }
        data[i][j] = value;
    }

    @Override public String toString() {
        StringBuilder stroka = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            stroka.append("[ ");
            for (int j = 0; j < cols; j++) {
                stroka.append(data[i][j]);
                if (j < cols - 1) {
                    stroka.append("\t");
                }
            }
            stroka.append("]\n");
        }
        return stroka.toString();
    }

    public ComplexMatrix add(ComplexMatrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Воу.. потише! Недопустимый размер матриц для сложения");
        }
        ComplexMatrix result = new ComplexMatrix(this.rows, this.cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.set(i, j, this.get(i, j).add(other.get(i, j)));
            }
        }
        return result;
    }

    public ComplexMatrix multiply(ComplexMatrix other) {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException("Тих-тих-тих... Недопустимый размер матриц для умножения");
        }
        ComplexMatrix result = new ComplexMatrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                Complex sum = new Complex(0, 0);
                for (int k = 0; k < this.cols; k++) {
                    sum = sum.add(this.get(i, k).multiply(other.get(k, j)));
                }
                result.set(i, j, sum);
            }
        }
        return result;
    }

    public ComplexMatrix transpose() {
        ComplexMatrix result = new ComplexMatrix(cols, rows);
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                result.set(i,j,this.get(j, i));
            }
        }
        return result;
    }

    public Complex determinant() {
        if (rows != cols) {
            throw new IllegalArgumentException("Это не законно! Определитель существует только у квадратной матрицы");
        }
        int n = rows;
        Complex[][] m = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = data[i][j];
            }
        }

        Complex det = new Complex(1, 0);

        for (int col = 0; col < n; col++) {
            int pivot = col;
            for (int r = col + 1; r < n; r++) {
                if (m[r][col].abs() > m[pivot][col].abs()) {
                    pivot = r;
                }
            }

            if (m[pivot][col].isZero()) {
                return new Complex(0, 0);
            }

            if (pivot != col) {
                Complex[] tmp = m[col];
                m[col] = m[pivot];
                m[pivot] = tmp;
                det = det.multiply(new Complex(-1, 0));
            }

            det = det.multiply(m[col][col]);

            for (int r = col + 1; r < n; r++) {
                Complex factor = m[r][col].divide(m[col][col]);
                for (int c = col; c < n; c++) {
                    m[r][c] = m[r][c].subtract(factor.multiply(m[col][c]));
                }
            }
        }
        return det;
    }

    public ComplexMatrix inverse() {
        if (rows != cols) {
            throw new IllegalArgumentException("Обратная матрица существует только у квадратной матрицы, Вы что...");
        }
        int n = rows;
        Complex[][] m = new Complex[n][2 * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = data[i][j];
                if (i == j) {
                    m[i][n + j] = new Complex(1, 0);
                } else {
                    m[i][n + j] = new Complex(0, 0);
                }
            }
        }

        for (int col = 0; col < n; col++) {
            int pivot = col;
            for (int r = col + 1; r < n; r++) {
                if (m[r][col].abs() > m[pivot][col].abs()) {
                    pivot = r;
                }
            }

            if (m[pivot][col].isZero()) {
                throw new ArithmeticException("Матрица вырожденная, обратной никак не существует");
            }

            if (pivot != col) {
                Complex[] tmp = m[col];
                m[col] = m[pivot];
                m[pivot] = tmp;
            }

            Complex p = m[col][col];
            for (int c = 0; c < 2 * n; c++) {
                m[col][c] = m[col][c].divide(p);
            }

            for (int r = 0; r < n; r++) {
                if (r != col) {
                    Complex factor = m[r][col];
                    for (int c = 0; c < 2 * n; c++) {
                        m[r][c] = m[r][c].subtract(factor.multiply(m[col][c]));
                    }
                }
            }
        }

        ComplexMatrix result = new ComplexMatrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result.set(i, j, m[i][n + j]);
            }
        }
        return result;
    }

    public ComplexMatrix divide(ComplexMatrix other) {
        return this.multiply(other.inverse());
    }
}