package tikape.runko.domain;

public class AnnosRaakaAine {

    private Integer id;
    private Integer annosId;
    private Integer raakaAineId;
    private Integer jarjestys;
    private Integer maara;
    private String ohje;

    public AnnosRaakaAine(Integer id, Integer annosId, Integer raakaAineId, Integer jarjestys, Integer maara, String ohje) {
        this.id = id;
        this.annosId = annosId;
        this.raakaAineId = raakaAineId;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAnnosId() {
        return annosId;
    }

    public void setAnnosId(Integer annosId) {
        this.annosId = annosId;
    }

    public Integer getRaakaAineId() {
        return raakaAineId;
    }

    public void setRaakaAineId(Integer raakaAineId) {
        this.raakaAineId = raakaAineId;
    }

    public Integer getJarjestys() {
        return jarjestys;
    }

    public void setJarjestys(Integer jarjestys) {
        this.jarjestys = jarjestys;
    }

    public Integer getMaara() {
        return maara;
    }

    public void setMaara(Integer maara) {
        this.maara = maara;
    }

    public String getOhje() {
        return ohje;
    }

    public void setOhje(String ohje) {
        this.ohje = ohje;
    }

}
