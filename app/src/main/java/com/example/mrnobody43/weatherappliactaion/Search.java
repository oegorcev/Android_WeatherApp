package com.example.mrnobody43.weatherappliactaion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import Util.Utills;

public class Search extends AppCompatActivity {

    public ListView listView;
    public EditText search;
    ArrayList<String> list;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = (EditText) findViewById(R.id.search);
        listView = (ListView) findViewById(R.id.list_of_cities);
        initList();
        listView.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.toString().equals("")) {
                    list.clear();
                } else {
                    searchItem(s.toString());
               }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void initList() {
        items = new ArrayList<>(MainActivity.cityNameArray);
        list = new ArrayList<>(items);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                TextView textView = (TextView) itemClicked;
                String strText = textView.getText().toString();
                String[] words = strText.split(" ");

                Intent intent = new Intent();

                if (words[1].equals("Hurzuf")) {
                    intent.putExtra(Utills.CODE, "707860");
                }
                else {
                    intent.putExtra(Utills.CODE, words[0]);
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void searchItem(String text) {
       list.clear();
        for (String item : items) {
            if (item.contains(text)) {
                list.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
