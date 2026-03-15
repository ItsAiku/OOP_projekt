import java.util.Random;

public class Töötaja {
    private String nimi;
    private int palk;
    private double töökus;

    public Töötaja(String nimi, int palk){
        this.nimi = nimi;
        this.palk = palk;
        Random rand = new Random();
        this.töökus = rand.nextDouble() * 0.99;
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

    public double getTöökus() {
        return töökus;
    }

    public void setTöökus(double töökus) {
        this.töökus = töökus;
    }
}