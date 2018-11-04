package id.co.outlabs.adhi.arkanfotoclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {

    ScrollView sc;
    Animation bounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sc = (ScrollView) findViewById(R.id.l3);
        bounce = AnimationUtils.loadAnimation(this,R.anim.bounce);

        sc.setAnimation(bounce);

    }
}
