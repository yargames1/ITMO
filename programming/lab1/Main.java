import java.util.Arrays;


public class Main {

	
	public static void main(String[] args) {
		// Пункт 1
		int[] f = new int[18]; // Создать одномерный массив f типа int
		for (int i = 0; i<=17; i++) { // Заполнить его числами от 3 до 20 включительно в порядке убывания
			f[i] = (int) (20 - i);
		}
		// Пункт 2
		Random random = new Random();
		double[] x = new double[18]; // Создать одномерный массив x типа double.
		for (int i = 0; i<=17; i++){ // Заполнить его 18-ю случайными числами в диапазоне от -4.0 до 12.0.
			x[i] = (double) random.nextDouble()*16 - 4;
		}
		System.out.println(Arrays.toString(x));
		
	}
	

}
