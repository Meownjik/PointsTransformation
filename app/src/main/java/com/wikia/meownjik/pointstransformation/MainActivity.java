package com.wikia.meownjik.pointstransformation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {
    private EditText inputInitVal, inputEndVal, inputInitMax, inputEndMax;
    private Button buttonFlip;
    private ProgressBar progressBar;
    private RadioButton radioInitCustom, radioInit5, radioInitEcts;
    private RadioButton radioEndCustom, radioEnd5, radioEndEcts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        initListeners();
    }

    private void initComponents() {
        inputInitVal = (EditText) findViewById(R.id.inputInitValue);
        inputEndVal = (EditText) findViewById(R.id.inputEndValue);
        inputInitMax = (EditText) findViewById(R.id.inputInitMax);
        inputEndMax = (EditText) findViewById(R.id.inputEndMax);
        buttonFlip = (Button) findViewById(R.id.buttonFlip);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        radioInitCustom = (RadioButton) findViewById(R.id.radioCustomInit);
        radioInit5 = (RadioButton) findViewById(R.id.radio5Init);
        radioInitEcts = (RadioButton) findViewById(R.id.radioEctsInit);
        radioEndCustom = (RadioButton) findViewById(R.id.radioCustomEnd);
        radioEnd5 = (RadioButton) findViewById(R.id.radio5End);
        radioEndEcts = (RadioButton) findViewById(R.id.radioEctsEnd);
    }

    private void initListeners() {
        buttonFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = inputEndVal.getText().toString();
                inputEndVal.setText(inputInitVal.getText());
                inputInitVal.setText(temp);

                temp = inputEndMax.getText().toString();
                inputEndMax.setText(inputInitMax.getText());
                inputInitMax.setText(temp);

                recalculateMark();
                updateProgressBar();
            }
        });

        inputInitVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                recalculateMark();
                updateProgressBar();
            }
        });
    }

    private void updateProgressBar() {
        if (progressBar != null) {
            try {
                float scored = Float.parseFloat(inputInitVal.getText().toString());
                float max = Float.parseFloat(inputInitMax.getText().toString());
                int success = (int) (scored / max * 100);
                progressBar.setProgress(success);
            } catch (NumberFormatException er) {
                progressBar.setProgress(0);
            }
        }
    }

    private void recalculateMark() {
        try {
            float scored = Float.parseFloat(inputInitVal.getText().toString());
            float max = Float.parseFloat(inputInitMax.getText().toString());
            float maxNew = Float.parseFloat(inputEndMax.getText().toString());
            float result = scored / max * maxNew;
            result = Math.round(result * 100) / 100f;
            inputEndVal.setText(String.valueOf(result));
        }
        catch (NumberFormatException er) {
            //Log.e(er.getMessage());
            inputEndVal.setText("");
        }
    }
}