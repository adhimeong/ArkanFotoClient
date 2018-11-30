package id.co.outlabs.adhi.arkanfotoclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.vision.barcode.Barcode;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.co.outlabs.adhi.arkanfotoclient.getset.UserController;
import id.co.outlabs.adhi.arkanfotoclient.volley.MySingleton;
import id.co.outlabs.adhi.arkanfotoclient.volley.Server;

public class InputIdCardActivity extends AppCompatActivity {

    //scan camera
    CardView btnscan;
    public String result;
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;

    //login to server
    private ProgressDialog pd;
    String urldata = "app/login.php";
    String url = Server.url_server +urldata;

    String nama_pelanggan, no_kartu ;

    //animasi
    RippleBackground rippleBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_id_card);

        //permisi kamera
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        pd = new ProgressDialog(this);
        pd.setMessage("loading");

        rippleBackground=(RippleBackground)findViewById(R.id.scankartu);
        rippleBackground.startRippleAnimation();

        btnscan = (CardView) findViewById(R.id.cardbtnscancard);
        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ScanCardActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                final Barcode barcode = data.getParcelableExtra("barcode");
                result = String.valueOf(barcode.displayValue);
                login_data_from_server(result);
            }
        }
    }

    public void login_data_from_server(final String a ) {
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

                                nama_pelanggan = jsonobject.getString("nama_pelanggan").trim();
                                no_kartu = jsonobject.getString("no_kartu").trim();

                            }

                        } catch (JSONException e) {
                            pd.dismiss();
                            e.printStackTrace();
                        }

                        if (no_kartu == null){
                            Log.d("DATA SERVER", "kosong");
                        }else{
                            UserController user = new UserController( nama_pelanggan, no_kartu);
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                            finish();
                            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                        }
                        pd.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            startActivity(new Intent(InputIdCardActivity.this, GagalKoneksiActivity.class));
                            FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
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
                params.put("no_kartu", a);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
