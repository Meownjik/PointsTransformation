package com.wikia.meownjik.pointstransformation;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "PT";
    private EditText inputInitVal, inputEndVal, inputInitMax, inputEndMax;
    private Button buttonFlip;
    private ProgressBar progressBar;
    private RadioGroup radioGroupInit, radioGroupEnd;
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

        radioGroupInit = (RadioGroup) findViewById(R.id.radioGroupInit);
        radioGroupEnd = (RadioGroup) findViewById(R.id.radioGroupEnd);
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

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                recalculateMark();
                updateProgressBar();
            }
        };
        inputInitVal.addTextChangedListener(textWatcher);
        inputInitMax.addTextChangedListener(textWatcher);
        inputEndMax.addTextChangedListener(textWatcher);

        radioGroupInit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioInitCustom.isChecked()) {
                    inputInitVal.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    inputInitMax.setEnabled(true);
                    inputInitMax.setPaintFlags( inputInitMax.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                } else {
                    inputInitVal.setInputType(InputType.TYPE_CLASS_TEXT);
                    inputInitMax.setEnabled(false);
                    inputInitMax.setPaintFlags(inputInitMax.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                enableFlip();
                recalculateMark();
            }
        });
        //radioGroupInit.setOnClickListener();

        radioGroupEnd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioEndCustom.isChecked()) {
                    inputEndVal.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    inputEndMax.setEnabled(true);
                    inputEndMax.setPaintFlags( inputEndMax.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                } else {
                    inputEndVal.setInputType(InputType.TYPE_CLASS_TEXT);
                    inputEndMax.setEnabled(false);
                    inputEndMax.setPaintFlags(inputEndMax.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                enableFlip();
                recalculateMark();
            }
        });
    }

    private void enableFlip() {
        if (radioEndCustom.isChecked() && radioInitCustom.isChecked()) {
            buttonFlip.setEnabled(true);
        }
        else {
            buttonFlip.setEnabled(false);
        }
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
        String scored = inputInitVal.getText().toString();
        float max = 0;
        float maxNew = 0;
        float scoredFloat = 0;
        try {
            max = Float.parseFloat(inputInitMax.getText().toString());
            maxNew = Float.parseFloat(inputEndMax.getText().toString());
            if (radioInitCustom.isChecked()) {
                scoredFloat = Float.parseFloat(scored);
            }
        } catch (NumberFormatException er) {
            Log.e(TAG, er.toString());
            inputEndVal.setText("0");
            return;
        }
        //From custom
        if (radioInitCustom.isChecked()) {
            if (radioEndCustom.isChecked()) {
                inputEndVal.setText(String.valueOf(PointsCalculator.transform(scoredFloat, max, maxNew)));
            } else if (radioEnd5.isChecked()) {
                inputEndVal.setText(PointsCalculator.toClassic(scoredFloat, max));
            } else if (radioEndEcts.isChecked()) {
                inputEndVal.setText(PointsCalculator.toEcts(scoredFloat, max));
            }
        }
        //From classic 5+
        else if (radioInit5.isChecked()) {
            if (radioEndCustom.isChecked()) {
                inputEndVal.setText(String.valueOf(PointsCalculator.fromClassic(scored, maxNew)));
            } else if (radioEndEcts.isChecked()) {
                inputEndVal.setText(PointsCalculator.toEcts(
                        PointsCalculator.fromClassic(scored, 5), 5));
            } else {
                inputEndVal.setText(scored);
            }
        }
        //From ECTS
        else if (radioInitEcts.isChecked()) {
            if (radioEndCustom.isChecked()) {
                inputEndVal.setText(String.valueOf(PointsCalculator.fromEcts(scored, maxNew)));
            } else if (radioEnd5.isChecked()) {
                inputEndVal.setText(PointsCalculator.toClassic(
                        PointsCalculator.fromEcts(scored, 100), 100));
            } else {
                inputEndVal.setText(scored);
            }
        }
        //From a special system to the same system
        else {
            inputEndVal.setText(scored);
        }
    }
}