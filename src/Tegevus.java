import java.util.List;
import java.util.Random;

public class Tegevus {

    public void töötajaPalkamine(StartUp startup, String nimi){
        int kordaja = startup.getTöötajad().size(); //kordaja on vajalik uue töötaja palga mõjutamisel ja tuleneb olemasolevate töötajate arvust
        Töötaja uusTöötaja = new Töötaja(nimi, kordaja);
    }

    public void turundusKampaania(StartUp startup){
        double koef = startup.getKlientideArv() * (1 + Math.random()); //turunduskampaania suurendab klientide arvu mingi % võrra
    }

    public void töökuseReroll(StartUp startup){
        List<Töötaja> töötajad = startup.getTöötajad();
        Random rand = new Random();
        Töötaja töötaja = töötajad.get(rand.nextInt(töötajad.size()));
        Random rand1 = new Random();
        töötaja.setTöökus(rand1.nextDouble() * 0.99);
    }

    public void töökuseReroll(StartUp startup, String nimi){
        List<Töötaja> töötajad = startup.getTöötajad();
        for (Töötaja t : töötajad){
            if (t.getNimi().equals(nimi)){
                Random rand = new Random();
                t.setTöökus(rand.nextDouble() * 0.99);
            }
        }
    }
    public void perkid(int number, StartUp startup) {
        if (number < 1 || number > 100) {
            throw new IllegalArgumentException("Number peab olema 1–100.");
        }

        Random rand = new Random();

        // 1–20: Suurenda klientide arvu x korda
        if (number >= 1 && number <= 20) {
            int kordaja = 2 + rand.nextInt(4); // x = 2 kuni 5
            int praeguneArv = startup.getKlientideArv();
            int uusArv = praeguneArv + kordaja;
            startup.suurendaKliente(uusArv - praeguneArv);
            System.out.println("Klientide arv suurenes " + kordaja + " võrra!");
        }
        // 21–40: Suurenda, mida teenitakse iga kliendi pealt (kasutame kapitali)
        else if (number >= 21 && number <= 40) {
            int lisaKapital = (int) (startup.getKapital() * (0.05 + 0.15 * rand.nextDouble())); // +5%–20%
            startup.suurendaKapital(lisaKapital);
            System.out.println("Tulu per klient suurenes! Kapital + " + lisaKapital);
        }
        // 41–55: Töötaja lahkub
        else if (number >= 41 && number <= 55) {
            // Eeldame, et startup.getTöötajadList() tagastab List<Töötaja>
            List<Töötaja> töötajad = startup.getTöötajad();
            if (töötajad != null && !töötajad.isEmpty()) {
                int lahkuvIndeks = rand.nextInt(töötajad.size());
                Töötaja t = töötajad.remove(lahkuvIndeks);
                System.out.println("Töötaja " + t.getNimi() + " lahkub!");
            } else {
                System.out.println("Ei ole töötajaid, kes lahkuks.");
            }
        }
        // 56–75: Vähenda klientide arvu 1–20%
        else if (number >= 56 && number <= 75) {
            int protsent = 1 + rand.nextInt(20);
            int kaotus = (int) Math.round(startup.getKlientideArv() * protsent / 100.0);
            startup.suurendaKliente(-kaotus);
            System.out.println("Klientide arv vähenes " + kaotus + " võrra (" + protsent + "%).");
        }
        // 76–95: Kaotad kapitali 5–20%
        else if (number >= 76 && number <= 95) {
            int protsent = 5 + rand.nextInt(16); // 5–20%
            int kaotus = (int) Math.round(startup.getKapital() * protsent / 100.0);
            startup.suurendaKapital(-kaotus);
            System.out.println("Kaotasid kapitali " + kaotus + " (" + protsent + "%).");
        }
        // 96–100: Ei juhtu midagi
        else {
            System.out.println("Ei juhtu midagi.");
        }
    }
}
