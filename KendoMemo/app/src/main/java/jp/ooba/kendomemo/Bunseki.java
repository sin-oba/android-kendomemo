package jp.ooba.kendomemo;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Bunseki extends AppCompatActivity {
    private DatabaseHelper helper = null;
    public void onCreate(Bundle savedInsatance) {
        super.onCreate(savedInsatance);
        setContentView(R.layout.bunseki_view);
        Button btn = (Button)findViewById(R.id.btnbunskei);
        helper = new DatabaseHelper(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pl = (EditText) findViewById(R.id.sensyu);
                EditText tm = (EditText) findViewById(R.id.team);
                TextView siai = (TextView) findViewById(R.id.siai);
                TextView syouri = (TextView) findViewById(R.id.syouri);
                TextView haiboku = (TextView) findViewById(R.id.haiboku);
                TextView hikiwake = (TextView) findViewById(R.id.hikiwake);
                TextView sment = (TextView) findViewById(R.id.smen);
                TextView skotet = (TextView) findViewById(R.id.skote);
                TextView sdout = (TextView) findViewById(R.id.sdou);
                TextView stukit = (TextView) findViewById(R.id.stuki);
                TextView shant = (TextView) findViewById(R.id.shan);
                TextView shusen = (TextView) findViewById(R.id.husen);
                TextView rment = (TextView) findViewById(R.id.rmen);
                TextView rkotet = (TextView) findViewById(R.id.rkote);
                TextView rdout = (TextView) findViewById(R.id.rdou);
                TextView rtukit = (TextView) findViewById(R.id.rtuki);
                TextView rhant = (TextView) findViewById(R.id.rhan);
                TextView syouritu = (TextView) findViewById(R.id.syouritu);
                TextView makenai = (TextView) findViewById(R.id.makenairitu);
                TextView hy = (TextView)findViewById(R.id.hyukou);
                TextView hhy = (TextView)findViewById(R.id.hhiyukou);
                if ((!tm.getText().toString().equals("")) && (!pl.getText().toString().equals(""))) {
                    Log.d("LIFE","???");
                    try (SQLiteDatabase db = helper.getReadableDatabase();
                         Cursor cs = db.query("bunseki", null, "name=? AND team=?", new String[]{pl.getText().toString(), tm.getText().toString()}, null, null, null)) {
                        if (cs.moveToFirst()) {
                            siai.setText("?????????:" + cs.getString(3));
                            syouri.setText("?????????:" + cs.getString(4));
                            haiboku.setText("?????????:" + cs.getString(5));
                            hikiwake.setText("????????????:" + cs.getString(6));
                            Float k = ((cs.getFloat(4) / cs.getFloat(3)) * 100);
                            Float m = ((cs.getFloat(5) / cs.getFloat(3)) * 100);
                            Float h = ((cs.getFloat(6) / cs.getFloat(3)) * 100);
                            createPieChart(k, m, h);
                            BigDecimal num = new BigDecimal(String.valueOf(k));
                            double num1 = num.setScale(1,BigDecimal.ROUND_DOWN).doubleValue();
                            syouritu.setText("??????:"+num1+"%");
                            Float nm = k + h;
                            num = new BigDecimal(String.valueOf(nm));
                            num1 = num.setScale(1,BigDecimal.ROUND_DOWN).doubleValue();
                            makenai.setText("???????????????"+num1+"%");

                            sment.setText("???:" + cs.getString(7));
                            skotet.setText("??????:" + cs.getString(8));
                            sdout.setText("???:" + cs.getString(9));
                            stukit.setText("??????:" + cs.getString(10));
                            shant.setText("??????:" + cs.getString(11));
                            shusen.setText("?????????:" + cs.getString(12));
                            Float ssum = cs.getFloat(7) + cs.getFloat(8) + cs.getFloat(9) + cs.getFloat(10) + cs.getFloat(11) + cs.getFloat(12);
                            Float smen = (cs.getFloat(7) / ssum) * 100;
                            Float skote = (cs.getFloat(8) / ssum) * 100;
                            Float sdou = (cs.getFloat(9) / ssum) * 100;
                            Float stuki = (cs.getFloat(10) / ssum) * 100;
                            Float shan = (cs.getFloat(11) / ssum) * 100;
                            Float husen = (cs.getFloat(12) / ssum) * 100;
                            Float hyukou = ssum/cs.getFloat(3);
                            num = new BigDecimal(String.valueOf(hyukou));
                            num1 = num.setScale(1,BigDecimal.ROUND_DOWN).doubleValue();
                            hy.setText("??????????????????:"+num1+"???");
                            Syutokuhonsu(smen, skote, sdou, stuki, shan, husen);
                            rment.setText("???:" + cs.getString(13));
                            rkotet.setText("??????:" + cs.getString(14));
                            rdout.setText("???:" + cs.getString(15));
                            rtukit.setText("??????:" + cs.getString(16));
                            rhant.setText("??????:" + cs.getString(17));
                            Float tsum = cs.getFloat(13) + cs.getFloat(14) + cs.getFloat(15) + cs.getFloat(16) + cs.getFloat(17);
                            Float tmen = (cs.getFloat(13) / tsum) * 100;
                            Float tkote = (cs.getFloat(14) / tsum) * 100;
                            Float tdou = (cs.getFloat(15) / tsum) * 100;
                            Float ttuki = (cs.getFloat(16) / tsum) * 100;
                            Float than = (cs.getFloat(17) / tsum) * 100;
                            Float hhiyukou = tsum/cs.getFloat(3);
                            num = new BigDecimal(String.valueOf(hhiyukou));
                            num1 = num.setScale(1,BigDecimal.ROUND_DOWN).doubleValue();
                            hhy.setText("?????????????????????:"+num1+"???");
                            Torarehonsu(tmen, tkote, tdou, ttuki, than);
                            //syou.setText(cs.getInt(0));
                        } else {

                        }
                    }
                }else if((tm.getText().toString().equals(""))&&(!pl.getText().toString().equals(""))) {
                    Log.d("LIFE","???");
                    try (SQLiteDatabase db = helper.getReadableDatabase();
                         Cursor cs = db.rawQuery("SELECT name,SUM(gamen),SUM(win),SUM(lose),SUM(draw),SUM(getmen),SUM(getkote),SUM(getdou),SUM(gettuki),SUM(gethan),SUM(husen),SUM(rmen),SUM(rkote),SUM(rdou),SUM(rtuki),SUM(rhan) FROM bunseki WHERE name = ? GROUP BY name ;",new String[]{pl.getText().toString()})) {
                        if (cs.moveToFirst()) {
                            siai.setText("?????????:" + cs.getString(1));
                            syouri.setText("?????????:" + cs.getString(2));
                            haiboku.setText("?????????:" + cs.getString(3));
                            hikiwake.setText("????????????:" + cs.getString(4));
                            Float k = ((cs.getFloat(2) / cs.getFloat(1)) * 100);
                            Float m = ((cs.getFloat(3) / cs.getFloat(1)) * 100);
                            Float h = ((cs.getFloat(4) / cs.getFloat(1)) * 100);
                            BigDecimal num = new BigDecimal(String.valueOf(k));
                            double num1 = num.setScale(1,BigDecimal.ROUND_DOWN).doubleValue();
                            syouritu.setText("??????:"+num1+"%");
                            Float nm = k + h;
                            num = new BigDecimal(String.valueOf(nm));
                            num1 = num.setScale(1,BigDecimal.ROUND_DOWN).doubleValue();
                            makenai.setText("???????????????:"+num1+"%");
                            createPieChart(k, m, h);
                            sment.setText("???:" + cs.getString(5));
                            skotet.setText("??????:" + cs.getString(6));
                            sdout.setText("???:" + cs.getString(7));
                            stukit.setText("??????:" + cs.getString(8));
                            shant.setText("??????:" + cs.getString(9));
                            shusen.setText("?????????:" + cs.getString(10));
                            Float ssum = cs.getFloat(5) + cs.getFloat(6) + cs.getFloat(7) + cs.getFloat(8) + cs.getFloat(9) + cs.getFloat(10);
                            Float smen = (cs.getFloat(5) / ssum) * 100;
                            Float skote = (cs.getFloat(6) / ssum) * 100;
                            Float sdou = (cs.getFloat(7) / ssum) * 100;
                            Float stuki = (cs.getFloat(8) / ssum) * 100;
                            Float shan = (cs.getFloat(9) / ssum) * 100;
                            Float husen = (cs.getFloat(10) / ssum) * 100;
                            Float hyukou = ssum/cs.getFloat(1);
                            num = new BigDecimal(String.valueOf(hyukou));
                            num1 = num.setScale(1,BigDecimal.ROUND_DOWN).doubleValue();
                            hy.setText("??????????????????:"+num1+"???");
                            Syutokuhonsu(smen, skote, sdou, stuki, shan, husen);
                            rment.setText("???:" + cs.getString(11));
                            rkotet.setText("??????:" + cs.getString(12));
                            rdout.setText("???:" + cs.getString(13));
                            rtukit.setText("??????:" + cs.getString(14));
                            rhant.setText("??????:" + cs.getString(15));
                            Float tsum = cs.getFloat(11) + cs.getFloat(12) + cs.getFloat(13) + cs.getFloat(14) + cs.getFloat(15);
                            Float tmen = (cs.getFloat(11) / tsum) * 100;
                            Float tkote = (cs.getFloat(12) / tsum) * 100;
                            Float tdou = (cs.getFloat(13) / tsum) * 100;
                            Float ttuki = (cs.getFloat(14) / tsum) * 100;
                            Float than = (cs.getFloat(15) / tsum) * 100;
                            Float hhiyukou = tsum/cs.getFloat(1);
                            num = new BigDecimal(String.valueOf(hhiyukou));
                            num1 = num.setScale(1,BigDecimal.ROUND_DOWN).doubleValue();
                            hhy.setText("?????????????????????:"+num1+"???");
                            Torarehonsu(tmen, tkote, tdou, ttuki, than);
                            //syou.setText(cs.getInt(0));
                        } else {

                        }
                    }
                }else {
                    Log.d("LIFE","???");
                }
            }
            });

    }

    private void createPieChart(Float k,Float m,Float h) {
        PieChart pieChart = (PieChart) findViewById(R.id.pichart);

        pieChart.setDrawHoleEnabled(true); // ???????????????????????????????????????
        pieChart.setHoleRadius(50f);       // ???????????????????????????(%??????)
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.setRotationAngle(270);          // ?????????????????????
        pieChart.setRotationEnabled(true);       // ????????????????????????
        pieChart.getLegend().setEnabled(true);   //
        pieChart.setCenterText("??????");
        Description d = new Description();
        d.setText("");
        pieChart.setCenterTextSize(25f);
        pieChart.setDescription(d);
        pieChart.setData(createPieChartData(k, m, h));


        // ??????
        pieChart.invalidate();
        // ?????????????????????
        pieChart.animateXY(2000, 2000); // ???????????????????????????
    }

    // pieChart??????????????????
    private PieData createPieChartData(Float k,Float m,Float h) {
        ArrayList<PieEntry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();


        if(k == 0 && m == 0 ) {
            xVals.add("");
            xVals.add("");
            xVals.add("????????????");
        } else if(k == 0 && h == 0) {
            xVals.add("");
            xVals.add("??????");
            xVals.add("");
        }else if(m == 0 && h==0) {
            xVals.add("??????");
            xVals.add("");
            xVals.add("");
        }else{
            xVals.add("??????");
            xVals.add("??????");
            xVals.add("????????????");
        }

        yVals.add(new PieEntry(k, xVals.get(0)));
        yVals.add(new PieEntry(m, xVals.get(1)));
        yVals.add(new PieEntry(h, xVals.get(2)));



        PieDataSet dataSet = new PieDataSet(yVals, "Data");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(1f);

        // ????????????
        Resources res = getResources();
        int color = res.getColor(R.color.c1);
        // ????????????
        colors.add(color);
        color = res.getColor(R.color.c2);
        colors.add(color);
        color = res.getColor(R.color.c3);
        colors.add(color);
        dataSet.setColors(colors);
        dataSet.setDrawValues(true);
        PieData data = new PieData();
        data.setDataSet(dataSet);
        data.setValueFormatter(new PercentFormatter());

        // ?????????????????????
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        return data;
    }

    private void Syutokuhonsu(Float men,Float kote,Float dou,Float tuki,Float han,Float husen) {
        PieChart pieChart = (PieChart) findViewById(R.id.shon);

        pieChart.setDrawHoleEnabled(true); // ???????????????????????????????????????
        pieChart.setHoleRadius(50f);       // ???????????????????????????(%??????)
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.setRotationAngle(270);          // ?????????????????????
        pieChart.setRotationEnabled(true);       // ????????????????????????
        pieChart.getLegend().setEnabled(true);   //
        pieChart.setCenterText("????????????");
        Description d = new Description();
        d.setText("");
        pieChart.setCenterTextSize(15f);
        pieChart.setDescription(d);
        pieChart.setData(createSyutokuhonsu(men, kote, dou,tuki,han,husen));


        // ??????
        pieChart.invalidate();
        // ?????????????????????
        pieChart.animateXY(2000, 2000); // ???????????????????????????
    }

    // pieChart??????????????????
    private PieData createSyutokuhonsu(Float men,Float kote,Float dou,Float tuki,Float han,Float husen) {
        ArrayList<PieEntry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        if(men == 0) {
            xVals.add("");
        }else {
            xVals.add("???");
        }
        if(kote == 0) {
            xVals.add("");
        }else {
            xVals.add("??????");
        }
        if (dou == 0) {
            xVals.add("");
        } else {
            xVals.add("???");
        }
        if (tuki == 0) {
            xVals.add("");
        }else {
            xVals.add("??????");
        }
        if(han ==0 ){
            xVals.add("");
        }else {
            xVals.add("??????");
        }
        if(husen==0) {
            xVals.add("");
        }else {
            xVals.add("?????????");
        }


        yVals.add(new PieEntry(men, xVals.get(0)));
        yVals.add(new PieEntry(kote, xVals.get(1)));
        yVals.add(new PieEntry(dou, xVals.get(2)));
        yVals.add(new PieEntry(tuki, xVals.get(3)));
        yVals.add(new PieEntry(han, xVals.get(4)));
        yVals.add(new PieEntry(husen, xVals.get(5)));


        PieDataSet dataSet = new PieDataSet(yVals, "Data");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(1f);

        Resources res = getResources();
        int color = res.getColor(R.color.c1);
        // ????????????
        colors.add(color);
        color = res.getColor(R.color.c2);
        colors.add(color);
        color = res.getColor(R.color.c3);
        colors.add(color);
        color = res.getColor(R.color.c4);
        colors.add(color);
        color = res.getColor(R.color.c5);
        colors.add(color);
        color = res.getColor(R.color.c6);
        colors.add(color);



        dataSet.setColors(colors);
        dataSet.setDrawValues(true);
        PieData data = new PieData();
        data.setDataSet(dataSet);
        data.setValueFormatter(new PercentFormatter());

        // ?????????????????????
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        return data;
    }

    private void Torarehonsu(Float men,Float kote,Float dou,Float tuki,Float han) {
        PieChart pieChart = (PieChart) findViewById(R.id.thon);

        pieChart.setDrawHoleEnabled(true); // ???????????????????????????????????????
        pieChart.setHoleRadius(50f);       // ???????????????????????????(%??????)
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.setRotationAngle(270);          // ?????????????????????
        pieChart.setRotationEnabled(true);       // ????????????????????????
        pieChart.getLegend().setEnabled(true);   //
        pieChart.setCenterText("???????????????");
        Description d = new Description();
        d.setText("");
        pieChart.setCenterTextSize(15f);
        pieChart.setDescription(d);
        pieChart.setData(createTorarehonsu(men, kote, dou,tuki,han));


        // ??????
        pieChart.invalidate();
        // ?????????????????????
        pieChart.animateXY(2000, 2000); // ???????????????????????????
    }

    // pieChart??????????????????
    private PieData createTorarehonsu(Float men,Float kote,Float dou,Float tuki,Float han) {
        ArrayList<PieEntry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        if(men == 0) {
            xVals.add("");
        }else {
            xVals.add("???");
        }
        if(kote == 0) {
            xVals.add("");
        }else {
            xVals.add("??????");
        }
        if (dou == 0) {
            xVals.add("");
        } else {
            xVals.add("???");
        }
        if (tuki == 0) {
            xVals.add("");
        }else {
            xVals.add("??????");
        }
        if(han ==0 ){
            xVals.add("");
        }else {
            xVals.add("??????");
        }


        yVals.add(new PieEntry(men, xVals.get(0)));
        yVals.add(new PieEntry(kote, xVals.get(1)));
        yVals.add(new PieEntry(dou, xVals.get(2)));
        yVals.add(new PieEntry(tuki, xVals.get(3)));
        yVals.add(new PieEntry(han, xVals.get(4)));


        PieDataSet dataSet = new PieDataSet(yVals, "Data");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(1f);

        // ????????????
        Resources res = getResources();
        int color = res.getColor(R.color.c1);
        // ????????????
        colors.add(color);
        color = res.getColor(R.color.c2);
        colors.add(color);
        color = res.getColor(R.color.c3);
        colors.add(color);
        color = res.getColor(R.color.c4);
        colors.add(color);
        color = res.getColor(R.color.c5);
        colors.add(color);
        dataSet.setColors(colors);
        dataSet.setDrawValues(true);
        PieData data = new PieData();
        data.setDataSet(dataSet);
        data.setValueFormatter(new PercentFormatter());

        // ?????????????????????
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        return data;
    }
}
