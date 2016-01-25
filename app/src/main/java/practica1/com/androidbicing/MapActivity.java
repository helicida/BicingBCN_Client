package practica1.com.androidbicing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Map
        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        // Añadimos el zoom a través de multitouch
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        //  Setteamos el zoom al mismo nivel y ajustamos la posición a un geopunto
        IMapController mapController = map.getController();
        mapController.setZoom(9);
        GeoPoint startPoint = new GeoPoint(41.355895, 2.114489);
        mapController.setCenter(startPoint);

    }
}
