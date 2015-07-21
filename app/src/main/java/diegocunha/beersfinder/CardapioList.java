package diegocunha.beersfinder;


public class CardapioList implements Comparable<CardapioList>
{
    private String strNomeCeva, strPrecoCeva, strTipoCeva;
    private double dPrecoCeva;

    public CardapioList(String strNomeCeva, String strPrecoCeva, String strTipoCeva, double dPrecoCeva)
    {
        this.strNomeCeva = strNomeCeva;
        this.strPrecoCeva = strPrecoCeva;
        this.strTipoCeva = strTipoCeva;
        this.dPrecoCeva = dPrecoCeva;
    }

    public String getNomeCeva()
    {
        return strNomeCeva;
    }

    public void setNomeCeva(String strNomeCeva)
    {
        this.strNomeCeva = strNomeCeva;
    }

    public String getPrecoCeva()
    {
        return strPrecoCeva;
    }

    public void setPrecoCeva(String strPrecoCeva)
    {
        this.strPrecoCeva = strPrecoCeva;
    }

    public String getstrTipoCeva()
    {
        return strTipoCeva;
    }

    public void setStrTipoCeva(String strTipoCeva)
    {
        this.strTipoCeva = strTipoCeva;
    }

    public double getdPrecoCeva()
    {
        return dPrecoCeva;
    }

    public void setdPrecoCeva(double dPrecoCeva)
    {
        this.dPrecoCeva = dPrecoCeva;
    }

    @Override
    public int compareTo(CardapioList another) {
        if(getdPrecoCeva() > another.dPrecoCeva)
            return 1;
        else return -1;
    }
}
