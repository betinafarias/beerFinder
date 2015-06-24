package diegocunha.beersfinder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
    private AlertDialog.Builder alertB;
    private ProgressDialog mProgressDialog;
    private Intent intent;
    private TextView textCount;
    private int size = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcomentarios);
        getUser();

        //Inicializa as classes necessarias
        MeuLugar = new myLocation(this);
        Bundle extras = getIntent().getExtras();
        mProgressDialog = new ProgressDialog(this);

        //Inicializa os TextView e EditView
        eTexto = (EditText)findViewById(R.id.txtSave);
        eTexto.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        textCount = (TextView)findViewById(R.id.txtCount);

        //Cria o Live Counter para atualizar o maximo e caracteres disponiveis
        textCount.setText(String.valueOf(size));
        eTexto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textCount.setText(String.valueOf(size - s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Se recebeu valores pelo Intent
        if(extras != null)
        {
            strNomeBar = extras.getString("NomeBar");
            strRuaBar = extras.getString("RuaBar");

            intent = new Intent(this, ComentariosActivity.class);
            intent.putExtra("NomeBar", strNomeBar);
            intent.putExtra("RuaBar", strRuaBar);

        }
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funcao: getUser                                       ****
     * Funcionalidade: Bloqueia pagina sem login             ****
     * Data Criacao: 05/05/2015                              ****
     ***********************************************************/
    protected void getUser()
    {
        //Busca do parse o current user
        ParseUser currentUser = ParseUser.getCurrentUser();

        //Se nao a usuario selecionado manda para a tela de login
        if(currentUser == null)
        {
            Intent intent = new Intent(this, firstActivity.class);
            startActivity(intent);

        }
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funcao: verificaConexao                               ****
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

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funcao: addComent                                     ****
     * Funcionalidade: Adiciona comentario                   ****
     * Data Criacao: 21/09/2015                              ****
     ***********************************************************/
    public void addComent(View view)
    {
        //Inicializa o ProgressDialog
        mProgressDialog.setTitle("Carregando");
        mProgressDialog.setMessage("Loading. . .");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        //Verifica se tem conexao
        if(verificaConexao())
        {
            try
            {
                //Busca username salvo nas preferencias
                SharedPreferences sp1=this.getSharedPreferences("Login", 0);
                strUser = sp1.getString("Unm", null);

                //Recebe texto do EditText
                strText = eTexto.getText().toString();

                //Salva o comentario no Parse
                ParseObject query = new ParseObject("Comentarios");
                query.put("NomeBar", strNomeBar);
                query.put("RuaBar", strRuaBar);
                query.put("Username", strUser);
                query.put("Comentario", strText);
                query.save();

                mProgressDialog.dismiss();
                Toast.makeText(getApplication(), "Comentario adicionado com sucesso", Toast.LENGTH_LONG);
                startActivity(intent);
            }
            catch (Exception ex)
            {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                startActivity(intent);
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
     * Funçao: OpenNet                                       ****
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
        alertB.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(intent2);
            }
        });

        AlertDialog alert11 = alertB.create();
        alert11.show();
    }

    public void cancelComent(View view)
    {
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
