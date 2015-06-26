package diegocunha.beersfinder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class myAdapter extends BaseAdapter {

    //Variaveis Globais
    private LayoutInflater mInflater;
    private List<ListaBares> listaBares;
    Context context;

    /***********************************************************
     * Autores: Diego Cunha Gabriel Cataneo Betina Farias   ****
     * Criação: 14/05/2015                                  ****
     * Função: public myAdapter                             ****
     * Funcionalidade: inicia o Adapter                     ****
     **********************************************************/
    public myAdapter(Context context, List<ListaBares> listaBares)
    {
        this.listaBares = listaBares;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    /**********************************************************
     * Autores: Diego Cunha Gabriel Cataneo Betina Farias  ****
     * Criação: 14/05/2015                                 ****
     * Função: int getCount                                ****
     * Funcionalidade: inicia o Adapter                    ****
     **********************************************************/
    @Override
    public int getCount() {
        return listaBares.size();
    }

    /**********************************************************
     * Autores: Diego Cunha Gabriel Cataneo Betina Farias  ****
     * Criação: 14/05/2015                                 ****
     * Função: ListaBares getItem                          ****
     * Funcionalidade: Retorna item da lista               ****
     **********************************************************/
    public ListaBares getItem(int position)
    {
        return listaBares.get(position);
    }

    /*********************************************************
     * Autores: Diego Cunha Gabriel Cataneo Betina Farias ****
     * Criação: 14/05/2015                                ****
     * Função: long getItemId                             ****
     * Funcionalidade: Retorna id do item                 ****
     ********************************************************/
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*********************************************************
     * Autores: Diego Cunha Gabriel Cataneo Betina Farias ****
     * Criação: 14/05/2015                                ****
     * Função: View getView                               ****
     * Funcionalidade: Preenche o ListView                ****
     *********************************************************/
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


        ListaBares item = listaBares.get(position);
        itemHolder.txtNomeBar.setText(item.getNomeBar());
        itemHolder.txtRuaBar.setText(item.getRuaBar());
        itemHolder.txtDistancia.setText(item.getDistBar());

        //Ao Selecionar Opção no ListView
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectedBarActivity.class);
                intent.putExtra("NomeBar", listaBares.get(position).getNomeBar());
                intent.putExtra("RuaBar", listaBares.get(position).getRuaBar());
                intent.putExtra("DistBar", listaBares.get(position).getDistBar());
                intent.putExtra("Latitude", listaBares.get(position).getLatBar());
                intent.putExtra("Longitude", listaBares.get(position).getLngBar());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    /**********************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias ****
     * Criação: 14/05/2015                                 ****
     * Classe: ItemSuporte                                 ****
     * Funcionalidade: Declara os TextView                 ****
     *********************************************************/
    private class ItemSuporte {
        TextView txtNomeBar;
        TextView txtRuaBar;
        TextView txtDistancia;
    }
}
