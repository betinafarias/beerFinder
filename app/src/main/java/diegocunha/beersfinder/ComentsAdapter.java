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

/************************************************************
 * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
 * Classe: ComentsAdapter                                ****
 * Funcionalidade: Adapter para mostrar comentarios      ****
 * Data Criacao: 21/06/2015                              ****
 ***********************************************************/
public class ComentsAdapter extends BaseAdapter
{
    //Variaveis Globais
    private LayoutInflater mInflater;
    private List<ComentariosList> comentariosLists;
    Context context;

    //Recebe itens para serem adicionados
    public ComentsAdapter (Context context, List<ComentariosList> comentariosLists)
    {
        this.comentariosLists = comentariosLists;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    //Conta quantos elementos tem na lista
    @Override
    public int getCount() {
        return comentariosLists.size();
    }

    //Busca item pela posição
    @Override
    public ComentariosList getItem(int position)
    {
        return comentariosLists.get(position);
    }

    //ID do item na memoria
    @Override
    public long getItemId(int position) {
        return 0;
    }

    //Carrega o View
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        //Classe para receber itens
        ItemSuporte itemHolder;

        //Se a visualização nao foi iniciada
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

        //Itens para serem exibidos
        ComentariosList item = comentariosLists.get(position);
        itemHolder.user.setText(item.getStrUser());
        itemHolder.comentario.setText(item.getStrComentario());

        //Funcao no comentario selecionado
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectedComentarioActivity.class);
                intent.putExtra("Usuario", comentariosLists.get(position).getStrUser());
                intent.putExtra("Comentario", comentariosLists.get(position).getStrComentario());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    //Classe para exibir itens
    private class ItemSuporte
    {
        TextView user;
        TextView comentario;
    }
}
