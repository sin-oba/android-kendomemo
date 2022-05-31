package jp.ooba.kendomemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class Filedelete extends DialogFragment {
    private DatabaseHelper helper = null;
    private Bunsekidelete bl;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String hogeString = getArguments().getString("num");
        int hogeint = getArguments().getInt("id");
        Integer n = hogeint;
        String[] params = {n.toString()};
        bl = new Bunsekidelete();
        helper = new DatabaseHelper(getActivity());
        return builder.setTitle("ファイルを削除する").setMessage("このファイルを削除しますか？")
                .setIcon(R.mipmap.ic_launcher02)

                //「はい」ボタンの設定
                .setPositiveButton("はい",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    if(hogeString.equals("kiroku")) {
                                        //分析からデータ削除
                                        bl.Bdelete(hogeint,getActivity());

                                        try(SQLiteDatabase db = helper.getReadableDatabase()) {
                                            db.delete("kiroku", "num = ?", params);
                                        }
                                        Log.d("LIFE","sakujo");
                                        Intent i = new Intent(getActivity(), FolderActivity.class);
                                        Toast.makeText(getActivity(), "ファイルの削除に成功しました。", Toast.LENGTH_SHORT).show();
                                        startActivity(i);
                                    } else if(hogeString.equals("kojin")) {
                                        //分析からデータ削除
                                        bl.Kdelete(hogeint,getActivity());

                                        try(SQLiteDatabase db = helper.getReadableDatabase()) {
                                            db.delete("kojin", "num = ?",params );
                                        }
                                        Intent i = new Intent(getActivity(), KojinFoldrActivity.class);
                                        Toast.makeText(getActivity(), "ファイルの削除に成功しました。", Toast.LENGTH_SHORT).show();
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
