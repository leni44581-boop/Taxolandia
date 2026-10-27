package com.example.taxolandia.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GameDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "taxolandia.db";
    private static final int DB_VERSION = 5;  // ← ИЗМЕНЕНО с 1 на 5!
    private static final String TABLE = "game_progress";

    public GameDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "created_at TEXT, " +
                "quiz_answers TEXT, " +
                "character_type INTEGER, " +
                "start_courage INTEGER, start_kindness INTEGER, " +
                "start_cunning INTEGER, start_wisdom INTEGER, " +
                "current_courage INTEGER, current_kindness INTEGER, " +
                "current_cunning INTEGER, current_wisdom INTEGER, " +
                "current_scene TEXT, current_chapter TEXT, " +
                "flags TEXT, status INTEGER, ending_type INTEGER, " +
                "end_courage INTEGER, end_kindness INTEGER, " +
                "end_cunning INTEGER, end_wisdom INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public long insertProgress(GameProgress progress) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        fillValues(values, progress);
        return db.insert(TABLE, null, values);
    }

    public int updateProgress(GameProgress progress) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        fillValues(values, progress);
        return db.update(TABLE, values, "id=?",
                new String[]{String.valueOf(progress.getId())});
    }

    public void deleteProgress(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, "id=?", new String[]{String.valueOf(id)});
    }

    private void fillValues(ContentValues values, GameProgress p) {
        values.put("created_at", p.getCreatedAt());
        StringBuilder sb = new StringBuilder();
        int[] answers = p.getQuizAnswers();
        for (int i = 0; i < answers.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(answers[i]);
        }
        values.put("quiz_answers", sb.toString());
        values.put("character_type", p.getCharacterType());
        values.put("start_courage", p.getStartCourage());
        values.put("start_kindness", p.getStartKindness());
        values.put("start_cunning", p.getStartCunning());
        values.put("start_wisdom", p.getStartWisdom());
        values.put("current_courage", p.getCurrentCourage());
        values.put("current_kindness", p.getCurrentKindness());
        values.put("current_cunning", p.getCurrentCunning());
        values.put("current_wisdom", p.getCurrentWisdom());
        values.put("current_scene", p.getCurrentSceneId());
        values.put("current_chapter", p.getCurrentChapter());
        values.put("flags", p.getFlags());
        values.put("status", p.getStatus());
        values.put("ending_type", p.getEndingType());
        values.put("end_courage", p.getEndCourage());
        values.put("end_kindness", p.getEndKindness());
        values.put("end_cunning", p.getEndCunning());
        values.put("end_wisdom", p.getEndWisdom());
    }

    public GameProgress getInProgress() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, "status=?",
                new String[]{"0"}, null, null, "id DESC", "1");
        GameProgress progress = null;
        if (cursor.moveToFirst()) {
            progress = cursorToProgress(cursor);
        }
        cursor.close();
        return progress;
    }

    public List<GameProgress> getCompletedHistory() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, "status=?",
                new String[]{"1"}, null, null, "id DESC");
        List<GameProgress> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(cursorToProgress(cursor));
        }
        cursor.close();
        return list;
    }

    private GameProgress cursorToProgress(Cursor cursor) {
        GameProgress p = new GameProgress();
        p.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
        p.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow("created_at")));
        String answersStr = cursor.getString(cursor.getColumnIndexOrThrow("quiz_answers"));
        int[] answers = new int[20];
        if (answersStr != null && !answersStr.isEmpty()) {
            String[] parts = answersStr.split(",");
            for (int i = 0; i < Math.min(parts.length, 20); i++) {
                try { answers[i] = Integer.parseInt(parts[i].trim()); }
                catch (Exception e) { answers[i] = 0; }
            }
        }
        p.setQuizAnswers(answers);
        p.setCharacterType(cursor.getInt(cursor.getColumnIndexOrThrow("character_type")));
        p.setStartCourage(cursor.getInt(cursor.getColumnIndexOrThrow("start_courage")));
        p.setStartKindness(cursor.getInt(cursor.getColumnIndexOrThrow("start_kindness")));
        p.setStartCunning(cursor.getInt(cursor.getColumnIndexOrThrow("start_cunning")));
        p.setStartWisdom(cursor.getInt(cursor.getColumnIndexOrThrow("start_wisdom")));
        p.setCurrentCourage(cursor.getInt(cursor.getColumnIndexOrThrow("current_courage")));
        p.setCurrentKindness(cursor.getInt(cursor.getColumnIndexOrThrow("current_kindness")));
        p.setCurrentCunning(cursor.getInt(cursor.getColumnIndexOrThrow("current_cunning")));
        p.setCurrentWisdom(cursor.getInt(cursor.getColumnIndexOrThrow("current_wisdom")));
        p.setCurrentSceneId(cursor.getString(cursor.getColumnIndexOrThrow("current_scene")));
        p.setCurrentChapter(cursor.getString(cursor.getColumnIndexOrThrow("current_chapter")));
        p.setFlags(cursor.getString(cursor.getColumnIndexOrThrow("flags")));
        p.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow("status")));
        p.setEndingType(cursor.getInt(cursor.getColumnIndexOrThrow("ending_type")));
        p.setEndCourage(cursor.getInt(cursor.getColumnIndexOrThrow("end_courage")));
        p.setEndKindness(cursor.getInt(cursor.getColumnIndexOrThrow("end_kindness")));
        p.setEndCunning(cursor.getInt(cursor.getColumnIndexOrThrow("end_cunning")));
        p.setEndWisdom(cursor.getInt(cursor.getColumnIndexOrThrow("end_wisdom")));
        return p;
    }
}