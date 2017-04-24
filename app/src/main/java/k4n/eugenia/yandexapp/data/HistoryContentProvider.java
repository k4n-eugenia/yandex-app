package k4n.eugenia.yandexapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class HistoryContentProvider extends ContentProvider {
    static final String AUTHORITY = "k4n.eugenia.yandexapp.data.provider";
    public static final String URL = "content://" + AUTHORITY + "/history";
    static final Uri CONTENT_URI = Uri.parse(URL);

    private static HashMap<String, String> HISTORY_PROJECTION_MAP;

    static final int HISTORY = 1;
    static final int HISTORY_ID = 2;

    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "history", HISTORY);
        uriMatcher.addURI(AUTHORITY, "history/#", HISTORY_ID);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        HistorySQLiteHelper dbHelper = new HistorySQLiteHelper(context);

        /**
         * Create a write table database which will trigger its
         * creation if it doesn't already exist.
         */

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    public HistoryContentProvider() {
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Добавить новую запись в историю переводов
         */
        long rowID = db.insert(	HistorySQLiteHelper.History.TABLE_NAME, "", values);

        /**
         * Если вставка прошла успешно
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setTables(HistorySQLiteHelper.History.TABLE_NAME);

            switch (uriMatcher.match(uri)) {
                case HISTORY:
                    qb.setProjectionMap(HISTORY_PROJECTION_MAP);
                    break;

                case HISTORY_ID:
                    qb.appendWhere( HistorySQLiteHelper.History._ID + "=" + uri.getPathSegments().get(1));
                    break;

                default:
            }

            if (sortOrder == null || sortOrder == ""){
                /**
                 * По умолчанию сортируем по ID в порядке убывания
                 */
                sortOrder = HistorySQLiteHelper.History._ID + " DESC";
            }

            Cursor c = qb.query(db,	projection,	selection,
                    selectionArgs,null, null, sortOrder);
            /**
             * register to watch a content URI for changes
             */
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
}

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
            int count = 0;
            switch (uriMatcher.match(uri)) {
                case HISTORY:
                    count = db.update(HistorySQLiteHelper.History.TABLE_NAME, values, selection, selectionArgs);
                    break;

                case HISTORY_ID:
                    count = db.update(HistorySQLiteHelper.History.TABLE_NAME, values,
                            HistorySQLiteHelper.History._ID + " = " + uri.getPathSegments().get(1) +
                                    (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri );
            }

            getContext().getContentResolver().notifyChange(uri, null);
            return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case HISTORY:
                count = db.delete(HistorySQLiteHelper.History.TABLE_NAME, selection, selectionArgs);
                break;

            case HISTORY_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( HistorySQLiteHelper.History.TABLE_NAME, HistorySQLiteHelper.History._ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case HISTORY:
                return "vnd.android.cursor.dir/vnd.translate.history";
            case HISTORY_ID:
                return "vnd.android.cursor.item/vnd.translate.history";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
