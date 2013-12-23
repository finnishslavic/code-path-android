/**
 * File: EditItemActivity.java
 * Created: 12/23/13
 * Author: Viacheslav Panasenko
 */
package com.panasenko.codepath.todoapp;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * EditItemActivity
 * The activity where a user can edit an item.
 */
public class EditItemActivity extends ActionBarActivity {

    public static final String EXTRA_ITEM_TEXT = "com.panasenko.codepath.todoapp.EXTRA_ITEM_TEXT";
    public static final String EXTRA_ITEM_POSITION = "com.panasenko.codepath.todoapp.EXTRA_ITEM_POSITION";

    private EditText mTextInput;
    private String mOriginalText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);
        mTextInput = (EditText) findViewById(R.id.item_input);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(EXTRA_ITEM_TEXT)) {
            mOriginalText = extras.getString(EXTRA_ITEM_TEXT);
            mTextInput.setText(mOriginalText);
            mTextInput.setSelection(mOriginalText.length());
        } else {
            // Something went terribly wrong, show error toast notification and finish activity
            Toast.makeText(this, R.string.error_no_extra, Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    /**
     * Called when 'Save' button is pressed, validates input and saves the data.
     * @param v Reference to a caller view.
     */
    public void onSaveClicked(View v) {
        String newText = mTextInput.getText().toString();
        if (newText.isEmpty() || newText.equals(mOriginalText)) {
            // Just ignore any changes
            setResult(RESULT_CANCELED);
        } else {
            Intent result = new Intent();
            result.putExtra(EXTRA_ITEM_TEXT, newText);
            result.putExtra(EXTRA_ITEM_POSITION, getIntent().getIntExtra(EXTRA_ITEM_POSITION, 0));
            setResult(RESULT_OK, result);
        }

        finish();
    }
}
