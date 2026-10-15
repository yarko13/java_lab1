public final class Complex {
    private static final double EPS = 1e-9;

    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public static Complex parse(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Пустая строка вместо числа");
        }

        s = s.replace(" ", "").replace(',', '.');
        if (s.isEmpty()) {
            throw new IllegalArgumentException("Пустая строка вместо числа");
        }

        if (s.endsWith("i") == false) {
            return new Complex(Double.parseDouble(s), 0);
        }

        String body = s.substring(0, s.length() - 1);

        int idx = Math.max(body.lastIndexOf('+'), body.lastIndexOf('-'));

        String re_Str;
        String im_Str;
        if (idx <= 0) {
            re_Str = "0";
            im_Str = body;
        } else {
            re_Str = body.substring(0, idx);
            im_Str = body.substring(idx);
        }

        if (im_Str.isEmpty() || im_Str.equals("+")) {
            im_Str = "1";
        } else if (im_Str.equals("-")) {
            im_Str = "-1";
        }

        return new Complex(Double.parseDouble(re_Str), Double.parseDouble(im_Str));
    }

    public double getRe() {
        return re;
    }
    public double getIm() {
        return im;
    }

    public Complex add(Complex other){
        return new Complex(this.re + other.re, this.im + other.im);
    }
    public Complex subtract(Complex other){
        return new Complex(this.re - other.re, this.im - other.im);
    }
    public Complex multiply(Complex other){
        double a1 = this.re * other.re - this.im * other.im;
        double a2 = this.re * other.im + this.im * other.re;
        return new Complex(a1, a2);
    }
    public Complex divide(Complex other){
        if (other.isZero()) {
            throw new ArithmeticException("А-та-та! Произошло деление на ноль!");
        }
        double delit = other.im*other.im + other.re*other.re;
        double a1 = (this.re*other.re + this.im*other.im)/delit;
        double a2 = (other.re*this.im - this.re*other.im)/delit;
        return new Complex(a1, a2);
    }

    public double abs(){
        double ans = Math.sqrt(re*re + im*im);
        return ans;
    }

    public boolean isZero() {
        return Math.abs(re) < EPS && Math.abs(im) < EPS;
    }

    private static double clean(double x) {
        if (Math.abs(x) < EPS) {
            return 0;
        }
        return Math.round(x * 1e6) / 1e6;
    }

    private static String fmt(double x) {
        if (x == Math.rint(x)) {
            return String.valueOf((long) x);
        }
        return String.valueOf(x);
    }

    @Override public String toString() {
        double r = clean(re);
        double i = clean(im);
        if (i >= 0) {
            return fmt(r) + " + " + fmt(i) + "i";
        } else {
            return fmt(r) + " - " + fmt(-i) + "i";
        }
    }
}