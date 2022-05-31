package jp.ooba.kendomemo;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class DantaiwriteActivity extends AppCompatActivity {
    private DatabaseHelper helper = null;
    private TextView timerText;
    private String name = new String();
    private CountDown countDown;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("mm:ss.SSS", Locale.US);
    private long stoptime;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dantai_write);
        setTitle("団体戦記録");
        //ヘルパーを準備
        helper = new DatabaseHelper(this);


        final EditText teamA = (EditText)findViewById(R.id.txtTeamA);
        final EditText teamB = (EditText)findViewById(R.id.txtTeamB);
        final EditText mem = (EditText)findViewById(R.id.MEMO);
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


        //日付

        Date d = new Date();
        EditText showDate = (EditText)findViewById(R.id.txtday);
        SimpleDateFormat d1 = new SimpleDateFormat("yyyy / MM / dd");
        String c1 = d1.format(d);
        showDate.setText(c1);
        //EditTextにリスナーをつける
        showDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendarインスタンスを取得
                final Calendar date = Calendar.getInstance();

                //DatePickerDialogインスタンスを取得
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        DantaiwriteActivity.this,
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



        Button bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = new EditText(DantaiwriteActivity.this);
                new AlertDialog.Builder(DantaiwriteActivity.this)
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
                                String txtMemo = mem.getText().toString();
                                StringBuilder sb = new StringBuilder();
                                String[] players = new String[12];
                                String[] spiners = new String[24];
                                //boolean[] hs = new boolean[24];
                                int[] hs = new int[24];
                                //name = txtTeamA + "対" + txtTeamB;
                                //番号を付ける
                                int num = 0;

                                //番号を付ける
                                Integer nums;
                                try (SQLiteDatabase db = helper.getReadableDatabase();
                                     Cursor cs = db.query("kiroku", null, null, null, null, null, null)) {
                                    if (cs.moveToFirst()) {
                                        num = 1;
                                        nums = cs.getInt(0);
                                        Log.d("LIFE",nums.toString());
                                        while (cs.moveToNext()) {
                                            num = cs.getInt(0) + 1;
                                            nums = num - 1;
                                            Log.d("LIFE",nums.toString());
                                        }
                                    } else {
                                        num = 0;
                                    }
                                }
                                nums = num;
                                Log.d("LIFE",nums.toString());

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

                                try(SQLiteDatabase db = helper.getWritableDatabase()) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("num",num);
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
                                    db.insert("kiroku",null,cv);
                                    Toast.makeText(DantaiwriteActivity.this,"データを登録しました。", Toast.LENGTH_LONG).show();
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

                                Intent i = new Intent(DantaiwriteActivity.this, MainActivity.class);
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

        //タイマー処理
        // 3分= 3x60x1000 = 180000 msec
        final long[] countNumber = {180000};
        // インターバル msec
        long interval = 10;

        Button startButton = findViewById(R.id.start_button);
        Button stopButton = findViewById(R.id.stop_button);
        Button resetButton = findViewById(R.id.reset_button);

        timerText = findViewById(R.id.timer);
        timerText.setText(dataFormat.format(countNumber[0]));


        NumberPicker minute = findViewById(R.id.minute);
        NumberPicker second = findViewById(R.id.second);


        minute.setMaxValue(59);
        minute.setMinValue(0);
        minute.setValue(3);
        second.setMaxValue(59);
        second.setMinValue(0);
        minute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                countNumber[0] = (long) (minute.getValue() * 60 * 1000) + (second.getValue() * 1000);
            }
        });
        second.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                countNumber[0] = (long) (minute.getValue() * 60 * 1000) + (second.getValue() * 1000);
            }
        });

        // インスタンス生成
        // CountDownTimer(long millisInFuture, long countDownInterval)
        countDown = new CountDown(countNumber[0], interval);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 開始
                //timerText.setText(dataFormat.format(countNumber[0]));
                countDown.start();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 中止
                countDown.cancel();
                countNumber[0] = stoptime;
                countDown = new CountDown(countNumber[0],interval);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDown.cancel();
                countNumber[0] = (long) (minute.getValue() * 60 * 1000) + (second.getValue() * 1000);
                timerText.setText(dataFormat.format(countNumber[0]));
                countDown = new CountDown(countNumber[0],interval);
            }
        });

    }

    class CountDown extends CountDownTimer {

        CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 完了
            timerText.setText(dataFormat.format(0));
        }

        // インターバルで呼ばれる
        @Override
        public void onTick(long millisUntilFinished) {
            timerText.setText(dataFormat.format(millisUntilFinished));
            stoptime = millisUntilFinished;
        }
    }


}

