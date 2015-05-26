package diegocunha.beersfinder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.widget.Toast;;
import com.parse.Parse;
import com.parse.ParseCrashReporting;

/*******************************************
 * Autores: Diego Cunha Gabriel Cataneo ****
 * Criação: 28/04/2015                  ****
 * Classe: myLocation                   ****
 * Função: Recebe informações do GPS    ****
 ******************************************/
public class myLocation extends Service implements LocationListener
{
	//Variáveis Globais
    private final Context mcontext;
	boolean isGPS = false, isNet = false, haveLocation = false;
	Location location;
	double latitude, longitude, teta, dist, Radius;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
	private static final long MIN_TIME_BW_UPDATES = 0;
	protected LocationManager locationManager;
	private String AppID, ClientID;

	public myLocation(Context context)
	{
		this.mcontext = context;
		getLocation();
	}

	/**********************************************
	 * Autores: Diego Cunha Gabriel Cataneo    ****
	 * Criação: 28/04/2015                     ****
	 * Função: Location getLocation            ****
	 * Funcionalidade: Recebe infos GPS        ****
	 * OBS: Não mexer					       ****
	 **********************************************/
	public Location getLocation()
	{
		try
		{
			locationManager = (LocationManager) mcontext.getSystemService(LOCATION_SERVICE);
			
			//Atribui os provedores de localização
			isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNet = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			if(!isGPS && !isNet)
			{
				this.haveLocation = false;
				toastHandlerGPSoff.sendEmptyMessage(0);
			}
			else
			{
				this.haveLocation = true;
				if(isNet)
				{
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

					if(locationManager != null)
					{
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

						if(location != null)
						{
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				else if(isGPS)
				{
					if(location == null)
					{
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

						if(locationManager != null)
						{
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

							if(location != null)
							{
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			latitude = 0.00;
			longitude = 0.00;
		}

		return location;
	}

	/**********************************************
	 * Autores: Diego Cunha Gabriel Cataneo    ****
	 * Criação: 28/04/2015                     ****
	 * Função: void stopUsingGPS               ****
	 * Funcionalidade: Encerra uso GPS         ****
	 * OBS: Não mexer					       ****
	 **********************************************/
	public void stopUsingGPS()
	{
		if (locationManager != null)
		{
			locationManager.removeUpdates(myLocation.this);
		}
	}

	/**********************************************
	 * Autores: Diego Cunha Gabriel Cataneo    ****
	 * Criação: 28/04/2015                     ****
	 * Função: double getLatitude              ****
	 * Funcionalidade: Retorna valor latitude  ****
	 * OBS: Não mexer					       ****
	 **********************************************/
	public double getLatitude()
	{
		if(location != null)
		{
			latitude = location.getLatitude();
		}
		return latitude;
	}

	/**********************************************
	 * Autores: Diego Cunha Gabriel Cataneo    ****
	 * Criação: 28/04/2015                     ****
	 * Função: double getLongitude             ****
	 * Funcionalidade: Retorna valor longitude ****
	 * OBS: Não mexer					       ****
	 **********************************************/
	public double getLongitude()
	{
		if(location != null)
		{
			longitude = location.getLongitude();
		}
		return longitude;
	}

	/**********************************************
	 * Autores: Diego Cunha Gabriel Cataneo    ****
	 * Criação: 05/05/2015                     ****
	 * Função: double calculaDistancia         ****
	 * Funcionalidade: Realiza cálculo dist.   ****
	 * Correção: 22/05/2015 - Diego Cunha
	 *
	 **********************************************/
	public double calculaDistancia(double lat1, double lat2, double lng1, double lng2)
	{
		try
		{
			Radius = 6371;
			double dLat= Math.toRadians(lat2-lat1);
			double dLng = Math.toRadians(lng2-lng1);
			double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng/2) * Math.sin(dLng/2);
			dist = 2 * Math.asin(Math.sqrt(a));
			dist = dist * Radius;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			Toast.makeText(getApplication(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
		}
		return dist;
	}

	/**********************************************
	 * Autores: Diego Cunha Gabriel Cataneo    ****
	 * Criação: 05/05/2015                     ****
	 * Função: double deg2rad                  ****
	 * Funcionalidade: Realiza calculo dist.   ****
	 * OBS: Não mexer					       ****
	 **********************************************/
	private double deg2rad(double deg)
	{
		return (deg * Math.PI / 180.0);
	}

	/**********************************************
	 * Autores: Diego Cunha Gabriel Cataneo    ****
	 * Criação: 05/05/2015                     ****
	 * Função: double rad2deg                  ****
	 * Funcionalidade: Realiza calculo dist.   ****
	 * OBS: Não mexer					       ****
	 **********************************************/
	private double rad2deg(double rad)
	{
		return (rad * 180.0 / Math.PI);
	}

	/**********************************************
	 * Autores: Diego Cunha Gabriel Cataneo    ****
	 * Criação: 28/04/2015                     ****
	 * Função: boolean canGetLocation          ****
	 * Funcionalidade: Retorna se tem GPS      ****
	 * OBS: Não mexer					       ****
	 **********************************************/
	public boolean canGetLocation()
	{
		return this.haveLocation;
	}

	/**********************************************
	 * Autores: Diego Cunha Gabriel Cataneo    ****
	 * Criação: 28/04/2015                     ****
	 * Função: void AbreConfigGPS              ****
	 * Funcionalidade: Abre tela GPS           ****
	 **********************************************/
	public void AbreConfigGPS()
	{
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		mcontext.startActivity(intent);
	}

	/**********************************************
	 * Autores: Diego Cunha Gabriel Cataneo    ****
	 * Criação: 28/04/2015                     ****
	 * Função: void AbreConfigNET              ****
	 * Funcionalidade: Abre tela WIFI          ****
	 **********************************************/
	public void AbreConfigNET()
	{
		Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
		mcontext.startActivity(intent);
	}

	/**********************************************
	 * Autores: Diego Cunha Gabriel Cataneo    ****
	 * Criação: 28/04/2015                     ****
	 * Função: void onLocationChanged          ****
	 * Funcionalidade: Atualiza info GPS       ****
	 * OBS: Não mexer					       ****
	 **********************************************/
	@Override
	public void onLocationChanged(Location location)
	{
			latitude = location.getLatitude();
			longitude = location.getLongitude();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	private final Handler toastHandlerGPSoff = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Toast.makeText(mcontext, "Sem provedor de informações disponível", Toast.LENGTH_SHORT).show();
		}
	};
}
