package jp.ooba.kendomemo;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class Kojineturan extends AppCompatActivity {
    private DatabaseHelper helper = null;
    private String name = new String();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kojin_eturan);
        setTitle("個人戦閲覧");

        //ヘルパーを準備
        helper = new DatabaseHelper(this);

        TextView hA = (TextView)findViewById(R.id.nhonA);
        TextView hB = (TextView)findViewById(R.id.nhonB);
        TextView sA = (TextView)findViewById(R.id.nsyouA);
        TextView sB = (TextView)findViewById(R.id.nsyouB);
        final EditText teamA = (EditText)findViewById(R.id.txtTeamA);
        final EditText teamB = (EditText)findViewById(R.id.txtTeamB);
        final EditText mem = (EditText)findViewById(R.id.MEMO);
        ArrayList<Spinner> sp = new ArrayList<>();
        sp.add(findViewById(R.id.spA1));
        sp.add(findViewById(R.id.spA2));
        sp.add(findViewById(R.id.spB1));
        sp.add(findViewById(R.id.spB2));
        EditText plA = (EditText)findViewById(R.id.playerA);
        EditText plB = (EditText)findViewById(R.id.playerB);
        ArrayList<ToggleButton> hansoku = new ArrayList<>();
        hansoku.add(findViewById(R.id.hanA1));
        hansoku.add(findViewById(R.id.hanA2));
        hansoku.add(findViewById(R.id.hanB1));
        hansoku.add(findViewById(R.id.hanB2));

        // インテントを取得
        Intent intent = Kojineturan.this.getIntent();
        // インテントに保存されたデータを取得
        int dat = intent.getIntExtra("filenum",0);
        Integer ig = dat;
        String data[] = {ig.toString()};

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
                        Kojineturan.this,
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


        int sn = 0; //スピナーの番号
        int k = 0; //列の番号

        //更新前のデータ
        String[] pl = new String[2]; //選手名
        String[] tm = new String[2];  //チーム名
        int[] kk = new int[2]; //結果０勝利１敗北２引き分け
        int[] th = new int[4]; //取得した部位//0なし１面２小手３胴４突き５反則６不戦勝
        int[] rh = new int[4]; //取られた部位０なし１面2小手3胴４突き５反則６不戦勝
        //データベースを読み込みで開く
        try(SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cs = db.query("kojin",null,"num = ?",data,null,null,null)) {
            if (cs.moveToFirst()) {
                name = cs.getString(1);
                showDate.setText(cs.getString(2));
                teamA.setText(cs.getString(3));
                tm[0] = cs.getString(3);
                teamB.setText(cs.getString(4));
                tm[1] = cs.getString(4);
                plA.setText(cs.getString(5));
                pl[0] = cs.getString(5);
                plB.setText(cs.getString(6));
                pl[1] = cs.getString(6);
                k = 6;
                for (int i = 0; i < 4; i++) {
                    k++;
                    if(i < 2) {
                        switch (cs.getString(k)) {
                            case "":
                                sn = 0;
                                th[i] = 0;
                                rh[i + 2] = 0;
                                break;
                            case "メ":
                                sn = 1;
                                th[i] = 1;
                                rh[i + 2] = 1;
                                break;
                            case "コ":
                                sn = 2;
                                th[i] = 2;
                                rh[i + 2] = 2;
                                break;
                            case "ド":
                                sn = 3;
                                th[i] = 3;
                                rh[i + 2] = 3;
                                break;
                            case "ツ":
                                sn = 4;
                                th[i] = 4;
                                rh[i + 2] = 4;
                                break;
                            case "反":
                                sn = 5;
                                th[i] = 5;
                                rh[i + 2] = 5;
                                break;
                            case "不":
                                sn = 6;
                                th[i] = 6;
                                rh[i + 2] = 6;
                                break;
                            case "(メ)":
                                sn = 7;
                                th[i] = 1;
                                rh[i+2] = 1;
                                break;
                            case "(コ)":
                                sn = 8;
                                th[i] = 2;
                                rh[i+2] = 2;
                                break;
                            case "(ド)":
                                sn = 9;
                                th[i] = 3;
                                rh[i+2] = 3;
                                break;
                            case "(ツ)":
                                sn = 10;
                                th[i] = 4;
                                rh[i+2] = 4;
                                break;
                            case "(反)":
                                sn = 11;
                                th[i] = 5;
                                rh[i+2] = 5;
                                break;
                            default:
                                sn = 0;
                                th[i] = 0;
                                rh[i + 2] = 0;
                                break;
                        }
                    }else{
                        switch (cs.getString(k)) {
                            case "":
                                sn = 0;
                                th[i] = 0;
                                rh[i - 2] = 0;
                                break;
                            case "メ":
                                sn = 1;
                                th[i] = 1;
                                rh[i - 2] = 1;
                                break;
                            case "コ":
                                sn = 2;
                                th[i] = 2;
                                rh[i - 2] = 2;
                                break;
                            case "ド":
                                sn = 3;
                                th[i] = 3;
                                rh[i - 2] = 3;
                                break;
                            case "ツ":
                                sn = 4;
                                th[i] = 4;
                                rh[i - 2] = 4;
                                break;
                            case "反":
                                sn = 5;
                                th[i] = 5;
                                rh[i - 2] = 5;
                                break;
                            case "不":
                                sn = 6;
                                th[i] = 6;
                                rh[i - 2] = 6;
                                break;
                            case "(メ)":
                                sn = 7;
                                th[i] = 1;
                                rh[i-2] = 1;
                                break;
                            case "(コ)":
                                sn = 8;
                                th[i] = 2;
                                rh[i-2] = 2;
                                break;
                            case "(ド)":
                                sn = 9;
                                th[i] = 3;
                                rh[i-2] = 3;
                                break;
                            case "(ツ)":
                                sn = 10;
                                th[i] = 4;
                                rh[i-2] = 4;
                                break;
                            case "(反)":
                                sn = 11;
                                th[i] = 5;
                                rh[i-2] = 5;
                                break;
                            default:
                                sn = 7;
                                th[i] = 0;
                                rh[i - 2] = 0;
                                break;
                        }
                    }
                    sp.get(i).setSelection(sn);

                }
                for (int i = 0; i < 4; i++) {
                    k++;
                    if (cs.getInt(k) == 1) {
                        hansoku.get(i).setChecked(true);
                    } else if (cs.getInt(k) == 0) {
                        hansoku.get(i).setChecked(false);
                    }
                }
                k++;
                mem.setText(cs.getString(k));
                int at = 0;
                int bt = 0;
                //個人の勝敗判定

                at = 0;
                bt = 0;
                if(!cs.getString(7).equals("")) {
                    at++;
                }
                if(!cs.getString(8).equals("")) {
                    at++;
                }
                if(!cs.getString(9).equals("")){
                    bt++;
                }
                if(!cs.getString(10).equals("")){
                    bt++;
                }
                if(at > bt) {
                    kk[0] = 0;
                    kk[1] = 1;
                }else if(bt > at) {
                    kk[0] = 1;
                    kk[1] = 0;
                }else{
                    kk[0] = 2;
                    kk[1]=2;
                }

            } else {
                Toast.makeText(this, "データがありません。", Toast.LENGTH_SHORT).show();
            }
        }

        //更新


        Button bt = (Button) findViewById(R.id.kousin);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = new EditText(Kojineturan.this);
                editText.setText(name);
                new AlertDialog.Builder(Kojineturan.this)
                        .setTitle("名前をつける")
                        .setMessage("ファイル名を入力してください")
                        .setView(editText)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        name = editText.getText().toString();
                                        if(name.equals("")) {
                                            String name = plA.getText().toString() + "対" + plB.getText().toString();
                                        }
                                        int[] hs = new int[4];
                                        for (int i = 0; i < 4; i++) {
                                            if (hansoku.get(i).isChecked()) {
                                                hs[i] = 1;
                                            } else {
                                                hs[i] = 0;
                                            }
                                        }


                                        //更新前のデータ削除
                                        int[] sakujo = new int[15];
                                        int a = 0;
                                        for(int i = 0;i<2;i++) {
                                            if(!pl[i].equals("")) {
                                                if(i == 0) {
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
                                                    if (i == 0) {
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

                                        try (SQLiteDatabase db = helper.getWritableDatabase()) {
                                            ContentValues cv = new ContentValues();

                                            cv.put("name", name);
                                            cv.put("date", showDate.getText().toString());
                                            cv.put("teamA", teamA.getText().toString());
                                            cv.put("teamB", teamB.getText().toString());
                                            cv.put("plrA", plA.getText().toString());
                                            cv.put("plrB", plB.getText().toString());
                                            cv.put("dtA1", sp.get(0).getSelectedItem().toString());
                                            cv.put("dtA2", sp.get(1).getSelectedItem().toString());
                                            cv.put("dtB1", sp.get(2).getSelectedItem().toString());
                                            cv.put("dtB2", sp.get(3).getSelectedItem().toString());
                                            cv.put("hanA1", hs[0]);
                                            cv.put("hanA2", hs[1]);
                                            cv.put("hanB1", hs[2]);
                                            cv.put("hanB2", hs[3]);
                                            cv.put("memo", mem.getText().toString());

                                            db.update("kojin", cv, "num = ?", data);

                                            Toast.makeText(Kojineturan.this, "データを更新しました。", Toast.LENGTH_LONG).show();
                                        }

                                        //分析番号をつける
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

                                        int an = 0;
                                        int bn = 0;
                                        int[] kousin = new int[15];
                                        int[] kiroku = new int[15];
                                        int suru = 0;
                                        if(!plA.getText().toString().equals("")) {
                                            try (SQLiteDatabase db = helper.getReadableDatabase();
                                                 Cursor cs = db.query("bunseki", null, "name=? AND team=?", new String[]{plA.getText().toString(), teamA.getText().toString()}, null, null, null)) {
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
                                            try(SQLiteDatabase db = helper.getWritableDatabase()) {
                                                ContentValues cv = new ContentValues();
                                                kiroku[0] = 1;
                                                //勝敗判定
                                                for(int p=0;p<2;p++) {
                                                    if (!sp.get(p).getSelectedItem().toString().equals("")) {
                                                        an++;
                                                    }
                                                    if (!sp.get(p+2).getSelectedItem().toString().equals("")) {
                                                        bn++;
                                                    }
                                                    switch (sp.get(p).getSelectedItem().toString()) {
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
                                                    switch (sp.get(p+2).getSelectedItem().toString()) {
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
                                                }

                                                if(an>bn) {
                                                    kiroku[1] = 1;
                                                } else if(bn>an) {
                                                    kiroku[2] = 1;
                                                } else {
                                                    kiroku[3] = 1;
                                                }

                                                if(suru == 0) {
                                                    //データ追加
                                                    cv.put("num",k);
                                                    cv.put("name",plA.getText().toString());
                                                    cv.put("team",teamA.getText().toString());
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
                                                    db.update("bunseki",cv,"name = ? AND team = ?",new String[]{plA.getText().toString(),teamA.getText().toString()});
                                                }
                                            }
                                        }
                                        //選手B
                                        k++;
                                        suru = 0;
                                        for(int q = 0;q<15;q++) {
                                            kousin[q] = 0;
                                            kiroku[q] = 0;
                                        }
                                        if(!plB.getText().toString().equals("")) {
                                            try (SQLiteDatabase db = helper.getReadableDatabase();
                                                 Cursor cs = db.query("bunseki", null, "name=? AND team=?", new String[]{plB.getText().toString(), teamB.getText().toString()}, null, null, null)) {
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
                                            try(SQLiteDatabase db = helper.getWritableDatabase()) {
                                                ContentValues cv = new ContentValues();
                                                kiroku[0] = 1;
                                                //勝敗判定
                                                for(int p=2;p<4;p++) {
                                                    switch (sp.get(p).getSelectedItem().toString()) {
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
                                                    switch (sp.get(p-2).getSelectedItem().toString()) {
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
                                                }

                                                if(an>bn) {
                                                    kiroku[2] = 1;
                                                } else if(bn>an) {
                                                    kiroku[1] = 1;
                                                } else {
                                                    kiroku[3] = 1;
                                                }

                                                if(suru == 0) {
                                                    //データ追加
                                                    cv.put("num",k);
                                                    cv.put("name",plB.getText().toString());
                                                    cv.put("team",teamB.getText().toString());
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
                                                    db.update("bunseki",cv,"name = ? AND team = ?",new String[]{plB.getText().toString(),teamB.getText().toString()});
                                                }
                                            }
                                        }

                                        Intent i = new Intent(Kojineturan.this, MainActivity.class);
                                        startActivity(i);
                                    }
                                }

                        )
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
                int an = 0;
                int bn = 0;
                int as = 0;
                int bs = 0;

                ArrayList<Spinner> spt = new ArrayList<>();
                spt.add(findViewById(R.id.spA1));
                spt.add(findViewById(R.id.spA2));
                spt.add(findViewById(R.id.spB1));
                spt.add(findViewById(R.id.spB2));

                if(!spt.get(0).getSelectedItem().toString().equals("")) {
                    an++;
                }
                if(!spt.get(1).getSelectedItem().toString().equals("")) {
                    an++;
                }
                if(!spt.get(2).getSelectedItem().toString().equals("")){
                    bn++;
                }
                if(!spt.get(3).getSelectedItem().toString().equals("")) {
                    bn++;
                }
                if(an > bn) {
                    as = 1;
                }else if(bn > an) {
                    bs = 1;
                }

                Integer ant = an;
                Integer bnt = bn;
                Integer ast = as;
                Integer bst = bs;
                hA.setText(ant.toString());
                hB.setText(bnt.toString());
                sA.setText(ast.toString());
                sB.setText(bst.toString());
            }
        };
        timer.schedule(task,0,1000);
    }
}
