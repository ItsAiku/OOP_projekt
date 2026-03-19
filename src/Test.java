import java.util.Scanner;
import java.util.Random;

public class Test {

    public static void kuvaSeis (StartUp startup){
        System.out.println("\n------ SUMMARY ------");
        System.out.println("Kapital: " + startup.getKapital());
        System.out.println("Kliendid: " + startup.getKlientideArv());
        System.out.println("Töötajad: " + startup.getTöötajad().size());
        System.out.println("Tulu kliendi kohta: " + startup.getTuluKliendiKohta());

        System.out.println("\nTöötajate list:");
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

        System.out.println("Tere tulemast Startup Simulaatorisse!");

        StartUp startup = new StartUp(10000);

        System.out.print("Sisesta oma esimese töötaja nimi: ");
        String nimi = scanner.nextLine();
        Töötaja esimene = new Töötaja(nimi, 1);
        startup.lisaTöötaja(esimene);

        boolean gameRunning = true;

        while (gameRunning) {

            // kontrolli kapitali enne iga käiku
            check = new KapitalCheck();
            check.kontrolliKapitali(startup);


            // menüü
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
                startup.setKapital(startup.getKapital()-t.getPalk());
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
                    System.out.println("Müüsid firma!");
                    kuvaSeis(startup);
                    gameRunning = false;
                    break;

                default:
                    System.out.println("Vale valik!");


            }
            double töökuseSumma = 0;
            for (Töötaja t : startup.getTöötajad()){
                töökuseSumma += t.getTöökus();
            }
            double avgTöökus = töökuseSumma/startup.getTöötajad().size();
            startup.setTuluKliendiKohta((int) (startup.getBaseTuluKliendiKohta()*avgTöökus));

            // iga käigu lõpus teenid raha klientidelt
            int tulu = startup.getKlientideArv() * startup.getTuluKliendiKohta();
            startup.suurendaKapital(tulu);

            System.out.println("Teenitud tulu: " + tulu);

            // game over check
            if (startup.getKapital() <= 0) {
                System.out.println("Sul sai raha otsa! Game over.");
                gameRunning = false;
            }
        }

        scanner.close();
        System.out.println("Aitäh mängimast!");
    }
}


