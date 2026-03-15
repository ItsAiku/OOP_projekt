public class Test {
    public static void main(String[] args) {
        Töötaja töötaja = new Töötaja("ats", 1);
        StartUp ahv = new StartUp();
        ahv.lisaTöötaja(töötaja);
        Tegevus tegevus = new Tegevus();
        tegevus.perkid(2,ahv);
        tegevus.perkid(27, ahv);
        tegevus.perkid(50,ahv);
        tegevus.perkid(75, ahv);
        tegevus.perkid(80, ahv);
        System.out.println(ahv.getKlientideArv());
        System.out.println(ahv.getTöötajad());
        System.out.println(ahv.getKapital());
    }
}
