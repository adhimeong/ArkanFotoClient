package id.co.outlabs.adhi.arkanfotoclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.outlabs.adhi.arkanfotoclient.InputIdCardActivity;
import id.co.outlabs.adhi.arkanfotoclient.R;
import id.co.outlabs.adhi.arkanfotoclient.WelcomeActivity;

/**
 * Created by adhi on 19/11/18.
 */

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;

    // list of images
    public int[] lst_images = {
            R.drawable.slide1,
            R.drawable.slide2,
            R.drawable.slide3
    };

    // list of descriptions
    public String[] lst_description = {
            "Scan kartu yang telah didapat",
            "Kumpulkan poin setiap bertansaksi",
            "Menangkan hadiahnya disetiap periode"
    };
    // list of background colors
    public int[]  lst_backgroundcolor = {
            Color.rgb(6,133,135),
            Color.rgb(237,85,59),
            Color.rgb(242,177,52)
    };


    public SlideAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lst_images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slidepetunjuk,container,false);
        LinearLayout layoutslide = (LinearLayout) view.findViewById(R.id.slidelinearlayout);
        Button btnnext = (Button) view.findViewById(R.id.slidebtnmulai);
        ImageView imgslide = (ImageView)  view.findViewById(R.id.slideimg);
        TextView description = (TextView) view.findViewById(R.id.txtdescription);
        layoutslide.setBackgroundColor(lst_backgroundcolor[position]);
        imgslide.setImageResource(lst_images[position]);
        description.setText(lst_description[position]);

        Log.d("posisi", String.valueOf(position));
        if (position == 2) {
            btnnext.setVisibility(View.VISIBLE);
        }

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, InputIdCardActivity.class);
                view.getContext().startActivity(i);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
