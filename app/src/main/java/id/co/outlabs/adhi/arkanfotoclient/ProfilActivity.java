package id.co.outlabs.adhi.arkanfotoclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.co.outlabs.adhi.arkanfotoclient.getset.UserController;
import id.co.outlabs.adhi.arkanfotoclient.volley.MySingleton;
import id.co.outlabs.adhi.arkanfotoclient.volley.Server;

public class ProfilActivity extends AppCompatActivity {

    NetworkImageView imageprev;
    ImageLoader mImageLoader;
    String url = Server.url_server +"app/images/";
    String IMAGE_URL ;
    String fotodefault = "defaultprofile.jpg";

    private ProgressDialog pd;
    String urldata2 = "app/profilpelanggan.php";
    String url2 = Server.url_server +urldata2;

    TextView viewnama, viewnokartu, viewkontak, viewalamat, viewemail;
    Button logoutbtn, updatebtn;
    String nokartu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        viewnama = (TextView) findViewById(R.id.txtprofilnama);
        viewnokartu = (TextView) findViewById(R.id.txtprofilnokartu);
        viewalamat = (TextView) findViewById(R.id.txtprofilalamat);
        viewemail = (TextView) findViewById(R.id.txtprofilemail);
        viewkontak = (TextView) findViewById(R.id.txtprofilkontak);
        imageprev = (NetworkImageView) findViewById(R.id.profilprevimage);

        logoutbtn = (Button) findViewById(R.id.btnlogout);
        updatebtn = (Button) findViewById(R.id.btnupdateprofil);

        //getting the current user
        UserController user = SharedPrefManager.getInstance(this.getApplicationContext()).getUser();
        pd = new ProgressDialog(this);
        pd.setMessage("loading");

        nokartu = user.getNo_kartu();
        viewnokartu.setText(nokartu);

        //memanggil data user
        load_datapelanggan_from_server2(nokartu);


        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(ProfilActivity.this).logout();
            }
        });

    }

    public void load_datapelanggan_from_server2(final String kartu) {
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url2,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String nama = jsonobject.getString("nama_pelanggan").trim();
                                String foto = jsonobject.getString("foto");
                                String alamat = jsonobject.getString("alamat").trim();
                                String kontak = jsonobject.getString("kontak").trim();
                                String email = jsonobject.getString("email").trim();

                                if (nama.equals("null")){
                                    viewnama.setText("Nama   :    belum dimasukan");
                                }else{
                                    viewnama.setText("Nama   :    " +nama);
                                }

                                if (kontak.equals("null")){
                                    viewkontak.setText("Kontak  :    belum dimasukan");
                                }else{
                                    viewkontak.setText("Kontak  :    " +kontak);
                                }

                                if (email.equals("null")){
                                    viewemail.setText("Email    :    belum dimasukan");
                                }else{
                                    viewemail.setText("Email    :    " +email);
                                }

                                if (alamat.equals("null")){
                                    viewalamat.setText("Alamat  :   belum dimasukan");
                                }else{
                                    viewalamat.setText("Alamat  :   " +alamat);
                                }

                                if (foto.equals("null")){
                                    mImageLoader = MySingleton.getInstance(ProfilActivity.this).getImageLoader();
                                    IMAGE_URL = url + String.valueOf(fotodefault);
                                    imageprev.setImageUrl(IMAGE_URL, mImageLoader);
                                }else{
                                    mImageLoader = MySingleton.getInstance(ProfilActivity.this.getApplicationContext()).getImageLoader();
                                    IMAGE_URL = url + String.valueOf(foto);
                                    imageprev.setImageUrl(IMAGE_URL, mImageLoader);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pd.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            FancyToast.makeText(ProfilActivity.this,"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                            pd.dismiss();
                        }
                    }
                }

        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("no_kartu", kartu);
                return params;
            }
        };

        MySingleton.getInstance(ProfilActivity.this).addToRequestQueue(stringRequest);
    }
}
