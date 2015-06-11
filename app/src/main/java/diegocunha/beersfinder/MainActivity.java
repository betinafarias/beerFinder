package diegocunha.beersfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Timer;

public class MainActivity extends ActionBarActivity {

    //Variaveis Globais
    private String AppID, ClientID;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Parse Crash Report
        ParseCrashReporting.enable(this);

        //Direciona ID do Parse
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);

        //Inicializa informacoes
        Parse.initialize(this, AppID, ClientID);
        ParseFacebookUtils.initialize(getApplicationContext());
        imageView = (ImageView)findViewById(R.id.gyro);
        imageView.setBackgroundResource(R.drawable.animation_xml);
        AnimationDrawable gyroAnimation = (AnimationDrawable)imageView.getBackground();
        start();
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Fun�ao: start                                         ****
     * Funcionalidade: Inicia o SplashScreen                 ****
     * Data Criacao: 14/05/2015                              ****
     ***********************************************************/
    public void start()
    {
        final AnimationDrawable gyroAnimation = (AnimationDrawable)imageView.getBackground();
        gyroAnimation.start();

        if(ParseCrashReporting.isCrashReportingEnabled())
        {
            SharedPreferences sp1=this.getSharedPreferences("Login", 0);

            String unm=sp1.getString("Unm", null);
            String pass = sp1.getString("Psw", null);

            if(unm != null && pass != null)
            {
                ParseUser.logInInBackground(unm, pass, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(parseUser != null)
                        {
                            gyroAnimation.stop();
                            loadSecond();
                        }
                        else
                        {
                            gyroAnimation.stop();
                            loadFirst();
                        }
                    }
                });
            }
            else
            {
                Intent intent = new Intent(this, firstActivity.class);
                gyroAnimation.stop();
                startActivity(intent);
            }
        }
        else
        {
            gyroAnimation.stop();
            Toast.makeText(getApplicationContext(), "errou", Toast.LENGTH_SHORT);
            finish();
        }
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Fun�ao: loadSecond                                    ****
     * Funcionalidade: Abre secondactivity                   ****
     * Data Criacao: 05/05/2015                              ****
     ***********************************************************/
    protected void loadSecond()
    {
        Intent intent = new Intent(this, secondActivity.class);
        startActivity(intent);
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Fun�ao: loadFirst                                     ****
     * Funcionalidade: Bloqueia pagina sem login             ****
     * Data Criacao: 05/05/2015                              ****
     ***********************************************************/
    protected void loadFirst()
    {
        Intent intent = new Intent(this, firstActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
