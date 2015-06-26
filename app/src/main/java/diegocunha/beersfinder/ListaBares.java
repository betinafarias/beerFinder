package diegocunha.beersfinder;

import java.util.ArrayList;
import java.util.List;

/************************************************************
 * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
 * Funçao: ListaBares                                    ****
 * Funcionalidade: Classe para bares                     ****
 * Data Criacao: 28/04/2015                              ****
 ***********************************************************/
public class ListaBares implements Comparable<ListaBares>
{
    //Variaveis Globais
    private String ruaBar;
    private String nomeBar;
    private String distBar;
    private Double dDistBar;
    private double latBar;
    private double lngBar;

    public ListaBares(){}

    //Recebe valores para serem adicionados na lista
    public ListaBares(String nomeBar, String ruaBar, String distBar, double dDistBar, double latBar, double lngBar)
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
    public int compareTo(ListaBares another) {
        if(getdDistBar() > another.dDistBar)
        return 1;
        else return -1;
    }
}

