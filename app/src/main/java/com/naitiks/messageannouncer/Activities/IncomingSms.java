package com.naitiks.messageannouncer.Activities;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsMessage;


public class IncomingSms extends BroadcastReceiver {
	public static final String SMS_BUNDLE = "pdus";
	String smsBody,address;
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                smsBody = smsMessage.getMessageBody().toString();
                address = smsMessage.getOriginatingAddress();
            }
            //i.putExtra("sender",address+" ("+ getContactName(context, address)+")");
            
            //this will update the UI with message
            /*SmsActivity inst = SmsActivity.instance();
            inst.updateList(smsMessageStr);*/
        }
	}
	public static String getContactName(Context context, String phoneNumber) {
	    ContentResolver cr = context.getContentResolver();
	    Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
	    Cursor cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
	    if (cursor == null) {
	        return null;
	    }
	    String contactName = null;
	    if(cursor.moveToFirst()) {
	        contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
	    }

	    if(cursor != null && !cursor.isClosed()) {
	        cursor.close();
	    }
	    if(contactName==null || contactName=="" )
	    	contactName=phoneNumber;
	    return contactName;
	}

}
