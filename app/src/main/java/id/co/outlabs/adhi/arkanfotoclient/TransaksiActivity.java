package id.co.outlabs.adhi.arkanfotoclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;

import faranjit.currency.edittext.CurrencyEditText;
import id.co.outlabs.adhi.arkanfotoclient.getset.UserController;

public class TransaksiActivity extends AppCompatActivity {

    Button btnbatal, btnproses;
    String txtjenistransaksi;
    EditText editrektujuan, editpenerima, editbanktujuan;
    CurrencyEditText editnominal;
    String txtrektujuan, txtnominal, txtpenerima, txtbanktujuan, txtnokartu, txtkodebank;
    double dd;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        //ambil data dari list jenis transaksi
        txtjenistransaksi = getIntent().getStringExtra("aksi");
        txtkodebank = getIntent().getStringExtra("kodebank");

        UserController user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        txtnokartu = user.getNo_kartu();

        editrektujuan = (EditText) findViewById(R.id.edittransaksitujuan);
        editnominal = (CurrencyEditText) findViewById(R.id.edittransaksinominal);
        editpenerima = (EditText) findViewById(R.id.edittransaksipenerima);
        editbanktujuan = (EditText) findViewById(R.id.edittransaksibank);

        switch (txtjenistransaksi){
            case "sesama":
                editbanktujuan.setVisibility(View.GONE);
                break;
            case "banklain":
                break;

            case "tunai":
                editrektujuan.setVisibility(View.GONE);
                editpenerima.setVisibility(View.GONE);
                editbanktujuan.setVisibility(View.GONE);
                txtpenerima = "";
                txtbanktujuan = "BANK BRI";
                txtkodebank = "002";
                break;
            case "pulsa" :
                editrektujuan.setHint("nomor telpon");
                editpenerima.setVisibility(View.GONE);
                editbanktujuan.setVisibility(View.GONE);
                break;

            case "bpjs" :
                editrektujuan.setHint("nomor bpjs");
                editnominal.setVisibility(View.GONE);
                editpenerima.setHint("atas nama");
                editbanktujuan.setVisibility(View.GONE);
                break;
            case "pln" :
                editrektujuan.setHint("nomor pelanggan pln");
                editnominal.setVisibility(View.GONE);
                editpenerima.setHint("atas nama");
                editbanktujuan.setVisibility(View.GONE);
                break;
            case "cicilan" :
                editrektujuan.setHint("nomor pelanggan");
                editnominal.setVisibility(View.GONE);
                editpenerima.setHint("atas nama");
                editbanktujuan.setHint("dealer/lising");
                break;
        }

        btnbatal = (Button) findViewById(R.id.btntransasibatal);
        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnproses = (Button) findViewById(R.id.btntransasiproses);
        btnproses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (txtjenistransaksi){
                    case "sesama":
                        txtrektujuan = editrektujuan.getText().toString();
                        try {
                            dd = editnominal.getCurrencyDouble();
                            id = (int)dd;
                            txtnominal = String.valueOf(id);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        txtpenerima = editpenerima.getText().toString();
                        txtbanktujuan = "BANK BRI";
                        txtkodebank = "002";

                        break;
                    case "banklain":

                        txtrektujuan = editrektujuan.getText().toString();
                        try {
                            dd = editnominal.getCurrencyDouble();
                            id = (int)dd;
                            txtnominal = String.valueOf(id);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        txtpenerima = editpenerima.getText().toString();
                        txtbanktujuan = editbanktujuan.getText().toString();
                        txtkodebank = "014"; //sementara
                        break;

                    case "tunai":
                        txtrektujuan = null;
                        txtpenerima = null;
                        txtbanktujuan = "BANK BRI";
                        txtkodebank = "002";

                        try {
                            dd = editnominal.getCurrencyDouble();
                            id = (int)dd;
                            txtnominal = String.valueOf(id);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "pulsa" :

                        txtrektujuan = editrektujuan.getText().toString();

                        try {
                            dd = editnominal.getCurrencyDouble();
                            id = (int)dd;
                            txtnominal = String.valueOf(id);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        txtpenerima = null;
                        txtbanktujuan = null;
                        txtkodebank = null;
                        break;

                    case "bpjs" :

                        txtrektujuan = editrektujuan.getText().toString();
                        txtpenerima = editpenerima.getText().toString();
                        txtbanktujuan = null;
                        txtkodebank = null;
                        txtnominal = null;

                        break;
                    case "pln" :
                        txtrektujuan = editrektujuan.getText().toString();
                        txtpenerima = editpenerima.getText().toString();
                        txtbanktujuan = null;
                        txtkodebank = null;
                        txtnominal = null;
                        break;
                    case "cicilan" :
                        txtrektujuan = editrektujuan.getText().toString();
                        txtpenerima = editpenerima.getText().toString();
                        txtbanktujuan = editbanktujuan.getText().toString();
                        txtkodebank = null;
                        txtnominal = null;
                        break;
                }

                Intent i = new Intent( TransaksiActivity.this, PrintActivity.class);
                i.putExtra("rektujuan", txtrektujuan);
                i.putExtra("nominal", txtnominal);
                i.putExtra("penerima", txtpenerima);
                i.putExtra("bank", txtbanktujuan);
                i.putExtra("kodebank", txtkodebank);
                i.putExtra("jenistransaksi", txtjenistransaksi);
                startActivity(i);
                finish();
            }
        });
    }
}
