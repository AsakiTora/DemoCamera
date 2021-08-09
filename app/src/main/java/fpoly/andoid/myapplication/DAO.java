package fpoly.andoid.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DAO {
    private SQLiteDatabase database;
    private Database mDatabase;

    public DAO(Context context){
        mDatabase = new Database(context);
    }

    public void open(){
        database = mDatabase.getWritableDatabase();
    }

    public void close(){
        mDatabase.close();
    }

    public long Add(Object object){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Object.COL_IMG, object.getImg());
        long res = database.insert(Object.TB_NAME, null, contentValues);
        return res;
    }

    public ArrayList<Object> GetAll(){
        ArrayList<Object> list = new ArrayList<>();
        String[] allCol = new String[]{Object.COL_ID, Object.COL_IMG};
        Cursor cursor = database.query(Object.TB_NAME, allCol, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int id = cursor.getInt(0);
            byte[] image = cursor.getBlob(1);
            Object object = new Object();
            object.setId(id);
            object.setImg(image);
            list.add(object);
            cursor.moveToNext();
        }
        return list;
    }
}
