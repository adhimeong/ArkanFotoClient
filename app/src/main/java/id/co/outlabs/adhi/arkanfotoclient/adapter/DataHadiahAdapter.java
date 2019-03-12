package id.co.outlabs.adhi.arkanfotoclient.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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
    int pointuser, pointhadiah;
    String no_kartu;

    public Dialog myDialog, dialogQrcode;

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

    public DataHadiahAdapter(Context context, List<DataHadiahController> Listdata, int pointuser, String no_kartu) {
        this.mContext = context;
        this.Listdata = Listdata;
        this.pointuser = pointuser;
        this.no_kartu = no_kartu;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listdatahadiah, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final DataHadiahController data = Listdata.get(position);
        holder.hadiahnama.setText(data.getNama_hadiah());
        holder.hadiahpoint.setText(data.getJumlah_point());
        holder.hadiahitems.setText(data.getJumlah_items());

        mImageLoader = MySingleton.getInstance(this.mContext).getImageLoader();
        IMAGE_URL = url + String.valueOf(data.getFoto_hadiah());
        holder.fotohadiah.setImageUrl(IMAGE_URL, mImageLoader);

        holder.cardhadiah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDialog = new Dialog(mContext);
                myDialog.setContentView(R.layout.dialoghadiah);

                TextView dialogketerangan = (TextView) myDialog.findViewById(R.id.dialoghadiahketerangan);
                ImageView dialogimage = (ImageView) myDialog.findViewById(R.id.dialoghadiahimage);
                Button dialogbtnambil = (Button) myDialog.findViewById(R.id.dialoghadiahambil);
                Button dialogbtnclose = (Button) myDialog.findViewById(R.id.dialoghadiahclose);

                pointhadiah = Integer.parseInt(data.getJumlah_point());

                if (pointuser >= pointhadiah){
                    dialogketerangan.setText("poinmu siap dipakai");
                    dialogimage.setImageResource(R.drawable.dialoggift);

                    dialogbtnambil.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            myDialog.dismiss();

                            dialogQrcode = new Dialog(mContext);
                            dialogQrcode.setContentView(R.layout.dialogqrcode);

                            ImageView dialogqrimage = (ImageView) dialogQrcode.findViewById(R.id.dialogqrcodeimage);
                            Button dialogqrbtnclose = (Button) dialogQrcode.findViewById(R.id.dialogqrcodeclose);

                            String idhadiah = data.getId_hadiah();
                            String nokartu = no_kartu;
                            String text2Qr = nokartu+","+idhadiah;

                            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                            try {

                                BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,500,500);
                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                dialogqrimage.setImageBitmap(bitmap);
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }

                            dialogqrbtnclose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogQrcode.dismiss();
                                }
                            });

                            dialogQrcode.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogQrcode.show();
                        }
                    });

                }else{
                    dialogketerangan.setText("poinmu belum cukup");
                    dialogimage.setImageResource(R.drawable.dialogpoin);
                    dialogbtnambil.setVisibility(View.GONE);
                }

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