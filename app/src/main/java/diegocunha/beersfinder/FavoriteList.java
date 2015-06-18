package diegocunha.beersfinder;

public class FavoriteList implements Comparable<FavoriteList>
{
    private String ruaBar;
    private String nomeBar;
    private double latBar;
    private double lngBar;
    private String distBar;
    private Double dDistBar;

    public FavoriteList(){}

    public FavoriteList(String nomeBar, String ruaBar, String distBar, double dDistBar, double latBar, double lngBar)
    {
        this.nomeBar = nomeBar;
        this.ruaBar = ruaBar;
        this.distBar = distBar;
        this.dDistBar = dDistBar;
        this.latBar = latBar;
        this.lngBar = lngBar;
    }

    //NomeBar
    public String getNomeBar()
    {
        return nomeBar;
    }

    public void setNomeBar(String nomeBar)
    {
        this.nomeBar = nomeBar;
    }

    //RuaBar
    public String getRuaBar()
    {
        return ruaBar;
    }

    public void setRuaBar(String ruaBar)
    {
        this.ruaBar = ruaBar;
    }

    //Latitude
    public double getLatBar(){return latBar;}

    public void setLatBar(double latBar)
    {
        this.latBar = latBar;
    }

    //Longitude
    public double getLngBar(){return lngBar;}

    public void setLngBar(double lngBar)
    {
        this.lngBar = lngBar;
    }

    //Distancia Bar (String)
    public String getDistBar()
    {
        return distBar;
    }

    public void setDistBar(String distBar)
    {
        this.distBar = distBar;
    }

    //Distancia Bar (Double)
    public void setdDistBar(Double dDistBar)
    {
        this.dDistBar = dDistBar;
    }

    public double getdDistBar()
    {
        return dDistBar;
    }


    //Ordena Lista para mostrar bares mais perto primeiro
    @Override
    public int compareTo(FavoriteList another) {
        if(getdDistBar() > another.dDistBar)
            return 1;
        else return -1;
    }
}
