package com.example.liturgiagodzin;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ts250231 on 12.12.13.
 */
public class PrepareContent {

    public ArrayList<String> listLinks = new ArrayList<String>();
    public ArrayList<String> listNames = new ArrayList<String>();
    public String chosenLink = "";


    public static PrepareContent instance;

    public static PrepareContent getInstance(){
        if(instance == null){
            instance = new PrepareContent();
        }
        return instance;
    }


    public String prepareListView(Document content){

        List<String> links;
        listLinks.clear();
        listNames.clear();

        chosenLink = "http://www.brewiarz.pl/" + prepareDate() + "/index.php3";
        links = prepareLinks(content);
        if(links != null){
            for(int i = 0; i< links.size(); i = i+2){
                if(links.get(i) == ""){
                    listLinks.add(chosenLink);
                }
                else{
                    listLinks.add(links.get(i));
                }
            }
            for(int i = 1; i<links.size(); i = i + 2){
                listNames.add(links.get(i));
            }
        }
        return null;
    }

    private List<String> prepareLinks(Document docJsoup){
        List<String> listLinks = new ArrayList<String>();

        Element content = docJsoup.select("table[border=0][width=490][align=center]").first();
        Elements links = content.select("div");

        for(Element e : links){
            listLinks.add(e.select("a[href]").attr("abs:href"));
            listLinks.add(e.text());
        }

        return listLinks;
    }

    public String preparePrayer(Document jsoupDoc){
        String prayer = "";
        Elements content = jsoupDoc.select("td[class=ww]");

        content.select("img").remove();
        content.select("div[align=center][style]").remove();
        for(int i =0; i<content.size(); i++){
            prayer = prayer + content.get(i).html();
        }
        prayer = prayer.replaceAll("\\<a href=.*\">", "");
        prayer = prayer.replaceAll("\\</a>", "");
        prayer = prayer.replace("color=\"red\"", "color=\"black\"");
        prayer = prayer.replace("color=\"Red\"", "color=\"black\"");
        prayer = prayer.replace("; color:black", "");
        prayer = prayer.replace("; color:red", "");
        prayer = prayer.replace(" style=\"font-size:10pt\"", "");
        prayer = prayer.replace(";font-size:7.5pt", "");
        prayer = prayer.replace("<a name=\"modl\">", "</div>");
        prayer = "<html><head><style type=\"text/css\">div{color: #FFFFFF;}div.c{text-indent:20pt;}"
                + "</style></head>"
                + "<body>"
                + prayer
                + "</body></html>";

        return prayer;
    }
    public String prepareGodzCzyt(Document jsoupDoc){
        String prayer = "";
        jsoupDoc.getElementById("def2").remove();
        Elements content = jsoupDoc.select("td[class=ww]");

        content.select("img").remove();
        content.select("div[align=center][style]").remove();
        for(int i =0; i<content.size(); i++){
            prayer = prayer + content.get(i).html();
        }
        prayer = prayer.replaceAll("\\<a href=.*\">", "");
        prayer = prayer.replaceAll("\\</a>", "");
        prayer = prayer.replace("color=\"red\"", "color=\"black\"");
        prayer = prayer.replace("color=\"Red\"", "color=\"black\"");
        prayer = prayer.replace("; color:black", "");
        prayer = prayer.replace("; color:red", "");
        prayer = prayer.replace(" style=\"font-size:10pt\"", "");
        prayer = prayer.replace(";font-size:7.5pt", "");
        prayer = prayer.replace("<a name=\"modl\">", "</div>");
        prayer = "<html><head><style type=\"text/css\">div{color: #FFFFFF;}div.c{text-indent:20pt;"
                + "</style></head>"
                + "<body>"
                + prayer
                + "</body></html>";

        return prayer;
    }

    public String prepareDate() {
        DateFormat date = new SimpleDateFormat("yyMMdd");
        Date dt = new Date();
        String currentDate = date.format(dt);

        String year = currentDate.substring(0, 2);
        String month = currentDate.substring(2, 4);
        String day = currentDate.substring(4, 6);

        return prepareRomanMonth(Integer.parseInt(month)) + "_" + year + "/" + day + month;
    }

    public String getDislayDate(){

        Calendar today = Calendar.getInstance();
        String dayName = today.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);

        if(dayName.equals("Monday"))
            dayName = "Poniedziałek";
        else if(dayName.equals("Tuesday"))
            dayName = "Wtorek";
        else if(dayName.equals("Wednesday"))
            dayName = "Sroda";
        else if(dayName.equals("Thursday"))
            dayName = "Czwartek";
        else if(dayName.equals("Friday"))
            dayName = "Piątek";
        else if(dayName.equals("Saturday"))
            dayName = "Sobota";
        else if(dayName.equals("Sunday"))
            dayName = "Niedziela";

        DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        Date dt = new Date();
        String currentDate = date.format(dt);


        return dayName + "\n" + currentDate;
    }

    private String prepareRomanMonth(int date){
        String month = "";
        switch(date){
            case 1: month = "i"; break;
            case 2:	month = "ii"; break;
            case 3:	month = "iii"; break;
            case 4:	month = "iv"; break;
            case 5:	month = "v"; break;
            case 6:	month = "vi"; break;
            case 7:	month = "vii"; break;
            case 8:	month = "viii"; break;
            case 9:	month = "ix"; break;
            case 10: month = "x"; break;
            case 11: month = "xi"; break;
            case 12: month = "xii"; break;
        }
        return month;
    }
}
