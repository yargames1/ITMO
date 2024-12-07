import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.print("Введите сообщение: ");
            String message = in.nextLine();
            String right_message = check(message);
            System.out.printf("Правильное сообщение: \n%s\n", right_message);
        }
    }
    static String check(String message){
        String s;
        int r1 = (int) message.charAt(0);// 0 = 48, 1 = 49
        int r2 = (int) message.charAt(1);
        int i1 = (int) message.charAt(2);
        int r3 = (int) message.charAt(3);
        int i2 = (int) message.charAt(4);
        int i3 = (int) message.charAt(5);
        int i4 = (int) message.charAt(6);
        String s1 = String.valueOf((r1+i1+i2+i4) % 2);
        String s2 =  String.valueOf((r2+i1+i3+i4) % 2);
        String s3 =  String.valueOf((r3+i2+i3+i4) % 2);
        int check = Integer.valueOf(s1+s2+s3, 2);
        s = switch (check) {
            case (4) -> String.format("%d%d%d%d", i1-48, i2-48, i3-48, i4-48)+"\nОшибка в бите 1";
            case (2) -> String.format("%d%d%d%d", i1-48, i2-48, i3-48, i4-48)+"\nОшибка в бите 2";
            case (6) -> String.format("%d%d%d%d", Math.abs(i1-48-1), i2, i3, i4)+"\nОшибка в бите 3";
            case (1) -> String.format("%d%d%d%d", i1-48, i2-48, i3-48, i4-48)+"\nОшибка в бите 4";
            case (5) -> String.format("%d%d%d%d", i1-48, Math.abs(i2-48-1), i3-48, i4-48)+"\nОшибка в бите 5";
            case (3) -> String.format("%d%d%d%d", i1-48, i2-48, Math.abs(i3-48-1), i4-48)+"\nОшибка в бите 6";
            case (7) -> String.format("%d%d%d%d", i1-48, i2-48, i3-48, Math.abs(i1-48-1))+"\nОшибка в бите 7";
            default -> String.format("%d%d%d%d", i1-48, i2-48, i3-48, i4-48)+"\nОшибок нет";
        };
        return s;
    }
}