package com.ineed.verytransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.ineed.verytransferlibrary.AsyncResponse;
import com.ineed.verytransferlibrary.TransferInfo;
import com.ineed.verytransferlibrary.VerifyAsyncTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sample usage
        VerifyAsyncTask verifyAsyncTask = new VerifyAsyncTask(new AsyncResponse() {
            @Override
            public void processFinish(TransferInfo output) {
                if (output != null) {
                    //pay found under requested params:
                    //3.0 CUP - amount to pay
                    //54868618 - number to receive pay
                    //false - means transfer can be 3.0 CUP or more, true - means exactly 3.0 CUP
                    //30 - means subscriptions days
                }
            }
        }, MainActivity.this, 3.0, "54868618", false, 30);
        verifyAsyncTask.execute();
    }
}