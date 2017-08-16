package com.naitiks.messageannouncer.Activities;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.naitiks.messageannouncer.R;

@SuppressLint("InflateParams")
public class MessageSendingActivity extends AppCompatActivity {
    TextView replyTo;
    Button send, cncl;
    EditText msg;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_reply);
        ActionBar actionBar = this.getSupportActionBar();
        View v = getLayoutInflater().inflate(R.layout.actionbarlayout, null);
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setTitle("Send Message");
        //actionBar.setCustomView(v);
        //actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        //actionBar.show();
        replyTo = (TextView) findViewById(R.id.textView1);
        String sender = getIntent().getStringExtra("sender");
        if (sender.length() <= 10)
            replyTo.setText(sender);
        else {
            replyTo.setText(sender.substring(0, 13));
        }
        msg = (EditText) findViewById(R.id.editText1);
        send = (Button) findViewById(R.id.btn_send);
        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new readMsg().execute();
            }
        });
        cncl = (Button) findViewById(R.id.btn_msg_cancel);
        cncl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                status = "cancel";
                finish();

            }
        });
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        status = "cancel";
        this.finish();
        onBackPressed();
        return true;
    }

    private class readMsg extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... arg0) {
            MessageSendingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(replyTo.getText().toString(), null, msg.getText().toString(), null, null);
                        status = "Message Sent";
                    } catch (Exception e) {
                        status = "Message not Sent";
                        //Toast.makeText(getApplicationContext(),
                        //"SMS faild, please try again later!",
                        //Toast.LENGTH_LONG).show();
                        //e.printStackTrace();
                    }
                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MessageSendingActivity.this, null,
                    "Sending Message...");

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();
            finish();
        }

    }
}
