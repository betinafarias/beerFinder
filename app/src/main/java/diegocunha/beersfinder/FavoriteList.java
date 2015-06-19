package diegocunha.beersfinder;

public class FavoriteList implements Comparable<FavoriteList>
{
    private String nomeBar, ruaBar, distBar;
    private double dPrecoCeva, dDistBar, latBar, lngBar;

    public FavoriteList(){}

    public FavoriteList(String nomeBar, String ruaBar, String distBar, double dPrecoCeva, double dDistBar, double latBar, double lngBar)
    {
        this.nomeBar = nomeBar;
        this.ruaBar = ruaBar;
        this.distBar = distBar;
        this.dPrecoCeva = dPrecoCeva;
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

    //Preco da cerveja
    public double getPrecoCeva()
    {
        return dPrecoCeva;
    }

    public void setdPrecoCeva(Double dPrecoCeva)
    {
        this.dPrecoCeva = dPrecoCeva;
    }

    //Ordena Lista para mostrar bares mais perto primeiro
    @Override
    public int compareTo(FavoriteList another) {
        if(getdDistBar() > another.dDistBar)
            return 1;
        else return -1;
    }
}
