import java.util.Arrays;


public class Main {

	
	public static void main(String[] args) {
		// Пункт 1
		int[] f = new int[18]; // Создать одномерный массив f типа int
		for (int i = 0; i<=17; i++) { // Заполнить его числами от 3 до 20 включительно в порядке убывания
			f[i] = (int) (20 - i);
		}
		System.out.println(Arrays.toString(f));
		
	}
	

}
