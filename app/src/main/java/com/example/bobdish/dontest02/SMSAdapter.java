package com.example.bobdish.dontest02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.ViewHolder> {

    List<SMS> mBody;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    Context context;
    String stUseitem;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public Button btnSMSDay;
        public TextView tvSMSPaymemo;
        public TextView tvSMSPrice;
        public TextView tvSMSTime;
        public Button btnAddSMS;
        public Spinner smsUseitem;

        public ViewHolder(View itemView) {
            super(itemView);
            btnSMSDay = (Button) itemView.findViewById(R.id.btnSMSDay);
            tvSMSPaymemo= (TextView) itemView.findViewById(R.id.tvSMSPaymemo);
            tvSMSPrice= (TextView) itemView.findViewById(R.id.tvSMSPrice);
            tvSMSTime= (TextView) itemView.findViewById(R.id.tvSMSTime);
            btnAddSMS = (Button) itemView.findViewById(R.id.btnAddSMS);
            smsUseitem = (Spinner) itemView.findViewById(R.id.smsUseitem);
        }
    }

    public SMSAdapter(List<SMS> mBody , Context context) {
        this.mBody = mBody;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        user = FirebaseAuth.getInstance().getCurrentUser();

        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_sms, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

      holder.tvSMSPaymemo.setText(mBody.get(position).getPayMemo());
      holder.btnSMSDay.setText(mBody.get(position).getYear()+"-"+mBody.get(position).getMonth()+"-"+mBody.get(position).getDay());
      holder.tvSMSPrice.setText("-" + mBody.get(position).getPrice()+"원");
      holder.tvSMSTime.setText("[신한체크]" + mBody.get(position).getTime());

      holder.smsUseitem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              stUseitem = (String) parent.getItemAtPosition(position);
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });

      holder.btnAddSMS.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              LedgerContent mledgerContent = new LedgerContent();
              mledgerContent.setPaymemo(mBody.get(position).getPayMemo());
              mledgerContent.setPrice(mBody.get(position).getPrice());
              mledgerContent.setUseItem(stUseitem);

              myRef.child(user.getUid()).child("Ledger")
                      .child(mBody.get(position).getYear())
                      .child(mBody.get(position).getMonth()).
                      child(mBody.get(position).getDay())
                      .child("지출")
                      .child(mBody.get(position).getTime())
                      .setValue(mledgerContent);
              Toast.makeText(context, "가계부에 추가되었습니다.", Toast.LENGTH_SHORT).show();
          }
      });
        }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mBody.size();
    }
}