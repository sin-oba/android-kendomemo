package jp.ooba.kendomemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class KojinFileActivity extends AppCompatActivity {
    private DatabaseHelper helper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("個人戦ファイル一覧");

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
        //日付がdateのデータを表示
        try(SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cs = db.query("kojin",null,"date = ?",date,null,null,null ,null)) {
            if(cs.moveToFirst()) {
                int n = cs.getCount();
                do {
                    TableRow tr = new TableRow(this);
                    tr.setGravity(Gravity.CENTER);
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));
                    for(int j = 0;j < 3;j++) {
                        Button button = new Button(this);
                        button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.file);
                        button.setText(cs.getString(1));
                        button.setId(cs.getInt(0));

                        //ボタンデザイン設定
                        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
                        Display disp = wm.getDefaultDisplay();
                        TableRow.LayoutParams buttonLayoutParams = new TableRow.LayoutParams(disp.getWidth()/4, disp.getHeight()/5);
                        button.setLayoutParams(buttonLayoutParams);

                        //ボタンクリック時の処理
                        button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                //EturansampleAcitivityに移動(idを渡す)
                                int id = view.getId();
                                Intent i = new Intent(KojinFileActivity.this, Kojineturan.class);
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
                                args.putString("num","kojin");
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
