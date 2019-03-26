package id.co.outlabs.adhi.arkanfotoclient;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GagalKoneksiActivity extends AppCompatActivity {

    Button btncoba, btnatur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gagal_koneksi);

        btncoba = (Button) findViewById(R.id.btngagalcobalagi);
        btnatur = (Button) findViewById(R.id.btngagalpengaturan);

        btncoba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GagalKoneksiActivity.this, HalamanUtamaActivity.class));
            }
        });
        btnatur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
            }
        });
    }
}
