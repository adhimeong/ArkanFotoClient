package id.co.outlabs.adhi.arkanfotoclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import id.co.outlabs.adhi.arkanfotoclient.R;
import id.co.outlabs.adhi.arkanfotoclient.getset.DataHadiahController;
import id.co.outlabs.adhi.arkanfotoclient.getset.DataPemenangController;
import id.co.outlabs.adhi.arkanfotoclient.volley.MySingleton;
import id.co.outlabs.adhi.arkanfotoclient.volley.Server;

/**
 * Created by adhi on 05/11/18.
 */

public class DataPemenangAdapter extends RecyclerView.Adapter<DataPemenangAdapter.MyViewHolder> {

    private List<DataPemenangController> Listdata;

    ImageLoader mImageLoader;
    String url = Server.url_server +"app/berita/";
    String IMAGE_URL ;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView pemenangfoto;
        public TextView pemenangjudul, pemenangtanggal;
        public Button pemenangbtn;

        public MyViewHolder(View view) {
            super(view);
            pemenangtanggal = (TextView) view.findViewById(R.id.listpemenangtanggal);
            pemenangjudul = (TextView) view.findViewById(R.id.listpemenangjudul);
            pemenangfoto = (NetworkImageView) view.findViewById(R.id.listpemenangfoto);
            pemenangbtn = (Button) view.findViewById(R.id.listpemenangbtnlengkap);
        }
    }

    public DataPemenangAdapter(List<DataPemenangController> Listdata) {
        this.Listdata = Listdata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listdatapemenang, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        DataPemenangController data = Listdata.get(position);
        holder.pemenangjudul.setText(data.getJudul().toString());
        holder.pemenangtanggal.setText(data.getTanggal().toString());

        mImageLoader = MySingleton.getInstance(this.mContext).getImageLoader();
        IMAGE_URL = url + String.valueOf(data.getFoto());
        holder.pemenangfoto.setImageUrl(IMAGE_URL, mImageLoader);

        holder.pemenangbtn.setOnClickListener(new View.OnClickListener() {
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
