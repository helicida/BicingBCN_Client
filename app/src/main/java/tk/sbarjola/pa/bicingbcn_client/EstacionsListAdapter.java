package tk.sbarjola.pa.bicingbcn_client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.List;

import tk.sbarjola.pa.bicingbcn_client.POJOBicing.Station;

/**
 * Created by 46465442z on 18/01/16.
 */
public class EstacionsListAdapter extends ArrayAdapter<Station> {

    public EstacionsListAdapter(Context context, int resource, List<Station> objects) {

        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    // Creamos el objeto en la posición correspondiente
        Station estacio = getItem(position);

    // Comprobamos si la view ya se ha usado antes, si no, la inflamos (es una buena practica y ahorramos recursos)
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.estacions_list_adapter, parent, false);
        }

    // Asociamos cada variable a su elemento del layout
        TextView nomCarrer = (TextView) convertView.findViewById(R.id.estacions_adapter_calle);
        nomCarrer.setText(estacio.getStreetName() + " - " + estacio.getStreetNumber());
        TextView numeroSlots = (TextView) convertView.findViewById(R.id.estacions_adapter_slots);
        numeroSlots.setText(estacio.getSlots());

        return convertView; //Devolvemos la view ya rellena
    }
}