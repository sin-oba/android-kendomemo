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

public class KojinFoldrActivity extends AppCompatActivity {
    private DatabaseHelper helper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("個人戦フォルダー一覧");
        //レイアウト作成
        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

        TableLayout tbl = new TableLayout(this);
        tbl.setGravity(Gravity.CENTER);
        tbl.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT));
        sv.addView(tbl);
        setContentView(sv);

        //ヘルパーを準備
        helper = new DatabaseHelper(this);

        //データ数を確認
        int num = 1;
        try(SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cs = db.query("kojin",null,null,null,null,null,null)) {
            if(cs.moveToFirst()){
                num = cs.getInt(0);
                while (cs.moveToNext()) {
                    num = cs.getInt(0) + 1;
                }
            }else {
                num = 1;
            }
        }
        String[] named = new String[num + 1];


        int i = 1;
        //日付ごとに表示
        try(SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cs = db.query("kojin",null,null,null,"date",null,null)) {
            if(cs.moveToFirst()) {
                int n = cs.getCount();
                do {
                    TableRow tr = new TableRow(this);
                    tr.setGravity(Gravity.CENTER);
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));
                    for (int j = 0;j < 3;j++) {
                        Button button = new Button(this);
                        button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.folderselect, 0, 0);
                        button.setText(cs.getString(2));
                        button.setId(cs.getInt(0));
                        named[cs.getInt(0)] = cs.getString(2);

                        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
                        Display disp = wm.getDefaultDisplay();
                        TableRow.LayoutParams buttonLayoutParams = new TableRow.LayoutParams(disp.getWidth()/4, disp.getHeight()/5);
                        //横幅150dp,縦幅100dpに設定

                        button.setLayoutParams(buttonLayoutParams);

                        //ボタンクリック時の設定
                        button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                //FileviewActivityに移動(日付を渡す)
                                int date = view.getId();
                                Intent i = new Intent(KojinFoldrActivity.this, KojinFileActivity.class);
                                i.putExtra("date", named[date]);
                                startActivity(i);
                            }
                        });

                        button.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                int date = view.getId();
                                DialogFragment dialog = new MyDialogFragment();
                                Bundle args = new Bundle();
                                args.putInt("num",2);
                                args.putString("date",named[date]);
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
                }while (i == 1);
            } else {
                Toast.makeText(this,"データがありません。",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
