package com.example.apoorva.hw9;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LegislatorDetails extends Activity {

    ImageView star,facebook,twitter,website, getImage,partyImage,toolbarBack;
    TextView partyText,name,nameValue,email,emailValue,chamber,chamberValue,contact,contactValue,startTerm,startTermValue,endTerm,endTermValue;
    TextView officeValue,stateValue,faxValue,birthdayValue,progressText;
    ProgressBar term;
    JSONObject jsonObj;
    String callFrom;

    String picture = "http://theunitedstates.io/images/congress/225x275/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legislator_details);

        try {
            //callFrom = getIntent().getStringExtra("callingFrom").toString();
            jsonObj = new JSONObject(getIntent().getBundleExtra("jsonObject").getString("jsonObject"));
            LegislatorJSON legislatorJSON = setJSONValue(jsonObj);
            setImagesOnClick(legislatorJSON);
            setValueOfJSON(legislatorJSON);

            toolbarBack = (ImageView)findViewById(R.id.toolbarBack);
            toolbarBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();

                    //finishActivity(1);
                }
            });
        } catch (Exception e) {
            System.out.print(e.getStackTrace());
        }
    }

        /*@Override
        protected void onPause() {
            this.finish();
        }*/

        /*@Override
        protected void onStop() {
            finishActivity(1);
        }*/


    public void setImagesOnClick(final LegislatorJSON legislatorJSON) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        star=(ImageView)findViewById(R.id.star);
        if( MainActivity.fav_legislator_json == null|| !MainActivity.fav_legislator_json.has(legislatorJSON.getBioguide_id())) {
            star.setImageResource(R.drawable.whitestar);
        } else {
            star.setImageResource(R.drawable.yellowstar);
        }

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CHANGE STAR COLOR IF REQUIRED
                if(!MainActivity.fav_legislator_json.has(legislatorJSON.getBioguide_id())){
                    try {
                        MainActivity.fav_legislator_json.put(legislatorJSON.getBioguide_id(), jsonObj.toString());

                        SharedPreferences settings = getSharedPreferences("FavFile", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("legislator", MainActivity.fav_legislator_json.toString()).apply();
                        editor.commit();
                            /*Toast.makeText(this, MainActivity.fav_legislator_json.toString(), Toast.LENGTH_SHORT).show()*/
                        Log.d("legislator", MainActivity.fav_legislator_json.toString());
                        star.setImageResource(R.drawable.yellowstar);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    MainActivity.fav_legislator_json.remove(legislatorJSON.getBioguide_id());

                    SharedPreferences settings = getSharedPreferences("FavFile", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("legislator", MainActivity.fav_legislator_json.toString()).apply();
                    editor.commit();
                    star.setImageResource(R.drawable.whitestar);
                }
            }
        });

        facebook=(ImageView)findViewById(R.id.facebook);
        facebook.setImageResource(R.drawable.facebook);


        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(legislatorJSON.getFacebook_id()== "null"){
                        Toast.makeText(getApplicationContext(), "No Facebook Account", Toast.LENGTH_SHORT).show();
                    } else{
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/"+legislatorJSON.getFacebook_id())));
                    }
                } catch(Exception e) {}
            }
        });

        twitter=(ImageView)findViewById(R.id.twitter);
        twitter.setImageResource(R.drawable.twitter);

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(legislatorJSON.getTwitter_id()== "null"){
                        Toast.makeText(getApplicationContext(),"No Twitter Account" , Toast.LENGTH_SHORT).show();
                    } else{
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + legislatorJSON.getTwitter_id())));
                    }
                } catch(Exception e) {}
            }
        });

        website=(ImageView)findViewById(R.id.website);
        website.setImageResource(R.drawable.world);

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(legislatorJSON.getWebsite()== "null"){
                        Toast.makeText(getApplicationContext(),"No Website" , Toast.LENGTH_SHORT).show();
                    } else{
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(legislatorJSON.getWebsite())));
                    }
                } catch(Exception e) {}
            }
        });

        getImage=(ImageView)findViewById(R.id.picture);
        getImage.setImageBitmap(LoadImageFromWebOperations(picture+legislatorJSON.getBioguide_id()+".jpg"));

        if(legislatorJSON.getChamber().equals("house")) {
            partyImage = (ImageView) findViewById(R.id.partyImage);
            partyImage.setImageResource(R.drawable.image_d);

            partyText = (TextView)findViewById(R.id.partyName);
            partyText.setText("Democratic");
            System.out.println(partyText);

        } else  {
            partyImage = (ImageView) findViewById(R.id.partyImage);
            partyImage.setImageResource(R.drawable.image_r);

            partyText = (TextView)findViewById(R.id.partyName);
            partyText.setText("Republic");
            System.out.println(partyText);
        }

        term = (ProgressBar) findViewById(R.id.progress);
        term.setProgress(legislatorJSON.getTermValue());
        progressText=(TextView)findViewById(R.id.progress_text);
        Integer termValue=legislatorJSON.getTermValue();
        progressText.setText(termValue.toString()+"%");
    }

    public void setValueOfJSON(LegislatorJSON legislatorJSON) {

        nameValue = (TextView)findViewById(R.id.nameValue);
        nameValue.setText(legislatorJSON.getTitle()+"."+ legislatorJSON.getLast_name()+"," + legislatorJSON.getFirst_name());
        System.out.println(nameValue);

        emailValue = (TextView)findViewById(R.id.emailValue);
        emailValue.setText(legislatorJSON.getOc_email() == "null" ? "N.A" : legislatorJSON.getOc_email());
        System.out.println(emailValue);

        chamberValue = (TextView)findViewById(R.id.chamberValue);
        chamberValue.setText(legislatorJSON.getChamber() == "null" ? "N.A" : legislatorJSON.getChamber());
        System.out.println(chamberValue);

        contactValue = (TextView)findViewById(R.id.contactValue);
        contactValue.setText(legislatorJSON.getPhone() =="null" ? "N.A" : legislatorJSON.getPhone());
        System.out.println(contactValue);

        startTermValue = (TextView)findViewById(R.id.startTermValue);
        startTermValue.setText(legislatorJSON.getTerm_start().toString());
        System.out.println(startTermValue == null ? "N.A" : startTermValue);

        endTermValue = (TextView)findViewById(R.id.endTermValue);
        endTermValue.setText(legislatorJSON.getTerm_end().toString() =="null" ? "N.A" : legislatorJSON.getTerm_end().toString());
        System.out.println(endTermValue);

        officeValue = (TextView)findViewById(R.id.officeValue);
        officeValue.setText(legislatorJSON.getOffice() == "null" ?"N.A" : legislatorJSON.getOffice());
        System.out.println(officeValue);

        stateValue = (TextView)findViewById(R.id.stateValue);
        stateValue.setText(legislatorJSON.getState_name() == "null" ?"N.A" : legislatorJSON.getState_name());
        System.out.println(stateValue);

        faxValue  = (TextView)findViewById(R.id.faxValue);
        faxValue.setText(legislatorJSON.getFax() == "null" ?"N.A" : legislatorJSON.getFax());
        System.out.println(faxValue);

        birthdayValue = (TextView)findViewById(R.id.birthdayValue);
        birthdayValue.setText(legislatorJSON.getBirthday().toString() == "null" ?"N.A" :legislatorJSON.getBirthday().toString());
        System.out.println(birthdayValue);

    }

    public static Bitmap LoadImageFromWebOperations(String url) {
        try {
            return BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
        } catch (Exception e) {
            return null;
        }
    }

    public LegislatorJSON setJSONValue(JSONObject obj) throws Exception{
        LegislatorJSON legislatorJSON = new LegislatorJSON();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat toFormat = new SimpleDateFormat("MMM dd, yyyy");

        try {
            legislatorJSON.setBioguide_id(obj.getString("bioguide_id"));
        }catch(Exception e){
            legislatorJSON.setBioguide_id("null");
        }

        try {
            legislatorJSON.setChamber(obj.getString("chamber"));
        } catch(Exception e){
            legislatorJSON.setChamber("null");
        }

        try{
            legislatorJSON.setBirthday(toFormat.format(format.parse(obj.getString("birthday"))));
        } catch (Exception e){
            legislatorJSON.setBirthday("null");
        }

        try{
            legislatorJSON.setFax(obj.getString("fax"));
        } catch (Exception e){
            legislatorJSON.setFax("null");
        }

        try{
            legislatorJSON.setFirst_name(obj.getString("first_name"));
        } catch (Exception e){
            legislatorJSON.setFirst_name("null");
        }

        try{
            legislatorJSON.setLast_name(obj.getString("last_name"));
        } catch (Exception e){
            legislatorJSON.setLast_name("null");
        }


        try{
            legislatorJSON.setTitle(obj.getString("title"));
        } catch (Exception e){
            legislatorJSON.setTitle("null");
        }

        try{
            legislatorJSON.setOc_email( obj.getString("oc_email") );
        } catch (Exception e){
            legislatorJSON.setOc_email("null");
        }

        try{
            legislatorJSON.setPhone(obj.getString("phone"));
        } catch (Exception e){
            legislatorJSON.setPhone("null");
        }

        try{
            legislatorJSON.setOffice(obj.getString("office"));
        } catch (Exception e){
            legislatorJSON.setOffice("null");
        }

        try{
            legislatorJSON.setState_name(obj.getString("state_name"));
        } catch (Exception e){
            legislatorJSON.setState_name("null");
        }

        try{
            legislatorJSON.setTerm_start(toFormat.format(format.parse(obj.getString("term_start"))));
        } catch (Exception e){
            legislatorJSON.setTerm_start("null");
        }


        try{
            legislatorJSON.setTerm_end(toFormat.format(format.parse(obj.getString("term_end"))));
        } catch (Exception e){
            legislatorJSON.setTerm_end("null");
        }

        try{
            legislatorJSON.setWebsite(obj.getString("website"));
        } catch (Exception e){
            legislatorJSON.setWebsite("null");
        }

        try{
            legislatorJSON.setFacebook_id(obj.getString("facebook_id"));
        } catch (Exception e){
            legislatorJSON.setFacebook_id("null");
        }

        try{
            legislatorJSON.setTwitter_id(obj.getString("twitter_id"));
        } catch (Exception e){
            legislatorJSON.setTwitter_id("null");
        }


        Long startTerm = format.parse(obj.getString("term_start")).getTime();
        Long endTerm = format.parse(obj.getString("term_end")).getTime();
        Long current = new Date().getTime();


        int percent = (int)(100*(current-startTerm)/(endTerm-startTerm));
        legislatorJSON.setTermValue(percent);

        return legislatorJSON;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if((resultCode == RESULT_OK) && (requestCode == 1)) {

        }
    }


}
