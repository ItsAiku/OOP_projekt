import java.util.List;
import java.util.Random;
import static java.lang.Math.round;

public class Tegevus {

    public void töötajaPalkamine(StartUp startup, String nimi){
        int kordaja = startup.getTöötajad().size(); //kordaja on vajalik uue töötaja palga mõjutamisel ja tuleneb olemasolevate töötajate arvust
        Töötaja uusTöötaja = new Töötaja(nimi, kordaja);
    }

    public void turundusKampaania(StartUp startup){
        double rand1 = Math.random();
        if (startup.getKlientideArv() == 0 || rand1 > 0.1){
            Random rand = new Random();
            int uuedKliendid = rand.nextInt(301);//suurendab klientide arvu ming arvu võrra 0-300
            startup.suurendaKliente(uuedKliendid);
        }
        else {//tegemis on harva sündmusega, et mängu balanseerida
            int lisa = Math.toIntExact(round(startup.getKlientideArv() * (0.5*Math.random())));
            int praegusedKliendid = startup.getKlientideArv();
            int uus_arv = praegusedKliendid + lisa;
            startup.suurendaKliente(uus_arv);//turunduskampaania suurendab klientide arvu mingi % võrra
            }
        //turunduskampaania maksab, seega peame kapitali vähendama, max kulu on 10% kapitalist, min 2%
        int kapital = startup.getKapital();
        Random r = new Random();
        double kordaja = 0.9 + 0.08 * r.nextDouble();
        startup.setKapital(Math.toIntExact(round(kapital * kordaja)));

    }
//suvalise töötaja reroll, ei kasuta praegu
//    public void töökuseReroll(StartUp startup){
//        List<Töötaja> töötajad = startup.getTöötajad();
//        Random rand = new Random();
//        Töötaja töötaja = töötajad.get(rand.nextInt(töötajad.size()));
//        Random rand1 = new Random();
//        töötaja.setTöökus(rand1.nextDouble() * 0.99);
//    }

    public void töökuseReroll(StartUp startup, String nimi){//rerollib valitud töötaja töökuse määra
        List<Töötaja> töötajad = startup.getTöötajad();
        for (Töötaja t : töötajad){
            if (t.getNimi().equals(nimi)){
                Random rand = new Random();
                t.setTöökus(Math.round(rand.nextDouble() * 0.99 * 100.0) / 100.0);
            }
        }
        int kapital = startup.getKapital();
        startup.setKapital(kapital-200);//reroll maksab 200
    }
    public void perkid(int number, StartUp startup) {
        if (number < 1 || number > 100) {//erinevatel perkidel on erinevad võimalused toimuda, selleks on erinevad number range'd
            throw new IllegalArgumentException("Number peab olema 1–100.");
        }

        Random rand = new Random();

        // 1–20: Suurenda klientide arvu x korda
        if (number >= 1 && number <= 20) {
            double suurendaja = 1 + rand.nextDouble(); // 1 < x < 2
            int praeguneArv = startup.getKlientideArv();
            int uusArv = (int) (praeguneArv * suurendaja);
            startup.suurendaKliente(uusArv - praeguneArv);
            System.out.println("Klientide arv suurenes " + round(suurendaja*100)/100 + " korda!");
        }
        // 21–40: Suurenda, mida teenitakse iga kliendi pealt (kasutame kapitali)
        else if (number >= 21 && number <= 40) {
            int lisaKapital = (int) (startup.getTuluKliendiKohta() * (1.05 + 0.15 * rand.nextDouble())); // +5% kuni 20%
            startup.suurendaKapital(lisaKapital);
            System.out.println("Tulu per klient suurenes! Kapital: " + startup.getKapital());
            
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
            int kaotus = (int) round(startup.getKlientideArv() * protsent / 100.0);
            startup.suurendaKliente(-kaotus);
            System.out.println("Klientide arv vähenes " + kaotus + " võrra (" + protsent + "%).");
        }
        // 76–95: Kaotad kapitali 5–20%
        else if (number >= 76 && number <= 95) {
            int protsent = 5 + rand.nextInt(16); // 5–20%
            int kaotus = (int) round(startup.getKapital() * protsent / 100.0);
            startup.suurendaKapital(-kaotus);
            System.out.println("Kaotasid kapitali " + kaotus + " (" + protsent + "%).");
        }
        // 96–100: Ei juhtu midagi
        else {
            System.out.println("Ei juhtu midagi.");
        }
        int kapital = startup.getKapital();
        startup.setKapital(kapital-500);//perkide kasutamine maksab 500 + perki enda toime
    }
}
