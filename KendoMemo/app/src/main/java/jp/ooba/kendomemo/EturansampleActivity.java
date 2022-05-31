package jp.ooba.kendomemo;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class EturansampleActivity extends AppCompatActivity {
    private DatabaseHelper helper = null;
    private String name = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eturan_sample);
        setTitle("団体戦閲覧");
        // インテントを取得
        Intent intent = EturansampleActivity.this.getIntent();
        // インテントに保存されたデータを取得
        int dat = intent.getIntExtra("filenum",0);
        Integer ig = dat;
        String data[] = {ig.toString()};
        //layoutに関連付け
        final EditText teamA = (EditText)findViewById(R.id.txtTeamA);
        final EditText teamB = (EditText)findViewById(R.id.txtTeamB);
        final EditText txtDay = (EditText)findViewById(R.id.txtday);
        final EditText memo = (EditText)findViewById(R.id.Memo);
        int[] han = new int[24];
        ArrayList<Spinner> sp = new ArrayList<>();
        sp.add(findViewById(R.id.spA1));
        sp.add(findViewById(R.id.spA2));
        sp.add(findViewById(R.id.spB1));
        sp.add(findViewById(R.id.spB2));
        sp.add(findViewById(R.id.spC1));
        sp.add(findViewById(R.id.spC2));
        sp.add(findViewById(R.id.spD1));
        sp.add(findViewById(R.id.spD2));
        sp.add(findViewById(R.id.spE1));
        sp.add(findViewById(R.id.spE2));
        sp.add(findViewById(R.id.spF1));
        sp.add(findViewById(R.id.spF2));
        sp.add(findViewById(R.id.spG1));
        sp.add(findViewById(R.id.spG2));
        sp.add(findViewById(R.id.spH1));
        sp.add(findViewById(R.id.spH2));
        sp.add(findViewById(R.id.spI1));
        sp.add(findViewById(R.id.spI2));
        sp.add(findViewById(R.id.spJ1));
        sp.add(findViewById(R.id.spJ2));
        sp.add(findViewById(R.id.spK1));
        sp.add(findViewById(R.id.spK2));
        sp.add(findViewById(R.id.spL1));
        sp.add(findViewById(R.id.spL2));


        ArrayList<EditText> plrs = new ArrayList<>();
        plrs.add(findViewById(R.id.playerA));
        plrs.add(findViewById(R.id.playerB));
        plrs.add(findViewById(R.id.playerC));
        plrs.add(findViewById(R.id.playerD));
        plrs.add(findViewById(R.id.playerE));
        plrs.add(findViewById(R.id.playerF));
        plrs.add(findViewById(R.id.playerG));
        plrs.add(findViewById(R.id.playerH));
        plrs.add(findViewById(R.id.playerI));
        plrs.add(findViewById(R.id.playerJ));
        plrs.add(findViewById(R.id.playerK));
        plrs.add(findViewById(R.id.playerL));

        ArrayList<ToggleButton> hansoku = new ArrayList<>();
        hansoku.add(findViewById(R.id.hanA1));
        hansoku.add(findViewById(R.id.hanA2));
        hansoku.add(findViewById(R.id.hanB1));
        hansoku.add(findViewById(R.id.hanB2));
        hansoku.add(findViewById(R.id.hanC1));
        hansoku.add(findViewById(R.id.hanC2));
        hansoku.add(findViewById(R.id.hanD1));
        hansoku.add(findViewById(R.id.hanD2));
        hansoku.add(findViewById(R.id.hanE1));
        hansoku.add(findViewById(R.id.hanE2));
        hansoku.add(findViewById(R.id.hanF1));
        hansoku.add(findViewById(R.id.hanF2));
        hansoku.add(findViewById(R.id.hanG1));
        hansoku.add(findViewById(R.id.hanG2));
        hansoku.add(findViewById(R.id.hanH1));
        hansoku.add(findViewById(R.id.hanH2));
        hansoku.add(findViewById(R.id.hanI1));
        hansoku.add(findViewById(R.id.hanI2));
        hansoku.add(findViewById(R.id.hanJ1));
        hansoku.add(findViewById(R.id.hanJ2));
        hansoku.add(findViewById(R.id.hanK1));
        hansoku.add(findViewById(R.id.hanK2));
        hansoku.add(findViewById(R.id.hanL1));
        hansoku.add(findViewById(R.id.hanL2));

        TextView hA = (TextView)findViewById(R.id.nhonA);
        TextView hB = (TextView)findViewById(R.id.nhonB);
        TextView sA = (TextView)findViewById(R.id.nsyouA);
        TextView sB = (TextView)findViewById(R.id.nsyouB);

        //ヘルパーを準備
        helper = new DatabaseHelper(this);


        int sn = 0; //スピナーの番号
        int k = 0; //列の番号

        //更新前のデータ
        String[] pl = new String[12]; //選手名
        String[] tm = new String[2];  //チーム名
        int[] kk = new int[12]; //結果０勝利１敗北２引き分け
        int[] th = new int[24]; //取得した部位//0なし１面２小手３胴４突き５反則６不戦勝
        int[] rh = new int[24]; //取られた部位０なし１面2小手3胴４突き５反則６不戦勝

        //データベースを読み込みで開く
        try(SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cs = db.query("kiroku",null,"num = ?",data,null,null,null)) {
            if (cs.moveToFirst()) {
                name = cs.getString(1);
                txtDay.setText(cs.getString(2));
                teamA.setText(cs.getString(3));
                tm[0] = cs.getString(3);
                teamB.setText(cs.getString(4));
                tm[1] = cs.getString(4);
                k = 4;
                for (int j = 0; j < 12; j++) {
                    k++;
                    plrs.get(j).setText(cs.getString(k));
                    pl[j] = cs.getString(k);
                }
                for (int i = 0; i < 24; i++) {
                    k++;
                    if(i < 12) {
                        switch (cs.getString(k)) {
                            case "":
                                sn = 0;
                                th[i] = 0;
                                rh[i + 12] = 0;
                                break;
                            case "メ":
                                sn = 1;
                                th[i] = 1;
                                rh[i + 12] = 1;
                                break;
                            case "コ":
                                sn = 2;
                                th[i] = 2;
                                rh[i+12] = 2;
                                break;
                            case "ド":
                                sn = 3;
                                th[i] = 3;
                                rh[i+12] = 3;
                                break;
                            case "ツ":
                                sn = 4;
                                th[i] = 4;
                                rh[i+12] = 4;
                                break;
                            case "反":
                                sn = 5;
                                th[i] = 5;
                                rh[i+12] = 5;
                                break;
                            case "不":
                                sn = 6;
                                th[i] = 6;
                                rh[i+12] = 6;
                                break;
                            case "(メ)":
                                sn = 7;
                                th[i] = 1;
                                rh[i+12] = 1;
                                break;
                            case "(コ)":
                                sn = 8;
                                th[i] = 2;
                                rh[i+12] = 2;
                                break;
                            case "(ド)":
                                sn = 9;
                                th[i] = 3;
                                rh[i+12] = 3;
                                break;
                            case "(ツ)":
                                sn = 10;
                                th[i] = 4;
                                rh[i+12] = 4;
                                break;
                            case "(反)":
                                sn = 11;
                                th[i] = 5;
                                rh[i+12] = 5;
                                break;
                            default:
                                sn = 0;
                                th[i] = 0;
                                rh[i+12] = 0;
                                break;
                        }
                    }
                    if(12 <= i) {
                        switch (cs.getString(k)) {
                            case "":
                                sn = 0;
                                th[i] = 0;
                                rh[i - 12] = 0;
                                break;
                            case "メ":
                                sn = 1;
                                th[i] = 1;
                                rh[i - 12] = 1;
                                break;
                            case "コ":
                                sn = 2;
                                th[i] = 2;
                                rh[i-12] = 2;
                                break;
                            case "ド":
                                sn = 3;
                                th[i] = 3;
                                rh[i-12] = 3;
                                break;
                            case "ツ":
                                sn = 4;
                                th[i] = 4;
                                rh[i-12] = 4;
                                break;
                            case "反":
                                sn = 5;
                                th[i] = 5;
                                rh[i-12] = 5;
                                break;
                            case "不":
                                sn = 6;
                                th[i] = 6;
                                rh[i-12] = 6;
                                break;
                            case "(メ)":
                                sn = 7;
                                th[i] = 1;
                                rh[i-12] = 1;
                                break;
                            case "(コ)":
                                sn = 8;
                                th[i] = 2;
                                rh[i-12] = 2;
                                break;
                            case "(ド)":
                                sn = 9;
                                th[i] = 3;
                                rh[i-12] = 3;
                                break;
                            case "(ツ)":
                                sn = 10;
                                th[i] = 4;
                                rh[i-12] = 4;
                                break;
                            case "(反)":
                                sn = 11;
                                th[i] = 5;
                                rh[i-12] = 5;
                                break;
                            default:
                                sn = 0;
                                th[i] = 0;
                                rh[i-12] = 0;
                                break;
                        }
                    }
                    sp.get(i).setSelection(sn);
                }
                for (int i = 0; i < 24; i++) {
                    k++;
                    han[i] = cs.getInt(k);
                    if (han[i] == 1) {
                        hansoku.get(i).setChecked(true);
                    } else if (han[i] == 0) {
                        hansoku.get(i).setChecked(false);
                    }
                }
                k++;
                memo.setText(cs.getString(k));
                int at = 0;
                int bt = 0;
                //個人の勝敗判定
                for(int i = 0;i < 6;i++) {
                    at = 0;
                    bt = 0;
                    if(!cs.getString(17+(i*2)).equals("")) {
                        at++;
                    }
                    if(!cs.getString(29+(i*2)).equals("")) {
                        bt++;
                    }
                    if(!cs.getString(18+(i*2)).equals("")){
                        at++;
                    }
                    if(!cs.getString(30+(i*2)).equals("")){
                        bt++;
                    }
                    if(at > bt) {
                        kk[i] = 0;
                        kk[i+6] = 1;
                    }else if(bt > at) {
                        kk[i] = 1;
                        kk[i+6] = 0;
                    }else{
                        kk[i] = 2;
                        kk[i+6]=2;
                    }
                }
            } else {
                Toast.makeText(this, "データがありません。", Toast.LENGTH_SHORT).show();
            }
        }

        //日付
        EditText showDate = (EditText)findViewById(R.id.txtday);
        //EditTextにリスナーをつける
        showDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendarインスタンスを取得
                final Calendar date = Calendar.getInstance();

                //DatePickerDialogインスタンスを取得
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EturansampleActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                showDate.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
                            }
                        },
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DATE)
                );

                //dialogを表示
                datePickerDialog.show();

            }
        });

        //更新
        Button bt = (Button) findViewById(R.id.kousin);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = new EditText(EturansampleActivity.this);
                editText.setText(name);
                new AlertDialog.Builder(EturansampleActivity.this)
                        .setTitle("名前をつける")
                        .setMessage("ファイル名を入力してください")
                        .setView(editText)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TextViewにセットしてあげる
                                        name = editText.getText().toString();
                                        if(name.equals("")) {
                                            name = teamA.getText().toString() + "対" + teamB.getText().toString();
                                        }
                                        String txtDay = showDate.getText().toString();
                                        String txtTeamA = teamA.getText().toString();
                                        String txtTeamB = teamB.getText().toString();
                                        String txtMemo = memo.getText().toString();
                                        StringBuilder sb = new StringBuilder();
                                        String[] players = new String[12];
                                        String[] spiners = new String[24];
                                        //boolean[] hs = new boolean[24];
                                        int[] hs = new int[24];

                                        for(int i = 0;i<12;i++) {
                                            players[i] = plrs.get(i).getText().toString();
                                        }
                                        for(int i = 0;i<24;i++) {
                                            spiners[i] = sp.get(i).getSelectedItem().toString();
                                        }
                                        for(int i = 0;i<24;i++) {
                                            if(hansoku.get(i).isChecked()) {
                                                hs[i] = 1;
                                            }else {
                                                hs[i] = 0;
                                            }
                                        }

                                        int[] sakujo = new int[15];
                                        int a = 0;

                                        //bunsekiから更新前のデータを削除する
                                        for(int i = 0;i<12;i++) {
                                            if(!pl[i].equals("")) {
                                                if(i < 6) {
                                                    try (SQLiteDatabase db = helper.getReadableDatabase();
                                                         Cursor cs = db.query("bunseki", null, "name=? AND team=?", new String[]{pl[i], tm[0]}, null, null, null)) {
                                                        if (cs.moveToFirst()) {
                                                            //データあり
                                                            for (int l = 0; l < 15; l++) {
                                                                sakujo[l] = cs.getInt(l + 3);
                                                            }
                                                        }
                                                    }
                                                } else  {
                                                    try (SQLiteDatabase db = helper.getReadableDatabase();
                                                         Cursor cs = db.query("bunseki", null, "name=? AND team=?", new String[]{pl[i], tm[1]}, null, null, null)) {
                                                        if (cs.moveToFirst()) {
                                                            //データあり
                                                            for (int l = 0; l < 15; l++) {
                                                                sakujo[l] = cs.getInt(l + 3);
                                                            }
                                                        }
                                                    }
                                                }
                                                try(SQLiteDatabase db = helper.getWritableDatabase()) {
                                                    ContentValues cv = new ContentValues();
                                                    cv.put("gamen", sakujo[0] - 1);
                                                    switch (kk[i]) {
                                                        case 0:
                                                            cv.put("win", sakujo[1] - 1);
                                                            break;
                                                        case 1:
                                                            cv.put("lose", sakujo[2] - 1);
                                                            break;
                                                        case 2:
                                                            cv.put("draw", sakujo[3] - 1);
                                                            break;
                                                        default:
                                                            break;
                                                    }

                                                    switch (th[a]) {
                                                        case 1:
                                                            sakujo[4]--;
                                                            break;
                                                        case 2:
                                                            sakujo[5]--;
                                                            break;
                                                        case 3:
                                                            sakujo[6]--;
                                                            break;
                                                        case 4:
                                                            sakujo[7]--;
                                                            break;
                                                        case 5:
                                                            sakujo[8]--;
                                                            break;
                                                        case 6:
                                                            sakujo[9]--;
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                    switch (rh[a]) {
                                                        case 1:
                                                            sakujo[10]--;
                                                            break;
                                                        case 2:
                                                            sakujo[11]--;
                                                            break;
                                                        case 3:
                                                            sakujo[12]--;
                                                            break;
                                                        case 4:
                                                            sakujo[13]--;
                                                            break;
                                                        case 5:
                                                            sakujo[14]--;
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                    a++;
                                                    switch (th[a]) {
                                                        case 1:
                                                            sakujo[4]--;
                                                            break;
                                                        case 2:
                                                            sakujo[5]--;
                                                            break;
                                                        case 3:
                                                            sakujo[6]--;
                                                            break;
                                                        case 4:
                                                            sakujo[7]--;
                                                            break;
                                                        case 5:
                                                            sakujo[8]--;
                                                            break;
                                                        case 6:
                                                            sakujo[9]--;
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                    switch (rh[a]) {
                                                        case 1:
                                                            sakujo[10]--;
                                                            break;
                                                        case 2:
                                                            sakujo[11]--;
                                                            break;
                                                        case 3:
                                                            sakujo[12]--;
                                                            break;
                                                        case 4:
                                                            sakujo[13]--;
                                                            break;
                                                        case 5:
                                                            sakujo[14]--;
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                    a++;

                                                    cv.put("getmen", sakujo[4]);
                                                    cv.put("getkote", sakujo[5]);
                                                    cv.put("getdou", sakujo[6]);
                                                    cv.put("gettuki", sakujo[7]);
                                                    cv.put("gethan", sakujo[8]);
                                                    cv.put("husen", sakujo[9]);
                                                    cv.put("rmen", sakujo[10]);
                                                    cv.put("rkote", sakujo[11]);
                                                    cv.put("rdou", sakujo[12]);
                                                    cv.put("rtuki", sakujo[13]);
                                                    cv.put("rhan", sakujo[14]);
                                                    if (i < 6) {
                                                        db.update("bunseki", cv, "name = ? AND team = ?", new String[]{pl[i], tm[0]});
                                                        if(sakujo[0] == 1) {
                                                            db.delete("bunseki", "name = ? AND team == ?", new String[]{pl[i], tm[0]});
                                                        }
                                                    } else {
                                                        db.update("bunseki", cv, "name = ? AND team = ?", new String[]{pl[i], tm[1]});
                                                        if(sakujo[0] == 1) {
                                                            db.delete("bunseki", "name = ? AND team == ?", new String[]{pl[i], tm[1]});
                                                        }
                                                    }
                                                }
                                            }else {
                                                a = a+2;
                                            }
                                        }

                                        try(SQLiteDatabase db = helper.getWritableDatabase()) {
                                            ContentValues cv = new ContentValues();
                                            cv.put("name",name);
                                            cv.put("date",txtDay);
                                            cv.put("teamA",txtTeamA);
                                            cv.put("teamB",txtTeamB);
                                            cv.put("plrA",players[0]);
                                            cv.put("plrB",players[1]);
                                            cv.put("plrC",players[2]);
                                            cv.put("plrD",players[3]);
                                            cv.put("plrE",players[4]);
                                            cv.put("plrF",players[5]);
                                            cv.put("plrG",players[6]);
                                            cv.put("plrH",players[7]);
                                            cv.put("plrI",players[8]);
                                            cv.put("plrJ",players[9]);
                                            cv.put("plrK",players[10]);
                                            cv.put("plrL",players[11]);
                                            cv.put("dtA1",spiners[0]);
                                            cv.put("dtA2",spiners[1]);
                                            cv.put("dtB1",spiners[2]);
                                            cv.put("dtB2",spiners[3]);
                                            cv.put("dtC1",spiners[4]);
                                            cv.put("dtC2",spiners[5]);
                                            cv.put("dtD1",spiners[6]);
                                            cv.put("dtD2",spiners[7]);
                                            cv.put("dtE1",spiners[8]);
                                            cv.put("dtE2",spiners[9]);
                                            cv.put("dtF1",spiners[10]);
                                            cv.put("dtF2",spiners[11]);
                                            cv.put("dtG1",spiners[12]);
                                            cv.put("dtG2",spiners[13]);
                                            cv.put("dtH1",spiners[14]);
                                            cv.put("dtH2",spiners[15]);
                                            cv.put("dtI1",spiners[16]);
                                            cv.put("dtI2",spiners[17]);
                                            cv.put("dtJ1",spiners[18]);
                                            cv.put("dtJ2",spiners[19]);
                                            cv.put("dtK1",spiners[20]);
                                            cv.put("dtK2",spiners[21]);
                                            cv.put("dtL1",spiners[22]);
                                            cv.put("dtL2",spiners[23]);
                                            cv.put("hanA1",hs[0]);
                                            cv.put("hanA2",hs[1]);
                                            cv.put("hanB1",hs[2]);
                                            cv.put("hanB2",hs[3]);
                                            cv.put("hanC1",hs[4]);
                                            cv.put("hanC2",hs[5]);
                                            cv.put("hanD1",hs[6]);
                                            cv.put("hanD2",hs[7]);
                                            cv.put("hanE1",hs[8]);
                                            cv.put("hanE2",hs[9]);
                                            cv.put("hanF1",hs[10]);
                                            cv.put("hanF2",hs[11]);
                                            cv.put("hanG1",hs[12]);
                                            cv.put("hanG2",hs[13]);
                                            cv.put("hanH1",hs[14]);
                                            cv.put("hanH2",hs[15]);
                                            cv.put("hanI1",hs[16]);
                                            cv.put("hanI2",hs[17]);
                                            cv.put("hanJ1",hs[18]);
                                            cv.put("hanJ2",hs[19]);
                                            cv.put("hanK1",hs[20]);
                                            cv.put("hanK2",hs[21]);
                                            cv.put("hanL1",hs[22]);
                                            cv.put("hanL2",hs[23]);
                                            cv.put("memo",txtMemo);
                                            db.update("kiroku",cv,"num=?",data);
                                            Toast.makeText(EturansampleActivity.this,"データを更新しました。", Toast.LENGTH_LONG).show();
                                        }


                                        //番号を付ける(分析)
                                        int k = 0;
                                        try (SQLiteDatabase db = helper.getReadableDatabase();
                                             Cursor cs = db.query("bunseki", null, null, null, null, null, null)) {
                                            if (cs.moveToFirst()) {
                                                k = 1;
                                                while (cs.moveToNext()) {
                                                    k = cs.getInt(0) + 1;
                                                }
                                            } else {
                                                k = 0;
                                            }
                                        }

                                        int[] an = new int[6];
                                        int[] bn = new int[6];
                                        int j = 0;
                                        //分析

                                        //Aチーム
                                        int[] kousin = new int[15];
                                        int[] kiroku = new int[15];
                                        int suru = 0;
                                        for(int i = 0;i < 6;i++) {
                                            if(!players[i].equals("")) {
                                                suru = 0;
                                                for(int q = 0;q < 15;q++) {
                                                    kousin[q] = 0;
                                                    kiroku[q] = 0;
                                                }
                                                //選手名、チームで一致するデータの有無を確認
                                                try (SQLiteDatabase db = helper.getReadableDatabase();
                                                     Cursor cs = db.query("bunseki", null, "name=? AND team=?", new String[]{players[i], txtTeamA}, null, null, null)) {
                                                    if (cs.moveToFirst()) {
                                                        //データあり
                                                        for (int l = 0; l < 15; l++) {
                                                            kousin[l] = cs.getInt(l + 3);
                                                        }
                                                        suru = 1;
                                                    } else {
                                                        //データなし
                                                        suru = 0;
                                                    }
                                                }


                                                //選手ごとに結果を記録
                                                try(SQLiteDatabase db = helper.getWritableDatabase()) {
                                                    ContentValues cv = new ContentValues();
                                                    kiroku[0] = 1;
                                                    //勝敗判定
                                                    for(int p=0;p<2;p++) {
                                                        if (!spiners[j].equals("")) {
                                                            an[i] = an[i] + 1;
                                                        }
                                                        if (!spiners[j + 12].equals("")) {
                                                            bn[i] = bn[i] + 1;
                                                        }
                                                        switch (spiners[j]) {
                                                            case "メ":
                                                                kiroku[4]++;
                                                                break;
                                                            case "コ":
                                                                kiroku[5]++;
                                                                break;
                                                            case "ド":
                                                                kiroku[6]++;
                                                                break;
                                                            case "ツ":
                                                                kiroku[7]++;
                                                                break;
                                                            case "反":
                                                                kiroku[8]++;
                                                                break;
                                                            case "不":
                                                                kiroku[9]++;
                                                                break;
                                                            case "(メ)":
                                                                kiroku[4]++;
                                                                break;
                                                            case "(コ)":
                                                                kiroku[5]++;
                                                                break;
                                                            case "(ド)":
                                                                kiroku[6]++;
                                                                break;
                                                            case "(ツ)":
                                                                kiroku[7]++;
                                                                break;
                                                            case "(反)":
                                                                kiroku[8]++;
                                                                break;
                                                            default:
                                                                break;
                                                        }
                                                        switch (spiners[j + 12]) {
                                                            case "メ":
                                                                kiroku[10]++;
                                                                break;
                                                            case "コ":
                                                                kiroku[11]++;
                                                                break;
                                                            case "ド":
                                                                kiroku[12]++;
                                                                break;
                                                            case "ツ":
                                                                kiroku[13]++;
                                                                break;
                                                            case "反":
                                                                kiroku[14]++;
                                                                break;
                                                            case "(メ)":
                                                                kiroku[10]++;
                                                                break;
                                                            case "(コ)":
                                                                kiroku[11]++;
                                                                break;
                                                            case "(ド)":
                                                                kiroku[12]++;
                                                                break;
                                                            case "(ツ)":
                                                                kiroku[13]++;
                                                                break;
                                                            case "(反)":
                                                                kiroku[14]++;
                                                                break;
                                                            default:
                                                                break;
                                                        }
                                                        j++;
                                                    }

                                                    if(an[i]>bn[i]) {
                                                        kiroku[1] = 1;
                                                    } else if(bn[i]>an[i]) {
                                                        kiroku[2] = 1;
                                                    } else {
                                                        kiroku[3] = 1;
                                                    }

                                                    if(suru == 0) {
                                                        //データ追加
                                                        cv.put("num",k);
                                                        cv.put("name",players[i]);
                                                        cv.put("team",txtTeamA);
                                                        cv.put("gamen",kiroku[0]);
                                                        cv.put("win",kiroku[1]);
                                                        cv.put("lose",kiroku[2]);
                                                        cv.put("draw",kiroku[3]);
                                                        cv.put("getmen",kiroku[4]);
                                                        cv.put("getkote",kiroku[5]);
                                                        cv.put("getdou",kiroku[6]);
                                                        cv.put("gettuki",kiroku[7]);
                                                        cv.put("gethan",kiroku[8]);
                                                        cv.put("husen",kiroku[9]);
                                                        cv.put("rmen",kiroku[10]);
                                                        cv.put("rkote",kiroku[11]);
                                                        cv.put("rdou",kiroku[12]);
                                                        cv.put("rtuki",kiroku[13]);
                                                        cv.put("rhan",kiroku[14]);
                                                        db.insert("bunseki",null,cv);
                                                        k++;
                                                    }else {
                                                        //データ更新
                                                        cv.put("gamen",kiroku[0]+kousin[0]);
                                                        cv.put("win",kiroku[1]+kousin[1]);
                                                        cv.put("lose",kiroku[2]+kousin[2]);
                                                        cv.put("draw",kiroku[3]+kousin[3]);
                                                        cv.put("getmen",kiroku[4]+kousin[4]);
                                                        cv.put("getkote",kiroku[5]+kousin[5]);
                                                        cv.put("getdou",kiroku[6]+kousin[6]);
                                                        cv.put("gettuki",kiroku[7]+kousin[7]);
                                                        cv.put("gethan",kiroku[8]+kousin[8]);
                                                        cv.put("husen",kiroku[9]+kousin[9]);
                                                        cv.put("rmen",kiroku[10]+kousin[10]);
                                                        cv.put("rkote",kiroku[11]+kousin[11]);
                                                        cv.put("rdou",kiroku[12]+kousin[12]);
                                                        cv.put("rtuki",kiroku[13]+kousin[13]);
                                                        cv.put("rhan",kiroku[14]+kousin[14]);
                                                        db.update("bunseki",cv,"name = ? AND team = ?",new String[]{players[i],txtTeamA});
                                                    }
                                                }
                                            } else {
                                                j = j + 2;
                                            }
                                        }
                                        //Bチーム
                                        j = 12;


                                        for(int i = 6;i < 12;i++) {
                                            if(!players[i].equals("")) {
                                                for(int q = 0;q<15;q++) {
                                                    kousin[q] = 0;
                                                    kiroku[q] = 0;
                                                }
                                                suru = 0;
                                                //選手名、チームで一致するデータの有無を確認
                                                try (SQLiteDatabase db = helper.getReadableDatabase();
                                                     Cursor cs = db.query("bunseki", null, "name=? AND team=?", new String[]{players[i], txtTeamB}, null, null, null)) {
                                                    if (cs.moveToFirst()) {
                                                        //データあり
                                                        for (int l = 0; l < 15; l++) {
                                                            kousin[l] = cs.getInt(l + 3);
                                                        }
                                                        suru = 1;
                                                    } else {
                                                        //データなし
                                                        suru = 0;
                                                    }
                                                }


                                                //選手ごとに結果を記録
                                                try(SQLiteDatabase db = helper.getWritableDatabase()) {
                                                    ContentValues cv = new ContentValues();
                                                    kiroku[0] = 1;
                                                    //勝敗判定
                                                    for(int p=0;p<2;p++) {

                                                        switch (spiners[j]) {
                                                            case "メ":
                                                                kiroku[4]++;
                                                                break;
                                                            case "コ":
                                                                kiroku[5]++;
                                                                break;
                                                            case "ド":
                                                                kiroku[6]++;
                                                                break;
                                                            case "ツ":
                                                                kiroku[7]++;
                                                                break;
                                                            case "反":
                                                                kiroku[8]++;
                                                                break;
                                                            case "不":
                                                                kiroku[9]++;
                                                                break;
                                                            case "(メ)":
                                                                kiroku[4]++;
                                                                break;
                                                            case "(コ)":
                                                                kiroku[5]++;
                                                                break;
                                                            case "(ド)":
                                                                kiroku[6]++;
                                                                break;
                                                            case "(ツ)":
                                                                kiroku[7]++;
                                                                break;
                                                            case "(反)":
                                                                kiroku[8]++;
                                                                break;
                                                            default:
                                                                break;
                                                        }
                                                        switch (spiners[j - 12]) {
                                                            case "メ":
                                                                kiroku[10]++;
                                                                break;
                                                            case "コ":
                                                                kiroku[11]++;
                                                                break;
                                                            case "ド":
                                                                kiroku[12]++;
                                                                break;
                                                            case "ツ":
                                                                kiroku[13]++;
                                                                break;
                                                            case "反":
                                                                kiroku[14]++;
                                                                break;
                                                            case "(メ)":
                                                                kiroku[10]++;
                                                                break;
                                                            case "(コ)":
                                                                kiroku[11]++;
                                                                break;
                                                            case "(ド)":
                                                                kiroku[12]++;
                                                                break;
                                                            case "(ツ)":
                                                                kiroku[13]++;
                                                                break;
                                                            case "(反)":
                                                                kiroku[14]++;
                                                                break;
                                                            default:
                                                                break;
                                                        }
                                                        j++;
                                                    }

                                                    if(an[i-6]>bn[i-6]) {
                                                        kiroku[2] = 1;
                                                    } else if(bn[i-6]>an[i-6]) {
                                                        kiroku[1] = 1;
                                                    } else {
                                                        kiroku[3] = 1;
                                                    }

                                                    if(suru == 0) {
                                                        //データ追加
                                                        cv.put("num",k);
                                                        cv.put("name",players[i]);
                                                        cv.put("team",txtTeamB);
                                                        cv.put("gamen",kiroku[0]);
                                                        cv.put("win",kiroku[1]);
                                                        cv.put("lose",kiroku[2]);
                                                        cv.put("draw",kiroku[3]);
                                                        cv.put("getmen",kiroku[4]);
                                                        cv.put("getkote",kiroku[5]);
                                                        cv.put("getdou",kiroku[6]);
                                                        cv.put("gettuki",kiroku[7]);
                                                        cv.put("gethan",kiroku[8]);
                                                        cv.put("husen",kiroku[9]);
                                                        cv.put("rmen",kiroku[10]);
                                                        cv.put("rkote",kiroku[11]);
                                                        cv.put("rdou",kiroku[12]);
                                                        cv.put("rtuki",kiroku[13]);
                                                        cv.put("rhan",kiroku[14]);
                                                        db.insert("bunseki",null,cv);
                                                        k++;
                                                    }else {
                                                        //データ更新
                                                        cv.put("gamen",kiroku[0]+kousin[0]);
                                                        cv.put("win",kiroku[1]+kousin[1]);
                                                        cv.put("lose",kiroku[2]+kousin[2]);
                                                        cv.put("draw",kiroku[3]+kousin[3]);
                                                        cv.put("getmen",kiroku[4]+kousin[4]);
                                                        cv.put("getkote",kiroku[5]+kousin[5]);
                                                        cv.put("getdou",kiroku[6]+kousin[6]);
                                                        cv.put("gettuki",kiroku[7]+kousin[7]);
                                                        cv.put("gethan",kiroku[8]+kousin[8]);
                                                        cv.put("husen",kiroku[9]+kousin[9]);
                                                        cv.put("rmen",kiroku[10]+kousin[10]);
                                                        cv.put("rkote",kiroku[11]+kousin[11]);
                                                        cv.put("rdou",kiroku[12]+kousin[12]);
                                                        cv.put("rtuki",kiroku[13]+kousin[13]);
                                                        cv.put("rhan",kiroku[14]+kousin[14]);
                                                        db.update("bunseki",cv,"name = ? AND team = ?",new String[]{players[i],txtTeamB});
                                                    }
                                                }
                                            } else {
                                                j = j + 2;
                                            }
                                        }

                                        Intent i = new Intent(EturansampleActivity.this, MainActivity.class);
                                        startActivity(i);

                                    }
                                })
                        .setNegativeButton("キャンセル",null)
                        .show();

            }
        });


        //定期実行


        Timer timer = new Timer(false);
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                //ここに定期実行させたい処理を記述
                //勝ち数計算
                int ans = 0;
                int bns = 0;
                int an = 0;
                int bn = 0;
                int as = 0;
                int bs = 0;

                ArrayList<Spinner> spt = new ArrayList<>();
                spt.add(findViewById(R.id.spA1));
                spt.add(findViewById(R.id.spA2));
                spt.add(findViewById(R.id.spG1));
                spt.add(findViewById(R.id.spG2));
                spt.add(findViewById(R.id.spB1));
                spt.add(findViewById(R.id.spB2));
                spt.add(findViewById(R.id.spH1));
                spt.add(findViewById(R.id.spH2));
                spt.add(findViewById(R.id.spC1));
                spt.add(findViewById(R.id.spC2));
                spt.add(findViewById(R.id.spI1));
                spt.add(findViewById(R.id.spI2));
                spt.add(findViewById(R.id.spD1));
                spt.add(findViewById(R.id.spD2));
                spt.add(findViewById(R.id.spJ1));
                spt.add(findViewById(R.id.spJ2));
                spt.add(findViewById(R.id.spE1));
                spt.add(findViewById(R.id.spE2));
                spt.add(findViewById(R.id.spK1));
                spt.add(findViewById(R.id.spK2));
                String[] spiners = new String[20];
                for(int i = 0;i<20;i++) {
                    spiners[i] = spt.get(i).getSelectedItem().toString();
                }
                for(int i = 0;i<20;i++) {
                    if(!spiners[i].equals("")) {
                        an++;
                    }
                    i++;
                    if(!spiners[i].equals("")) {
                        an++;
                    }
                    i++;
                    if(!spiners[i].equals("")){
                        bn++;
                    }
                    i++;
                    if(!spiners[i].equals("")) {
                        bn++;
                    }
                    if(an > bn) {
                        as++;
                    }else if(bn > an) {
                        bs++;
                    }
                    ans = ans + an;
                    bns = bns + bn;
                    an = 0;
                    bn = 0;
                }

                Integer anst = ans;
                Integer bnst = bns;
                Integer ast = as;
                Integer bst = bs;
                hA.setText(anst.toString());
                hB.setText(bnst.toString());
                sA.setText(ast.toString());
                sB.setText(bst.toString());
            }
        };
        timer.schedule(task,0,1000);

    }
}
