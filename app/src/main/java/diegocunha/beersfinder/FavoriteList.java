package diegocunha.beersfinder;

/**
 * Created by SnowGhost on 10/06/2015.
 */
public class FavoriteList
{
    private String ruaBar;
    private String nomeBar;

    public FavoriteList(){}

    public FavoriteList(String nomeBar, String ruaBar)
    {
        this.nomeBar = nomeBar;
        this.ruaBar = ruaBar;
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
}
