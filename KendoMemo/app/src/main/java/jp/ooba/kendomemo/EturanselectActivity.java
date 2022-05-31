package jp.ooba.kendomemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class EturanselectActivity extends AppCompatActivity {
    private AdView mAdView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eturanselect);
        setTitle("閲覧");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    public void btndantai_Onclick(View view) {
        //DantaiwriteActivityへのインテント作成
        Intent i = new Intent(this, FolderActivity.class);
        //アクティビティ起動
        startActivity(i);
    }
    public void btnkojin_Onclick(View view) {
        //KojinwriteActivityへ移動
        Intent i = new Intent(this, KojinFoldrActivity.class);
        startActivity(i);
    }
}
