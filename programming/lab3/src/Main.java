import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<ShortyHider> Hiders = new ArrayList<>();
        ArrayList<String> ExitedHiders = new ArrayList<>();
        ShortyHider ShortyHider_1 = new ShortyHider("Валера");
        ShortyHider ShortyHider_2 = new ShortyHider("Аркадий");
        ShortyHider ShortyHider_3 = new ShortyHider("Виталий");
        ShortyHider ShortyHider_4 = new ShortyHider("Данил");
        ShortyHider ShortyHider_5 = new ShortyHider("Ибрагим");

        Hiders.add(ShortyHider_1);
        Hiders.add(ShortyHider_2);
        Hiders.add(ShortyHider_3);
        Hiders.add(ShortyHider_4);
        Hiders.add(ShortyHider_5);

        Znaika Znaika = new Znaika();

        Znaika.useRemoteToSwithOn();

        System.out.println("Коротыши разбежались по павильону");
        for (ShortyHider hider : Hiders) {
            hider.hide();
        }

        Znaika.announceClosure();
        int size = Hiders.size();
        int i = 0;
        while (i < size){
            try {
                if (Hiders.get(i).listenToSeeker(Znaika.getInculcation())) {  // реализовать прогон по всем локам и прятальщикам
                    ExitedHiders.add(Hiders.get(i).toString() + " вышел самостоятельно после того, как узнал о закрытии павильона");
                    Hiders.remove(Hiders.get(i));
                    i --;
                }
            } catch (HiderDidNotHear e){
                System.out.println(e.getMessage());
            }
            i++;
            size = Hiders.size();
        }

        i = 0;
        while (i < size){

            if (Hiders.contains(Hiders.get(i))) {
                Znaika.moveTo(Hiders.get(i).getLocation());
                boolean try_to_find_hider = Znaika.lookForHider(Hiders.get(i));
                try {
                    WereChecked Hider_2 = new WereChecked(Hiders.get(i), try_to_find_hider);
                    ExitedHiders.add(Hider_2.ShortyHider().toString() + " был выведен знайкой");
                    System.out.printf("%s был уведен из павильона\n", Hiders.get(i).toString());
                    Hiders.remove(Hiders.get(i));
                    i--;
                } catch (HiderWasNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                i++;
                size = Hiders.size();
            }
        }

        Znaika.useRemoteToSwithOff();
        i = 0;
        while (i < size){
            if (Hiders.contains(Hiders.get(i))) {
                Hiders.get(i).fall();
                if (Hiders.get(i).getMobility()){
                    ExitedHiders.add(Hiders.get(i).toString() + " вышел самостоятельно после того, как гравитацию отключили");
                    System.out.printf("%s вышел из павильона самостоятельно\n", Hiders.get(i).toString());
                    Hiders.remove(Hiders.get(i));
                    i --;
                }
                else {
                    System.out.printf("%s не может двигаться из-за повреждений, поэтому не выйдет из павильона", Hiders.get(i).toString());
                }
            }
            i++;
            size = Hiders.size();
        }
        System.out.println("Знайка закрыл павильон до утра");

        System.out.println("Итоги:");
        for (String info: ExitedHiders){
            System.out.println(info);
        }
        for (ShortyHider Hider : Hiders){
            System.out.printf("%s не покинул павильон и остался там до утра\n", Hider.toString());
        }
    }
}