package diegocunha.beersfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Timer;


/************************************************************
 * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
 * Funçao: MainActivity                                  ****
 * Funcionalidade: Inicia o SplashScreen                 ****
 * Data Criacao: 14/05/2015                              ****
 ***********************************************************/
public class MainActivity extends Activity {

    //Variaveis Globais
    private String AppID, ClientID;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
     * Funçao: start                                         ****
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
     * Funçao: loadSecond                                    ****
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
     * Funçao: loadFirst                                     ****
     * Funcionalidade: Bloqueia pagina sem login             ****
     * Data Criacao: 05/05/2015                              ****
     ***********************************************************/
    protected void loadFirst()
    {
        Intent intent = new Intent(this, firstActivity.class);
        startActivity(intent);
    }

}
