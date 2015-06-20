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

public class ComentsAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private List<ComentariosList> comentariosLists;
    Context context;

    public ComentsAdapter (Context context, List<ComentariosList> comentariosLists)
    {
        this.comentariosLists = comentariosLists;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return comentariosLists.size();
    }

    @Override
    public ComentariosList getItem(int position)
    {
        return comentariosLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ItemSuporte itemHolder;

        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.com_list, null);
            itemHolder = new ItemSuporte();
            itemHolder.user = (TextView) convertView.findViewById(R.id.usuario);
            itemHolder.comentario = (TextView) convertView.findViewById(R.id.comentarios);

            convertView.setTag(itemHolder);
        }
        else
        {
            itemHolder = (ItemSuporte) convertView.getTag();
        }

        ComentariosList item = comentariosLists.get(position);
        itemHolder.user.setText(item.getStrUser());
        itemHolder.comentario.setText(item.getStrComentario());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HowtoGoActivity.class);
                intent.putExtra("Usuario", comentariosLists.get(position).getStrUser());
                intent.putExtra("Comentario", comentariosLists.get(position).getStrComentario());
                intent.putExtra("NomeBar", comentariosLists.get(position).getStrNomeBar());
                intent.putExtra("RuaBar", comentariosLists.get(position).getStrRuaBar());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ItemSuporte
    {
        TextView user;
        TextView comentario;
    }
}
