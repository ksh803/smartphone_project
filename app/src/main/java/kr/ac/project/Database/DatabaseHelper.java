package kr.ac.project.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import kr.ac.project.Event;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper instance; // Singleton instance

    // 사용자 테이블 및 컬럼 이름 정의
    public static final String TABLE_USER = "user";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_CREATE_USER =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT);";

    // 이벤트 테이블 및 컬럼 이름 정의
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_EVENT_TITLE = "title";
    public static final String COLUMN_EVENT_TIME = "time";
    public static final String COLUMN_EVENT_DATE = "date";

    private static final String TABLE_CREATE_EVENTS =
            "CREATE TABLE " + TABLE_EVENTS + " (" +
                    COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EVENT_TITLE + " TEXT, " +
                    COLUMN_EVENT_TIME + " TEXT, " +
                    COLUMN_EVENT_DATE + " TEXT);";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Singleton 인스턴스 반환
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USER);
        db.execSQL(TABLE_CREATE_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    // 이벤트 삽입 메서드
    public long insertEvent(String title, String time, CalendarDay date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_TITLE, title);
        values.put(COLUMN_EVENT_TIME, time);
        values.put(COLUMN_EVENT_DATE, date.getDate().toString());
        return db.insert(TABLE_EVENTS, null, values);
    }

    // 특정 날짜의 이벤트 가져오기
    public List<Event> getEventsForDate(CalendarDay date) {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_EVENT_TITLE, COLUMN_EVENT_TIME, COLUMN_EVENT_DATE};
        String selection = COLUMN_EVENT_DATE + " = ?";
        String[] selectionArgs = {date.getDate().toString()};
        Cursor cursor = db.query(TABLE_EVENTS, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            do {
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_TITLE));
                String time = cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_TIME));
                String eventDate = cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_DATE));
                try {
                    Date dateParsed = dateFormat.parse(eventDate);
                    events.add(new Event(CalendarDay.from(dateParsed), title, time));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return events;
    }

    // 모든 이벤트 가져오기
    public Set<Event> getAllEvents() {
        Set<Event> events = new HashSet<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_EVENT_TITLE, COLUMN_EVENT_TIME, COLUMN_EVENT_DATE};
        Cursor cursor = db.query(TABLE_EVENTS, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            do {
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_TITLE));
                String time = cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_TIME));
                String eventDate = cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_DATE));
                try {
                    Date dateParsed = dateFormat.parse(eventDate);
                    events.add(new Event(CalendarDay.from(dateParsed), title, time));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return events;
    }
}
