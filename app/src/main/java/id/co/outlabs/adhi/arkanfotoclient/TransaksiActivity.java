package id.co.outlabs.adhi.arkanfotoclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import faranjit.currency.edittext.CurrencyEditText;
import id.co.outlabs.adhi.arkanfotoclient.adapter.DataBankAdapter;
import id.co.outlabs.adhi.arkanfotoclient.getset.DataBankController;
import id.co.outlabs.adhi.arkanfotoclient.getset.UserController;
import id.co.outlabs.adhi.arkanfotoclient.volley.MySingleton;
import id.co.outlabs.adhi.arkanfotoclient.volley.Server;

public class TransaksiActivity extends AppCompatActivity {

    Button btnbatal, btnproses;
    String txtjenistransaksi;
    EditText editrektujuan, editpenerima;
    AutoCompleteTextView editbanktujuan;
    CurrencyEditText editnominal;
    String txtrektujuan, txtnominal, txtpenerima, txtbanktujuan, txtnokartu, txtkodebank;
    double dd;
    int id;

    //volley untuk bank
    private ProgressDialog pd;
    String urldata = "app/bank_list.php";
    String url = Server.url_server +urldata;
    //list costume adapter
    List<DataBankController> dataController = new ArrayList<DataBankController>();
    DataBankAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        //ambil data dari list jenis transaksi
        txtjenistransaksi = getIntent().getStringExtra("aksi");
        txtkodebank = getIntent().getStringExtra("kodebank");

        UserController user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        txtnokartu = user.getNo_kartu();

        //progres dialog
        pd = new ProgressDialog(this);
        pd.setMessage("loading");

        editrektujuan = (EditText) findViewById(R.id.edittransaksitujuan);
        editnominal = (CurrencyEditText) findViewById(R.id.edittransaksinominal);
        editpenerima = (EditText) findViewById(R.id.edittransaksipenerima);
        editbanktujuan = (AutoCompleteTextView) findViewById(R.id.edittransaksibank);

        switch (txtjenistransaksi){
            case "sesama":
                editbanktujuan.setVisibility(View.GONE);
                break;
            case "banklain":

                String aksi = "semua";
                String databank = "kosong";
                // Fetching data from server

                load_data_from_server(databank, aksi);

                editbanktujuan.setThreshold(2);
                editbanktujuan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //Toast.makeText(MainActivity.this, (CharSequence) parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        DataBankController data = dataController.get(i);
                        Toast.makeText(TransaksiActivity.this, data.getNamabank(), Toast.LENGTH_SHORT).show();
                    }
                });
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
                        String hasilautocomplete = editbanktujuan.getText().toString();

                        String[] separated = hasilautocomplete.split("-");
                        txtkodebank = separated[0];
                        txtbanktujuan = separated[1];

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

    public void load_data_from_server(final String data, final String prosesaksi) {

        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String id = jsonobject.getString("id_bank").trim();
                                String nama = jsonobject.getString("nama_bank").trim();
                                String kode = jsonobject.getString("kode_bank").trim();


                                dataController.add(new DataBankController(id, nama, kode));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        adapter = new DataBankAdapter(TransaksiActivity.this,  dataController );
                        adapter.notifyDataSetChanged();
                        editbanktujuan.setAdapter(adapter);
                        pd.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            pd.dismiss();
                            finish();
                            FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();

                        }
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("data", data);
                params.put("aksi", prosesaksi);
                return params;
            }

        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


}
