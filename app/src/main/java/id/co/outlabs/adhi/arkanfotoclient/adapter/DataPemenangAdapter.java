package id.co.outlabs.adhi.arkanfotoclient.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import id.co.outlabs.adhi.arkanfotoclient.R;
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

    public Dialog myDialog;

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

    public DataPemenangAdapter( Context context, List<DataPemenangController> Listdata) {
        this.mContext = context;
        this.Listdata = Listdata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listdatapemenang, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final DataPemenangController data = Listdata.get(position);
        holder.pemenangjudul.setText(data.getJudul().toString());
        holder.pemenangtanggal.setText(data.getTanggal().toString());

        mImageLoader = MySingleton.getInstance(this.mContext).getImageLoader();
        IMAGE_URL = url + String.valueOf(data.getFoto());
        holder.pemenangfoto.setImageUrl(IMAGE_URL, mImageLoader);

        holder.pemenangfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(mContext);
                myDialog.setContentView(R.layout.dialogpemenang);

                TextView dialogjudul = (TextView) myDialog.findViewById(R.id.dialogpemenangjudul);
                TextView dialogtanggal = (TextView) myDialog.findViewById(R.id.dialogpemenangtanggal);
                TextView dialogisi = (TextView) myDialog.findViewById(R.id.dialogpemenangisi);
                NetworkImageView dialogfoto = (NetworkImageView) myDialog.findViewById(R.id.dialogpemenangfoto);
                Button dialogbtnclose = (Button) myDialog.findViewById(R.id.dialogpemenangtutup);

                dialogjudul.setText(data.getJudul().toString());
                dialogtanggal.setText(data.getTanggal().toString());
                dialogisi.setText(data.getIsi().toString());

                mImageLoader = MySingleton.getInstance(mContext).getImageLoader();
                IMAGE_URL = url + String.valueOf(data.getFoto());
                dialogfoto.setImageUrl(IMAGE_URL, mImageLoader);

                dialogbtnclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });

                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
            }
        });

        holder.pemenangbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDialog = new Dialog(mContext);
                myDialog.setContentView(R.layout.dialogpemenang);

                TextView dialogjudul = (TextView) myDialog.findViewById(R.id.dialogpemenangjudul);
                TextView dialogtanggal = (TextView) myDialog.findViewById(R.id.dialogpemenangtanggal);
                TextView dialogisi = (TextView) myDialog.findViewById(R.id.dialogpemenangisi);
                NetworkImageView dialogfoto = (NetworkImageView) myDialog.findViewById(R.id.dialogpemenangfoto);
                Button dialogbtnclose = (Button) myDialog.findViewById(R.id.dialogpemenangtutup);

                dialogjudul.setText(data.getJudul().toString());
                dialogtanggal.setText(data.getTanggal().toString());
                dialogisi.setText(data.getIsi().toString());

                mImageLoader = MySingleton.getInstance(mContext).getImageLoader();
                IMAGE_URL = url + String.valueOf(data.getFoto());
                dialogfoto.setImageUrl(IMAGE_URL, mImageLoader);

                dialogbtnclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });

                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return Listdata.size();
    }

}
