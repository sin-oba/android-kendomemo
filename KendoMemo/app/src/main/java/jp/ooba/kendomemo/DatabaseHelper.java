package jp.ooba.kendomemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    static final private int VERSION = 14; //データベースのバージョン
    static final private String DBNAME = "KENDO.db"; //データベース名
    static final private String _ID = "_id";
    //private static final String SQL_CREATE_ENTRIES = "CREATE TABLE" + "kiroku" + "(" + _ID + "INTEGER PRIMARY KEY," +


    //コンストラクタ
    DatabaseHelper(Context context) {
        super(context,DBNAME,null,VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
    //データベース作成
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE kiroku (" +
                "num INTEGER PRIMARY KEY," +
                "name TEXT," +
                "date TEXT," +
                "teamA TEXT," +
                "teamB TEXT," +
                "plrA TEXT," +
                "plrB TEXT," +
                "plrC TEXT," +
                "plrD TEXT," +
                "plrE TEXT," +
                "plrF TEXT," +
                "plrG TEXT," +
                "plrH TEXT," +
                "plrI TEXT," +
                "plrJ TEXT," +
                "plrK TEXT," +
                "plrL TEXT," +
                "dtA1 TEXT,dtA2 TEXT," +
                "dtB1 TEXT,dtB2 TEXT," +
                "dtC1 TEXT,dtC2 TEXT," +
                "dtD1 TEXT,dtD2 TEXT, " +
                "dtE1 TEXT,dtE2 TEXT," +
                "dtF1 TEXT,dtF2 TEXT," +
                "dtG1 TEXT,dtG2 TEXT," +
                "dtH1 TEXT,dtH2 TEXT," +
                "dtI1 TEXT,dtI2 TEXT," +
                "dtJ1 TEXT,dtJ2 TEXT," +
                "dtK1 TEXT,dtK2 TEXT, " +
                "dtL1 TEXT,dtL2 TEXT," +
                "hanA1 INT,hanA2 INT," +
                "hanB1 INT,hanB2 INT," +
                "hanC1 INT,hanC2 INT," +
                "hanD1 INT,hanD2 INT," +
                "hanE1 INT,hanE2 INT," +
                "hanF1 INT,hanF2 INT," +
                "hanG1 INT,hanG2 INT," +
                "hanH1 INT,hanH2 INT," +
                "hanI1 INT,hanI2 INT," +
                "hanJ1 INT,hanJ2 INT," +
                "hanK1 INT,hanK2 INT," +
                "hanL1 INT,hanL2 INT," +
                "memo TEXT)");

        db.execSQL("CREATE TABLE kojin (" +
                "num INTEGER PRIMARY KEY," +
                "name TEXT," +
                "date TEXT," +
                "teamA TEXT," +
                "teamB TEXT," +
                "plrA TEXT," +
                "plrB TEXT," +
                "dtA1 TEXT,dtA2 TEXT," +
                "dtB1 TEXT,dtB2 TEXT," +
                "hanA1 INT,hanA2 INT," +
                "hanB1 INT,hanB2 INT," +
                "memo TEXT)");

        db.execSQL("CREATE TABLE bunseki("+
                "num INTEGER PRIMARY KEY,"+
                "name TEXT,"+
                "team TEXT,"+
                "gamen INT,"+
                "win INT,"+
                "lose INT,"+
                "draw INT,"+
                "getmen INT,"+
                "getkote INT,"+
                "getdou INT,"+
                "gettuki INT,"+
                "gethan INT,"+
                "husen INT,"+
                "rmen INT,"+
                "rkote INT,"+
                "rdou INT,"+
                "rtuki INT,"+
                "rhan INT)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS kiroku");
        db.execSQL("DROP TABLE IF EXISTS kojin");
        db.execSQL("DROP TABLE IF EXISTS bunseki");
        onCreate(db);
    }
}

