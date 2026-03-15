public class Test {
    public static void main(String[] args) {
        StartUp ahv = new StartUp();
        Tegevus tegevus = new Tegevus();
        tegevus.perkid(7,ahv);
        System.out.println(ahv.getKlientideArv());
    }
}
