package com.example.bobdish.dontest02;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import java.sql.BatchUpdateException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

import androidx.annotation.NonNull;


public class LedgerRegFragment extends android.app.Fragment {

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    Context context;

    String stUseItem;
    String stPrice;
    String stPaymemo;
    Calendar c = Calendar.getInstance(); // Firebase내에 날짜로 저장
    SimpleDateFormat years = new SimpleDateFormat("yyyy");
    SimpleDateFormat months = new SimpleDateFormat("MM");
    SimpleDateFormat days = new SimpleDateFormat("dd");
    String stYear = years.format(c.getTime());
    String stMonth = months.format(c.getTime());
    String stDay = days.format(c.getTime());

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        user = FirebaseAuth.getInstance().getCurrentUser();

        View v = inflater.inflate(R.layout.fragment_ledger_reg, container, false);

        final Spinner spnUseitem = (Spinner) v.findViewById(R.id.spnUseitem);
        Button btnSave = (Button) v.findViewById(R.id.btnSave);
        final EditText etPrice = (EditText) v.findViewById(R.id.etPrice);
        final EditText etPaymemo = (EditText) v.findViewById(R.id.etPaymemo);
        CalendarView cvCalender = (CalendarView) v.findViewById(R.id.cvCalender);
        final RadioButton rbConsume = (RadioButton) v.findViewById(R.id.rbConsume);
        RadioButton rbIncome = (RadioButton) v.findViewById(R.id.rbIncome);
        Button btnOcr = (Button)v.findViewById(R.id.btnOcr);
        Button btnSMS = (Button)v.findViewById(R.id.btnSMS) ;

        spnUseitem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stUseItem = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        cvCalender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                stYear = Integer.toString(year);
                stMonth = String.format("%02d",month+1);
                stDay =  String.format("%02d",day);

               Toast.makeText(getActivity(), stYear+"-"+stMonth+"-"+stDay, Toast.LENGTH_SHORT).show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stPrice = etPrice.getText().toString();
                stPaymemo = etPaymemo.getText().toString();
                c = Calendar.getInstance();
                SimpleDateFormat time = new SimpleDateFormat("HHmmss");
                String stTime = time.format(c.getTime());
                Hashtable<String, String> ledger   // HashTable로 연결
                        = new Hashtable<String, String>();
                ledger.put("useItem", stUseItem);
                ledger.put("price", stPrice);
                ledger.put("paymemo",stPaymemo);

                if (stPrice.equals("")) {
                    Toast.makeText(getActivity(), "금액란을 채워주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (rbConsume.isChecked()) {
                        myRef.child(user.getUid()).child("Ledger").child(stYear).child(stMonth).child(stDay).child("지출").child(stTime).setValue(ledger);
                        Toast.makeText(getActivity(), "저장하였습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        myRef.child(user.getUid()).child("Ledger").child(stYear).child(stMonth).child(stDay).child("수입").child(stTime).setValue(ledger);
                        Toast.makeText(getActivity(), "저장하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                etPrice.setText("");
                etPaymemo.setText("");


            }
        });

        btnOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ImageActivity.class);
                startActivity(intent);
            }
        });

        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SMSActivity.class);
                startActivity(intent);
            }
        });


        return v;
    }


}
