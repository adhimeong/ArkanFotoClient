package id.co.outlabs.adhi.arkanfotoclient;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import id.co.outlabs.adhi.arkanfotoclient.adapter.SlideAdapter;

public class PetunjukActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SlideAdapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petunjuk);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        myadapter = new SlideAdapter(this);
        viewPager.setAdapter(myadapter);

    }
}
