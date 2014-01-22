package com.panasenko.tipcalculator.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * TipCalculatorActivity
 * Allows to input bill total and calculated select tip percent to see output tip amount.
 */
public class TipCalculatorActivity extends Activity {

    private static final String TAG = TipCalculatorActivity.class.getSimpleName();

    private static final double SMALL_TIP_MULTIPLIER = 0.10;
    private static final double MEDIUM_TIP_MULTIPLIER = 0.15;
    private static final double HIGH_TIP_MULTIPLIER = 0.20;

    private static final String EXTRA_SELECTED_TIP_ID = "com.panasenko.tipcalulator.android.EXTRA_SELECTED_TIP_ID";
    private static final String EXTRA_BILL_TOTAL = "com.panasenko.tipcalulator.android.EXTRA_BILL_TOTAL";

    private EditText mTipInput;
    private TextView mTipAmountTextView;
    private RadioGroup mTipRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tip_activity);

        initViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(EXTRA_SELECTED_TIP_ID, mTipRadioGroup.getCheckedRadioButtonId());
        outState.putString(EXTRA_BILL_TOTAL, mTipInput.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int selectedTipButtonId = savedInstanceState.getInt(EXTRA_SELECTED_TIP_ID);
        mTipRadioGroup.check(selectedTipButtonId);

        String billTotalText = savedInstanceState.getString(EXTRA_BILL_TOTAL);
        mTipInput.setText(billTotalText);
    }

    /**
     * Called when one of tip selection buttons is clicked.
     * @param v Reference to a caller view, used to determine tip multiplier.
     */
    public void onTipSelected(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_small_tip:
                updateTipAmount(SMALL_TIP_MULTIPLIER);
                break;
            case R.id.btn_medium_tip:
                updateTipAmount(MEDIUM_TIP_MULTIPLIER);
                break;
            case R.id.btn_high_bill:
                updateTipAmount(HIGH_TIP_MULTIPLIER);
                break;
            default:
                return;
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTipInput.getWindowToken(), 0);
    }

    /**
     * Initializes views from xml and set's default notification listeners.
     */
    private void initViews() {
        mTipInput = (EditText) findViewById(R.id.tip_input);
        mTipAmountTextView = (TextView) findViewById(R.id.txt_tip_amount);
        mTipRadioGroup = (RadioGroup) findViewById(R.id.tip_selector);

        mTipInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (!TextUtils.isEmpty(text)) {
                    updateWithSelectedTipButton(mTipRadioGroup.getCheckedRadioButtonId());
                }
            }
        });

        mTipRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                updateWithSelectedTipButton(id);
            }
        });
    }

    /**
     * Returns bill total from value stored in the bill value input.
     * @return Value greater then 0 if there is a value, 0 otherwise.
     */
    private double getBillTotal() {
        double totalAmount = 0.0;
        String text = mTipInput.getText().toString();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(this, R.string.error_no_tip, Toast.LENGTH_SHORT).show();
        } else {
            totalAmount = Double.parseDouble(text);
        }

        return totalAmount;
    }

    /**
     * Calculates tip amount and updates result text view.
     * @param tipMultiplier Tip multiplier, decimal value (e.g. 0.15).
     */
    private void updateTipAmount(double tipMultiplier) {
        Log.d(TAG, "updateTipAmount called");

        double tipAmount = getBillTotal();
        if (tipAmount <= 0.0) {
            Toast.makeText(this, R.string.error_non_positive, Toast.LENGTH_SHORT).show();
            return;
        }

        if (tipAmount > 0.0 && tipMultiplier > 0.0) {
            NumberFormat df = new DecimalFormat("0.00");
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);

            mTipAmountTextView.setText(getResources().getString(R.string.tip_amount_format,
                    df.format(tipAmount * tipMultiplier)));
            Log.d(TAG, "updateTipAmount calculated: " + tipAmount * tipMultiplier);
        }
    }

    /**
     * Updates tip amount according to a selected tip button id.
     * @param buttonId Id of the radio group button specifying selected tip percentage.
     */
    private void updateWithSelectedTipButton(int buttonId)
    {
        switch (buttonId) {
            case R.id.btn_small_tip:
                updateTipAmount(SMALL_TIP_MULTIPLIER);
                break;
            case R.id.btn_medium_tip:
                updateTipAmount(MEDIUM_TIP_MULTIPLIER);
                break;
            case R.id.btn_high_bill:
                updateTipAmount(HIGH_TIP_MULTIPLIER);
                break;
            default:
                return;
        }
    }
}
