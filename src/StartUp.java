import java.util.ArrayList;
import java.util.List;

public class StartUp {
    private int kapital;
    private List<Töötaja> töötajad;
    private int klientideArv;

    public StartUp(int kapital) {
        this.kapital = kapital;
        this.töötajad = new ArrayList<>();
        this.klientideArv = 0;
    }

    public StartUp(){
        this.kapital = (int) ((Math.random()*(5000))+5000);
        this.töötajad = new ArrayList<>();
        this.klientideArv = 0;
    }

    public void setKapital(int uusKapital) {this.kapital = uusKapital;}

    public void suurendaKapital(int lisaKapital) {this.kapital += lisaKapital;}

    public void suurendaKliente(int uuteKlientideArv) {this.klientideArv += uuteKlientideArv;}

    public void lisaTöötaja(Töötaja uusTöötaja) {töötajad.add(uusTöötaja);}

    public int getKapital() {return kapital;}

    public int getKlientideArv() {return klientideArv;}

    public List<Töötaja> getTöötajad(){return töötajad;}
}