package id.co.outlabs.adhi.arkanfotoclient;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private static int splashInterval = 1200;
    ImageView logo;
    Animation fadeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = (ImageView) findViewById(R.id.splashlogo);
        fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        logo.setAnimation(fadeout);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                logo.setImageAlpha(0);

                //if the user is already logged in we will directly start the main activity
                if (SharedPrefManager.getInstance(SplashActivity.this).isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    this.finish();
                }else{
                    Intent i = new Intent(SplashActivity.this, PetunjukActivity.class);
                    startActivity(i);
                    this.finish();
                }

            }

            private void finish() {
                // TODO Auto-generated method stub

            }
        }, splashInterval);
    }
}
