package diegocunha.beersfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class AddComentariosActivity extends ActionBarActivity {

    private ConnectivityManager conectivtyManager;
    private boolean isOn;
    private myLocation MeuLugar;
    private String strUser, strNomeBar, strRuaBar, strText;
    private EditText eTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcomentarios);
        getUser();

        MeuLugar = new myLocation(this);
        Bundle extras = getIntent().getExtras();
        eTexto = (EditText)findViewById(R.id.txtSave);

        if(extras != null)
        {
            strNomeBar = extras.getString("NomeBar");
            strRuaBar = extras.getString("RuaBar");
        }


    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: getUser                                       ****
     * Funcionalidade: Bloqueia pagina sem login             ****
     * Data Criacao: 05/05/2015                              ****
     ***********************************************************/
    protected void getUser()
    {
        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser == null)
        {
            Intent intent = new Intent(this, firstActivity.class);
            startActivity(intent);

        }
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: verificaConexao                               ****
     * Funcionalidade: Verifica status internet              ****
     * Data Criacao: 28/04/2015                              ****
     ***********************************************************/
    public  boolean verificaConexao()
    {
        conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected())
        {
            isOn = true;
        }
        else
        {
            isOn = false;
        }

        return isOn;
    }


    public void addComent(View view)
    {
        try
        {
            SharedPreferences sp1=this.getSharedPreferences("Login", 0);
            strUser = sp1.getString("Unm", null);

            strText = eTexto.getText().toString();

            ParseObject query = new ParseObject("Comentarios");
            query.put("NomeBar", strNomeBar);
            query.put("RuaBar", strRuaBar);
            query.put("Username", strUser);
            query.put("Comentario", strText);
            query.saveInBackground();

            Toast.makeText(getApplication(), "Comentario adicionado com sucesso", Toast.LENGTH_LONG);
            Intent intent = new Intent(this, ComentariosActivity.class);
            intent.putExtra("NomeBar", strNomeBar);
            intent.putExtra("RuaBar", strRuaBar);
            startActivity(intent);
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void cancelComent(View view)
    {
        Intent intent = new Intent(this, ComentariosActivity.class);
        intent.putExtra("NomeBar", strNomeBar);
        intent.putExtra("RuaBar", strRuaBar);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_comentarios, menu);
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
