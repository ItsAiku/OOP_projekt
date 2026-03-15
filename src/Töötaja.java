public class Töötaja {
    private String nimi;
    private int palk;
    private int töökus;

    public Töötaja(String nimi, int palk, int töökus){
        this.nimi = nimi;
        this.palk = palk;
        this.töökus = töökus;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public int getPalk() {
        return palk;
    }

    public void setPalk(int palk) {
        this.palk = palk;
    }

    public int getTöökus() {
        return töökus;
    }

    public void setTöökus(int töökus) {
        this.töökus = töökus;
    }
}


