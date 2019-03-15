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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import id.co.outlabs.adhi.arkanfotoclient.getset.UserController;
import id.co.outlabs.adhi.arkanfotoclient.print.UnicodeFormatter;

public class PrintActivity extends Activity implements Runnable{

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

    String nokartu, rektujuan, nominal, bank, penerima, kodebank, jenistransaksi, tarif;
    String totalbayar, tanggal, waktu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        UserController user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        nokartu = user.getNo_kartu();

        //ambil data dari fragment
        rektujuan = getIntent().getStringExtra("rektujuan");
        nominal = getIntent().getStringExtra("nominal");
        penerima = getIntent().getStringExtra("penerima");
        bank = getIntent().getStringExtra("bank");
        kodebank = getIntent().getStringExtra("kodebank");
        jenistransaksi = getIntent().getStringExtra("jenistransaksi");

        //animasi
        rippleBackground=(RippleBackground)findViewById(R.id.scanprinter);
        rippleBackground.startRippleAnimation();

        if (rektujuan != null){
            Log.d("data1", rektujuan);
        }

        if (nominal !=null){
            Log.d("data2", nominal);
            tarif = "0";
            int a = Integer.parseInt(nominal);
            int b = Integer.parseInt(tarif);
            int total = a + b;

            totalbayar = String.valueOf(total);
        }else{
            totalbayar = "SESUAI TAGIHAN";
            tarif = "SESUAI TAGIHAN";
        }

        if (penerima != null){
            Log.d("data3", penerima);
        }

        if (bank != null){
            Log.d("data4", bank);
        }

        if (kodebank != null){
            Log.d("data5", kodebank);
        }

        if (jenistransaksi != null){
            Log.d("data6", jenistransaksi);
        }

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
            dialogprint();

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

    public void  dialogprint(){

        myDialog = new Dialog(PrintActivity.this);
        myDialog.setContentView(R.layout.dialoghadiah);

        TextView dialogketerangan = (TextView) myDialog.findViewById(R.id.dialoghadiahketerangan);
        ImageView dialogimage = (ImageView) myDialog.findViewById(R.id.dialoghadiahimage);
        Button dialogbtnambil = (Button) myDialog.findViewById(R.id.dialoghadiahambil);
        Button dialogbtnclose = (Button) myDialog.findViewById(R.id.dialoghadiahclose);

        dialogketerangan.setText("cetak antrian");
        dialogimage.setImageResource(R.drawable.antrianprint);
        dialogbtnambil.setText("CETAK");
        dialogbtnclose.setText("SELESAI");

        dialogbtnambil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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

                            BILL = BILL + "BUKTI TRANSAKSI";
                            BILL = BILL + "\n";
                            BILL = BILL + "TANGGAL : " + tanggal + "\n";
                            BILL = BILL + "PUKUL : " + waktu + "\n";
                            BILL = BILL + "\n";
                            BILL = BILL
                                    + "--------------------------------";
                            BILL = BILL + "JENIS : " + jenistransaksi + "\n";
                            BILL = BILL
                                    + "--------------------------------";

                            switch (jenistransaksi){
                                case "sesama":

                                    BILL = BILL + "\n" + "BANK  : " + bank ;
                                    BILL = BILL + "\n" + "NO REKENING : " + rektujuan;
                                    BILL = BILL + "\n" + "NAMA PENERIMA : " + penerima;
                                    BILL = BILL + "\n" + "NOMINAL : " + nominal;

                                    break;
                                case "banklain":

                                    BILL = BILL + "\n" + "BANK  : " + bank ;
                                    BILL = BILL + "\n" + "NO REKENING : " + rektujuan;
                                    BILL = BILL + "\n" + "NAMA PENERIMA : " + penerima;
                                    BILL = BILL + "\n" + "NOMINAL : " + nominal;

                                    break;

                                case "tunai":

                                    BILL = BILL + "\n" + "NOMINAL : " + nominal;

                                    break;
                                case "pulsa" :

                                    BILL = BILL + "\n" + "NOMOR : " + rektujuan;
                                    BILL = BILL + "\n" + "NOMINAL : " + nominal;

                                    break;

                                case "bpjs" :

                                    BILL = BILL + "\n" + "NO BPJS : " + rektujuan;
                                    BILL = BILL + "\n" + "NAMA : " + penerima;

                                    break;
                                case "pln" :

                                    BILL = BILL + "\n" + "NO PLN : " + rektujuan;
                                    BILL = BILL + "\n" + "NAMA : " + penerima;

                                    break;
                                case "cicilan" :

                                    BILL = BILL + "\n" + "NOPELANGGAN : " + rektujuan;
                                    BILL = BILL + "\n" + "NAMA : " + penerima;
                                    BILL = BILL + "\n" + "LEASING : " + bank;

                                    break;
                            }


                            BILL = BILL
                                    + "\n--------------------------------";
                            BILL = BILL + "\n\n";

                            BILL = BILL + "BIAYA ADMINISTRASI :" + "\n";
                            BILL = BILL + tarif + "\n";

                            BILL = BILL + "TOTAL PEMBAYARAN :" + "\n";
                            BILL = BILL + totalbayar + "\n";

                            BILL = BILL
                                    + "================================\n";
                            BILL = BILL + "DI ISI OLEH PETUGAS" + "\n";
                            BILL = BILL + "STATUS TRANSAKSI :" + "\n";
                            BILL = BILL + "[ ] selesai    [ ] pending" + "\n";
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

            }
        });


        dialogbtnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mBluetoothAdapter != null)
                    mBluetoothAdapter.disable();

                myDialog.dismiss();
                startActivity(new Intent(PrintActivity.this, HalamanUtamaActivity.class));
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }
}

