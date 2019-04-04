package id.co.outlabs.adhi.arkanfotoclient.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.outlabs.adhi.arkanfotoclient.R;
import id.co.outlabs.adhi.arkanfotoclient.getset.DataBankController;

/**
 * Created by adhi on 27/03/19.
 */

public class DataBankAdapter extends ArrayAdapter<DataBankController>{

    private List<DataBankController> countryListFull;

    public DataBankAdapter(@NonNull Context context, @NonNull List<DataBankController> countryList) {
        super(context, 0, countryList);
        countryListFull = new ArrayList<>(countryList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return countryFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listdatabank, parent, false
            );
        }

        TextView namabank = (TextView) convertView.findViewById(R.id.listbanknama);
        TextView kodebank = (TextView) convertView.findViewById(R.id.listbankkode);


        DataBankController countryItem = getItem(position);

        if (countryItem != null) {
            namabank.setText(countryItem.getNamabank());
            kodebank.setText(countryItem.getKodebank());
        }

        return convertView;
    }

    private Filter countryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<DataBankController> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(countryListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (DataBankController item : countryListFull) {
                    if (item.getNamabank().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((DataBankController) resultValue).getKodebank() +"-"+((DataBankController) resultValue).getNamabank();
        }
    };
}