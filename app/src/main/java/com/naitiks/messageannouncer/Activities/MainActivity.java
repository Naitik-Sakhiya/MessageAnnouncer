package com.naitiks.messageannouncer.Activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

import com.naitiks.messageannouncer.R;

import java.util.Calendar;
import java.util.Date;

import static com.naitiks.messageannouncer.Activities.IncomingSms.getContactName;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    Button btnInbox, btnSent, btnDraft;
    TextView contactName, title;
    String msg;
    ListView msgList;
    Intent intent;
    ImageButton btn_setting;
    ActionBar actionBar;
    // Cursor Adapter
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = this.getSupportActionBar();
        title = (TextView)findViewById(R.id.textView_title);
        btn_setting = (ImageButton)findViewById(R.id.btn_actionbar_setting);
        btn_setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        title.setText("Inbox");
        btnInbox = (Button) findViewById(R.id.btn_inbox);
        btnInbox.setOnClickListener(this);
        btnSent = (Button) findViewById(R.id.btn_Outbox);
        btnSent.setOnClickListener(this);
        btnDraft = (Button) findViewById(R.id.btn_draft);
        btnDraft.setOnClickListener(this);
        msgList = (ListView) findViewById(R.id.listView_msgs);
        loadmsgs(INBOX);
    }

    private final String INBOX = "content://sms/inbox";
    private final String DRAFT = "content://sms/draft";
    private final String SENT = "content://sms/sent";

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        switch(permsRequestCode){
            case 0:
                boolean readSMS = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                boolean writeSMS = grantResults[1]==PackageManager.PERMISSION_GRANTED;
                boolean readContact = grantResults[2]==PackageManager.PERMISSION_GRANTED;
                if(readSMS && writeSMS && readContact){
                    loadmsgs(INBOX);
                }
                break;
        }

    }

    private int REQUEST_CODE = 0;
    private void loadmsgs(String uriAdd) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)  {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS,Manifest.permission.SEND_SMS,Manifest.permission.READ_CONTACTS},
                        REQUEST_CODE);
                return;
            }
        }
        Uri inboxURI = Uri.parse(uriAdd);
        String[] reqCols = new String[]{"_id", "address", "body", "date"};
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(inboxURI, reqCols, null, null, null);
        adapter = new SimpleCursorAdapter(this, R.layout.row, c, new String[]{"address", "body", "date"}, new int[]{R.id.lblNumber, R.id.lblMsg, R.id.lblDate});
        adapter.setViewBinder(new ViewBinder() {

            public boolean setViewValue(View aView, Cursor aCursor, int aColumnIndex) {

                if (aColumnIndex == 1) {
                    String name = aCursor.getString(aColumnIndex);
                    String newName = getContactName(getApplicationContext(), name);
                    TextView textView = (TextView) aView;
                    if (name.equals(newName))
                        textView.setText(name);
                    else
                        textView.setText(name + " (" + newName + ")");
                    return true;
                }
                if (aColumnIndex == 3) {
                    long createDate = aCursor.getLong(aColumnIndex);
                    TextView textView = (TextView) aView;
                    textView.setText("" + millisToDate(createDate));
                    return true;
                }

                return false;
            }
        });
        msgList.setAdapter(adapter);
        msgList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView msgText = (TextView) view.findViewById(R.id.lblMsg);
                TextView sender = (TextView) view.findViewById(R.id.lblNumber);
                msg = msgText.getText().toString();
                intent = new Intent(MainActivity.this, ReadingService.class);
                intent.putExtra("sender", sender.getText().toString());
                intent.putExtra("msg", msg);
                startService(intent);
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v == btnInbox) {
            loadmsgs(INBOX);
            //actionBar.setTitle("Inbox");
            title.setText("Inbox");
        }

        if (v == btnSent) {
            loadmsgs(SENT);
            //actionBar.setTitle("Sent Messages");
            title.setText("Sent Messages");
        }

        if (v == btnDraft) {
            loadmsgs(DRAFT);
            //actionBar.setTitle("Drafts");
            title.setText("Draft");
        }
    }

    @SuppressWarnings("deprecation")
    public static String millisToDate(long currentTime) {
        String finalDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        Date date = calendar.getTime();
        finalDate = "" + date.getDate() + "/" + (date.getMonth() + 1) + "/" + (date.getYear() - 100);
        return finalDate;
    }

    @SuppressWarnings("unused")
    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
            return contactName;
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (contactName == null || contactName == "")
            return phoneNumber;
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

