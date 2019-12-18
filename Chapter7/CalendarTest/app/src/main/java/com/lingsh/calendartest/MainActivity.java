package com.lingsh.calendartest;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // 投影数组 通过索引获取常量
    public static final String[] EVENT_PROJECTION = new String[]{
            // 0
            Calendars._ID,
            // 1
            Calendars.ACCOUNT_NAME,
            // 2
            Calendars.CALENDAR_DISPLAY_NAME,
            // 3
            Calendars.OWNER_ACCOUNT
    };

    // 数组索引
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private static final int REQUEST_READ_CALENDAR_CODE = 1;
    private static final int REQUEST_WRITE_CALENDAR_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 日历表 calendar
        Button queryCalendar = (Button) findViewById(R.id.query_calendar);
        Button modifyCalendar = (Button) findViewById(R.id.modify_calendar);
        Button insertCalendar = (Button) findViewById(R.id.insert_calendar);
        // 事件表 event
        Button addEvent = (Button) findViewById(R.id.add_event);
        Button queryEvent = (Button) findViewById(R.id.query_event);
        Button updateEvent = (Button) findViewById(R.id.update_event);
        Button deleteEvent = (Button) findViewById(R.id.delete_event);
        // 参与者 participant
        Button addParticipant = (Button) findViewById(R.id.add_participant);
        // 提醒 notify
        Button addNotify = (Button) findViewById(R.id.add_notify);
        // 实例 instance
        Button queryInstance = (Button) findViewById(R.id.query_instance);

        queryCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionIsGranted(Manifest.permission.READ_CALENDAR, REQUEST_READ_CALENDAR_CODE)) {
                    queryCalendar();
                }
            }
        });

        modifyCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionIsGranted(Manifest.permission.WRITE_CALENDAR, REQUEST_WRITE_CALENDAR_CODE)) {
                    modifyCalendar();
                }
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionIsGranted(Manifest.permission.WRITE_CALENDAR, REQUEST_WRITE_CALENDAR_CODE)) {
                    addEvent();
                }
            }
        });

        queryEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionIsGranted(Manifest.permission.READ_CALENDAR, REQUEST_READ_CALENDAR_CODE)) {
                    queryEvent();
                }
            }
        });

        updateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionIsGranted(Manifest.permission.WRITE_CALENDAR, REQUEST_WRITE_CALENDAR_CODE)) {
                    updateEvent();
                }
            }
        });

        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionIsGranted(Manifest.permission.WRITE_CALENDAR, REQUEST_WRITE_CALENDAR_CODE)) {
                    deleteEvent();
                }
            }
        });


    }

    private void deleteEvent() {
        Log.d(TAG, "deleteEvent: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        ContentResolver cr = getContentResolver();
        int delete = cr.delete(Events.CONTENT_URI, "title = ?", new String[]{"Ling ShouHong"});
        Log.d(TAG, String.format("delete:%d", delete));
    }

    private void updateEvent() {
        Log.d(TAG, "updateEvent: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("title", "Ling ShouHong");

        int update = cr.update(Events.CONTENT_URI, values, "title = ?", new String[]{"Jazzercise"});
        Log.d(TAG, String.format("update:%d", update));
    }

    private void queryEvent() {
        Log.d(TAG, "queryEvent: ");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        ContentResolver cr = getContentResolver();
        Cursor cursor = null;
        cursor = cr.query(Events.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long startMillis = cursor.getLong(cursor.getColumnIndex(Events.DTSTART));
                long endMillis = cursor.getLong(cursor.getColumnIndex(Events.DTEND));
                String title = cursor.getString(cursor.getColumnIndex(Events.TITLE));
                String description = cursor.getString(cursor.getColumnIndex(Events.DESCRIPTION));
                long calId = cursor.getLong(cursor.getColumnIndex(Events.CALENDAR_ID));
                String timezone = cursor.getString(cursor.getColumnIndex(Events.EVENT_TIMEZONE));

                Log.d(TAG, String.format("query event: [s:%d, e:%d] title:%s desc:%s id:%d tz:%s",
                        startMillis, endMillis, title, description, calId, timezone));
            }
            cursor.close();
        }
    }

    private void addEvent() {
        Log.d(TAG, "addEvent: ");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        // 注意这里的月份是从0开始的
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2012, 9, 14, 7, 30);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2012, 9, 14, 8, 45);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
        values.put(Events.TITLE, "Jazzercise");
        values.put(Events.DESCRIPTION, "Group workout");
        values.put(Events.CALENDAR_ID, calID);
        values.put(Events.EVENT_TIMEZONE, "America/Los_Angeles");
        Uri uri = cr.insert(Events.CONTENT_URI, values);

        long eventID = Long.parseLong(uri.getLastPathSegment());

        Log.d(TAG, String.format("values:%s id:%d", values, eventID));
    }

    private void modifyCalendar() {
        Log.d(TAG, "modifyCalendar: ");

        long calID = 2;
        ContentValues values = new ContentValues();
        // 修改日历的显示名
        values.put(Calendars.CALENDAR_DISPLAY_NAME, "Trevor's Calendar");
        Uri updateUri = ContentUris.withAppendedId(Calendars.CONTENT_URI, calID);
        int rows = getContentResolver().update(updateUri, values, null, null);
        Log.i(TAG, "Rows updated: " + rows);
    }

    private boolean checkPermissionIsGranted(String permission, int requestCalendarCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCalendarCode);
            return false;
        } else {
            return true;
        }
    }

    private void queryCalendar() {
        Log.d(TAG, "queryCalendar: ");

        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = Calendars.CONTENT_URI;
        String selection = "((" + Calendars.ACCOUNT_NAME +
                " = ?) AND (" + Calendars.ACCOUNT_TYPE +
                " = ?) AND (" + Calendars.OWNER_ACCOUNT +
                " = ?))";
        String[] selectionArgs = new String[]{"hera@example.com", "com.example",
                "hera@example.com"};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        if (cur != null) {
            while (cur.moveToNext()) {
                long calID = cur.getLong(PROJECTION_ID_INDEX);
                String displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                String accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                String ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
                Log.d(TAG, "queryCalendar: " + String.format("id:%d display:%s account:%s owner:%s",
                        calID, displayName, accountName, ownerName));
            }
            cur.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult() called with: " +
                "requestCode = [" + requestCode + "], " +
                "permissions = [" + Arrays.toString(permissions) + "], " +
                "grantResults = [" + Arrays.toString(grantResults) + "]");

        switch (requestCode) {
            case REQUEST_READ_CALENDAR_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Thank for you grant the permission of READ CALENDAR! Please press again~", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You deny the permission! read", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_WRITE_CALENDAR_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Thank for you grant the permission of WRITE CALENDAR! Please press again~", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You deny the permission! write", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}
