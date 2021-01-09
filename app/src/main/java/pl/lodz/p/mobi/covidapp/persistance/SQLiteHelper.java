package pl.lodz.p.mobi.covidapp.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.lodz.p.mobi.covidapp.instruction.Questionnaire;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "covidapp.db";
    // TABLE
    public static final String QUESTIONNAIRES_TABLE_NAME = "USER_QUESTIONNAIRES";
    // COLUMNS
    public static final String QUESTIONNAIRE_CREATION_DATE_COLUMN = "CREATION_DATE";
    public static final String QUESTIONNAIRE_ID_COLUMN = "QUESTIONNAIRE_ID";
    public static final String QUESTIONNAIRE_COLUMN = "QUESTIONNAIRE";

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

        db.execSQL(questionnairesTableStatement);
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
