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
        if (startup.getKapital()<100){
            this.turundusKampaania = false;
        }
        if (startup.getKapital()<200){
            this.töökuseReroll = false;
        }
        if (startup.getKapital()<500){
            this.perkid = false;
        }
        int töötajad = startup.getTöötajad().size();
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
