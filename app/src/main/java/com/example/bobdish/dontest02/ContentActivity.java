package com.example.bobdish.dontest02;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ContentActivity extends AppCompatActivity {

    TextView OCRResult;
    TextView OCRToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        String result="";
        String toast = "";
        Intent in = getIntent();
        OCRResult = (TextView) findViewById(R.id.OCRResult);
        OCRToast = (TextView) findViewById(R.id.OCRToast);

        result = in.getStringExtra("result");
        toast = in.getStringExtra("finalResult");

        OCRResult.setText("[ OCR 인식 결과 ]\n" + result);
        OCRToast.setText(toast);
    }
}
