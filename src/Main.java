import java.util.Scanner;
import java.util.Random;

public class Main {

    public static void kuvaSeis (StartUp startup){
        System.out.println("\n------ SUMMARY ------");//annab ülevaate hetkeseisust
        System.out.println("Kapital: " + startup.getKapital());
        System.out.println("Kliendid: " + startup.getKlientideArv());
        System.out.println("Töötajad: " + startup.getTöötajad().size());
        System.out.println("Tulu kliendi kohta: " + startup.getTuluKliendiKohta());

        System.out.println("\nTöötajate list:");//annab ülevaate töötajatest
        for (Töötaja t : startup.getTöötajad()) {
            System.out.println("- " + t.getNimi() +
                    " | palk: " + t.getPalk() +
                    " | töökus: " + t.getTöökus());
        }
        System.out.println("----------------------\n");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Tegevus tegevus = new Tegevus();
        KapitalCheck check = new KapitalCheck();
        Random rand = new Random();
        boolean gameRunning = true;


        System.out.println("Tere tulemast Startup Simulaatorisse!");

        StartUp startup = new StartUp(10000);//luuakse ettevõtte algkapitalige 10000, on olemas ka random kapitaliga konstruktor mida praegu ei kasutata

        System.out.print("Sisesta oma esimese töötaja nimi: ");//luuakse esimene töötaja
        String nimi = scanner.nextLine();
        Töötaja esimene = new Töötaja(nimi, 1);
        startup.lisaTöötaja(esimene);

        while (gameRunning) {

            // kontrolli kapitali enne iga käiku
            check.kontrolliKapitali(startup);


            // menüü, kust saab tegevusi valida
            System.out.println("\nVali tegevus:");
            System.out.println("1 - Töötaja palkamine");
            System.out.println("2 - Turunduskampaania");
            System.out.println("3 - Töötaja töökuse reroll");
            System.out.println("4 - Perkid");
            System.out.println("5 - Skip");
            System.out.println("6 - Müü firma");

            System.out.print("Sisesta valik: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Vigane sisend!");
                continue;
            }

            for (Töötaja t : startup.getTöötajad()){
                startup.setKapital(startup.getKapital()-t.getPalk());//iga kuu makstakse töötajatele palka, mis võetakse kapitalist maha
            }
            switch (choice) {

                case 1:
                    if (!check.isTöötajaPalkamine()) {
                        System.out.println("Pole piisavalt raha töötaja palkamiseks!");
                        kuvaSeis(startup);
                        break;
                    }
                    System.out.print("Sisesta töötaja nimi: ");
                    String töötajaNimi = scanner.nextLine();
                    tegevus.töötajaPalkamine(startup, töötajaNimi);
                    startup.lisaTöötaja(new Töötaja(töötajaNimi, startup.getTöötajad().size()));
                    System.out.println("Palkasid uue töötaja!");
                    kuvaSeis(startup);
                    break;

                case 2:
                    if (!check.isTurundusKampaania()) {
                        System.out.println("Pole piisavalt raha kampaaniaks!");
                        kuvaSeis(startup);
                        break;
                    }
                    tegevus.turundusKampaania(startup);
                    System.out.println("Tegid turunduskampaania!");
                    kuvaSeis(startup);
                    break;

                case 3:
                    if (!check.isTöökuseReroll()) {
                        System.out.println("Pole piisavalt raha rerolliks!");
                        kuvaSeis(startup);
                        break;
                    }
                    System.out.print("Sisesta töötaja nimi: ");
                    String nimiReroll = scanner.nextLine();
                    tegevus.töökuseReroll(startup, nimiReroll);
                    System.out.println("Reroll tehtud!");
                    kuvaSeis(startup);
                    break;

                case 4:
                    if (!check.isPerkid()) {
                        System.out.println("Pole piisavalt raha perkideks!");
                        kuvaSeis(startup);
                        break;
                    }
                    int number = rand.nextInt(100) + 1;
                    tegevus.perkid(number, startup);
                    kuvaSeis(startup);
                    break;

                case 5:
                    System.out.println("Skipid käigu...");
                    kuvaSeis(startup);
                    break;

                case 6:
                    System.out.println("Müüsid firma! " + startup.getKapital() + " euroga maha.");
                    gameRunning = false;
                    break;

                default:
                    System.out.println("Vale valik!");


            }
            //iga kuu lõpus arvutatakse uuesti keskmine töökus ja selle põhjal tulu kliendi kohta
            //keskminst töökust muudavad uue töötaja palkamine ja töötaja reroll
            double töökuseSumma = 0;
            for (Töötaja t : startup.getTöötajad()){
                töökuseSumma += t.getTöökus();
            }
            double avgTöökus = töökuseSumma/startup.getTöötajad().size();
            startup.setTuluKliendiKohta((int) (startup.getBaseTuluKliendiKohta()*avgTöökus));

            //klientide "churn" ehk 5% kliente lahkub iga kuu
            startup.setKliendid((int) (startup.getKlientideArv()*0.95));

            // iga käigu lõpus teenid raha klientidelt
            int tulu = startup.getKlientideArv() * startup.getTuluKliendiKohta();
            startup.suurendaKapital(tulu);

            System.out.println("Teenitud tulu: " + tulu);

            // game over check
            if (startup.getKapital() <= 0) {
                System.out.println("Sul sai raha otsa! Mäng läbi.");
                gameRunning = false;
            }
        }

        scanner.close();
    }
}