package diegocunha.beersfinder;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**************************************************************
 * Autores: Diego Cunha Gabriel Cataneo  Betina Farias    ****
 * Funçao: SelectedComentarioActivity                     ****
 * Funcionalidade: Mostra comentario selecionado          ****
 * Data Criacao: 25/06/2015                               ****
 *************************************************************/
public class SelectedComentarioActivity extends ActionBarActivity {

    //Variaveis Globais
    private TextView txUser, txComentario;
    private String strUser, strComent;
    private Bundle extras;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectedcomentario);

        //Inicializa variaveis
        extras = getIntent().getExtras();
        txUser = (TextView)findViewById(R.id.txtUsuario);
        txComentario = (TextView)findViewById(R.id.Comentario);
        mProgressDialog = new ProgressDialog(this);

        if(extras != null)
        {
            //Inicializa ProgressDialog
            mProgressDialog.setTitle("Carregando");
            mProgressDialog.setMessage("Loading. . .");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            //Busca Extras e adiciona a variaveis
            strUser = extras.getString("Usuario");
            strComent = extras.getString("Comentario");

            txUser.setText(strUser);
            txComentario.setText(strComent);

            mProgressDialog.dismiss();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selected_comentario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
