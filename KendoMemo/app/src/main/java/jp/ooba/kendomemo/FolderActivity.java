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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class FolderActivity extends AppCompatActivity {
    private DatabaseHelper helper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("LIFE","folderonCreate");
        super.onCreate(savedInstanceState);

    }

    protected void onResume() {
        Log.d("LIFE","folderonResume");
        super.onResume();
        setTitle("団体戦フォルダー一覧");
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
            Cursor cs = db.query("kiroku",null,null,null,null,null,null)) {
            if(cs.moveToFirst()){
                num = cs.getInt(0);
                while (cs.moveToNext()) {
                    num = cs.getInt(0) + 1;
                }
            }else {
                num = 1;
            }
        }

        TextView tvv = new TextView(this);
        Integer nums = num;
        tvv.setText(nums.toString());
        Log.d("LIFE",nums.toString());

        String[] named = new String[num + 1];
        int i = 1;
        //日付ごとに表示

        try(SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cs = db.query("kiroku",null,null,null,"date",null,null)) {
            if(cs.moveToFirst()) {
                do {
                    TableRow tr = new TableRow(FolderActivity.this);
                    tr.setGravity(Gravity.CENTER);
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));
                    for(int j=0;j<3;j++) {
                        Button button = new Button(this);
                        button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.folderselect, 0, 0);
                        button.setText(cs.getString(2));
                        button.setId(cs.getInt(0));
                        named[cs.getInt(0)] = cs.getString(2);
                        nums = cs.getInt(0);
                        Log.d("LIFE", nums.toString());
                        Log.d("LIFE", cs.getString(2));


                        //ボタンデザイン設定
                        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
                        Display disp = wm.getDefaultDisplay();
                        TableRow.LayoutParams buttonLayoutParams = new TableRow.LayoutParams(disp.getWidth()/4, disp.getHeight()/5);
                        button.setLayoutParams(buttonLayoutParams);
                        tr.addView(button);

                        //ボタンクリック時の設定
                        button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                //FileviewActivityに移動(日付を渡す)
                                int date = view.getId();
                                Intent i = new Intent(FolderActivity.this, FileviewActivity.class);
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
                                args.putInt("num",1);
                                args.putString("date",named[date]);
                                dialog.setArguments(args);
                                dialog.show(getSupportFragmentManager(),"dialog_button");
                                return true;

                            }
                        });

                        if(cs.moveToNext() == false) {
                            i = 0;
                            break;
                        }
                    }
                    tbl.addView(tr);
                } while (i == 1);
            }else {
                Toast.makeText(FolderActivity.this,"データがありません。",Toast.LENGTH_SHORT).show();
            }

        }

    }

    protected void onPause() {
        Log.d("LIFE","folderonPause");
        super.onPause();
    }
    protected void onStop() {
        Log.d("LIFE","folderonStop");
        super.onStop();
    }
    protected void onDestroy() {
        Log.d("LIFE","folderonDestroy");
        super.onDestroy();
    }
}
