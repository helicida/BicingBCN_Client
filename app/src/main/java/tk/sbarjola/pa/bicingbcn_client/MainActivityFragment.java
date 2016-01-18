package tk.sbarjola.pa.bicingbcn_client;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;
import tk.sbarjola.pa.bicingbcn_client.POJOBicing.Bicing;
import tk.sbarjola.pa.bicingbcn_client.POJOBicing.Station;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    BiciServiceEstacions serveiBicis;
    EstacionsListAdapter myListAdapter; // Adaptador per al listView
    private ArrayList<Station> items;    // ArrayList amb els items
    private ListView llistaEstacions;   // Llista estacions

    // Datos de la API
    private String BaseURL = "http://wservice.viabicing.cat/v2/"; //Principio de la URL que usará retrofit

    // Declarem el retrofit variable global per a que així puguem ferlo servir desde tots els metodes sense tornar-ho a definir
    private Retrofit retrofit = new Retrofit.Builder() //Retrofit
            .baseUrl(BaseURL) //Primera parte de la url
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View fragmentoLista = inflater.inflate(R.layout.fragment_main, container, false); //Definimos el fragment

        llistaEstacions = (ListView) fragmentoLista.findViewById(R.id.listView);

        items = new ArrayList<>(); //array list que contindrà les pel·licules

        myListAdapter = new EstacionsListAdapter(getContext(), 0, items); // Definim adaptador al layaout predefinit i al nostre array items
        llistaEstacions.setAdapter(myListAdapter); //Acoplem el adaptador

        DescarregarEstacions descargarEstaciones = new DescarregarEstacions(); // Instanciams nuestro asyncTask para descargar en segundo plano las noticias
        descargarEstaciones.execute();

        //Afegim diverses entrades al ListView que apareixeran per defecte
        return fragmentoLista;
    }

    public void estacionsBici(){

        serveiBicis = retrofit.create(BiciServiceEstacions.class);
        final Call<Bicing> llamada = (Call<Bicing>) serveiBicis.estacions();

        llamada.enqueue(new Callback<Bicing>() {

            @Override
            public void onResponse(Response<Bicing> response) {

                Bicing llistaEstacions =  response.body();

                for (int iterador = 0; iterador < llistaEstacions.getStations().size(); iterador++){
                    items.add(llistaEstacions.getStations().get(iterador));
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public interface BiciServiceEstacions{ //Interficie per a la llista de popular
        @GET("stations")
        Call<Bicing> estacions(); // Le pasamos la URL entera ya construida
    }

    class DescarregarEstacions extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            estacionsBici();
            return null;
        }
    }
}
