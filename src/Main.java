import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Algus
        System.out.println("Tere tulemast Startup Simulaatorisse!");
        StartUp startup = new StartUp(5000);
        System.out.println("Sisesta oma esimese töötaja nimi: ");
        String nimi = scanner.nextLine();
        Töötaja esimene = new Töötaja(nimi, 1);
        startup.lisaTöötaja(esimene);
        System.out.println("--------------------------------------");

        boolean gameRunning = true;

        while (gameRunning) {
            // Menüü
            System.out.println("\nVali tegevus:");
            System.out.println("1 - Töötaja palkamine");
            System.out.println("2 - Turunduskampaania");
            System.out.println("3 - Töötaja töökuse reroll");
            System.out.println("4 - Perkid / boonused");
            System.out.println("5 - Skip (järgmine käik)");
            System.out.println("6 - Müü firma");

            System.out.print("Sisesta valik: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Vigane sisend! Proovi uuesti.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("Selleks, et palgata töötajat. Peate sisestama nime: ");
                    String töötaja = scanner.nextLine();
                    startup.lisaTöötaja(new Töötaja(töötaja, startup.getTöötajad().size()));
                    System.out.println("Palkasid uue töötaja!");
                    break;

                case 2:
                    System.out.println("Alustasid turunduskampaaniat!");
                    // company.startMarketing();
                    break;

                case 3:
                    System.out.println("Rerollisid töötaja töökuse!");
                    // company.rerollEmployee();
                    break;

                case 4:
                    System.out.println("Avad perkide menüü!");
                    // company.showPerks();
                    break;

                case 5:
                    System.out.println("Skipid käigu...");
                    // company.nextTurn();
                    break;

                case 6:
                    System.out.println("Müüsid oma firma! Mäng läbi.");
                    // company.sellCompany();
                    gameRunning = false;
                    break;

                default:
                    System.out.println("Sellist valikut ei ole!");
            }
        }

        scanner.close();
        System.out.println("Aitäh mängimast!");
    }
}