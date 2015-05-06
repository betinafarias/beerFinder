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
import android.widget.Toast;

/*******************************************
 * Autores: Diego Cunha Gabriel Cataneo ****
 * Cria��o: 28/04/2015                  ****
 * Classe: myLocation                   ****
 * Fun��o: Recebe informa��es do GPS    ****
  ******************************************/
public class myLocation extends Service implements LocationListener
{
	//Variaveis Globais
    private final Context mcontext;
	boolean isGPS = false, isNet = false, haveLocation = false;
	Location location;
	double latitude, longitude, teta, dist;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
	private static final long MIN_TIME_BW_UPDATES = 0;
	protected LocationManager locationManager;

	public myLocation(Context context)
	{
		this.mcontext = context;
		getLocation();
	}

	//Verifica conexao com internet e GPS para prover sinal de posi��o.
	public Location getLocation()
	{
		try
		{
			locationManager = (LocationManager) mcontext.getSystemService(LOCATION_SERVICE);
			
			//Atribui os provedores de localiza��o
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
							toastHandlerREDE.sendEmptyMessage(0);
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
								toastHandlerGPS.sendEmptyMessage(0);
							}
						}
					}
				}
				else
				{
					AbreConfigGPS();
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

	public void stopUsingGPS()
	{
		if (locationManager != null)
		{
			locationManager.removeUpdates(myLocation.this);
		}
	}

	public double getLatitude()
	{
		if(location != null)
		{
			latitude = location.getLatitude();
		}
		return latitude;
	}
	public double getLongitude()
	{
		if(location != null)
		{
			longitude = location.getLongitude();
		}
		return longitude;
	}

	public double calculaDistancia(double lat1, double lat2, double lng1, double lng2)
	{
		try
		{
			teta = lng1 - lng2;
			dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(teta));
			dist = Math.acos(dist);
			dist = rad2deg(dist);
			dist = dist * 60 * 1.1515;
			dist = dist * 1.609344;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			Toast.makeText(getApplication(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
			dist = 0.00;
		}

		return dist;
	}

	private double deg2rad(double deg)
	{
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad)
	{
		return (rad * 180.0 / Math.PI);
	}

	public boolean canGetLocation()
	{
		return this.haveLocation;
	}

	//abrir tela ativa��o gps
	public void AbreConfigGPS()
	{
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		mcontext.startActivity(intent);
	}

	@Override
	public void onLocationChanged(Location location)
	{
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			toastHandlerGPS.sendEmptyMessage(0);
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

	private final Handler toastHandlerREDE = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Toast.makeText(mcontext, "REDE - Latitude: "+ latitude +" Longitude: "+ longitude, Toast.LENGTH_SHORT).show();
		}
	};
	private final Handler toastHandlerGPS = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Toast.makeText(mcontext, "GPS - Latitude: "+ latitude +" Longitude: "+ longitude, Toast.LENGTH_SHORT).show();
		}
	};
	private final Handler toastHandlerGPSoff = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Toast.makeText(mcontext, "Sem provedor de informa��es dispon�vel", Toast.LENGTH_SHORT).show();
		}
	};
}
