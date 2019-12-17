package com.lingsh.rememberpwdtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

/**
 * @author lingsh
 */
public class LoginActivity extends BaseActivity {

    public static final String KEY_REMEMBER_PASSWORD = "remember_password";
    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_PASSWORD = "password";
    private EditText mAccountEdit;
    private EditText mPasswordEdit;
    private Button mLoginButton;
    private CheckBox mRememberPwd;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPref = PreferenceManager.getDefaultSharedPreferences(this);

        mAccountEdit = findViewById(R.id.account);
        mPasswordEdit = findViewById(R.id.password);
        mRememberPwd = (CheckBox) findViewById(R.id.remember_password);
        boolean isRemember = mPref.getBoolean(KEY_REMEMBER_PASSWORD, false);
        if (isRemember) {
            String account = mPref.getString(KEY_ACCOUNT, "");
            String password = mPref.getString(KEY_PASSWORD, "");
            mAccountEdit.setText(account);
            mPasswordEdit.setText(password);
            mRememberPwd.setChecked(true);
        }

        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = mAccountEdit.getText().toString();
                String password = mPasswordEdit.getText().toString();
                if ("admin".equals(account) && "123456".equals(password)) {
                    rememberPwd(account, password);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "account or password is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void rememberPwd(String account, String password) {
        SharedPreferences.Editor edit = mPref.edit();
        if (mRememberPwd.isChecked()) {
            edit.putBoolean(KEY_REMEMBER_PASSWORD, true);
            edit.putString(KEY_ACCOUNT, account);
            edit.putString(KEY_PASSWORD, password);
        } else {
            edit.clear();
        }
        edit.apply();
    }
}
