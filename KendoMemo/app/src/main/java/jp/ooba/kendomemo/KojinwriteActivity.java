package jp.ooba.kendomemo;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class KojinwriteActivity extends AppCompatActivity {
    private DatabaseHelper helper = null;
    private TextView timerText;
    private String name = new String();
    private CountDown countDown;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("mm:ss.SSS", Locale.US);
    private long stoptime;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kojin_write);
        setTitle("個人戦記録");

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
                        KojinwriteActivity.this,
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
                EditText editText = new EditText(KojinwriteActivity.this);
                new AlertDialog.Builder(KojinwriteActivity.this)
                        .setTitle("名前をつける")
                        .setMessage("ファイル名を入力してください")
                        .setView(editText)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                name = editText.getText().toString();
                                String txtDay = showDate.getText().toString();
                                String txtTeamA = teamA.getText().toString();
                                String txtTeamB = teamB.getText().toString();
                                String txtMemo = mem.getText().toString();
                                if(name.equals("")) {
                                    name = plA.getText().toString() + "対" + plB.getText().toString();
                                }
                                int[] hs = new int[4];
                                //番号を付ける
                                int num = 1;
                                //番号を付ける
                                try (SQLiteDatabase db = helper.getReadableDatabase();
                                     Cursor cs = db.query("kojin", null, null, null, null, null, null)) {
                                    if (cs.moveToFirst()) {
                                        num = 1;
                                        while (cs.moveToNext()) {
                                            num = cs.getInt(0) + 1;
                                        }
                                    } else {
                                        num = 0;
                                    }
                                }

                                for (int i = 0; i < 4; i++) {
                                    if (hansoku.get(i).isChecked()) {
                                        hs[i] = 1;
                                    } else {
                                        hs[i] = 0;
                                    }
                                }

                                try (SQLiteDatabase db = helper.getWritableDatabase()) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("num", num);
                                    cv.put("name", name);
                                    cv.put("date", txtDay);
                                    cv.put("teamA", txtTeamA);
                                    cv.put("teamB", txtTeamB);
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
                                    cv.put("memo", txtMemo);
                                    db.insert("kojin", null, cv);
                                    Toast.makeText(KojinwriteActivity.this, "データを登録しました。", Toast.LENGTH_LONG).show();
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
                                //選手A
                                if(!plA.getText().toString().equals("")) {
                                    try (SQLiteDatabase db = helper.getReadableDatabase();
                                         Cursor cs = db.query("bunseki", null, "name=? AND team=?", new String[]{plA.getText().toString(), txtTeamA}, null, null, null)) {
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
                                            db.update("bunseki",cv,"name = ? AND team = ?",new String[]{plA.getText().toString(),txtTeamA});
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
                                                 Cursor cs = db.query("bunseki", null, "name=? AND team=?", new String[]{plB.getText().toString(), txtTeamB}, null, null, null)) {
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
                                                    db.update("bunseki",cv,"name = ? AND team = ?",new String[]{plB.getText().toString(),txtTeamB});
                                                }
                                            }
                                        }




                                Intent i = new Intent(KojinwriteActivity.this, MainActivity.class);
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
