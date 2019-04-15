package id.co.outlabs.adhi.arkanfotoclient;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import id.co.outlabs.adhi.arkanfotoclient.getset.UserController;
import id.co.outlabs.adhi.arkanfotoclient.print.UnicodeFormatter;
import id.co.outlabs.adhi.arkanfotoclient.volley.MySingleton;
import id.co.outlabs.adhi.arkanfotoclient.volley.Server;

public class PrintActivity extends Activity implements Runnable{

    //point
    String urldata = "app/prosesantriantransaksi.php";
    String url = Server.url_server +urldata;
    private ProgressDialog pd;

    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mPrint, mDisc;

    CardView btnscan;
    public Dialog myDialog;
    //animasi
    RippleBackground rippleBackground;

    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;

    String nokartu, rektujuan, nominal, bank, penerima, kodebank, jenistransaksi, tarif,  antrian;
    String totalbayar, tanggal, waktu;
    String nominalprint, totalbayarprint, tarifprint;
    Toast toastgagal;

    //format rupiah
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        toastgagal = Toast.makeText(PrintActivity.this, "no Device", Toast.LENGTH_SHORT);

        UserController user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        nokartu = user.getNo_kartu();

        //progres dialog
        pd = new ProgressDialog(this);
        pd.setMessage("loading");


        //ambil data dari transaksi activity
        rektujuan = getIntent().getStringExtra("rektujuan");
        nominal = getIntent().getStringExtra("nominal");
        penerima = getIntent().getStringExtra("penerima");
        bank = getIntent().getStringExtra("bank");
        kodebank = getIntent().getStringExtra("kodebank");
        jenistransaksi = getIntent().getStringExtra("jenistransaksi");

        //nokartu, rektujuan, nominal, penerima, bank, kodebank, jenistransaksi


        //animasi
        rippleBackground=(RippleBackground)findViewById(R.id.scanprinter);
        rippleBackground.startRippleAnimation();


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        waktu = mdformat.format(calendar.getTime());

        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat mdformat02 = new SimpleDateFormat("dd-MM-yyyy");
        tanggal = mdformat02.format(calendar1.getTime());

        btnscan = (CardView) findViewById(R.id.cardbtnscanprinter);
        btnscan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {

                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                if (mBluetoothAdapter == null) {
                    Toast.makeText(PrintActivity.this, "Message1", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent,
                                REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(PrintActivity.this,
                                DeviceListActivity.class);
                        startActivityForResult(connectIntent,
                                REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(PrintActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(PrintActivity.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            mBluetoothConnectProgressDialog.dismiss();
            toastgagal.show();
            finish();
            return;
        }
    }
    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(PrintActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
            dialogprint(nokartu, rektujuan, nominal, penerima, bank, kodebank, jenistransaksi);

        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

    //nokartu, rektujuan, nominal, penerima, bank, kodebank, jenistransaksi
    public void  dialogprint(final String nokartu, final String rektujuan, final String nominal, final String penerima, final String bank, final String kodebank, final String jenistransaksi){

        myDialog = new Dialog(PrintActivity.this);
        myDialog.setContentView(R.layout.dialogprint);

        TextView dialogketerangan = (TextView) myDialog.findViewById(R.id.dialogprintketerangan);
        ImageView dialogimage = (ImageView) myDialog.findViewById(R.id.dialogprintimage);
        final Button dialogbtnambil = (Button) myDialog.findViewById(R.id.dialogprintcetak);
        Button dialogbtnclose = (Button) myDialog.findViewById(R.id.dialogprintclose);

        dialogketerangan.setText("cetak antrian");
        dialogimage.setImageResource(R.drawable.antrianprint);
        dialogbtnambil.setText("CETAK");
        dialogbtnclose.setText("SELESAI");

        dialogbtnambil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //daftar transaksi ke server
                pd.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.d("Respone transaksi  :",response);

                                try {

                                    JSONObject jObj = new JSONObject(response);
                                    tarif = jObj.getString("tarif");
                                    antrian = jObj.getString("antrian");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (nominal !=null){

                                    int a = Integer.parseInt(nominal);
                                    int b = Integer.parseInt(tarif);
                                    int total = a + b;

                                    totalbayar = String.valueOf(total);
                                    //untuk ngprint

                                    nominalprint = formatRupiah.format(Double.valueOf(nominal));
                                    tarifprint = formatRupiah.format(Double.valueOf(tarif));
                                    totalbayarprint = formatRupiah.format(Double.valueOf(totalbayar));

                                }else{
                                    totalbayarprint = "SESUAI TAGIHAN";
                                    tarifprint = "SESUAI TAGIHAN";
                                }

                                pd.dismiss();

                                Thread t = new Thread() {
                                    public void run() {
                                        try {
                                            OutputStream os = mBluetoothSocket
                                                    .getOutputStream();
                                            String BILL = "";

                                            BILL = "           BANK BRI    \n"
                                                    + "             BRILink     \n " +
                                                    "          ARKAN FOTO    \n" +
                                                    " Jln.Cijerah 2 Blok 19 No. 147 \n" +
                                                    "    No.Agen :000001370101851 \n";
                                            BILL = BILL
                                                    + "================================\n";

                                            BILL = BILL + "BUKTI TRANSAKSI         NO:" + antrian;
                                            BILL = BILL + "\n";
                                            BILL = BILL + "TANGGAL : " + tanggal + "\n";
                                            BILL = BILL + "PUKUL   : " + waktu + "\n";
                                            BILL = BILL
                                                    + "--------------------------------";
                                            BILL = BILL + "JENIS   : " + jenistransaksi + "\n";
                                            BILL = BILL
                                                    + "--------------------------------";

                                            switch (jenistransaksi){
                                                case "sesama":

                                                    BILL = BILL + "\n" + "BANK        : " + bank ;
                                                    BILL = BILL + "\n" + "NO REKENING : " + kodebank +"-"+rektujuan;
                                                    BILL = BILL + "\n" + "PENERIMA    : " + penerima;
                                                    BILL = BILL + "\n" + "NOMINAL     : " + nominalprint;

                                                    break;
                                                case "banklain":

                                                    BILL = BILL + "\n" + "BANK        : " + bank ;
                                                    BILL = BILL + "\n" + "NO REKENING : " + kodebank +"-"+rektujuan;
                                                    BILL = BILL + "\n" + "PENERIMA    : " + penerima;
                                                    BILL = BILL + "\n" + "NOMINAL     : " + nominalprint;

                                                    break;

                                                case "tunai":

                                                    BILL = BILL + "\n" + "NOMINAL : " + nominalprint;

                                                    break;
                                                case "pulsa" :

                                                    BILL = BILL + "\n" + "NOMOR   : " + rektujuan;
                                                    BILL = BILL + "\n" + "NOMINAL : " + nominalprint;

                                                    break;

                                                case "bpjs" :

                                                    BILL = BILL + "\n" + "NO BPJS : " + rektujuan;
                                                    BILL = BILL + "\n" + "NAMA    : " + penerima;

                                                    break;
                                                case "pln" :

                                                    BILL = BILL + "\n" + "NO PLN : " + rektujuan;
                                                    BILL = BILL + "\n" + "NAMA   : " + penerima;

                                                    break;
                                                case "cicilan" :

                                                    BILL = BILL + "\n" + "NOPELANGGAN : " + rektujuan;
                                                    BILL = BILL + "\n" + "NAMA        : " + penerima;
                                                    BILL = BILL + "\n" + "LEASING     : " + bank;

                                                    break;
                                            }

                                            BILL = BILL
                                                    + "\n--------------------------------";

                                            BILL = BILL + "ADMINISTRASI :" + tarifprint + "\n";
                                            BILL = BILL + "TOTAL        :" + totalbayarprint + "\n";
                                            BILL = BILL
                                                    + "================================\n";
                                            BILL = BILL + "DI ISI OLEH PETUGAS" + "\n";
                                            BILL = BILL + "STATUS TRANSAKSI :" + "\n";
                                            BILL = BILL + "[ ] pending   [ ] selesai " + "\n";
                                            BILL = BILL
                                                    + "================================\n";
                                            BILL = BILL + "\n\n ";
                                            os.write(BILL.getBytes());
                                            //This is printer specific code you can comment ==== > Start

                                            // Setting height
                                            int gs = 29;
                                            os.write(intToByteArray(gs));
                                            int h = 104;
                                            os.write(intToByteArray(h));
                                            int n = 162;
                                            os.write(intToByteArray(n));

                                            // Setting Width
                                            int gs_width = 29;
                                            os.write(intToByteArray(gs_width));
                                            int w = 119;
                                            os.write(intToByteArray(w));
                                            int n_width = 2;
                                            os.write(intToByteArray(n_width));


                                        } catch (Exception e) {
                                            Log.e("PrintActivity", "Exe ", e);
                                        }
                                    }
                                };

                                t.start();
                                dialogbtnambil.setVisibility(View.INVISIBLE);

                                try {
                                    t.join();
                                    //memutuskan soket dan mematikan bluetooth
                                    if (mBluetoothAdapter != null) {
                                        closeSocket(mBluetoothSocket);
                                        mBluetoothAdapter.disable();
                                    }
                                    //myDialog.dismiss();
                                    //startActivity(new Intent(PrintActivity.this, HalamanUtamaActivity.class));
                                    //finish();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if(error != null){

                                    FancyToast.makeText(getApplicationContext(),"Coba Lagi, Internet Tidak Stabil",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
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
                        //nokartu, rektujuan, nominal, penerima, bank, kodebank, jenistransaksi
                        switch (jenistransaksi){
                            case "tunai":
                                params.put("nokartu", nokartu);
                                params.put("nominal", nominal);
                                params.put("kodebank", kodebank);
                                params.put("jenistransaksi", jenistransaksi);
                                //rektujuan, penerima, bank kosong
                                break;

                            case "pulsa":
                                params.put("nokartu", nokartu);
                                params.put("rektujuan", rektujuan);
                                params.put("nominal", nominal);
                                params.put("jenistransaksi", jenistransaksi);
                                //penerima, bank, kodebank kosong
                                break;

                            case "bpjs" :
                                params.put("nokartu", nokartu);
                                params.put("rektujuan", rektujuan);
                                params.put("penerima", penerima);
                                params.put("jenistransaksi", jenistransaksi);
                                //nominal, bank, kodebank kosong
                                break;

                            case "pln" :
                                params.put("nokartu", nokartu);
                                params.put("rektujuan", rektujuan);
                                params.put("penerima", penerima);
                                params.put("jenistransaksi", jenistransaksi);
                                //nominal, bank, kodebank kosong
                                break;

                            case "cicilan":
                                params.put("nokartu", nokartu);
                                params.put("rektujuan", rektujuan);
                                params.put("penerima", penerima);
                                params.put("bank", bank);
                                params.put("jenistransaksi", jenistransaksi);
                                //nominal, kodebank
                                break;

                            default :
                                params.put("nokartu", nokartu);
                                params.put("rektujuan", rektujuan);
                                params.put("nominal", nominal);
                                params.put("penerima", penerima);
                                params.put("bank", bank);
                                params.put("kodebank", kodebank);
                                params.put("jenistransaksi", jenistransaksi);
                                break;
                        }

                        return params;
                    }
                };

                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            }
        });


        dialogbtnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mBluetoothAdapter != null) {
                    closeSocket(mBluetoothSocket);
                    mBluetoothAdapter.disable();
                }

                myDialog.dismiss();
                startActivity(new Intent(PrintActivity.this, HalamanUtamaActivity.class));
                finish();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

}

