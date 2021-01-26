package com.ineed.verytransferlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.Telephony;

import androidx.core.app.ActivityCompat;

import java.util.Date;

public class VerifyAsyncTask extends AsyncTask<Object, Object, TransferInfo> {

    AsyncResponse asyncResponse = null;
    Context context;
    Double desired_amount;
    String number_benefactor;
    Boolean exact;
    Integer pay_days;

    public VerifyAsyncTask(AsyncResponse delegate, Context context, Double desired_amount, String number_benefactor, Boolean exact, Integer pay_days) {
        this.asyncResponse = delegate;
        this.context = context;
        this.desired_amount = desired_amount;
        this.number_benefactor = number_benefactor;
        this.exact = exact;
        this.pay_days = pay_days;
    }

    @Override
    protected TransferInfo doInBackground(Object... objects) {
        TransferInfo transferInfo;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_SMS}, 0);
        } else {
            ContentResolver cr = context.getContentResolver();
            Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, "DATE DESC");
            int totalSMS;
            if (c != null) {
                totalSMS = c.getCount();
                if (c.moveToFirst()) {
                    for (int i = 0; i < totalSMS; i++) {
                        String smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));
                        if ((Long.parseLong(smsDate) + ((long) (1000 * 60 * 60 * 24) * pay_days)) >= new Date().getTime()) {
                            String smsFrom = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                            String smsBody = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));
                            String smsType = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE));
                            if (Tools.TransferChecker(smsFrom, smsBody, smsType, smsDate, desired_amount, number_benefactor, exact, pay_days)) {
                                transferInfo = new TransferInfo();
                                transferInfo.setSmsDate(smsDate);
                                transferInfo.setSmsBody(smsBody);
                                transferInfo.setSmsFrom(smsFrom);
                                transferInfo.setSmsType(smsType);
                                return transferInfo;
                            }
                            c.moveToNext();
                        }
                    }
                    c.close();
                }
            }
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(TransferInfo result) {
        asyncResponse.processFinish(result);
    }
}
