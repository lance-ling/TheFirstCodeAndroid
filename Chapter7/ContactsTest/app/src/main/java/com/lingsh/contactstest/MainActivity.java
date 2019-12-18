package com.lingsh.contactstest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> contactsList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView contactsView = (ListView) findViewById(R.id.contacts_view);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactsList);
        contactsView.setAdapter(mAdapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            readContracts();
        }
    }

    public void readContracts() {
        Cursor cursor = null;

        try {
            cursor = getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String dispalyName = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    contactsList.add(String.format("%s \n %s", dispalyName, number));
                }
                mAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContracts();
                } else {
                    Toast.makeText(this, "You deny the permission : READ_CONTACT", Toast.LENGTH_SHORT).show();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
