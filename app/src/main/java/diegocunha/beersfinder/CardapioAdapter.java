package diegocunha.beersfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CardapioAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private List<CardapioList> lista_fav;
    Context context;

    public CardapioAdapter(Context context, List<CardapioList> lista_fav)
    {
        this.lista_fav = lista_fav;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount()
    {
        return lista_fav.size();
    }

    @Override
    public CardapioList getItem(int position)
    {
        return lista_fav.get(position);
    }

    //Pega endereco de memoria do item
    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ItemSuporte itemHolder;

        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.item_list, null);
            itemHolder = new ItemSuporte();

        }

        return convertView;
    }

    private class ItemSuporte {
        TextView NomeCeva;
        TextView Preco;
    }
}


