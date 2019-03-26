package id.co.outlabs.adhi.arkanfotoclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class HalamanUtamaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CardView cardsetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_utama);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        cardsetting = (CardView) findViewById(R.id.cardmenusetting);
        cardsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new DashBoardFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.halaman_utama, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new DashBoardFragment()).commit();
        } else if (id == R.id.nav_point) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new NavPointFragment()).commit();
        }else if(id == R.id.nav_tansaksi){
            //Intent i = new Intent(getApplicationContext(), JenisTransaksiActivity.class);
            //startActivity(i);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new NavTransaksiFragment()).commit();
        } else if (id == R.id.nav_haidah) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new NavHadiahFragment()).commit();
        } else if(id == R.id.nav_kartu){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new NavKartuFragment()).commit();
        } else if(id == R.id.nav_profil){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ProfilFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
