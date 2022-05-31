package jp.ooba.kendomemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class FileviewActivity extends AppCompatActivity {
    private DatabaseHelper helper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("LIFE","fileonCreate");
        super.onCreate(savedInstanceState);
    }
    protected void onResume() {
        Log.d("LIFE","fileonResume");
        super.onResume();
        setTitle("団体戦ファイル一覧");

        //レイアウト作成
        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

        TableLayout tbl = new TableLayout(this);
        tbl.setGravity(Gravity.CENTER);
        tbl.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT));
        sv.addView(tbl);
        setContentView(sv);


        // FolderActivityから日付を取得
        Intent intent = getIntent();
        String day = intent.getStringExtra("date");
        String date[] = {day};

        //ヘルパーを準備
        helper = new DatabaseHelper(this);

        int i = 1;
        String[] cols = {"num","name","date","teamA","teamB","plrA","plrB","plrC","plrD","plrE","plrF","plrG","plrH","plrI","plrJ","plrK",
                "plrL","dtA1","dtA2","dtB1","dtB2","dtC1","dtC2","dtD1","dtD2","dtE1","dtE2","dtF1","dtF2","dtG1","dtG2","dtH1",
                "dtH2","dtI1","dtI2","dtJ1","dtJ2","dtK1","dtK2","dtL1","dtL2"};
        //日付がdateのデータを表示
        try(SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cs = db.query("kiroku",cols,"date = ?",date,null,null,null ,null)) {
            if(cs.moveToFirst()) {
                int n = cs.getCount();

                do {
                    TableRow tr = new TableRow(this);
                    tr.setGravity(Gravity.CENTER);
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));
                    for(int j = 0;j < 3;j++) {

                        Button button = new Button(this);
                        button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.file,0,0);
                        button.setText(cs.getString(1));
                        button.setId(cs.getInt(0));

                        //ボタンデザイン設定
                        float scale = getResources().getDisplayMetrics().density;
                        int buttonWidth = (int) (100 * scale);
                        //横幅150dp,縦幅100dpに設定

                        //buttonLayoutParams.setMargins(left,top,right,end);
                        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
                        Display disp = wm.getDefaultDisplay();
                        TableRow.LayoutParams buttonLayoutParams = new TableRow.LayoutParams(disp.getWidth()/4, disp.getHeight()/5);

                        button.setLayoutParams(buttonLayoutParams);

                        //ボタンクリック時の処理
                        button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                //EturansampleAcitivityに移動(idを渡す)
                                int id = view.getId();
                                Intent i = new Intent(FileviewActivity.this, EturansampleActivity.class);
                                i.putExtra("filenum", id);
                                startActivity(i);


                            }
                        });

                        button.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                int id = view.getId();
                                DialogFragment dialog = new Filedelete();
                                Bundle args = new Bundle();
                                args.putInt("id",id);
                                args.putString("num","kiroku");
                                dialog.setArguments(args);
                                dialog.show(getSupportFragmentManager(),"dialog_button");
                                return true;
                            }
                        });
                        tr.addView(button);
                        if(!cs.moveToNext()) {
                            i = 0;
                            break;
                        }
                    }
                    tbl.addView(tr);
                } while (i == 1);
            } else {
                Toast.makeText(this,"データがありません。",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
