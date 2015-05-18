package diegocunha.beersfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

    //Vari�veis Globais
    ListView listView;
    String bar, ceva;
    private String AppID, ClientID;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter, adapter2;
    List<String> lbares = null;
    List<String> ruaBar = null;
    List<ListaBares> listaBares = null;
    myAdapter adapterList;
    ListaBares listinha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Inicializa o Parse
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);
        Parse.initialize(this, AppID, ClientID);

        //Bloqueia pagina de usu�rio sem acesso
        getUser();

        //Inicializa variaeis
        lbares = new ArrayList<String>();
        ruaBar = new ArrayList<String>();
        listaBares = new ArrayList<ListaBares>();
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lbares);
        adapterList = new myAdapter(this, listaBares);
        listinha = new ListaBares();
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
     Fun��o: getUser                        **
     Funcionalidade: Verifica usu�rio       **
     Data Cria��o: 05/05/2015               **
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

            ParseQuery<ParseObject> query = ParseQuery.getQuery("BaresLocal");
            query.whereEqualTo("NomeBar", Bar);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if(e == null)
                    {

                        for(int i = 0; i <list.size();i++)
                        {
                            ParseObject pObject = list.get(i);
                            //lbares.add(pObject.getString("RuaBar"));
                            listinha.setNomeBar(pObject.getString("NomeBar"));
                            listinha.setRuaBar(pObject.getString("RuaBar"));
                            listaBares.add(listinha);

                            //ruaBar.add(pObject.getString("RuaBar"));
                        }
                        //adapter.notifyDataSetChanged();
                        adapterList.notifyDataSetChanged();
                        mProgressDialog.dismiss();
                        //adapter2.notifyDataSetChanged();
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
        }
        finally
        {
            listView = (ListView)findViewById(R.id.listView);
            listView.setAdapter(adapterList);
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
