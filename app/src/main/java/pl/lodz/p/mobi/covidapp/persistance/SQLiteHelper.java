package pl.lodz.p.mobi.covidapp.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

import pl.lodz.p.mobi.covidapp.instruction.Questionnaire;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "covidapp.db";
    // TABLE
    public static final String QUESTIONNAIRES_TABLE_NAME = "USER_QUESTIONNAIRES";
    public static final String FILTER_CONFIG_TABLE = "FILTER_CONFIG_TABLE";
    // COLUMNS
    public static final String QUESTIONNAIRE_CREATION_DATE_COLUMN = "CREATION_DATE";
    public static final String QUESTIONNAIRE_ID_COLUMN = "QUESTIONNAIRE_ID";
    public static final String QUESTIONNAIRE_COLUMN = "QUESTIONNAIRE";
    public static final String CONFIG_ID_COLUMN = "CONFIG_ID";
    public static final String CONFIG_META_KEY_COLUMN = "CONFIG_META_KEY";
    public static final String CONFIG_META_VALUE_COLUMN = "CONFIG_META_VALUE";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String questionnairesTableStatement = "CREATE TABLE "
                + QUESTIONNAIRES_TABLE_NAME
                + " (" + QUESTIONNAIRE_ID_COLUMN + " TEXT PRIMARY KEY NOT NULL,"
                + QUESTIONNAIRE_CREATION_DATE_COLUMN + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + QUESTIONNAIRE_COLUMN + " TEXT"
                + ")";

        String configTableStatement = "CREATE TABLE "
                + FILTER_CONFIG_TABLE
                + " (" + CONFIG_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + CONFIG_META_KEY_COLUMN + " TEXT NOT NULL,"
                + CONFIG_META_VALUE_COLUMN + " TEXT NOT NULL"
                + ")";

        db.execSQL(questionnairesTableStatement);
        db.execSQL(configTableStatement);

        addDefaultConfig(db, "DAYS_CONSIDERED", "7");
        addDefaultConfig(db,"CHART_TYPE", "PER_DAY");
        addDefaultConfig(db,"DATA_TYPE", "CONFIRMED");
    }

    private boolean addDefaultConfig(SQLiteDatabase db, String key, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONFIG_META_KEY_COLUMN, key);
        contentValues.put(CONFIG_META_VALUE_COLUMN, value);
        long insert = db.insert(FILTER_CONFIG_TABLE, null, contentValues);
        return insert != -1;
    }

    public Map<String, String> getConfig() {
        SQLiteDatabase db = getReadableDatabase();
        Map<String, String> config = new HashMap<>();

        String query = "SELECT * FROM "
                + FILTER_CONFIG_TABLE;

        Cursor cursor = db.rawQuery(query, new String []{});
        if (cursor.moveToFirst()) {
            do {
                config.put(cursor.getString(cursor.getColumnIndex(CONFIG_META_KEY_COLUMN)),
                        cursor.getString(cursor.getColumnIndex(CONFIG_META_VALUE_COLUMN)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return config;
    }

    public boolean updateConfig(String key, String value) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONFIG_META_VALUE_COLUMN, value);
        long insert = sqLiteDatabase.update(FILTER_CONFIG_TABLE, contentValues, CONFIG_META_KEY_COLUMN + "=\"" + key + "\"", null);
        return insert != -1;
    }

    public boolean addQuestionnairesResults(Questionnaire questionnaire) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Gson gson = new Gson();
        String questionnaireSerialized = gson.toJson(questionnaire, Questionnaire.class);

        ContentValues contentValues = new ContentValues();
        contentValues.put(QUESTIONNAIRE_ID_COLUMN, questionnaire.getId().toString());
        contentValues.put(QUESTIONNAIRE_COLUMN, questionnaireSerialized);
        long insert = sqLiteDatabase.insert(QUESTIONNAIRES_TABLE_NAME, null, contentValues);
        return insert != -1;
    }

    public Map<Integer, Pair<String, Questionnaire>> getQuestionnairesResults() {
        SQLiteDatabase db = getReadableDatabase();
        Map<Integer, Pair<String, Questionnaire>> questionnaires = new HashMap<>();
        Gson gson = new Gson();

        String query = "SELECT * FROM "
                + QUESTIONNAIRES_TABLE_NAME
                + " ORDER BY "
                + QUESTIONNAIRE_CREATION_DATE_COLUMN
                + " DESC LIMIT 6 ";

        int index = 0;
        Cursor cursor = db.rawQuery(query, new String []{});
        if (cursor.moveToFirst()) {
            do {
                questionnaires.put(index++,
                        new Pair<>(cursor.getString(cursor.getColumnIndex(QUESTIONNAIRE_CREATION_DATE_COLUMN)),
                        gson.fromJson(cursor.getString(cursor.getColumnIndex(QUESTIONNAIRE_COLUMN)), Questionnaire.class)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionnaires;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
