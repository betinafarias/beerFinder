package diegocunha.beersfinder;

import java.util.ArrayList;
import java.util.List;

/********************************************
 * Autores: Diego Cunha Gabriel Cataneo  ****
 * Cria��o: 08/05/2015                   ****
 * Classe: ListaBares                    ****
 * Fun��o: Login no Aplicativo           ****
 ********************************************/
public class ListaBares implements Comparable<ListaBares>{
    private String nomeBar;
    private double distBar;
    private double latBar;
    private double lngBar;

    //NomeBar
    public String getNomeBar()
    {
        return nomeBar;
    }

    public void setNomeBar(String nomeBar)
    {
        this.nomeBar = nomeBar;
    }


    //Distancia Bar
    public double getDistBar()
    {
        return distBar;
    }

    public void setDistBar(double distBar)
    {
        this.distBar = distBar;
    }

    //Latitude
    public double getLatBar()
    {
        return latBar;
    }

    public void setLatBar(double latBar)
    {
        this.latBar = latBar;
    }

    //Longitude
    public double getLngBar()
    {
        return lngBar;
    }

    public void setLngBar(double lngBar)
    {
        this.lngBar = lngBar;
    }

    @Override
    public int compareTo(ListaBares listaBares) {
        if(this.distBar > listaBares.distBar)
        {
            return -1;
        }

        return this.getNomeBar().compareToIgnoreCase(listaBares.getNomeBar());
    }
}

