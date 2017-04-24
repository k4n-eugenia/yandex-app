package k4n.eugenia.yandexapp.data.models;

import android.content.ContentValues;
import android.database.Cursor;

import k4n.eugenia.yandexapp.data.HistorySQLiteHelper;

/**
 * Created by Eugenia Kan on 24.04.2017.
 */

public class Translate {
    private int id;
    private String text;
    private String translate;
    private String direction;
    private boolean isFavourite;

    public Translate() {
        // Пустой конструктор
    }

    /**
     * Коеструктор со всеми полями
     * @param text
     * @param translate
     * @param direction
     * @param isFavourite
     */
    public Translate(String text, String translate, String direction, boolean isFavourite) {
        this.text = text;
        this.translate = translate;
        this.direction = direction;
        this.isFavourite = isFavourite;
    }

    public Translate(int id, String text, String translate, String direction, boolean isFavourite) {
        this.text = text;
        this.translate = translate;
        this.direction = direction;
        this.isFavourite = isFavourite;
    }

    /**
     * Конструктор с курсором
     * @param cursor
     */
    public Translate(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndex(HistorySQLiteHelper.History._ID));
        text = cursor.getString(cursor.getColumnIndex(HistorySQLiteHelper.History.COLUMN_TEXT));
        translate = cursor.getString(cursor.getColumnIndex(HistorySQLiteHelper.History.COLUMN_TRANSLATE));
        direction = cursor.getString(cursor.getColumnIndex(HistorySQLiteHelper.History.COLUMN_DIRECTION));
        isFavourite = cursor.getInt(cursor.getColumnIndex(HistorySQLiteHelper.History.COLUMN_IS_FAVOURITE)) == 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(HistorySQLiteHelper.History.COLUMN_TEXT, text);
        cv.put(HistorySQLiteHelper.History.COLUMN_TRANSLATE, translate);
        cv.put(HistorySQLiteHelper.History.COLUMN_DIRECTION, direction);
        cv.put(HistorySQLiteHelper.History.COLUMN_IS_FAVOURITE, isFavourite);
        return cv;
    }
}
