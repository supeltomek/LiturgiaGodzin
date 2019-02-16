package com.example.liturgiagodzin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SecondActivity extends ActionBarActivity {

    private ArrayList<String> chosenLinkList = new ArrayList<String>();
    private ArrayList<String> chosenPrayerList = new ArrayList<String>();

    private String chosenLink = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_second);
        Intent sender = getIntent();
        chosenLink = sender.getExtras().getString("chosenLink");
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(PrepareContent.getInstance().getDislayDate());

        Button next = (Button)findViewById(R.id.button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                startActivityForResult(intent, RESULT_OK);
                finish();

            }
        });

        chosenLink = chosenLink.substring(0, chosenLink.lastIndexOf("/")+1);
        preparePrayersList();

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setCacheColorHint(Color.TRANSPARENT);
        StableArrayAdapter adapter = adapter = new StableArrayAdapter(getBaseContext(),
                R.layout.prayerlist, R.id.textItem, chosenPrayerList){

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(R.id.textItem);
                textView.setTextColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);

                return view;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(view.getContext(), ThirdActivity.class);
                intent.putExtra("chosenLink", chosenLinkList.get(position));
                intent.putExtra("choosenPrayer", chosenPrayerList.get(position));

                startActivityForResult(intent, Activity.RESULT_OK);
                finish();
            }

        });


    }

    private void preparePrayersList(){
        String[] chosenLinkTab = new String[]{
                chosenLink + "wezw.php3",
                chosenLink + "jutrznia.php3",
                chosenLink + "godzczyt.php3",
                chosenLink + "modlitwa1.php3",
                chosenLink + "modlitwa2.php3",
                chosenLink + "modlitwa3.php3",
                chosenLink + "nieszpory.php3",
                chosenLink + "kompleta.php3"
        };
        for(int i = 0; i<chosenLinkTab.length; i++){
            chosenLinkList.add(chosenLinkTab[i]);
        }
        String[] choosenPrayers = new String[]{
                "Wezwanie",
                "Jutrznia",
                "Godzina czytań",
                "Modlitwa przedpołudniowa",
                "Modlitwa południowa",
                "Modlitwa popołudniowa",
                "Nieszpory",
                "Kompleta"
        };
        for(int i = 0; i<choosenPrayers.length; i++){
            chosenPrayerList.add(choosenPrayers[i]);
        }

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int layoutId, int textViewResourceId,
                                  List<String> objects) {
            super(context, layoutId, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getBaseContext() ,MainActivity.class);
        startActivityForResult(intent, RESULT_OK);
        finish();
    }

}
