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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class selectBaresA extends ActionBarActivity {


    ListView listView;
    String bar, ceva;
    String[] lista = {""};
    private String AppID, ClientID;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inicializa o Parse
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);
        Parse.initialize(this, AppID, ClientID);
        getUser();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            bar = extras.getString("strBar");
            ceva = extras.getString("strCeva");

            ParseQuery<ParseObject> query = ParseQuery.getQuery("BaresLocal");
            query.whereEqualTo("NomeBar", bar);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null) {
                        lista = new String[list.size()];

                        for (int i = 0; i < list.size(); i++) {
                            lista[i] = list.get(i).getString("NomeBar");
                        }
                        adapter.notifyDataSetChanged();
                    }
                    listView = (ListView) findViewById(R.id.listView);
                    listView.setAdapter(adapter);
                }
            });
        }

        setContentView(R.layout.activity_selectbares);
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
