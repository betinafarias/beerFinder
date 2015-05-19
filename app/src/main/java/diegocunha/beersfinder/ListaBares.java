package diegocunha.beersfinder;

import java.util.ArrayList;
import java.util.List;

/********************************************
 * Autores: Diego Cunha Gabriel Cataneo  ****
 * Cria��o: 08/05/2015                   ****
 * Classe: ListaBares                    ****
 * Fun��o: Login no Aplicativo           ****
 ********************************************/
public class ListaBares implements Comparable<ListaBares>
{
    private String ruaBar;
    private String nomeBar;
    private String distBar;
    private Double dDistBar;
    //private double latBar;
    //private double lngBar;

    public ListaBares(){}

    public ListaBares(String nomeBar, String ruaBar, String distBar, double dDistBar)
    {
        this.nomeBar = nomeBar;
        this.ruaBar = ruaBar;
        this.distBar = distBar;
        this.dDistBar = dDistBar;
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

    //Distancia Bar
    public String getDistBar()
    {
        return distBar;
    }

    public void setDistBar(String distBar)
    {
        this.distBar = distBar;
    }

    public void setdDistBar(Double dDistBar)
    {
        this.dDistBar = dDistBar;
    }

    public double getdDistBar()
    {
        return dDistBar;
    }

    @Override
    public int compareTo(ListaBares another) {
        if(getdDistBar() > another.dDistBar)
        return 1;
        else return -1;
    }
}

