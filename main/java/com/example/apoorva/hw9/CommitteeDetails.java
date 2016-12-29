package com.example.apoorva.hw9;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

public class CommitteeDetails extends Activity {

    TextView commIDText,commNameText,commChamberText,commParentText,commContactText,commOfficeText;
    ImageView toolbarBack,star,partyImage;
    JSONObject jsonObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee_details);


        try {
            jsonObj = new JSONObject(getIntent().getBundleExtra("jsonObject").getString("jsonObject"));
            CommitteeJSON committeeJSON = setJSONValue(jsonObj);
            setImagesOnClick(committeeJSON);
            setValueOfJSON(committeeJSON);

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


    public void setImagesOnClick(final CommitteeJSON committeeJSON) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        star=(ImageView)findViewById(R.id.star);
        if( MainActivity.fav_committee_json == null|| !MainActivity.fav_committee_json.has(committeeJSON.getCommittee_id())) {
            star.setImageResource(R.drawable.whitestar);
        } else {
            star.setImageResource(R.drawable.yellowstar);
        }

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CHANGE STAR COLOR IF REQUIRED
                if(!MainActivity.fav_committee_json.has(committeeJSON.getCommittee_id())){
                    try {
                        MainActivity.fav_committee_json.put(committeeJSON.getCommittee_id(), jsonObj.toString() );

                        SharedPreferences settings = getSharedPreferences("FavFile", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("committee", MainActivity.fav_committee_json.toString());
                        editor.commit();
                            /*Toast.makeText(this, MainActivity.fav_legislator_json.toString(), Toast.LENGTH_SHORT).show()*/
                        Log.d("committee", MainActivity.fav_committee_json.toString());
                        star.setImageResource(R.drawable.yellowstar);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    MainActivity.fav_committee_json.remove(committeeJSON.getCommittee_id());
                    SharedPreferences settings = getSharedPreferences("FavFile", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("committee", MainActivity.fav_committee_json.toString());
                    editor.commit();
                    star.setImageResource(R.drawable.whitestar);
                }
            }
        });



         if(committeeJSON.getChamber().equals("house")) {
            partyImage = (ImageView) findViewById(R.id.partyImage);
            partyImage.setImageResource(R.drawable.house_rep);
         } else  {
            partyImage = (ImageView) findViewById(R.id.partyImage);
            partyImage.setImageResource(R.drawable.senate);
         }

    }

    public void setValueOfJSON(CommitteeJSON committeeJSON) {

        commNameText = (TextView)findViewById(R.id.commNameValue);
        commNameText.setText(committeeJSON.getName()== "null" ? "N.A" :committeeJSON.getName());
        System.out.println(commNameText);

        commIDText = (TextView)findViewById(R.id.commIdValue);
        commIDText.setText(committeeJSON.getCommittee_id()== "null" ? "N.A" :committeeJSON.getCommittee_id());
        System.out.println(commIDText);

        commChamberText = (TextView)findViewById(R.id.commChamberValue);
        commChamberText.setText(committeeJSON.getChamber()== "null" ? "N.A" :committeeJSON.getChamber());
        System.out.println(commChamberText);

        commParentText = (TextView)findViewById(R.id.parentCommitteeValue);
        commParentText.setText(committeeJSON.getParent_committee()== "null" ? "N.A" :committeeJSON.getParent_committee());
        System.out.println(commParentText);

        commContactText = (TextView)findViewById(R.id.commContactValue);
        commContactText.setText(committeeJSON.getContact()== "null" ? "N.A" :committeeJSON.getContact());
        System.out.println(commContactText);

        commOfficeText = (TextView)findViewById(R.id.commOfficeValue);
        commOfficeText.setText(committeeJSON.getOffice()== "null" ? "N.A" :committeeJSON.getOffice());
        System.out.println(commOfficeText);
    }

    public static Bitmap LoadImageFromWebOperations(String url) {
        try {
            return BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
        } catch (Exception e) {
            return null;
        }
    }

    public CommitteeJSON setJSONValue(JSONObject obj) throws Exception{

        CommitteeJSON committeeJSON = new CommitteeJSON();

        try {
            committeeJSON.setCommittee_id(obj.getString("committee_id"));
        }catch(Exception e){
            committeeJSON.setCommittee_id("null");
        }

        try {
            committeeJSON.setName(obj.getString("name"));
        }catch(Exception e){
            committeeJSON.setName("null");
        }

        try {
            committeeJSON.setChamber(obj.getString("chamber"));
        }catch(Exception e){
            committeeJSON.setChamber("null");
        }

        try {
            committeeJSON.setParent_committee(obj.getString("parent_committee_id"));
        }catch(Exception e){
            committeeJSON.setParent_committee("null");
        }

        try {
            committeeJSON.setContact(obj.getString("phone"));
        }catch(Exception e){
            committeeJSON.setContact("null");
        }

        try {
            committeeJSON.setOffice(obj.getString("office"));
        }catch(Exception e){
            committeeJSON.setOffice("null");
        }

        return committeeJSON;

    }

}
