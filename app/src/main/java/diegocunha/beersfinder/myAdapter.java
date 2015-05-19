package diegocunha.beersfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by nelogica on 18/05/15.
 */
public class myAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ListaBares> listaBares;

    public myAdapter(Context context, List<ListaBares> listaBares)
    {
        this.listaBares = listaBares;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return listaBares.size();
    }

    public ListaBares getItem(int position)
    {
        return listaBares.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        return convertView;
    }

    private class ItemSuporte {

        TextView txtNomeBar;
        TextView txtRuaBar;
        TextView txtDistancia;
    }
}
