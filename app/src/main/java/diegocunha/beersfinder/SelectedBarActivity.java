package diegocunha.beersfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class SelectedBarActivity extends ActionBarActivity {

    //Variaveis Globais
    private String AppID, ClientID, nomedoBar, ruadoBar, distdoBar, strNomeCerveja, strPrecoCerveja, strHour, strAbertura, strFechamento;
    private TextView v1,v2, v3,v4,v5,v6,v7,v8,v9, v10,v11,v12;
    private double Lat, Lng;
    private ProgressDialog mProgressDialog;
    private Calendar cal1, cal2, cal3;

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
        v4 = (TextView)findViewById(R.id.txtCeva1);
        v5 = (TextView)findViewById(R.id.txtPrecoCeva1);
        v6 = (TextView)findViewById(R.id.txtCeva2);
        v7 = (TextView)findViewById(R.id.txtPrecoCeva2);
        v8 = (TextView)findViewById(R.id.txtCeva3);
        v9 = (TextView)findViewById(R.id.txtPrecoCeva3);
        v10 = (TextView)findViewById(R.id.txtStatus);
        v11 = (TextView)findViewById(R.id.hAbertura);
        v12 = (TextView)findViewById(R.id.hFechamento);

        //Inicializa o ProgressDialog
        mProgressDialog = new ProgressDialog(this);

        //Inicializa o Bundle
        Bundle extras = getIntent().getExtras();

        //Se foi passado valores pelo ListViewClick
        if(extras != null)
        {
            //Inicializa o ProgressDialog
            mProgressDialog.setTitle("Carregando");
            mProgressDialog.setMessage("Loading. . .");
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.show();

            //Nome do Bar
            nomedoBar = extras.getString("NomeBar");
            v1.setText(nomedoBar);

            //Rua do Bar
            ruadoBar = extras.getString("RuaBar");
            v2.setText(ruadoBar);

            //Distancia do Bar
            distdoBar = extras.getString("DistBar");
            v3.setText(distdoBar);

            //Pega Latitude e Longitude
            Lat = extras.getDouble("Latitude");
            Lng = extras.getDouble("Longitude");

            //Verifica se o bar esta aberto
            load_status(nomedoBar);

            //Busca as cervejas mais baratas
            load_cerveja(nomedoBar);
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


    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Fun�ao: load_cerveja                                  ****
     * Funcionalidade: Verifica se os bares esta aberto      ****
     * Data Criacao: 09/06/2015                              ****
     ***********************************************************/

    protected void load_status(String nome)
    {
        //DateTime
        cal1 = Calendar.getInstance(); // Abertura
        cal2 = Calendar.getInstance(); // Fechamento
        cal3 = Calendar.getInstance(); // Atual;

        //Hora Atual;
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        strHour = sdf.format(now);

        try
        {
            //Carrega informa��es do Parse
            ParseQuery<ParseObject> query = ParseQuery.getQuery("BaresLocal");
            query.whereEqualTo("NomeBar", nome);
            query.setLimit(1);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e)
                {
                    //Se nao ha excecao
                    if (e == null)
                    {
                        //Verifica se a lista esta preenchida
                        if (list.size() > 0)
                        {
                            //Adiciona valores do Parse as variaveis
                            for (int i = 0; i < list.size(); i++)
                            {
                                ParseObject pObject = list.get(i);

                                strAbertura = pObject.getString("Abertura");
                                v11.setText(strAbertura);

                                strFechamento = pObject.getString("Fechamento");
                                v12.setText(strFechamento);

                                //Abertura
                                String[] parts = strAbertura.split(":");
                                cal1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                                cal1.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                                cal1.set(Calendar.SECOND, Integer.parseInt(parts[2]));

                                //Fechamento
                                parts = strFechamento.split(":");
                                cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                                cal2.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                                cal2.set(Calendar.SECOND, Integer.parseInt(parts[2]));

                                //Hora atual
                                parts = strHour.split(":");
                                cal3.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                                cal3.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                                cal3.set(Calendar.SECOND, Integer.parseInt(parts[2]));

                                //Realiza verificacao do horario
                                if(cal3.before(cal1) || cal3.after(cal2))
                                {
                                    v10.setTextColor(Color.RED);
                                    v10.setText("Fechado");
                                }
                                else
                                {
                                    v10.setTextColor(Color.GREEN);
                                    v10.setText("Aberto");
                                }
                            }
                        }
                        else
                        {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Lista vazia", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        mProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception ex)
        {
            mProgressDialog.dismiss();
            ex.printStackTrace();
        }
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Fun�ao: goDraw                                        ****
     * Funcionalidade: Abre activity do Maps                 ****
     * Data Criacao: 23/05/2015                              ****
     ***********************************************************/
    public void goDraw(View view)
    {
        Intent intent = new Intent(this, HowtoGoActivity.class);
        intent.putExtra("LatitudeBar", Lat);
        intent.putExtra("LongitudeBar", Lng);
        intent.putExtra("NomeBar", nomedoBar);
        intent.putExtra("RuaBar", ruadoBar);
        intent.putExtra("NomeCerveja", strNomeCerveja);
        intent.putExtra("PrecoCerveja", strPrecoCerveja);
        startActivity(intent);
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Fun�ao: load_cerveja                                  ****
     * Funcionalidade: Verifica as cervejas mais baratas     ****
     * Data Criacao: 09/06/2015                              ****
     ***********************************************************/
   protected void load_cerveja(String txNomeBar)
    {
        try
        {
            //Carrega informa��es do Parse
            ParseQuery<ParseObject> query = ParseQuery.getQuery(txNomeBar);
            query.setLimit(3);
            query.orderByAscending("Preco");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    //Se nao ha excecao
                    if(e == null)
                    {
                        //Se a lista nao esta fazia
                        if(list.size() > 0)
                        {
                            //Varre a lista
                            for(int i =0; i < list.size(); i++)
                            {
                                //Adiciona infos das cervejas mais barata
                                v4.setText(list.get(0).getString("NomeCerveja"));
                                v5.setText("R$ " + String.valueOf(list.get(0).getDouble("Preco")).replace(".", ","));
                                v6.setText(list.get(1).getString("NomeCerveja"));
                                v7.setText("R$ " + String.valueOf(list.get(1).getDouble("Preco")).replace(".", ","));
                                v8.setText(list.get(2).getString("NomeCerveja"));
                                v9.setText("R$ " + String.valueOf(list.get(2).getDouble("Preco")).replace(".", ","));

                                strNomeCerveja = list.get(0).getString("NomeCerveja");
                                strPrecoCerveja = "R$ " + String.valueOf(list.get(0).getDouble("Preco")).replace(".", ",");
                            }

                            mProgressDialog.dismiss();
                        }
                        else
                        {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Lista vazia", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        mProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            mProgressDialog.dismiss();
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
