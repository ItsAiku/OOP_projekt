//vajalik selleks, et mängija ei saaks negatiivse kaptaliga mängida ja tegevusi teha

public class KapitalCheck {
    boolean töötajaPalkamine;
    boolean turundusKampaania;
    boolean töökuseReroll;
    boolean perkid;

    public KapitalCheck(){
        this.töötajaPalkamine = true;
        this.turundusKampaania = true;
        this.töökuseReroll = true;
        this.perkid = true;
    }

    public void kontrolliKapitali(StartUp startup){
        if (startup.getKapital()<startup.getKapital()*0.1){ //turunduskampaania maksab kuni 10% kapitalist
            this.turundusKampaania = false;
        }
        if (startup.getKapital()<200){//töökuse reroll maksab 200
            this.töökuseReroll = false;
        }
        if (startup.getKapital()<500){//perkide kasutamine maksab 500
            this.perkid = false;
        }
        int töötajad = startup.getTöötajad().size();
        //kuna uue töötaja palk määratakse vastavalt
        // töötajate arvule ja log funktsiooni korrutisele, siis peab looma uue töötaja,
        // et saaks arvutada palga (ei lisata töötajate listi)

        Töötaja t = new Töötaja("", töötajad);
        if (t.getPalk()>startup.getKapital()){
            this.töötajaPalkamine = false;
        }
    }

    public boolean isTöötajaPalkamine() {
        return töötajaPalkamine;
    }

    public boolean isTurundusKampaania() {
        return turundusKampaania;
    }

    public boolean isTöökuseReroll() {
        return töökuseReroll;
    }

    public boolean isPerkid() {
        return perkid;
    }
}
