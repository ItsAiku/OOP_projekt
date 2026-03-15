import java.util.List;
import java.util.Random;

public class Tegevus {

    public void töötajaPalkamine(String nimi){
        int kordaja = StartUp.getTöötajad().length;
        Töötaja uusTöötaja = new Töötaja(nimi, kordaja);
    }

    public void turundusKampaania(){
        double koef = getKlientideArv() * (1 + Math.random());
    }

    public void töökuseReroll(){
        List<Töötaja> töötajad = getTöötajad();
        Random rand = new Random();
        Töötaja töötaja = töötajad.get(rand.nextInt(töötajad.size()));
        Random rand1 = new Random();
        töötaja.setTöökus(rand1.nextDouble() * 0.99);
    }

    public void töökuseReroll(String nimi){
        List<Töötaja> töötajad = getTöötajad();
        for (Töötaja t : töötajad){
            if (t.getNimi().equals(nimi)){
                Random rand = new Random();
                t.setTöökus(rand.nextDouble() * 0.99);
            }
        }
    }
}
