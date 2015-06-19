package diegocunha.beersfinder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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
import java.util.Random;

public class SelectedBarActivity extends ActionBarActivity {

    //Variaveis Globais
    private String AppID, ClientID, nomedoBar, ruadoBar, distdoBar, strNomeCerveja, strPrecoCerveja, strHour, strAbertura, strFechamento;
    private TextView v1,v2, v3,v4,v5,v6,v7,v8,v9, v10,v11,v12;
    private double Lat, Lng;
    private ProgressDialog mProgressDialog;
    private Calendar cal1, cal2, cal3, cal4;
    private ConnectivityManager conectivtyManager;
    private boolean conectado;
    private AlertDialog.Builder alertB;
    private int iNum;
    private FavoriteList favList;
    private List<FavoriteList> favorite;
    private SQLiteDatabase myDataBase;
    private Button btnFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Bloqueia pagina de usuario sem acesso
        getUser();
        OpenConsientizacao();
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
        btnFav = (Button)findViewById(R.id.btnFavorite);

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

            //Verifica favoritos no SQLITE
            load_fav(nomedoBar, ruadoBar);

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

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Fun�ao: getUser                                       ****
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
     * Fun�ao: verificaConexao                               ****
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
            conectado = true;
        }
        else
        {
            conectado = false;
        }

        return conectado;
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Fun�ao: load_status                                   ****
     * Funcionalidade: Verifica se os bares esta aberto      ****
     * Data Criacao: 09/06/2015                              ****
     ***********************************************************/
    protected void load_status(String nome)
    {
        //DateTime
        cal1 = Calendar.getInstance(); // Abertura
        cal2 = Calendar.getInstance(); // Fechamento
        cal3 = Calendar.getInstance(); // Atual
        cal4 = Calendar.getInstance(); // Dia

        //Hora Atual
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        strHour = sdf.format(now);

        //Verifica Conex�o com internet
        if(verificaConexao())
        {
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
                                    v11.setText(strAbertura.replace(":", "h") +"min");

                                    strFechamento = pObject.getString("Fechamento");
                                    v12.setText(strFechamento.replace(":", "h") +"min");

                                    //Abertura
                                    String[] parts = strAbertura.split(":");
                                    cal1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                                    cal1.set(Calendar.MINUTE, Integer.parseInt(parts[1]));

                                    //Fechamento
                                    parts = strFechamento.split(":");
                                    cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                                    cal2.set(Calendar.MINUTE, Integer.parseInt(parts[1]));

                                    //Hora atual
                                    parts = strHour.split(":");
                                    cal3.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                                    cal3.set(Calendar.MINUTE, Integer.parseInt(parts[1]));

                                    //Realiza verificacao do horario
                                    if(cal3.after(cal1) || cal3.before(cal2))
                                    {
                                        v10.setTextColor(Color.GREEN);
                                        v10.setText("Aberto");
                                    }
                                    else
                                    {
                                        v10.setTextColor(Color.RED);
                                        v10.setText("Fechado");
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
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            catch (Exception ex)
            {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
        }
        else
        {
            mProgressDialog.dismiss();
            OpenNet();
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
        if(verificaConexao())
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
                                    v5.setText("R$ " + (String.format("%.2f",list.get(0).getDouble("Preco"))));

                                    v6.setText(list.get(1).getString("NomeCerveja"));
                                    v7.setText("R$ " + (String.format("%.2f", list.get(1).getDouble("Preco"))));

                                    v8.setText(list.get(2).getString("NomeCerveja"));
                                    v9.setText("R$ " + (String.format("%.2f", list.get(2).getDouble("Preco"))));

                                    strNomeCerveja = list.get(0).getString("NomeCerveja");
                                    strPrecoCerveja = "R$ " + String.valueOf(String.format("%.2f", list.get(0).getDouble("Preco")));;
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
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
            catch (Exception ex)
            {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
        }
        else
        {
            mProgressDialog.dismiss();
            OpenNet();
        }
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Fun�ao: OpenGPS                                       ****
     * Funcionalidade: Abre Config de GPS                    ****
     * Data Criacao: 11/06/2015                              ****
     ***********************************************************/
    protected void OpenGPS()
    {
        final Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        final Intent intent2 = new Intent(this, secondActivity.class);

        alertB = new AlertDialog.Builder(this);
        alertB.setTitle("Aviso");
        alertB.setMessage("GPS desativado, deseja ativar?");
        alertB.setCancelable(false);
        alertB.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(intent);
            }
        });
        alertB.setNegativeButton("N�o", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(intent2);
            }
        });

        AlertDialog alert11 = alertB.create();
        alert11.show();
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Fun�ao: OpenNet                                       ****
     * Funcionalidade: Abre Config de internet               ****
     * Data Criacao: 11/06/2015                              ****
     ***********************************************************/
    protected void OpenNet()
    {
        final Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
        final Intent intent2 = new Intent(this, secondActivity.class);

        alertB = new AlertDialog.Builder(this);
        alertB.setTitle("Aviso");
        alertB.setMessage("Sem conexao com internet, deseja ativar?");
        alertB.setCancelable(false);
        alertB.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(intent);
            }
        });
        alertB.setNegativeButton("N�o", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(intent2);
            }
        });

        AlertDialog alert11 = alertB.create();
        alert11.show();
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Fun�ao: load_fav                                      ****
     * Funcionalidade: Carrega lista de favoritos            ****
     * Data Criacao: 13/06/2015                              ****
     ************************************************************/
    protected void load_fav(String txBar, String txRua)
    {
        try
        {
            //Abre o banco ou cria se não existe
            myDataBase = this.openOrCreateDatabase("Banco", SQLiteDatabase.CREATE_IF_NECESSARY, null);

            //Cria tabela se não existe
            myDataBase.execSQL("CREATE TABLE IF NOT EXISTS Favorites (NomeBar VARCHAR(255), RuaBar VARCHAR(255), Latitude VARCHAR(255), Longitiude VARCHAR(255));");

            //Verifica se a tabela possui valores
            Cursor controler = myDataBase.rawQuery("SELECT COUNT(*) FROM Favorites WHERE NomeBar='"+txBar+"' AND RuaBar='"+txRua+"'", null);

            //Move o resultado para a primeira linha
            controler.moveToFirst();

            //Se a tabela possui o bar
            if(controler.getInt(0) > 0)
            {
                btnFav.setBackgroundColor(Color.RED);
                btnFav.setTextColor(Color.WHITE);
                btnFav.setText("Remover");
                myDataBase.close();
            }
            else //Se não possui
            {
                btnFav.setBackgroundColor(Color.GREEN);
                btnFav.setTextColor(Color.BLACK);
                btnFav.setText("Adicionar");
                myDataBase.close();
            }

        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Fun�ao: add_fav                                       ****
     * Funcionalidade: Adiciona item aos favoritos           ****
     * Data Criacao: 13/06/2015                              ****
     ************************************************************/
    public void add_fav(View View)
    {
        myDataBase = this.openOrCreateDatabase("Banco", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        //Se nao encontrar o bar aparece a opcao para adicionar
        if(btnFav.getText().toString().equals("Adicionar"))
        {
            myDataBase.execSQL("INSERT INTO Favorites (NomeBar, RuaBar, Latitude, Longitiude) VALUES ('"+nomedoBar+"','"+ruadoBar+"','"+Lat+"','"+Lng+"');");
            btnFav.setBackgroundColor(Color.RED);
            btnFav.setTextColor(Color.WHITE);
            btnFav.setText("Remover");
            myDataBase.close();
            Toast.makeText(getApplicationContext(), "Bar adicionado com sucesso", Toast.LENGTH_SHORT).show();
        }
        else if(btnFav.getText().toString().equals("Remover")) // Caso encontre o bar marque para remover
        {
            myDataBase.delete("Favorites", "NomeBar='"+nomedoBar+"' AND Ruabar='"+ruadoBar+"'", null);
            btnFav.setBackgroundColor(Color.GREEN);
            btnFav.setTextColor(Color.BLACK);
            btnFav.setText("Adicionar");
            myDataBase.close();
            Toast.makeText(getApplicationContext(), "Bar removido com sucesso", Toast.LENGTH_SHORT).show();
        }
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Fun�ao: OpenConsientizacao                            ****
     * Funcionalidade: Abre Dialog de concientizacao         ****
     * Data Criacao: 16/06/2015                              ****
     ************************************************************/
    protected void OpenConsientizacao()
    {
        //Instancia classe para gerar numero aleatorio
        Random randomGenerator = new Random();

        //Recebe o numero aleatorio
        iNum = randomGenerator.nextInt(3);

        if(iNum == 2)
        {
            alertB = new AlertDialog.Builder(this);
            alertB.setTitle("Aviso");
            alertB.setMessage("Se beber n�o dirija!");
            alertB.setCancelable(false);
            alertB.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                alertB.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

            AlertDialog alert11 = alertB.create();
            alert11.show();
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