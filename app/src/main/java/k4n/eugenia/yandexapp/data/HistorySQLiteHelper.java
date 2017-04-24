package k4n.eugenia.yandexapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


/**
 * Created by Eugenia Kan on 19.04.2017.
 */

public class HistorySQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "history.db";
    public static final int DATABASE_VERSION = 1;

    public interface History extends BaseColumns {
        String TABLE_NAME = "history";

        String COLUMN_TEXT = "text";
        String COLUMN_TRANSLATE = "translate";
        String COLUMN_DIRECTION = "direction";
        String COLUMN_IS_FAVOURITE = "is_favourite";

        String CREATE_TABLE =
                " CREATE TABLE " + TABLE_NAME +
                        " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TEXT + " TEXT NOT NULL, " +
                        COLUMN_TRANSLATE + " TEXT NOT NULL, " +
                        COLUMN_DIRECTION + " TEXT NOT NULL, " +
                        COLUMN_IS_FAVOURITE + " INTEGER NOT NULL DEFAULT 0);";

    }


    public HistorySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(History.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +  History.TABLE_NAME);
        onCreate(db);
    }
}
