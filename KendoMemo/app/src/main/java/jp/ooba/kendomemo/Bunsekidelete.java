package jp.ooba.kendomemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

class Bunsekidelete extends AppCompatActivity {
    Bunsekidelete() {}
    private DatabaseHelper helper = null;

    void Bdelete (int num, Context context){
        String[] pl = new String[12]; //選手名
        String[] tm = new String[2];  //チーム名
        int[] kk = new int[12]; //結果０勝利１敗北２引き分け
        int[] th = new int[24]; //取得した部位//0なし１面２小手３胴４突き５反則６不戦勝
        int[] rh = new int[24]; //取られた部位０なし１面2小手3胴４突き５反則６不戦勝

        helper = new DatabaseHelper(context);

        Integer nums = num;
        String[] params = {nums.toString()};

        Log.d("LIFE","数字"+params[0]);
        try(SQLiteDatabase dbt = helper.getReadableDatabase();
            Cursor cs = dbt.query("kiroku",null,"num = ?",params,null,null,null)) {
            if (cs.moveToFirst()) {
                Log.d("LIFE","Database");
                int k;
                tm[0] = cs.getString(3);
                tm[1] = cs.getString(4);
                k = 4;
                for (int j = 0; j < 12; j++) {
                    k++;
                    pl[j] = cs.getString(k);
                }
                for (int i = 0; i < 24; i++) {
                    k++;
                    if(i < 12) {
                        switch (cs.getString(k)) {
                            case "":
                                th[i] = 0;
                                rh[i + 12] = 0;
                                break;
                            case "メ":
                                th[i] = 1;
                                rh[i + 12] = 1;
                                break;
                            case "コ":
                                th[i] = 2;
                                rh[i+12] = 2;
                                break;
                            case "ド":
                                th[i] = 3;
                                rh[i+12] = 3;
                                break;
                            case "ツ":
                                th[i] = 4;
                                rh[i+12] = 4;
                                break;
                            case "反":
                                th[i] = 5;
                                rh[i+12] = 5;
                                break;
                            case "不":
                                th[i] = 6;
                                rh[i+12] = 6;
                                break;
                            default:
                                th[i] = 0;
                                rh[i+12] = 0;
                                break;
                        }
                    }
                    if(12 < i) {
                        switch (cs.getString(k)) {
                            case "":
                                th[i] = 0;
                                rh[i - 12] = 0;
                                break;
                            case "メ":
                                th[i] = 1;
                                rh[i - 12] = 1;
                                break;
                            case "コ":
                                th[i] = 2;
                                rh[i-12] = 2;
                                break;
                            case "ド":
                                th[i] = 3;
                                rh[i-12] = 3;
                                break;
                            case "ツ":
                                th[i] = 4;
                                rh[i-12] = 4;
                                break;
                            case "反":
                                th[i] = 5;
                                rh[i-12] = 5;
                                break;
                            case "不":
                                th[i] = 6;
                                rh[i-12] = 6;
                                break;
                            default:
                                th[i] = 0;
                                rh[i-12] = 0;
                                break;
                        }
                    }
                }

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
            }
        }


        Log.d("LIFE",tm[0]);
        Log.d("LIFE",tm[1]);
        //bunsekiから更新前のデータを削除する

        int[] sakujo = new int[15];
        int a = 0;
        for(int i = 0;i<12;i++) {
            if(!pl[i].equals("")) {
                if(i < 6) {
                    try (SQLiteDatabase dba = helper.getReadableDatabase();
                         Cursor cs = dba.query("bunseki", null, "name=? AND team=?", new String[]{pl[i], tm[0]}, null, null, null)) {
                        if (cs.moveToFirst()) {
                            //データあり
                            for (int l = 0; l < 15; l++) {
                                sakujo[l] = cs.getInt(l + 3);
                            }
                        }
                    }
                } else  {
                    try (SQLiteDatabase dba = helper.getReadableDatabase();
                         Cursor cs = dba.query("bunseki", null, "name=? AND team=?", new String[]{pl[i], tm[1]}, null, null, null)) {
                        if (cs.moveToFirst()) {
                            //データあり
                            for (int l = 0; l < 15; l++) {
                                sakujo[l] = cs.getInt(l + 3);
                            }
                        }
                    }
                }
                try(SQLiteDatabase dbb = helper.getWritableDatabase()) {
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
                        dbb.update("bunseki", cv, "name = ? AND team = ?", new String[]{pl[i], tm[0]});
                        if(sakujo[0] == 1) {
                            dbb.delete("bunseki", "name = ? AND team == ?", new String[]{pl[i], tm[0]});
                        }
                    } else {
                        dbb.update("bunseki", cv, "name = ? AND team = ?", new String[]{pl[i], tm[1]});
                        if(sakujo[0] == 1) {
                            dbb.delete("bunseki", "name = ? AND team == ?", new String[]{pl[i], tm[1]});
                        }
                    }
                }
            }else {
                a = a+2;
            }
        }
    }

    void Kdelete (int num,Context context) {
        //更新前のデータ
        String[] pl = new String[2]; //選手名
        String[] tm = new String[2];  //チーム名
        int[] kk = new int[2]; //結果０勝利１敗北２引き分け
        int[] th = new int[4]; //取得した部位//0なし１面２小手３胴４突き５反則６不戦勝
        int[] rh = new int[4]; //取られた部位０なし１面2小手3胴４突き５反則６不戦勝
        Integer nums = num;
        String[] data = {nums.toString()};
        helper = new DatabaseHelper(context);
        //データベースを読み込みで開く
        try(SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cs = db.query("kojin",null,"num = ?",data,null,null,null)) {
            if (cs.moveToFirst()) {
                tm[0] = cs.getString(3);
                tm[1] = cs.getString(4);
                pl[0] = cs.getString(5);
                pl[1] = cs.getString(6);
                int k = 6;
                for (int i = 0; i < 4; i++) {
                    k++;
                    if(i < 2) {
                        switch (cs.getString(k)) {
                            case "":
                                th[i] = 0;
                                rh[i + 2] = 0;
                                break;
                            case "メ":
                                th[i] = 1;
                                rh[i + 2] = 1;
                                break;
                            case "コ":
                                th[i] = 2;
                                rh[i + 2] = 2;
                                break;
                            case "ド":
                                th[i] = 3;
                                rh[i + 2] = 3;
                                break;
                            case "ツ":
                                th[i] = 4;
                                rh[i + 2] = 4;
                                break;
                            case "反":
                                th[i] = 5;
                                rh[i + 2] = 5;
                                break;
                            case "不":
                                th[i] = 6;
                                rh[i + 2] = 6;
                                break;
                            default:
                                th[i] = 0;
                                rh[i + 2] = 0;
                                break;
                        }
                    }else{
                        switch (cs.getString(k)) {
                            case "":
                                th[i] = 0;
                                rh[i - 2] = 0;
                                break;
                            case "メ":
                                th[i] = 1;
                                rh[i - 2] = 1;
                                break;
                            case "コ":
                                th[i] = 2;
                                rh[i - 2] = 2;
                                break;
                            case "ド":
                                th[i] = 3;
                                rh[i - 2] = 3;
                                break;
                            case "ツ":
                                th[i] = 4;
                                rh[i - 2] = 4;
                                break;
                            case "反":
                                th[i] = 5;
                                rh[i - 2] = 5;
                                break;
                            case "不":
                                th[i] = 6;
                                rh[i - 2] = 6;
                                break;
                            default:
                                th[i] = 0;
                                rh[i - 2] = 0;
                                break;
                        }
                    }

                }

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

    }



}
