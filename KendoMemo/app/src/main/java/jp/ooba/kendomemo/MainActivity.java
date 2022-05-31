package jp.ooba.kendomemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import jp.ooba.kendomemo.Bunseki;
import jp.ooba.kendomemo.EturanselectActivity;
import jp.ooba.kendomemo.KirokuselectActyvity;
import jp.ooba.kendomemo.R;


public class MainActivity extends AppCompatActivity {
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher02);





    }



    public void btnwrite_Onclick(View view) {
        //kiroku_selectへ移動
        Intent i = new Intent(this, KirokuselectActyvity.class);
        startActivity(i);
    }
    public void btnview_Onclick(View view) {
        //FolderActivityへ移動
        Intent i = new Intent(this, EturanselectActivity.class);
        startActivity(i);
    }
    public void btnmail_onClick(View view) {
        Intent i = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:kaihatu0203@gmail.com"));
        i.putExtra(Intent.EXTRA_SUBJECT,"剣道記録:ご意見、ご要望等");
        startActivity(i);
    }
    public void btnbunseki_onClick(View view){
        Intent i = new Intent(this, Bunseki.class);
        startActivity(i);
    }





}