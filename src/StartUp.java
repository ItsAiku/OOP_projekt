import java.util.List;

public class StartUp {
    private int kapital;
    private List<Töötaja> töötajad;
    private int klientideArv;

    public StartUp(int kapital, List<Töötaja> töötajad, int klitideArv) {
        this.kapital = kapital;
        this.töötajad = töötajad;
        this.klientideArv = klitideArv;
    }

    public void setKapital(int uusKapital) {
        this.kapital = uusKapital;
    }
    public void suurendaKapital(int lisaKapital) {
        this.kapital += lisaKapital;
    }
    public int getKapital() {
        return kapital;
    }
    public void suurendaKliente(int uuteKlientideArv) {
        this.klientideArv += uuteKlientideArv;
    }
    public int getKlitideArv() {
        return klientideArv;
    }
    public void lisaTöötaja(Töötaja uusTöötaja) {
        töötajad.add(uusTöötaja);
    }
    public List<Töötaja> getTöötajad(){
        return töötajad;
    }
}
