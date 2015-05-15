package diegocunha.beersfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;


public class selectBaresA extends ActionBarActivity {

    //Variáveis Globais
    ListView listView;
    String bar, ceva;
    private String AppID, ClientID;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter, adapter2;
    List<String> lbares = null;
    List<String> ruaBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Inicializa o Parse
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);
        Parse.initialize(this, AppID, ClientID);

        //Bloqueia página usuário sem acesso
        getUser();

        //Inicializa variáveis
        lbares = new ArrayList<String>();
        ruaBar = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lbares);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, ruaBar);

        Bundle extras = getIntent().getExtras();

        //Recebe valores do BaresActivity
        if (extras != null)
        {
            bar = extras.getString("strBar");
            ceva = extras.getString("strCeva");
        }

        setContentView(R.layout.activity_selectbares);
        getListView(bar);
    }

    /*****************************************
     Autores: Diego Cunha, Gabriel Cataneo  **
     Função: getUser                        **
     Funcionalidade: Verifica usuário       **
     Data Criação: 05/05/2015               **
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

    /*****************************************
     Autores: Diego Cunha, Gabriel Cataneo  **
     Função: getListView(String Bar)        **
     Funcionalidade: Retorna busca          **
     Data Criação: 05/05/2015               **
     ******************************************/
    protected void getListView(String Bar)
    {
        try
        {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.setTitle("Verificando");
            mProgressDialog.setMessage("Carregando. . . ");

            ParseQuery<ParseObject> query = ParseQuery.getQuery("BaresLocal");
            query.whereEqualTo("NomeBar", Bar);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if(e == null)
                    {
                        mProgressDialog.show();

                        for(int i = 0; i <list.size();i++)
                        {
                            ParseObject pObject = list.get(i);
                            lbares.add(pObject.getString("NomeBar"));
                            ruaBar.add(pObject.getString("RuaBar"));
                        }
                        adapter.notifyDataSetChanged();
                        adapter2.notifyDataSetChanged();
                    }
                    else
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
        }
        finally
        {
            listView = (ListView)findViewById(R.id.listView);
            listView.setAdapter(adapter);
            mProgressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_bares, menu);
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
