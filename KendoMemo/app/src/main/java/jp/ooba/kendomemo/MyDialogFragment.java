package jp.ooba.kendomemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import jp.ooba.kendomemo.Bunsekidelete;
import jp.ooba.kendomemo.DatabaseHelper;
import jp.ooba.kendomemo.FolderActivity;
import jp.ooba.kendomemo.KojinFoldrActivity;

public class MyDialogFragment extends DialogFragment {
    private DatabaseHelper helper = null;
    private Bunsekidelete bl;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String hogeString = getArguments().getString("date");
        int hogeint = getArguments().getInt("num");
        String[] params = {hogeString};
        bl = new Bunsekidelete();
        helper = new DatabaseHelper(getActivity());
        return builder.setTitle("フォルダーを削除する").setMessage("フォルダー内のファイルが全て削除されます。削除しますか？")
                .setIcon(R.mipmap.ic_launcher02)

                //「はい」ボタンの設定
                .setPositiveButton("はい",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (hogeint == 1) {
                                    //日付がdateのファイルを開く
                                    try(SQLiteDatabase db = helper.getReadableDatabase();
                                        Cursor cs = db.query("kiroku",null,"date=?",params,null,null,null)) {
                                            if(cs.moveToFirst()) {
                                                do{
                                                    bl.Bdelete(cs.getInt(0),getActivity());
                                                }while(cs.moveToNext());
                                            }
                                    }
                                    try(SQLiteDatabase db = helper.getReadableDatabase()) {
                                        db.delete("kiroku", "date = ?", params);
                                    }
                                    Toast.makeText(getActivity(), "フォルダーの削除に成功しました。", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getActivity(), FolderActivity.class);
                                    startActivity(i);
                                }
                                if (hogeint == 2) {
                                    try(SQLiteDatabase db = helper.getReadableDatabase();
                                        Cursor cs = db.query("kojin",null,"date=?",params,null,null,null)) {
                                        if(cs.moveToFirst()) {
                                            do{
                                                bl.Kdelete(cs.getInt(0),getActivity());
                                            }while(cs.moveToNext());
                                        }
                                    }
                                    try(SQLiteDatabase db = helper.getReadableDatabase()) {
                                        db.delete("kojin", "date = ?", params);
                                    }
                                    Toast.makeText(getActivity(),"フォルダーの削除に成功しました。",Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getActivity(), KojinFoldrActivity.class);
                                    startActivity(i);
                                }
                            }
                        })

                //｛いいえ」ボタンの設定
                .setNegativeButton("いいえ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .create();
    }
}
