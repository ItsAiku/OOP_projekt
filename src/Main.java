import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Algus
        System.out.println("Tere tulemast Startup Simulaatorisse!");
        System.out.print("Sisesta oma startupi nimi: ");
        String startupName = scanner.nextLine();
        StartUp startUp = new StartUp();
        System.out.println("Sinu startup \"" + startupName + "\" on loodud!");
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
                    String nimi = scanner.nextLine();
                    //Töötaja uus = new Töötaja(nimi, töötajad.length);
                    System.out.println("Palkasid uue töötaja!");

                    // kutsu oma meetod nt: company.hireEmployee();
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