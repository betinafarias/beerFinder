package diegocunha.beersfinder;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class myIntineraire {

    //Variaveis Globais
    private Context context;
    private GoogleMap gMap;
    private double myLat, myLng, parseLat, parseLng;
    private List<LatLng> list;
    private long distance;


    public myIntineraire(){}
    public final static String MODE_DRIVING = "driving";

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: getDocument                                   ****
     * Funcionalidade: Recebe infos para Web Service         ****
     * Data Criacao: 28/05/2015                              ****
     ***********************************************************/
    public Document getDocument(LatLng start, LatLng end, String mode) {
        String url = "http://maps.googleapis.com/maps/api/directions/xml?"
                + "origin=" + start.latitude + "," + start.longitude
                + "&destination=" + end.latitude + "," + end.longitude
                + "&sensor=false&units=metric&mode=" + mode;

        try
        {
            //Conecta o webs service
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            InputStream in = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in);
            return doc;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: getDurationText                               ****
     * Funcionalidade: Recebe infos de duracao da rota       ****
     * Data Criacao: 28/05/2015                              ****
     ***********************************************************/
    public String getDurationText (Document doc)
    {
        NodeList nl1 = doc.getElementsByTagName("duration");
        Node node1 = nl1.item(0);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "text"));
        Log.i("DurationText", node2.getTextContent());
        return node2.getTextContent();
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: getDurationValue                              ****
     * Funcionalidade: Recebe infos de duracao da rota       ****
     * Data Criacao: 28/05/2015                              ****
     ***********************************************************/
    public int getDurationValue (Document doc)
    {
        NodeList nl1 = doc.getElementsByTagName("duration");
        Node node1 = nl1.item(0);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
        Log.i("DurationValue", node2.getTextContent());
        return Integer.parseInt(node2.getTextContent());
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: getDistanceText                               ****
     * Funcionalidade: Recebe infos de distancia da rota     ****
     * Data Criacao: 28/05/2015                              ****
     ***********************************************************/
    public String getDistanceText (Document doc)
    {
        NodeList nl1 = doc.getElementsByTagName("distance");
        Node node1 = nl1.item(0);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "text"));
        Log.i("DistanceText", node2.getTextContent());
        return node2.getTextContent();
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: getDistanceValue                              ****
     * Funcionalidade: Recebe infos de distancia da rota     ****
     * Data Criacao: 28/05/2015                              ****
     ***********************************************************/
    public int getDistanceValue (Document doc)
    {
        NodeList nl1 = doc.getElementsByTagName("distance");
        Node node1 = nl1.item(0);
        NodeList nl2 = node1.getChildNodes();
        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
        Log.i("DistanceValue", node2.getTextContent());
        return Integer.parseInt(node2.getTextContent());
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: getStartAddress                               ****
     * Funcionalidade: Recebe infos de Endereco de saida     ****
     * Data Criacao: 28/05/2015                              ****
     ***********************************************************/
    public String getStartAddress (Document doc)
    {
        NodeList nl1 = doc.getElementsByTagName("start_address");
        Node node1 = nl1.item(0);
        Log.i("StartAddress", node1.getTextContent());
        return node1.getTextContent();
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: getEndAddress                                 ****
     * Funcionalidade: Recebe infos de Endereco de chegada   ****
     * Data Criacao: 28/05/2015                              ****
     ***********************************************************/
    public String getEndAddress (Document doc)
    {
        NodeList nl1 = doc.getElementsByTagName("end_address");
        Node node1 = nl1.item(0);
        Log.i("StartAddress", node1.getTextContent());
        return node1.getTextContent();
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: getCopyRights                                 ****
     * Funcionalidade: Recebe infos de Copyrights            ****
     * Data Criacao: 28/05/2015                              ****
     ***********************************************************/
    public String getCopyRights (Document doc)
    {
        NodeList nl1 = doc.getElementsByTagName("copyrights");
        Node node1 = nl1.item(0);
        Log.i("CopyRights", node1.getTextContent());
        return node1.getTextContent();
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: getDirection                                  ****
     * Funcionalidade: Recebe infos de direcao por passo     ****
     * Data Criacao: 28/05/2015                              ****
     ***********************************************************/
    public ArrayList<LatLng> getDirection (Document doc)
    {
        NodeList nl1, nl2, nl3;
        ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
        nl1 = doc.getElementsByTagName("step");

        //Se Recebeu informacoes por passo
        if (nl1.getLength() > 0)
        {
            //Traca rota do inicio ate o fim de acordo com os passos
            for (int i = 0; i < nl1.getLength(); i++)
            {
                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();

                Node locationNode = nl2.item(getNodeIndex(nl2, "start_location"));
                nl3 = locationNode.getChildNodes();
                Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
                double lat = Double.parseDouble(latNode.getTextContent());
                Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                double lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));

                locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "points"));
                ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());

                //Adiciona a lista o resultado dos pasos
                for(int j = 0 ; j < arr.size() ; j++)
                {
                    listGeopoints.add(new LatLng(arr.get(j).latitude, arr.get(j).longitude));
                }

                locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "lat"));
                lat = Double.parseDouble(latNode.getTextContent());
                lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                lng = Double.parseDouble(lngNode.getTextContent());

                //Adiciona o resultado no mapa
                listGeopoints.add(new LatLng(lat, lng));
            }
        }

        return listGeopoints;
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: getNodeIndex                                  ****
     * Funcionalidade: Compara infos de acordo com o Nodo    ****
     * Data Criacao: 28/05/2015                              ****
     ***********************************************************/
    private int getNodeIndex(NodeList nl, String nodename)
    {
        for(int i = 0 ; i < nl.getLength() ; i++)
        {
            if(nl.item(i).getNodeName().equals(nodename))
                return i;
        }
        return -1;
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: decodePoly                                    ****
     * Funcionalidade: Recebe infos final e traça a rota     ****
     * Data Criacao: 28/05/2015                              ****
     ***********************************************************/
    private ArrayList<LatLng> decodePoly(String encoded)
    {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len)
        {
            int b, shift = 0, result = 0;
            do
            {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;

            do
            {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }
}