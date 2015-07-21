package diegocunha.beersfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/************************************************************
 * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
 * Funcao: BaresOnMapActivity                            ****
 * Funcionalidade: Mostra bares no mapa                  ****
 * Data Criacao: 04/06/2015                              ****
 ***********************************************************/
public class BaresOnMapActivity extends Activity {

    //Variaveis Globais
    private String AppID, ClientID, resultado, strHour, strAbertura, strFechamento, strResultado, strCerveja, strPreco;
    private GoogleMap googleMAp;
    private LatLng lBar, lMeuLugar;
    private double Latitude, Longitude;
    myLocation MeuLugar;
    private ProgressDialog mProgressDialog;
    private Marker barMarker, myMarker;
    private Calendar cal1, cal2, cal3;
    private boolean conectado;
    private ConnectivityManager conectivtyManager;
    private AlertDialog.Builder alertB;
    private int iNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bloqueia sem login
        getUser();

        //Abre conscientização
        OpenConsientizacao();
        setContentView(R.layout.activity_baresonmap);

        //Instancia maps a variavel
        googleMAp = ((MapFragment) getFragmentManager().findFragmentById(R.id.map2))
                .getMap();
        googleMAp.setMyLocationEnabled(true);

        //Inicializa informacoes
        mProgressDialog = new ProgressDialog(this);
        MeuLugar = new myLocation(this);

        //Carrrega bares no mapa
        loadbares();

    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funcao: getUser                                       ****
     * Funcionalidade: Bloqueia pagina sem login             ****
     * Data Criacao: 05/05/2015                              ****
     ***********************************************************/
    protected void getUser()
    {
        //Busca usuario atual do Parse
        ParseUser currentUser = ParseUser.getCurrentUser();

        //Verifica se existe usuario logado
        if(currentUser == null)
        {
            Intent intent = new Intent(this, firstActivity.class);
            startActivity(intent);
        }
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funï¿½ao: verificaConexao                               ****
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
     * Funcao: loadbares                                     ****
     * Funcionalidade: Mostra bares no maps                  ****
     * Criacao: 04/06/2015                                   ****
     ************************************************************/
    protected void loadbares()
    {
        //Incializa o ProgressDialog
        mProgressDialog.setTitle("Carregando");
        mProgressDialog.setMessage("Loading. . .");
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        //Verifica se ha conexao com internet
       if(verificaConexao())
       {
           //Verifica se consegue pegar info do GPS
          if(MeuLugar.canGetLocation())
          {
              //Adiciona valores de latitude e longitude
              Latitude = MeuLugar.getLatitude();
              Longitude = MeuLugar.getLongitude();
              lMeuLugar = new LatLng(Latitude, Longitude);

              //DateTime
              cal1 = Calendar.getInstance(); // Abertura
              cal2 = Calendar.getInstance(); // Fechamento
              cal3 = Calendar.getInstance(); // Atual

              //Hora Atual
              Date now = new Date();
              SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
              strHour = sdf.format(now);

              //Move camera para posicionamento do usuario
              googleMAp.moveCamera(CameraUpdateFactory.newLatLngZoom(lMeuLugar, 13));

              //Adiciona marker para posicao do usuario
              myMarker = googleMAp.addMarker(new MarkerOptions()
                      .title("Eu estou aqui")
                      .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position))
                      .position(lMeuLugar));

              try
              {
                  //Carrega informacoes do Parse
                  ParseQuery<ParseObject> query = ParseQuery.getQuery("BaresLocal");
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
                                      //Busca itens da lista
                                      ParseObject pObject = list.get(i);
                                      String strNomeBar = pObject.getString("NomeBar").replace("_", " ");
                                      String strRuaBar = pObject.getString("RuaBar");
                                      strCerveja = pObject.getString("NomeCerveja");
                                      strPreco = "R$: " + String.format("%.2f",list.get(i).getDouble("Preco"));
                                      double parseLat = pObject.getDouble("Latitude");
                                      double parseLng = pObject.getDouble("Longitude");
                                      strAbertura = pObject.getString("Abertura");
                                      strFechamento = pObject.getString("Fechamento");

                                      //Cria informação para ser adicionado no Snipet
                                      strResultado = strRuaBar + System.getProperty("line.separator") + strCerveja + " - " + strPreco ;

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


                                      //Verifica se o bar esta aberto
                                      if(cal3.after(cal1) || cal3.before(cal2))
                                      {
                                          resultado = "Aberto";
                                      }
                                      else
                                      {
                                          resultado = "Fechado";
                                      }

                                      //Adiciona marker no bar
                                      lBar = new LatLng(parseLat, parseLng);
                                      barMarker = googleMAp.addMarker(new MarkerOptions()
                                              .title(strNomeBar)
                                              .snippet(strResultado)
                                              .icon(BitmapDescriptorFactory.fromResource(R.drawable.bar_ico))
                                              .position(lBar));
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
              }
          }
          else
          {
              mProgressDialog.dismiss();
              OpenGPS();
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
     * Funcao: OpenGPS                                       ****
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
        alertB.setNegativeButton("Não", new DialogInterface.OnClickListener() {
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
     * Funcao: OpenNet                                       ****
     * Funcionalidade: Abre Config de internet               ****
     * Data Criacao: 11/06/2015                              ****
     ***********************************************************/
    protected void OpenNet()
    {
        final Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
        final Intent intent2 = new Intent(this, secondActivity.class);

        alertB = new AlertDialog.Builder(this);
        alertB.setTitle("Aviso");
        alertB.setMessage("Sem conexão com internet, deseja ativar?");
        alertB.setCancelable(false);
        alertB.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(intent);
            }
        });
        alertB.setNegativeButton("NÃ£o", new DialogInterface.OnClickListener() {
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
     * Funcao: OpenConsientizacao                            ****
     * Funcionalidade: Abre Dialog de concientizacao         ****
     * Data Criacao: 16/06/2015                              ****
     ************************************************************/
    protected void OpenConsientizacao()
    {
        Random randomGenerator = new Random();

        iNum = randomGenerator.nextInt(3);

        if(iNum == 2)
        {
            alertB = new AlertDialog.Builder(this);
            alertB.setTitle("Aviso");
            alertB.setMessage("Se beber não dirija!");
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


}
