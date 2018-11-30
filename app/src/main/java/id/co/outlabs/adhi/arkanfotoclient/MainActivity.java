package id.co.outlabs.adhi.arkanfotoclient;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.outlabs.adhi.arkanfotoclient.adapter.BannerAdapter;
import id.co.outlabs.adhi.arkanfotoclient.adapter.DataHadiahAdapter;
import id.co.outlabs.adhi.arkanfotoclient.adapter.DataPemenangAdapter;
import id.co.outlabs.adhi.arkanfotoclient.getset.DataBannerController;
import id.co.outlabs.adhi.arkanfotoclient.getset.DataHadiahController;
import id.co.outlabs.adhi.arkanfotoclient.getset.DataPemenangController;
import id.co.outlabs.adhi.arkanfotoclient.getset.UserController;
import id.co.outlabs.adhi.arkanfotoclient.volley.MySingleton;
import id.co.outlabs.adhi.arkanfotoclient.volley.Server;

public class MainActivity extends AppCompatActivity {

    //point
    String urldata4 = "app/perolehanpoint.php";
    String url4 = Server.url_server +urldata4;

    //banner
    private ProgressDialog pd;
    String urldata3 = "app/tampilbanner.php";
    String url3 = Server.url_server +urldata3;
    //hadiah
    String urldata = "app/service_hadiah.php";
    String url = Server.url_server +urldata;

    //pemenang
    String urldata2 = "app/service_berita.php";
    String url2 = Server.url_server +urldata2;

    //banner
    List<DataBannerController> databannerController = new ArrayList<DataBannerController>();
    BannerAdapter bannerAdapter;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    //hadiah
    public List<DataHadiahController> listdatahadiah = new ArrayList<DataHadiahController>();
    public RecyclerView recyclerView;
    public DataHadiahAdapter mAdapter;

    //pemenang
    public List<DataPemenangController> listdatapemenang = new ArrayList<DataPemenangController>();
    public RecyclerView recyclerView2;
    public DataPemenangAdapter mAdapter2;


    //animation
    ScrollView sc;
    Animation bounce, sliddown, fadein;

    //point
    int pointperolehanint;
    String no_kartu;
    Button pointview;

    //card
    CardView cardsetting, cardsesama, cardbanklain, cardtunai, cardpulsa, cardbpjs, cardpln, cardcicilan, cardlain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserController user = SharedPrefManager.getInstance(MainActivity.this).getUser();
        no_kartu = user.getNo_kartu();

        //cardview
        cardsetting = (CardView) findViewById(R.id.cardmenusetting);
        cardsesama= (CardView) findViewById(R.id.cardmenusesama);
        cardbanklain = (CardView) findViewById(R.id.cardmenuantar);
        cardtunai = (CardView) findViewById(R.id.cardmenutarik);
        cardpulsa = (CardView) findViewById(R.id.cardmenupulsa);
        cardbpjs = (CardView) findViewById(R.id.cardmenubpjs);
        cardpln = (CardView) findViewById(R.id.cardmenupln);
        cardcicilan = (CardView) findViewById(R.id.cardmenucicilan);
        cardlain = (CardView) findViewById(R.id.cardmenulain);
        pointview = (Button) findViewById(R.id.point2);


        sc = (ScrollView) findViewById(R.id.l3);
        bounce = AnimationUtils.loadAnimation(this,R.anim.bounce);
        sliddown = AnimationUtils.loadAnimation(this, R.anim.slidedown);
        fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
        sc.setAnimation(bounce);

        //progres dialog
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("loading");

        //hadiah
        recyclerView = (RecyclerView) findViewById(R.id.recyclehadiah);
        mAdapter = new DataHadiahAdapter(this, listdatahadiah);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setAnimation(fadein);

        //pemenang
        recyclerView2 = (RecyclerView) findViewById(R.id.recyclepemenang);
        mAdapter2 = new DataPemenangAdapter(this,listdatapemenang);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this.getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(mLayoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(mAdapter2);

        //banner
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundblue));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundyellow));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //cardclik
        cardsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"setting",Toast.LENGTH_SHORT).show();//
            }
        });
        cardsesama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, TransaksiActivity.class);
                i.putExtra("aksi", "sesama");
                startActivity(i);
            }
        });
        cardbanklain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, TransaksiActivity.class);
                i.putExtra("aksi", "banklain");
                startActivity(i);
            }
        });
        cardtunai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, TransaksiActivity.class);
                i.putExtra("aksi", "tunai");
                startActivity(i);
            }
        });
        cardpulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, TransaksiActivity.class);
                i.putExtra("aksi", "pulsa");
                startActivity(i);
            }
        });
        cardbpjs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, TransaksiActivity.class);
                i.putExtra("aksi", "bpjs");
                startActivity(i);
            }
        });
        cardpln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, TransaksiActivity.class);
                i.putExtra("aksi", "pln");
                startActivity(i);
            }
        });
        cardcicilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, TransaksiActivity.class);
                i.putExtra("aksi", "cicilan");
                startActivity(i);
            }
        });
        cardlain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, TransaksiActivity.class);
                i.putExtra("aksi", "lainnya");
                startActivity(i);
            }
        });

        load_point_from_server(no_kartu);
        load_banner_from_server();
        load_hadiah_form_server();
        load_pemenang_form_server();

    }

    public void load_point_from_server(final String kartu){
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url4,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String nama_pelanggan = jsonobject.getString("nama_pelanggan").trim();
                                String tanggal_pasif = jsonobject.getString("tanggal_pasif").trim();
                                String skor_point = jsonobject.getString("jumlah_point").trim();

                                pointview.setText(skor_point);
                                Log.d("point", skor_point);
                                pointperolehanint = Integer.parseInt(skor_point);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pd.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                            pd.hide();
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

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void load_pemenang_form_server(){

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

                                String id_berita = jsonobject.getString("id_berita").trim();
                                String judul_berita = jsonobject.getString("judul_berita").trim();
                                String foto_berita = jsonobject.getString("foto_berita").trim();
                                String tanggal_berita = jsonobject.getString("tanggal_berita").trim();
                                String isi_berita = jsonobject.getString("isi_berita").trim();


                                DataPemenangController d2 = new DataPemenangController();
                                d2.setFoto(foto_berita.toString());
                                d2.setJudul(judul_berita.toString());
                                d2.setTanggal(tanggal_berita.toString());
                                d2.setIsi(isi_berita);

                                listdatapemenang.add(d2);

                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                        mAdapter2.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            startActivity(new Intent(MainActivity.this, GagalKoneksiActivity.class));
                            FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_SHORT, FancyToast.ERROR,true).show();
                        }
                    }
                }

        );

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    public void load_hadiah_form_server(){

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

                                String id_hadiah = jsonobject.getString("id_hadiah").trim();
                                String nama_hadiah = jsonobject.getString("nama_hadiah").trim();
                                String foto_hadiah = jsonobject.getString("foto_hadiah").trim();
                                String jumlah_point = jsonobject.getString("jumlah_point").trim();
                                String jumlah_items = jsonobject.getString("jumlah_items").trim();

                                DataHadiahController d1 = new DataHadiahController();
                                d1.setId_hadiah(id_hadiah.toString());
                                d1.setNama_hadiah(nama_hadiah.toString());
                                d1.setFoto_hadiah(foto_hadiah.toString());
                                d1.setJumlah_point(jumlah_point.toString());
                                d1.setJumlah_items(jumlah_items.toString());

                                listdatahadiah.add(d1);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mAdapter.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            startActivity(new Intent(MainActivity.this, GagalKoneksiActivity.class));
                            FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_SHORT, FancyToast.ERROR,true).show();
                        }
                    }
                }

        );

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void load_banner_from_server(){

        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url3,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {

                            bannerAdapter = new BannerAdapter(databannerController,MainActivity.this);

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                //String id_banner = jsonobject.getString("id,_banner").trim();
                                //String nama_banner = jsonobject.getString("nama_banner").trim();
                                String foto_banner = jsonobject.getString("foto_banner").trim();
                                String fotobanner = foto_banner.toString();

                                DataBannerController d4 = new DataBannerController();
                                d4.setFoto_banner(fotobanner);
                                databannerController.add(d4);

                                Log.d("urlbanner",fotobanner);
                            }
                            pd.dismiss();

                        } catch (JSONException e) {
                            pd.dismiss();

                            e.printStackTrace();
                        }


                        viewPager.setAdapter(bannerAdapter);

                        dotscount = bannerAdapter.getCount();
                        dots = new ImageView[dotscount];

                        for(int i = 0; i < dotscount; i++){

                            dots[i] = new ImageView(MainActivity.this);
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundblue));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            params.setMargins(8, 0, 8, 0);

                            sliderDotspanel.addView(dots[i], params);
                        }

                        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundyellow));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            pd.dismiss();
                            startActivity(new Intent(MainActivity.this, GagalKoneksiActivity.class));
                            FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_SHORT, FancyToast.ERROR,true).show();
                        }
                    }
                }

        );

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}
