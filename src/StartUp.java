import java.util.ArrayList;
import java.util.List;

public class StartUp {
    private int kapital;
    private List<Töötaja> töötajad;
    private int klientideArv;
    private int tuluKliendiKohta;

    public StartUp(int kapital) {
        this.kapital = kapital;
        this.töötajad = new ArrayList<>(); //alguses pole töötajaid
        this.klientideArv = 0;
        this.tuluKliendiKohta = 150;
    }

    public StartUp(){
        this.kapital = (int) ((Math.random()*(5000))+5000); //genereerib random kapitali suuruse vahemikus 5000-10000
        this.töötajad = new ArrayList<>();
        this.klientideArv = 0;
        this.tuluKliendiKohta = 150;
    }

    public void setKapital(int uusKapital) {this.kapital = uusKapital;}

    public void suurendaKapital(int lisaKapital) {this.kapital += lisaKapital;}

    public void suurendaKliente(int uuteKlientideArv) {this.klientideArv += uuteKlientideArv;}

    public void lisaTöötaja(Töötaja uusTöötaja) {töötajad.add(uusTöötaja);}

    public void setTuluKliendiKohta(int uusTulu){this.tuluKliendiKohta = uusTulu;}

    public int getKapital() {return kapital;}

    public int getKlientideArv() {return klientideArv;}

    public List<Töötaja> getTöötajad(){return töötajad;}

    public int getTuluKliendiKohta() {return tuluKliendiKohta;}
}