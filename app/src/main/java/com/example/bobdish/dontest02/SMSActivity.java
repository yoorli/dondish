package com.example.bobdish.dontest02;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SMSActivity extends AppCompatActivity {
    static final int SMS_RECEIVE_PERMISSON=1;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    SMSAdapter mAdapter;
    List<SMS> mBody;
    Button btnSMSLoad;
    TextView tvCountSMS;
    SMS mSMS;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.activity_sms);

        tvCountSMS = (TextView) findViewById(android.R.id.tvCountSMS);
        btnSMSLoad = (Button) findViewById(android.R.id.btnLoadSMS);
        mRecyclerView = (RecyclerView) findViewById(android.R.id.rvSMS);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mBody = new ArrayList<>();


        // specify an adapter (see also next example)
        mAdapter = new SMSAdapter(mBody, this);
        mRecyclerView.setAdapter(mAdapter);

        //권한이 부여되어 있는지 확인
        int permissonCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if(permissonCheck == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(), "SMS 수신권한 있음", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "SMS 수신권한 없음", Toast.LENGTH_SHORT).show();

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)){
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(getApplicationContext(), "SMS권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_SMS}, SMS_RECEIVE_PERMISSON);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_SMS}, SMS_RECEIVE_PERMISSON);
            }
        }

        permissonCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        if(permissonCheck == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(), "SMS 리시버권한 있음", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "SMS 리시버권한 없음", Toast.LENGTH_SHORT).show();

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)){
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(getApplicationContext(), "SMS권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.RECEIVE_SMS}, SMS_RECEIVE_PERMISSON);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.RECEIVE_SMS}, SMS_RECEIVE_PERMISSON);
            }
        }


        btnSMSLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check == 0) {
                    Uri allMessage = Uri.parse("content://sms/inbox");
                    Cursor cur = getContentResolver().query(allMessage, null, null, null, null); // 전체 문자 받아오기
                    int count = 0;

                    String msg = "";
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                    SimpleDateFormat dft = new SimpleDateFormat("HH:mm:ss");
                    long date;
                    String sdate;
                    String timedate;


                    while (cur.moveToNext()) { // 카드 체크

                        if (cur.getString(cur.getColumnIndex("body")).contains("신한체크승인")) { // 신한체크카드
                            mSMS = new SMS();
                            msg = cur.getString(cur.getColumnIndex("body"));
                           // protocol = cur.getString(cur.getColumnIndex("address"));
                            date = cur.getLong(cur.getColumnIndex("date"));
                            sdate = df.format(date);
                            timedate = dft.format(date);

                            StringTokenizer tokenizer = new StringTokenizer(msg, " ");

                            tokenizer.nextToken();
                            tokenizer.nextToken();
                            tokenizer.nextToken();
                            tokenizer.nextToken();

                            String price = tokenizer.nextToken();
                            price.trim();
                            price = price.replace(",","");
                            price = price.replace("원","");
                            String payMemo = tokenizer.nextToken();
                            String store = tokenizer.nextToken();
                            if (store.contains("잔액"))
                            {

                            } else {
                                payMemo += store;
                            }


                            mSMS.setPayMemo(payMemo);
                            mSMS.setPrice(price);
                            mSMS.setYear(sdate.substring(0,4));
                            mSMS.setMonth(sdate.substring(4,6));
                            mSMS.setDay(sdate.substring(6,8));
                            mSMS.setTime(timedate);

                            mBody.add(mSMS);
                            mRecyclerView.scrollToPosition(0);
                            mAdapter.notifyItemInserted(mBody.size() - 1);
                            count++;
                        }
                    }
                    tvCountSMS.setText(count+"건의 기록이 확인되었습니다.");
                    check=1; // 한 번만 불러오게 하기
                    count = 0;
                }
            }
        });






    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int grantResults[]){
        switch(requestCode){
            case SMS_RECEIVE_PERMISSON:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(), "SMS권한 승인함", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "SMS권한 거부함", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



}
