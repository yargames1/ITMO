import java.util.List;
import java.util.Random;


public class Main {

	
	public static void main(String[] args) {
		// Пункт 1
		int[] n = new int[18]; // Создать одномерный массив n типа int
		for (int i = 20; i>=3; i--) { // Заполнить его числами от 3 до 20 включительно в порядке убывания
			n[20-i] = (int) (i);
		}
		// Пункт 2
		Random random = new Random();
		double[] x = new double[18]; // Создать одномерный массив x типа double.
		for (int i = 0; i<=17; i++){ // Заполнить его 18-ю случайными числами в диапазоне от -4.0 до 12.0.
			x[i] = (double) random.nextDouble()*16 - 4;
		}
		// Пункт 3
		double[][] w = new double[18][18];// Создать двумерный массив w размером 18x18.
		for (int i = 0; i<=17; i++) { //  Вычислить его элементы по следующей формуле
			for (int j = 0; j<= 17; j++) {
				w[i][j] = gen_new_el(n[i], x[j]);
			}
		}
		// Пункт 4
		print_answer(w);
		
	}
	
	
	static double gen_new_el(int n, double x) { // Вычисление очередного элемента двумерного массива должно быть реализовано в виде отдельного статического метода.
		List<Integer> list = List.of(3, 5, 6, 8, 9, 10, 12, 15, 20);
		if (n == 4) { // если n[i] = 4
			return Math.pow( Math.pow( ((1-x) / x), x) + 2  / Math.asin( (x+4)/16 ) , 3);
		}
		else if (list.contains(n)) { // если n[i] ∈ {3, 5, 6, 8, 9, 10, 12, 15, 20}
			return Math.pow( Math.asin( Math.pow( (x+4)/16 ,2) ) / 0.25f , 2);
		}
		else { // для остальных значений
			return Math.tan(Math.pow( 1/3 * Math.pow(x, (x-0.5f)/x ) ,2))/2;
		}
	}
	
	
	static void print_answer(double[][] w) { // Вывод матрицы реализовать в виде отдельного статического метода.
		for (int i = 0; i<=17; i++) {
			for (int j = 0; j<=17; j++) {
				System.out.printf("| %9.3f |", w[i][j]); // Напечатать полученный в результате массив в формате с тремя знаками после запятой.
			}
			System.out.print("\n");
		}
	}
	
	
}
