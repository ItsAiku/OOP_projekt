import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int koef = 1;
        // Algus
        System.out.println("Tere tulemast Startup Simulaatorisse!");
        System.out.println("Sisesta oma esimese töötaja nimi: ");
        String nimi = scanner.nextLine();
        Töötaja esimene = new Töötaja(nimi, koef++);
        List<Töötaja> töötajad = new ArrayList<>();
        töötajad.add(esimene);
        System.out.println("--------------------------------------");

    }
}