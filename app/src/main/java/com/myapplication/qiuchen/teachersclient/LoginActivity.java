package com.myapplication.qiuchen.teachersclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import MuYuanTeacher.aolanTeacherSystem;
import MuYuanTeacher.logininfo;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * 这个界面模板是AS自带的
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    // UI references.
    private AutoCompleteTextView mUsername;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);
        // Set up the login form.
        mUsername = (AutoCompleteTextView) findViewById (R.id.email);
        mPasswordView = (EditText) findViewById (R.id.password);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //如果需要透明导航栏，请加入标记
            getWindow ().getDecorView ().setSystemUiVisibility (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }

        logininfo.aolan = new aolanTeacherSystem ();
        logininfo.aolan.aolanTeacherSystem (this);
        logininfo.Share = this.getSharedPreferences ("QiuChenTeachersSet", MODE_PRIVATE);
        logininfo.edit = logininfo.Share.edit ();
        String user = logininfo.Share.getString ("user", "");
        if (user != "") {
            mUsername.setText (user);
        }
        String pass = logininfo.Share.getString ("pass", "");
        if (user != "") {
            mPasswordView.setText (pass);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            mPasswordView.setOnEditorActionListener (new TextView.OnEditorActionListener () {
                @Override
                public boolean onEditorAction (TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login||id == EditorInfo.IME_NULL) {
                        attemptLogin ();
                        return true;
                    }
                    return false;
                }
            });
        }

        //定义登录按钮的操作
        Button login_button = (Button) findViewById (R.id.Login_button);
        login_button.setOnClickListener (new OnClickListener () {
            @Override
            public void onClick (View view) {
                attemptLogin ();
            }
        });
        mLoginFormView = findViewById (R.id.login_form);
        mProgressView = findViewById (R.id.Login_ProgressBar);


        if (user != "") {
            login_button.callOnClick ();
        }
    }

    /**
     * 这个是登录的事件
     */
    private void attemptLogin () {

        // Reset errors.
        mUsername.setError (null);
        mPasswordView.setError (null);

        // Store values at the time of the login attempt.
        final String user = mUsername.getText ().toString ();
        final String password = mPasswordView.getText ().toString ();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (! TextUtils.isEmpty (password)&&! isPasswordValid (password)) {
            mPasswordView.setError ("请输入密码可好");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty (user)) {
            mUsername.setError ("账号在哪里...?");
            focusView = mUsername;
            cancel = true;
        }
//        if (TextUtils.isEmpty(password)) {
//            mPasswordView.setError("密码写了吗..?");
//            focusView = mPasswordView;
//            cancel = true;
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus ();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            mProgressView.setVisibility (View.VISIBLE);
            findViewById (R.id.Login_button).setVisibility (View.GONE);

            final Handler handler = new Handler () {
                @Override
                public void handleMessage (Message msg) {
                    super.handleMessage (msg);
                    if (logininfo.ErrorMsgCode != 1) {
                        mProgressView.setVisibility (View.GONE);
                        findViewById (R.id.Login_button).setVisibility (View.VISIBLE);
                    }
                    switch (logininfo.ErrorMsgCode) {
                        case 1:
                            Toast.makeText (LoginActivity.this, "登录成功!" + logininfo.mlogininfo.mName + "老师你好!", Toast.LENGTH_SHORT).show ();
                            logininfo.edit.putString ("user", user);
                            logininfo.edit.putString ("pass", password);
                            logininfo.edit.apply ();
                            final Handler hand = new Handler () {
                                @Override
                                public void handleMessage (Message msg) {
                                    super.handleMessage (msg);
                                    Intent i = new Intent (LoginActivity.this, MainPage.class);
                                    startActivity (i);
                                    finish ();
                                }
                            };
                            new Thread () {
                                @Override
                                public void run () {
                                    try {
                                        logininfo.aolan.findOnlinePersonCount ();
                                        hand.sendEmptyMessage (0);
                                    } catch (IOException e) {
                                        e.printStackTrace ();
                                    }
                                }
                            }.start ();
                            break;
                        case 2:
                            Toast.makeText (LoginActivity.this, logininfo.ErrorMessage, Toast.LENGTH_SHORT).show ();
                            break;
                        case 3:
                            Toast.makeText (LoginActivity.this, "未知错误!请联系作者!QQ:963084062", Toast.LENGTH_SHORT).show ();
                            break;
                    }

                }
            };

            new Thread () {
                @Override
                public void run () {
                    try {
                        logininfo.ErrorMsgCode = logininfo.aolan.mLoginSystem (user, password);
                    } catch (IOException e) {
                        e.printStackTrace ();
                    }
                    handler.sendEmptyMessage (0);
                }
            }.start ();

        }
    }

    private boolean isPasswordValid (String password) {
        //TODO: Replace this with your own logic
        return password.length () > 4;
    }

    @Override
    public Loader<Cursor> onCreateLoader (int i, Bundle bundle) {
        return new CursorLoader (this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath (ContactsContract.Profile.CONTENT_URI, ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", new String[] {ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished (Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<> ();
        cursor.moveToFirst ();
        while (! cursor.isAfterLast ()) {
            emails.add (cursor.getString (ProfileQuery.ADDRESS));
            cursor.moveToNext ();
        }

        addEmailsToAutoComplete (emails);
    }

    @Override
    public void onLoaderReset (Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete (List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<> (LoginActivity.this, android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        mUsername.setAdapter (adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Email.IS_PRIMARY,};

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}

