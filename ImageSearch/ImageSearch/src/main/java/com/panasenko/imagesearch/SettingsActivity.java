/**
 * File: SettingsActivity.java
 * Created: 1/24/14
 * Author: Viacheslav Panasenko
 */
package com.panasenko.imagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * SettingsActivity
 * Advanced search settings activity.
 */
public class SettingsActivity extends ActionBarActivity {

    public static final String EXTRA_FILTER = "com.panasenko.imagesearch.EXTRA_FILTER";

    private Spinner sizeSpinner;
    private Spinner colorSpinner;
    private Spinner typeSpinner;
    private EditText siteFilterInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        initViews();
    }

    /**
     * Called when 'save' button is clicked.
     * @param v Reference to a caller view.
     */
    public void onSaveClicked(View v) {
        SearchFilter filter = new SearchFilter();
        filter.setImageColor(colorSpinner.getSelectedItem().toString());
        filter.setImageSize(sizeSpinner.getSelectedItem().toString());
        filter.setImageType(typeSpinner.getSelectedItem().toString());
        filter.setSiteFilter(siteFilterInput.getText().toString());

        Intent result = new Intent();
        result.putExtra(EXTRA_FILTER, filter);
        setResult(RESULT_OK, result);
        finish();
    }

    /**
     * Initializes the UI view elements.
     */
    private void initViews() {
        sizeSpinner = (Spinner) findViewById(R.id.image_size_spinner);
        colorSpinner = (Spinner) findViewById(R.id.image_color_spinner);
        typeSpinner = (Spinner) findViewById(R.id.image_type_spinner);
        siteFilterInput = (EditText) findViewById(R.id.site_filter_input);

        initSpinnerWithAdapter(sizeSpinner, R.array.image_sizes);
        initSpinnerWithAdapter(colorSpinner, R.array.color_filters);
        initSpinnerWithAdapter(typeSpinner, R.array.type_filters);
    }

    /**
     * Initializes given spinner with ArrayAdapter containing strings from given resource.
     * @param spinner Spinner to init.
     * @param arrayResourceId Id of the array resource with data for adapter.
     */
    private void initSpinnerWithAdapter(Spinner spinner, int arrayResourceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayResourceId,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


}
