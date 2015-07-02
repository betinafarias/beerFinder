package diegocunha.beersfinder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**************************************************************
 * Autores: Diego Cunha Gabriel Cataneo  Betina Farias    ****
 * Funçao: SelectedComentarioActivity                     ****
 * Funcionalidade: Mostra comentario selecionado          ****
 * Data Criacao: 25/06/2015                               ****
 *************************************************************/
public class SelectedComentarioActivity extends Activity {

    //Variaveis Globais
    private TextView txUser, txComentario;
    private String strUser, strComent;
    private Bundle extras;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
}
