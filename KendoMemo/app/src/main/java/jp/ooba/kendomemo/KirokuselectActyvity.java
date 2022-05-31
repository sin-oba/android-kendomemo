package jp.ooba.kendomemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class KirokuselectActyvity extends AppCompatActivity {
    private AdView mAdView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kiroku_select);
        setTitle("記録");


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

   }
    public void btndantai_Onclick(View view) {
        //DantaiwriteActivityへのインテント作成
        Intent i = new Intent(this, DantaiwriteActivity.class);
        //アクティビティ起動
        startActivity(i);
    }
    public void btnkojin_Onclick(View view) {
        //KojinwriteActivityへ移動
        Intent i = new Intent(this, KojinwriteActivity.class);
        startActivity(i);
    }
}
