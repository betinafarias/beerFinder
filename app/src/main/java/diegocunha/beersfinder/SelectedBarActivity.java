package diegocunha.beersfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;

import org.w3c.dom.Text;


public class SelectedBarActivity extends ActionBarActivity {

    //Variaveis Globais
    private String AppID, ClientID, nomedoBar, ruadoBar, distdoBar;
    TextView v1,v2, v3;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inicializa o Parse
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);
        Parse.initialize(this, AppID, ClientID);

        //Bloqueia pagina de usuario sem acesso
        getUser();
        setContentView(R.layout.activity_selectedbar);

        //Seta os TextView
        v1 = (TextView)findViewById(R.id.txtNomeBar2);
        v2 = (TextView)findViewById(R.id.txtRuaBar2);
        v3 = (TextView)findViewById(R.id.txtDistBar);

        //Direciona o ProgressDialog
        mProgressDialog = new ProgressDialog(this);

        Bundle extras = getIntent().getExtras();

        //Se foi passado valores pelo ListViewClick
        if(extras != null)
        {
            mProgressDialog.setTitle("Carregando");
            mProgressDialog.setMessage("Loading. . .");
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.show();

            nomedoBar = extras.getString("NomeBar");
            v1.setText(nomedoBar);
            ruadoBar = extras.getString("RuaBar");
            v2.setText(ruadoBar);
            distdoBar = extras.getString("DistBar");
            v3.setText(distdoBar);
            mProgressDialog.dismiss();
        }
        else // Se nao foi
        {
            mProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Erro nos parametros", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, secondActivity.class);
            startActivity(intent);
        }
    }


    /*****************************************
     Autores: Diego Cunha, Gabriel Cataneo  **
     Fun??o: getUser                        **
     Funcionalidade: Verifica usu?rio       **
     Data Cria??o: 05/05/2015               **
     ******************************************/
    protected void getUser()
    {
        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser == null)
        {
            Intent intent = new Intent(this, firstActivity.class);
            startActivity(intent);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selected_bar, menu);
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
