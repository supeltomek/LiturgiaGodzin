package com.example.liturgiagodzin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainActivity extends ActionBarActivity {

    public String chosenLink = "";
    private boolean doubleBackToExitPressedOnce = false;
    private LinearLayout linearLayout;
    private LinearLayout linearLayoutReload;
    private Button btnReload;
    private ListView listview;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        textView.setText(PrepareContent.getInstance().getDislayDate());

        final Button btnInfo = (Button)findViewById(R.id.button);
        btnInfo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, android.app.ActionBar.LayoutParams.WRAP_CONTENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT);

                Button dismiss = (Button)popupView.findViewById(R.id.button);
                dismiss.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            popupWindow.showAtLocation(btnInfo.getRootView(), Gravity.CENTER, 0,0);
            }
        });


        listview = (ListView) findViewById(R.id.listView);
        listview.setCacheColorHint(Color.TRANSPARENT);
        linearLayout = (LinearLayout)findViewById(R.id.linlaHeaderProgress);
        linearLayoutReload = (LinearLayout)findViewById(R.id.linlaProgBtn);
        btnReload = (Button)findViewById(R.id.btnReloadLayout);

        chosenLink = "http://www.brewiarz.pl/" + PrepareContent.getInstance().prepareDate() + "/index.php3";
        new RetrivePage().execute(chosenLink);

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Naciśnij ponownie w celu zamknięcia", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;

            }
        }, 2000);
    }


    private class RetrivePage extends AsyncTask<String, Void, Document> {

        private Exception exception;

        private StableArrayAdapter adapter;

        protected Document doInBackground(String... urls) {
            Document doc = null;
            try {
                doc = Jsoup.connect(urls[0]).get();
                PrepareContent.getInstance().prepareListView(doc);


                adapter = new StableArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, PrepareContent.getInstance().listNames){
                    @Override
                    public View getView(int position, View convertView,
                                        ViewGroup parent) {
                        View view =super.getView(position, convertView, parent);

                        TextView textView=(TextView) view.findViewById(android.R.id.text1);
                        textView.setTextColor(Color.WHITE);

                        return view;
                    }
                };

                return doc;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            linearLayoutReload.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Document document) {
            if(document != null){
                listview.setAdapter(adapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {
                        final String item = (String) parent.getItemAtPosition(position);
                        //Toast.makeText(getApplicationContext(), "Wciśnieto: " + listLinks.get(position), Toast.LENGTH_SHORT).show();
                        chosenLink = PrepareContent.getInstance().listLinks.get(position);
                        Intent intent = new Intent(view.getContext(), SecondActivity.class);
                        intent.putExtra("chosenLink", chosenLink);
                        //startActivity(intent);

                        startActivityForResult(intent, Activity.RESULT_OK);
                        finish();
                    }

                });
                linearLayout.setVisibility(View.GONE);
            }
            else{
                linearLayout.setVisibility(View.GONE);
                linearLayoutReload.setVisibility(View.VISIBLE);
                btnReload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        startActivity(getIntent());
                    }
                });
            }
        }
    }
}


