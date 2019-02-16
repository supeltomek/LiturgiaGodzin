package com.example.liturgiagodzin;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ThirdActivity extends ActionBarActivity {

    private String chosenLink = "";
    private String chosenPrayer = "";
    private LinearLayout linearLayout;
    private LinearLayout linearLayoutReload;
    private Button btnReload;

    private WebView prayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_third);
        Intent sender = getIntent();
        chosenLink = sender.getExtras().getString("chosenLink");
        chosenPrayer = sender.getExtras().getString("chosenPrayer");

        linearLayout = (LinearLayout)findViewById(R.id.linlaHeaderProgress);
        linearLayoutReload = (LinearLayout)findViewById(R.id.linlaProgBtn);
        btnReload = (Button)findViewById(R.id.btnReloadLayout);

        prayer = (WebView)findViewById(R.id.webView);

        TextView textView = (TextView)findViewById(R.id.textViewtitle);
        textView.setText(PrepareContent.getInstance().getDislayDate());

        Button next = (Button)findViewById(R.id.button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btnBack = (Button)findViewById(R.id.buttonback);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SecondActivity.class);
                intent.putExtra("chosenLink", chosenLink);
                startActivityForResult(intent, 0);
                finish();
            }
        });

        TextView txt = (TextView)findViewById(R.id.textView);
        txt.setText(chosenPrayer);


        prayer.setBackgroundColor(Color.TRANSPARENT);
        prayer.setClickable(false);
        prayer.getSettings().setBuiltInZoomControls(true);
        prayer.getSettings().setSupportZoom(true);
        prayer.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        prayer.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        new RetrivePage().execute(chosenLink);
    }

    private String czytanie(Document jsoupDoc){
        String prayer = "";
        Elements content = jsoupDoc.select("div[align=right][style] b");
        for(Element e : content){
            prayer = prayer + e.html(); //e.removeAttr("a[href]");
        }

        return "<div style=\"font-size:10pt\"><b>" + prayer + "</b></font></div>";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getBaseContext(), SecondActivity.class);
        intent.putExtra("chosenLink", chosenLink);
        startActivityForResult(intent, 0);
        finish();
    }
    private class RetrivePage extends AsyncTask<String, Void, Document> {

        private Exception exception;

        private StableArrayAdapter adapter;

        protected Document doInBackground(String... urls) {

            try {
                Document doc = Jsoup.connect(urls[0]).get();

//                prayer.loadDataWithBaseURL(null, preparePrayer(doc), "text/html", "utf-8", "about:blank");

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
                if(chosenLink.contains("godzczyt")){
                    prayer.loadDataWithBaseURL(null, PrepareContent.getInstance().prepareGodzCzyt(document), "text/html", "utf-8", "about:blank");
                }
                else{
                    prayer.loadDataWithBaseURL(null, PrepareContent.getInstance().preparePrayer(document), "text/html", "utf-8", "about:blank");
                }

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
