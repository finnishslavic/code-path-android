package com.panasenko.codepath.todoapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TodoActivity extends ActionBarActivity {

    private static final int MAX_DUMMY_ITEMS = 10;
    private static final String TODO_FILE_NAME = "todo.txt";

    private ArrayList<String> mItems;
    private ArrayAdapter<String> mItemsAdapter;
    private ListView mListView;
    private EditText mNewItemInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        initView();
//        addDummyItems(5);
    }

    /**
     * Initializes view components, e.g. list views, creates adapters and listeners.
     */
    private void initView() {
        mListView = (ListView) findViewById(android.R.id.list);
        mNewItemInput = (EditText) findViewById(R.id.item_input);
        mItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mItems);
        mListView.setAdapter(mItemsAdapter);

        setUpListeners();
    }

    /**
     * Creates and defines listeners for UI components, e.g. list view.
     */
    private void setUpListeners() {
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                mItems.remove(position);
                mItemsAdapter.notifyDataSetInvalidated();
                saveItems();
                return true;
            }
        });
    }

    /**
     * Adds specified count of dummy items to the list.
     * @param count Count of items to be added (max = 10).
     */
    private void addDummyItems(int count) {
        if (mItems == null) {
            mItems = new ArrayList<String>();
        }

        if (count <= 0 || count > MAX_DUMMY_ITEMS) {
            count = MAX_DUMMY_ITEMS;
        }

        String[] numbers = getResources().getStringArray(R.array.ordered_numbers);
        for (int i = 0; i < count; i++) {
            mItems.add(numbers[i] + " item");
        }

        mItemsAdapter.notifyDataSetChanged();
    }

    /**
     * Called when the 'add' button clicked.
     * @param v Reference to a caller view.
     */
    public void onAddClicked(View v) {
        String text = mNewItemInput.getText().toString();
        if (!text.isEmpty()) {
            mItems.add(text);
            mItemsAdapter.notifyDataSetChanged();
            mNewItemInput.setText("");
            saveItems();
        } else {
            Toast.makeText(this, R.string.empty_item_warning, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Reads items from file.
     */
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, TODO_FILE_NAME);
        try {
            mItems = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            mItems = new ArrayList<String>();
            e.printStackTrace();
        }
    }

    /**
     * Writes items to file.
     */
    private void saveItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, TODO_FILE_NAME);
        try {
            FileUtils.writeLines(todoFile, mItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
