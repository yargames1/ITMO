public class Complex {
    double re, im;
    Complex(double re, double im) { this.re = re; this.im = im; }

    Complex plus(Complex c) { return new Complex(re + c.re, im + c.im); }
    Complex minus(Complex c) { return new Complex(re - c.re, im - c.im); }
    Complex times(Complex c) { return new Complex(re * c.re - im * c.im, re * c.im + im * c.re); }
    Complex divide(Complex c) {
        double denom = c.re * c.re + c.im * c.im;
        return new Complex((re * c.re + im * c.im) / denom, (im * c.re - re * c.im) / denom);
    }
    Complex pow(int n) {
        Complex result = new Complex(1, 0);
        for (int i = 0; i < n; i++) result = result.times(this);
        return result;
    }
    double abs() { return Math.hypot(re, im); }
}