package id.co.outlabs.adhi.arkanfotoclient.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.co.outlabs.adhi.arkanfotoclient.R;
import id.co.outlabs.adhi.arkanfotoclient.getset.DataTransaksiController;

/**
 * Created by adhi on 15/04/19.
 */

public class DataTransaksiAdapter extends ArrayAdapter<DataTransaksiController> implements View.OnClickListener {

    private List<DataTransaksiController> data;

    String txtidtransaksi, txtnokartu, txtrektujuan, txtnominal, txtjenis, txtbank, txttanggal, txtpenerima, txtadmin;

    Context mContext;

    private static class ViewHolder {
        TextView idtransaksi;
        TextView nokartu;
        TextView rektujuan;
        TextView nominal;
        TextView jenis;
        TextView bank;
        TextView tanggal;
        TextView penerima;
        TextView admin;
    }

    public DataTransaksiAdapter(List<DataTransaksiController> data, Context context) {
        super(context, R.layout.listdatatransaksi, data);
        this.data = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DataTransaksiController data = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listdatatransaksi, parent, false);

            viewHolder.idtransaksi = (TextView) convertView.findViewById(R.id.listtransid);
            viewHolder.nokartu = (TextView) convertView.findViewById(R.id.listtransnokartu);
            viewHolder.nominal = (TextView) convertView.findViewById(R.id.listtransnominal);
            viewHolder.rektujuan = (TextView) convertView.findViewById(R.id.listtransrek);
            viewHolder.bank = (TextView) convertView.findViewById(R.id.listtransbank);
            viewHolder.jenis = (TextView) convertView.findViewById(R.id.listtransjenis);
            viewHolder.tanggal = (TextView) convertView.findViewById(R.id.listtranstanggal);
            viewHolder.penerima = (TextView) convertView.findViewById(R.id.listtranspenerima);
            viewHolder.admin = (TextView) convertView.findViewById(R.id.listtransadmin);

            result = convertView;

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;

        }

        txtidtransaksi = data.getId_tansaksi();
        txtnokartu = data.getNo_kartu();
        txtnominal = data.getNominal();
        txtrektujuan = data.getRek_tujuan();
        txtbank = data.getBank();
        txtjenis = data.getJenis();
        txttanggal = data.getTanggaltransaksi();
        txtpenerima = data.getPenerima();
        txtadmin = data.getNamaadmin();

        if (txtnominal.equals("null")){
            txtnominal = "";
        }

        if (txtbank.equals("null")){
            txtbank = "";
        }

        if (txtrektujuan.equals("null")){
            txtrektujuan = "";
        }

        if (txtpenerima.equals("null")){
            txtpenerima = "";
        }

        if (txtadmin.equals("null")){
            txtadmin = "belum diproses";
        }


        viewHolder.idtransaksi.setText(String.valueOf(data.getId_tansaksi()));
        viewHolder.nokartu.setText(String.valueOf(data.getNo_kartu()));
        viewHolder.nominal.setText(String.valueOf(txtnominal));
        viewHolder.rektujuan.setText(String.valueOf(txtrektujuan));
        viewHolder.bank.setText(String.valueOf(txtbank));
        viewHolder.jenis.setText(String.valueOf(data.getJenis()));
        viewHolder.tanggal.setText(String.valueOf(data.getTanggaltransaksi()));
        viewHolder.penerima.setText(String.valueOf(txtpenerima));
        viewHolder.admin.setText(String.valueOf(txtadmin));

        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}
