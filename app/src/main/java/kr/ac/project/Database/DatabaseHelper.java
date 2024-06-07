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
import kr.ac.project.Activity.MemoActivity;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

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

    // 메모 테이블 및 컬럼 이름 정의
    public static final String TABLE_MEMO = "memo";
    public static final String COLUMN_MEMO_ID = "memo_id";
    public static final String COLUMN_MEMO_TITLE = "title";
    public static final String COLUMN_MEMO_TEXT = "text";
    public static final String COLUMN_MEMO_TIMESTAMP = "timestamp";

    private static final String TABLE_CREATE_MEMO =
            "CREATE TABLE " + TABLE_MEMO + " (" +
                    COLUMN_MEMO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MEMO_TITLE + " TEXT, " +
                    COLUMN_MEMO_TEXT + " TEXT, " +
                    COLUMN_MEMO_TIMESTAMP + " TEXT);";

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
        db.execSQL(TABLE_CREATE_MEMO); // 메모 테이블 생성
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMO); // 메모 테이블 삭제
        onCreate(db);
    }

    // 이벤트 삽입 메서드
    public long insertEvent(String title, String time, CalendarDay date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_TITLE, title);
        values.put(COLUMN_EVENT_TIME, time);
        values.put(COLUMN_EVENT_DATE, dateFormat.format(date.getDate())); // 날짜 형식 지정
        return db.insert(TABLE_EVENTS, null, values);
    }

    // 특정 날짜의 이벤트 가져오기
    public List<Event> getEventsForDate(CalendarDay date) {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_EVENT_TITLE, COLUMN_EVENT_TIME, COLUMN_EVENT_DATE};
        String selection = COLUMN_EVENT_DATE + " = ?";
        String[] selectionArgs = {dateFormat.format(date.getDate())}; // 날짜 형식 지정
        Cursor cursor = db.query(TABLE_EVENTS, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
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

    // 메모 삽입 메서드
    public long insertMemo(String title, String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEMO_TITLE, title);
        values.put(COLUMN_MEMO_TEXT, text);
        values.put(COLUMN_MEMO_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        return db.insert(TABLE_MEMO, null, values);
    }

    // 모든 메모 가져오기
    public List<MemoActivity> getAllMemos() {
        List<MemoActivity> memos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_MEMO_ID, COLUMN_MEMO_TITLE, COLUMN_MEMO_TEXT, COLUMN_MEMO_TIMESTAMP};
        Cursor cursor = db.query(TABLE_MEMO, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(COLUMN_MEMO_ID));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_MEMO_TITLE));
                String text = cursor.getString(cursor.getColumnIndex(COLUMN_MEMO_TEXT));
                String timestamp = cursor.getString(cursor.getColumnIndex(COLUMN_MEMO_TIMESTAMP));
                memos.add(new MemoActivity(id, title, text, timestamp));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return memos;
    }
    public void updateMemo(MemoActivity memo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEMO_TITLE, memo.getTitle());
        values.put(COLUMN_MEMO_TEXT, memo.getText());
        values.put(COLUMN_MEMO_TIMESTAMP, memo.getTimestamp());

        db.update(TABLE_MEMO, values, COLUMN_MEMO_ID + " = ?", new String[]{String.valueOf(memo.getId())});
        db.close();
    }

    // 메모 삭제 메서드 추가
    public void deleteMemo(long memoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEMO, COLUMN_MEMO_ID + " = ?", new String[]{String.valueOf(memoId)});
        db.close();
    }

}
