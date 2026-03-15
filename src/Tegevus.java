import java.util.List;
import java.util.Random;

public class Tegevus {

    public void töötajaPalkamine(StartUp startup, String nimi){
        int kordaja = startup.getTöötajad().size();
        Töötaja uusTöötaja = new Töötaja(nimi, kordaja);
    }

    public void turundusKampaania(StartUp startup){
        double koef = startup.getKlientideArv() * (1 + Math.random());
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
}
