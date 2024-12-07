public class Znaika extends ShortyMan {
    private double attentiveness = Math.random();
    private double inculcation;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Znaika z){
            return z.attentiveness == this.attentiveness;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 1000+(int) this.attentiveness*100;
    }

    @Override
    public String toString() {
        return "Коротыш Знайка";
    }
    public double getInculcation(){
        return inculcation;
    }
    RemoteController ZnaikaRemoteToSwithOn = new RemoteController(){
        @Override
        public void  switchOn(){
            if (Math.random()>0.1f) {
                Rock.changeGravityToLow();
                System.out.println("Гравитация была отключена");
            }
            else{
                System.out.println("Что-то пошло не так, гравитация не отключилась");
            }

        }

        @Override
        public void switchOff() {
            Rock.changeGravityToHigh();
            System.out.println("Гравитация была включена");
        }
    };

    public void useRemoteToSwithOn(){
        ZnaikaRemoteToSwithOn.switchOn();
    }

    void useRemoteToSwithOff(){
        ZnaikaRemoteToSwithOn.switchOff();
    }

    public void announceClosure(){
        System.out.println("Знайка оповещает о закрытии павилльона");
        this.inculcation = Math.random();
    }

    public boolean lookForHider(ShortyHider ShortyHider){

        if (ShortyHider.getStealth() < this.attentiveness && ShortyHider.getLocation() == this.location) {
            System.out.printf("Знайка нашел %s\n", ShortyHider.toString());
            return true;
        }
        System.out.printf("Знайка не заметил %s\n", ShortyHider.toString());
        return false;

    }

}
