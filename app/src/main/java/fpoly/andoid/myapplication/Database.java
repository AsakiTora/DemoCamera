package fpoly.andoid.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public static final String DB_NAME = "QLCT";
    public static final int VERSION = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String object = "CREATE TABLE object (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, img BLOD NOT NULL)";
        db.execSQL(object);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
