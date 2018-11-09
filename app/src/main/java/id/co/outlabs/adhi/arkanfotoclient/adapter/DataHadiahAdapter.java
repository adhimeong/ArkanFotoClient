package id.co.outlabs.adhi.arkanfotoclient.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import id.co.outlabs.adhi.arkanfotoclient.R;
import id.co.outlabs.adhi.arkanfotoclient.getset.DataHadiahController;
import id.co.outlabs.adhi.arkanfotoclient.volley.MySingleton;
import id.co.outlabs.adhi.arkanfotoclient.volley.Server;

/**
 * Created by adhi on 05/11/18.
 */

public class DataHadiahAdapter extends RecyclerView.Adapter<DataHadiahAdapter.MyViewHolder> {

    private List<DataHadiahController> Listdata;

    ImageLoader mImageLoader;
    String url = Server.url_server +"app/hadiah/";
    String IMAGE_URL ;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView fotohadiah;
        public TextView hadiahnama, hadiahpoint, hadiahitems;
        public CardView cardhadiah;

        public MyViewHolder(View view) {
            super(view);
            hadiahnama = (TextView) view.findViewById(R.id.listnamahadiah);
            hadiahpoint = (TextView) view.findViewById(R.id.listjumlah_point);
            hadiahitems = (TextView) view.findViewById(R.id.listjumlahitem);
            fotohadiah = (NetworkImageView) view.findViewById(R.id.listfotohadiah);
            cardhadiah = (CardView) view.findViewById(R.id.cardhadiah);
        }
    }

    public DataHadiahAdapter(List<DataHadiahController> Listdata) {
        this.Listdata = Listdata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listdatahadiah, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        DataHadiahController data = Listdata.get(position);
        holder.hadiahnama.setText(data.getNama_hadiah());
        holder.hadiahpoint.setText(data.getJumlah_point());
        holder.hadiahitems.setText(data.getJumlah_items());

        mImageLoader = MySingleton.getInstance(this.mContext).getImageLoader();
        IMAGE_URL = url + String.valueOf(data.getFoto_hadiah());
        holder.fotohadiah.setImageUrl(IMAGE_URL, mImageLoader);

        holder.cardhadiah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return Listdata.size();
    }

}