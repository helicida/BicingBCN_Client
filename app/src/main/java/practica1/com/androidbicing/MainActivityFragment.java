package practica1.com.androidbicing;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import practica1.com.androidbicing.ObjectsStations.Station;
import practica1.com.androidbicing.ObjectsStations.StationsList;
import practica1.com.androidbicing.provider.stations.StationsColumns;
import practica1.com.androidbicing.provider.stations.StationsContentValues;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;


/**
 * A placeholder fragment containing a simple view.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // URL de Bicing http://wservice.viabicing.cat/v2/stations

    Retrofit retrofit;                                              // Retrofit
    BicingService bicingService;                                    // Servicio debicing
    BicingAdapter adapter;                                          // Adapter
    public String BASE_URL = "http://wservice.viabicing.cat/v2/";



    public MainActivityFragment() {

    }

    public void onStart() {
        super.onStart();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainActivityFragment =  inflater.inflate(R.layout.fragment_main, container, false);
        getLoaderManager().restartLoader(0, null, this);

        // Descargamos los datos de las estaciones, limpiando previamente la BBDD

        createRetrofit();
        deleteDatabase();
        downloadStations();

        ListView listView = (ListView) mainActivityFragment.findViewById(R.id.LVstations);
        Button openMap = (Button) mainActivityFragment.findViewById(R.id.BTmap);

        // Listener del boton
        openMap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent map = new Intent(getContext(), MapActivity.class);
                startActivity(map);
            }
        });

        adapter = new BicingAdapter(
                getContext(),
                R.layout.listview_layout,
                null,
                new String[] {
                        StationsColumns._ID,
                        StationsColumns.BIKES,
                        StationsColumns.STREETNUMBER,
                        StationsColumns.STREETNAME,
                        StationsColumns.ALTITUDE,
                        StationsColumns.LATITUDE,
                        StationsColumns.LONGITUDE,
                        StationsColumns.NEARBYSTATIONS,
                        StationsColumns.SLOTS,
                        StationsColumns.STATUS},
                new int[] {
                        R.id.TVid,
                        R.id.TVbikes,
                        R.id.TVstreetNumber,
                        R.id.TVstreetName,
                        R.id.TValtitude,
                        R.id.TVlatitude,
                        R.id.TVlongitude,
                        R.id.TVnearbyStations,
                        R.id.TVslots,
                        R.id.TVstatus},
                0);
        listView.setAdapter(adapter);
        return mainActivityFragment;
    }

    public void deleteDatabase() {
        getContext().getContentResolver().delete(
                StationsColumns.CONTENT_URI,
                null,
                null);
    }

    public void downloadStations() {

        Call<StationsList> call = bicingService.getStations();
        call.enqueue(new Callback<StationsList>(){

            @Override
            public void onResponse(Response<StationsList> response) {

                if (response.isSuccess()) {

                    StationsList stationsList = response.body();
                    int size = stationsList.getStations().size();

                    for(int iterador = 0; iterador < size; iterador++) {
                        Station station = stationsList.getStations().get(iterador);
                        StationsContentValues values = new StationsContentValues();
                        values.putAltitude(station.getAltitude());
                        values.putBikes(station.getBikes());
                        values.putLatitude(station.getLatitude());
                        values.putLongitude(station.getLongitude());
                        values.putNearbystations(station.getNearbyStations());
                        values.putSlots(station.getSlots());
                        values.putStatus(station.getStatus());
                        values.putStreetname(station.getStreetName());
                        values.putStreetnumber(station.getStreetNumber());
                        getContext().getContentResolver().insert(StationsColumns.CONTENT_URI, values.values());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {}
        });
    }

    public void createRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bicingService = retrofit.create(BicingService.class);
    }

    public interface BicingService {
        @GET("stations")
        Call<StationsList> getStations();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                StationsColumns.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
