package diegocunha.beersfinder;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class FavoriteAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<FavoriteList> lista_fav;
    Context context;

    public FavoriteAdapter(Context context, List<FavoriteList> lista_fav)
    {
        this.lista_fav = lista_fav;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public int getCount() {
        return lista_fav.size();
    }

    @Override
    public FavoriteList getItem(int position)
    {
        return lista_fav.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /********************************************
     * Autores: Diego Cunha Gabriel Cataneo  ****
     * Criação: 14/05/2015                   ****
     * Função: View getView                  ****
     * Funcionalidade: Preenche o ListView   ****
     *******************************************/
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ItemSuporte itemHolder;

        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.item_list, null);
            itemHolder = new ItemSuporte();
            itemHolder.txtNomeBar = (TextView) convertView.findViewById(R.id.nameBar);
            itemHolder.txtRuaBar = (TextView) convertView.findViewById(R.id.ruaBar);
            itemHolder.txtDistancia = (TextView)convertView.findViewById(R.id.distancia);

            convertView.setTag(itemHolder);
        }
        else
        {
            itemHolder = (ItemSuporte) convertView.getTag();
        }


        FavoriteList item = lista_fav.get(position);
        itemHolder.txtNomeBar.setText(item.getNomeBar());
        itemHolder.txtRuaBar.setText(item.getRuaBar());
        itemHolder.txtDistancia.setText(item.getDistBar());

        //Ao Selecionar Opção no ListView
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectedBarActivity.class);
                intent.putExtra("NomeBar", lista_fav.get(position).getNomeBar());
                intent.putExtra("RuaBar", lista_fav.get(position).getRuaBar());
                intent.putExtra("DistBar", lista_fav.get(position).getDistBar());
                intent.putExtra("Latitude", lista_fav.get(position).getLatBar());
                intent.putExtra("Longitude", lista_fav.get(position).getLngBar());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    /********************************************
     * Autores: Diego Cunha Gabriel Cataneo  ****
     * Criação: 14/05/2015                   ****
     * Classe: ItemSuporte                   ****
     * Funcionalidade: Declara os TextView   ****
     *******************************************/
    private class ItemSuporte {
        TextView txtNomeBar;
        TextView txtRuaBar;
        TextView txtDistancia;
    }

}
