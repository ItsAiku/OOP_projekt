import java.util.Random;

import static java.lang.Math.round;

public class Töötaja {
    private String nimi;
    private int palk = 1200;
    private double töökus;
    private final int koef;

    public Töötaja(String nimi, int koef){
        this.nimi = nimi;
        this.koef = koef;
        this.palk = (int) (palk*Math.log(koef+1)); //koef tuleneb olemasolevate töötajate arvust ja mõjutab uue töötaja palka
        Random rand = new Random();
        this.töökus = Math.round(rand.nextDouble() * 0.99 * 100.0) / 100.0; //töötaja productivity määramine
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