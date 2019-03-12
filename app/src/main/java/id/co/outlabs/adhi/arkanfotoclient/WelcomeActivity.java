package id.co.outlabs.adhi.arkanfotoclient;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private static int Interval = 4300;

    LinearLayout l1,l2;
    ImageView logo;
    TextView selamat, nama;
    Animation uptodown,downtoup, zoomin, fadein, blink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);
        logo = (ImageView) findViewById(R.id.imglogo);
        selamat = (TextView) findViewById(R.id.txtselamatdatang);
        nama = (TextView) findViewById(R.id.txtnama);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        zoomin = AnimationUtils.loadAnimation(this,R.anim.zoomin);
        fadein = AnimationUtils.loadAnimation(this,R.anim.fadein);
        blink =AnimationUtils.loadAnimation(this, R.anim.blink);


        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);
        selamat.setAnimation(zoomin);
        logo.setAnimation(fadein);
        nama.setAnimation(blink);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent i = new Intent(WelcomeActivity.this, HalamanUtamaActivity.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(l1,"backgoundtransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this,
                        pairs);

                startActivity(i, options.toBundle());

                this.finish();
            }

            private void finish() {
                // TODO Auto-generated method stub

            }
        }, Interval);

    }
}
