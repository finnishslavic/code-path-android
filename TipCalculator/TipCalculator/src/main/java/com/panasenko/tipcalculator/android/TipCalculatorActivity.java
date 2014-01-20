package com.panasenko.tipcalculator.android;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * TipCalculatorActivity
 * Allows to input bill total and calculated select tip percent to see output tip amount.
 */
public class TipCalculatorActivity extends Activity {

    private static final double SMALL_TIP_MULTIPLIER = 0.10;
    private static final double MEDIUM_TIP_MULTIPLIER = 0.15;
    private static final double HIGH_TIP_MULTIPLIER = 0.20;

    private EditText mTipInput;
    private TextView mTipAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tip_activity);

        initViews();
    }

    /**
     * Called when one of tip selection buttons is clicked.
     * @param v Reference to a caller view, used to determine tip multiplier.
     */
    public void onTipSelected(View v)
    {
        double tipAmount = getBillTotal();
        if (tipAmount <= 0.0) {
            Toast.makeText(this, R.string.error_non_positive, Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId())
        {
            case R.id.btn_small_tip:
                updateTipAmount(tipAmount, SMALL_TIP_MULTIPLIER);
                break;
            case R.id.btn_medium_tip:
                updateTipAmount(tipAmount, MEDIUM_TIP_MULTIPLIER);
                break;
            case R.id.btn_high_bill:
                updateTipAmount(tipAmount, HIGH_TIP_MULTIPLIER);
                break;
            default:
                return;
        }
    }

    /**
     * Initializes views from xml and set's default notification listeners.
     */
    private void initViews() {
        mTipInput = (EditText) findViewById(R.id.tip_input);
        mTipAmountTextView = (TextView) findViewById(R.id.txt_tip_amount);
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
     * @param tipAmount Amount to be used as base for calculating tip.
     * @param tipMultiplier Tip multiplier, decimal value (e.g. 0.15).
     */
    private void updateTipAmount(double tipAmount, double tipMultiplier) {
        if (tipAmount > 0.0 && tipMultiplier > 0.0) {
            NumberFormat df = new DecimalFormat("0.00");
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);

            mTipAmountTextView.setText(getResources().getString(R.string.tip_amount_format,
                    df.format(tipAmount * tipMultiplier)));
        }
    }

}
