package com.example.apoorva.hw9;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class LegislatorFragment extends Fragment {


    public LegislatorFragment() {
        // Required empty public constructor
    }

    List<String> senate_images = new ArrayList<String>();
    List<String> senate_names = new ArrayList<String>();
    List<String> senate_psd = new ArrayList<String>();

    List<String> house_images = new ArrayList<String>();
    List<String> house_names = new ArrayList<String>();
    List<String> house_psd = new ArrayList<String>();

    String[] senateImageToSend;
    String[] senateNamesToSend;
    String[] senatePSDToSend;

    String[] houseImageToSend;
    String[] houseNamesToSend;
    String[] housePSDToSend;


    private ListView listViewLegislator;
    private LinearLayout sideIndexByState;

    private ListView listViewLegislatorHouse;
    private LinearLayout sideIndexByHouse;

    private ListView listViewLegislatorSenate;
    private LinearLayout sideIndexBySenate;


    ArrayList<HashMap<String, String>> legislatorList;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View legView = inflater.inflate(R.layout.fragment_legislator, container, false);

        legislatorList = new ArrayList<>();
        MainActivity act = (MainActivity) getActivity();
        JSONObject legislator = act.get_legislator_json();

        listViewLegislator = (ListView) legView.findViewById(R.id.myListView);

        listViewLegislatorHouse = (ListView) legView.findViewById(R.id.myListViewHouse);
        listViewLegislatorSenate = (ListView) legView.findViewById(R.id.myListViewSenate);

        sideIndexByState = (LinearLayout) legView.findViewById(R.id.side_index);
        sideIndexByHouse = (LinearLayout) legView.findViewById(R.id.side_index1);
        sideIndexBySenate = (LinearLayout) legView.findViewById(R.id.side_index2);

        TabHost host = (TabHost) legView.findViewById(R.id.tabHost);
        host.setup();
        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("BY STATE");
        spec.setContent(R.id.tab1);
        spec.setIndicator("BY STATE");
        host.addTab(spec);
        populateListview(legView, legislator, inflater);

        //Tab 2
        spec = host.newTabSpec("HOUSE");
        spec.setContent(R.id.tab2);
        spec.setIndicator("HOUSE");
        host.addTab(spec);
        ListView listView= (ListView) legView.findViewById(R.id.myListViewHouse);
        CustomerAdapter adapter=new CustomerAdapter(getContext(),houseImageToSend,houseNamesToSend,housePSDToSend);
        listView.setAdapter(adapter);

        //Tab 3
        spec = host.newTabSpec("SENATE");
        spec.setContent(R.id.tab3);
        spec.setIndicator("SENATE");
        host.addTab(spec);
        ListView listView2 = (ListView) legView.findViewById(R.id.myListViewSenate);
        CustomerAdapter adapter2 =new CustomerAdapter(getContext(),senateImageToSend ,senateNamesToSend,senatePSDToSend);
        listView2.setAdapter(adapter2);

        TabWidget widget = host.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);
            v.setBackgroundResource(R.drawable.each_tab_background);
        }
        return legView;

    }
    String fullname, party, state, district, partyState, bioguide_id, imageURL;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void populateListview(View v, JSONObject legislator, LayoutInflater inflater) {

        int k = 0, l = 0,m=0,n=0;
        try {
            int count = 0;
            String fullname, party, state, district, partyState, bioguide_id, imageURL;
            String[] images;
            String[] names;
            String[] psd;
            String[] stateNames;
            final JSONArray jsonArray = legislator.getJSONArray("results");
            final JSONArray houseArray = jsonArray;
            final JSONArray senateArray = jsonArray;
            final JSONArray jsonArray1= jsonArray;


            images = new String[jsonArray.length()];
            names = new String[jsonArray.length()];
            psd = new String[jsonArray.length()];
            stateNames = new String[jsonArray.length()];

//            senate_images = new String[jsonArray.length()];
//            senate_names = new String[jsonArray.length()];
//            senate_psd = new String[jsonArray.length()];
//
//            house_images = new String[jsonArray.length()];
//            house_names = new String[jsonArray.length()];
//            house_psd = new String[jsonArray.length()];



            final JSONArray sortedJsonArray = new JSONArray();
            final JSONArray sortedJsonArrayHouseAndSenate = new JSONArray();
            final JSONArray sortedJsonArrayHouse=new JSONArray();
            final JSONArray sortedJsonArraySenate=new JSONArray();

            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonValues.add(jsonArray.getJSONObject(i));
            }

            List<JSONObject> jsonValuesHouseAndSenate = new ArrayList<JSONObject>();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonValuesHouseAndSenate.add(jsonArray.getJSONObject(i));
            }

            Collections.sort( jsonValues, new Comparator<JSONObject>() {
                //You can change "Name" with "ID" if you want to sort by ID
                private static final String KEY_NAME = "state_name";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();

                    try {
                        valA = (String) a.get(KEY_NAME);
                        valB = (String) b.get(KEY_NAME);
                    }
                    catch (JSONException e) {
                        //do something
                    }
                    if(valA.compareTo(valB)==0){

                        String lastA = new String();
                        String lastB = new String();

                        try {
                            lastA= (String)a.get("last_name");
                            lastB = (String) b.get("last_name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return lastA.compareTo(lastB);

                    }

                    return valA.compareTo(valB);
                    //if you want to change the sort order, simply use the following:
                    //return -valA.compareTo(valB);
                }
            });

            for (int i = 0; i < jsonArray.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }


            Collections.sort( jsonValuesHouseAndSenate, new Comparator<JSONObject>() {
                //You can change "Name" with "ID" if you want to sort by ID
                private static final String KEY_NAME = "last_name";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();

                    try {
                        valA = (String) a.get(KEY_NAME);
                        valB = (String) b.get(KEY_NAME);
                    }
                    catch (JSONException e) {
                        //do something
                    }


                    return valA.compareTo(valB);
                    //if you want to change the sort order, simply use the following:
                    //return -valA.compareTo(valB);
                }
            });

            for (int i = 0; i < jsonArray.length(); i++) {
                sortedJsonArrayHouseAndSenate.put(jsonValuesHouseAndSenate.get(i));
            }

            while (count < sortedJsonArrayHouseAndSenate.length()) {

                JSONObject JO = sortedJsonArrayHouseAndSenate.getJSONObject(count);
                fullname = JO.getString("last_name") + ", "+ JO.getString("first_name");
                party = JO.getString("party");
                state = JO.getString("state_name");
                if(!(JO.isNull("district")))
                {
                    district=JO.getString("district");

                }
                else
                {
                    district="NA";
                }
                partyState = "(" + party + ")" + state + " - " + "District " + district;
                bioguide_id = JO.getString("bioguide_id");
                imageURL = "https://theunitedstates.io/images/congress/225x275/" + bioguide_id + ".jpg";

                // images[count] = imageURL;
                //names[count]=fullname;
                //psd[count]=partyState;
                //stateNames[count]=state;

                if(JO.getString("chamber").equals("senate")) {

                    senate_images.add(imageURL);
                    senate_names.add(fullname);
                    senate_psd.add(partyState);
                    k++;
                } else if(JO.getString("chamber").equals("house")) {

                    house_images.add(imageURL);
                    house_names.add(fullname);
                    house_psd.add(partyState);
                    l++;
                }

                count++;
            }
            int count1=0;
            while (count1 < sortedJsonArray.length()) {

                JSONObject JO = sortedJsonArray.getJSONObject(count1);
                fullname = JO.getString("last_name") + ", " + JO.getString("first_name");
                party = JO.getString("party");
                state = JO.getString("state_name");
                if(!(JO.isNull("district")))
                {
                    district=JO.getString("district");

                }
                else
                {
                    district="NA";
                }
                partyState = "(" + party + ")" + state + " - " + "District " + district;
                bioguide_id = JO.getString("bioguide_id");
                imageURL = "https://theunitedstates.io/images/congress/225x275/" + bioguide_id + ".jpg";

                images[count1] = imageURL;
                names[count1]=fullname;
                psd[count1]=partyState;
                stateNames[count1]=state;

              /*  if(JO.getString("chamber").equals("senate")) {

                    senate_images[k] = imageURL;
                    senate_names[k] = fullname;
                    senate_psd[k] = partyState;
                    k++;
                } else if(JO.getString("chamber").equals("house")) {

                    house_images[l] = imageURL;
                    house_names[l] = fullname;
                    house_psd[l] = partyState;
                    l++;
                } */

                count1++;
            }

            senateImageToSend = new String[senate_images.size()];
            senateImageToSend = senate_images.toArray(senateImageToSend);

            senateNamesToSend = new String[senate_names.size()];
            senateNamesToSend = senate_names.toArray(senateNamesToSend);

            senatePSDToSend = new String[senate_psd.size()];
            senatePSDToSend = senate_psd.toArray(senatePSDToSend);

            houseImageToSend = new String[house_images.size()];
            houseImageToSend = house_images.toArray(houseImageToSend);

            houseNamesToSend = new String[house_names.size()];
            houseNamesToSend = house_names.toArray(houseNamesToSend);

            housePSDToSend = new String[house_psd.size()];
            housePSDToSend = house_psd.toArray(housePSDToSend);

            ListView listView= (ListView) v.findViewById(R.id.myListView);
            CustomerAdapter adapter=new CustomerAdapter(getContext(),images,names,psd);
            listView.setAdapter(adapter);

            final HashMap<String,Integer>mapIndex = new HashMap<String, Integer>();
            for (int i = 0; i < sortedJsonArray.length(); i++) {
                JSONObject JO = sortedJsonArray.getJSONObject(i);
                String state_name = (String) JO.get("state_name");
                if(state_name!=null) {
                    String index = state_name.substring(0, 1);
                    if (mapIndex.get(index) == null)
                        mapIndex.put(index, i);

                }
            }

            TextView textView;
            List<String> indexList = new ArrayList<String>(mapIndex.keySet());
            Collections.sort(indexList);
            for (String index : indexList) {
                textView = (TextView) inflater.inflate(
                        R.layout.item_side, null);
                Log.i("TAG_TEST","creating index for "+ index);
                textView.setText(index);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView selectedIndex = (TextView) view;
                        Log.i("MY_TAG","scrolling to position "+mapIndex.get(selectedIndex.getText()));

                        listViewLegislator.setSelection(mapIndex.get(selectedIndex.getText()));

                    }
                });

                sideIndexByState.addView(textView);
            }


            final HashMap<String,Integer>mapIndex1 = new HashMap<String, Integer>();
            final HashMap<String,Integer>mapIndex2 = new HashMap<String, Integer>();
            for (int i = 0; i < sortedJsonArrayHouseAndSenate.length(); i++) {

                JSONObject JO = sortedJsonArrayHouseAndSenate.getJSONObject(i);
                if (JO.get("chamber").equals("house"))
                {
                    sortedJsonArrayHouse.put(m,JO);
                    m++;
                }
            }

            for (int i = 0; i < sortedJsonArrayHouse.length(); i++) {

                JSONObject JO = sortedJsonArrayHouse.getJSONObject(i);
                String last_name = (String) JO.get("last_name");
                if(last_name!=null) {

                    String index = last_name.substring(0, 1);
                    if (mapIndex1.get(index) == null)
                        mapIndex1.put(index, i);

                }
            }
            for (int i = 0; i < sortedJsonArrayHouseAndSenate.length(); i++) {

                JSONObject JO = sortedJsonArrayHouseAndSenate.getJSONObject(i);
                if (JO.get("chamber").equals("senate"))
                {
                    sortedJsonArraySenate.put(n,JO);
                    n++;
                }
            }

            for (int i = 0; i < sortedJsonArraySenate.length(); i++) {

                JSONObject JO = sortedJsonArraySenate.getJSONObject(i);
                String last_name = (String) JO.get("last_name");
                if(last_name!=null) {

                    String index = last_name.substring(0, 1);

                    if(mapIndex2.get(index) == null)
                        mapIndex2.put(index,i);
                }
            }


            TextView textView1 ;
            List<String> indexList1 = new ArrayList<String>(mapIndex1.keySet());
            Collections.sort(indexList1);
            for (String index : indexList1) {
                textView1 = (TextView) inflater.inflate(
                        R.layout.item_side, null);
                Log.i("TAG_TEST","creating index for "+ index);
                textView1.setText(index);
                textView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView selectedIndex = (TextView) view;
                        Log.i("MY_TAG","scrolling to position "+mapIndex1.get(selectedIndex.getText()));

                        listViewLegislatorHouse.setSelection(mapIndex1.get(selectedIndex.getText()));

                    }
                });

                sideIndexByHouse.addView(textView1);
            }

            TextView textView2 ;
            List<String> indexList2 = new ArrayList<String>(mapIndex2.keySet());
            Collections.sort(indexList2);
            for (String index : indexList2) {
                textView2 = (TextView) inflater.inflate(
                        R.layout.item_side, null);
                Log.i("TAG_TEST","creating index for "+ index);
                textView2.setText(index);
                textView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView selectedIndex = (TextView) view;
                        Log.i("MY_TAG","scrolling to position "+mapIndex2.get(selectedIndex.getText()));

                        listViewLegislatorSenate.setSelection(mapIndex2.get(selectedIndex.getText()));

                    }
                });

                sideIndexBySenate.addView(textView2);
            }





            listViewLegislator.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ////Toast.makeText(getContext(),"clicked", //Toast.LENGTH_SHORT);
                    try {
                        JSONObject jsonObject = sortedJsonArray.getJSONObject(i);
                        Intent intentApp = new Intent(getActivity(),LegislatorDetails.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("jsonObject",jsonObject.toString());
                        intentApp.putExtra("jsonObject",bundle);
                        getActivity().startActivityForResult(intentApp, 1);

                    }
                    catch(Exception e) {
                        e.getStackTrace();
                    }
                }
            });

            listViewLegislatorHouse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ////Toast.makeText(getContext(),"clicked", //Toast.LENGTH_SHORT);
                    try {
                        JSONObject jsonObject = sortedJsonArrayHouse.getJSONObject(i);
                        Intent intentApp = new Intent(getActivity(),LegislatorDetails.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("jsonObject",jsonObject.toString());
                        intentApp.putExtra("jsonObject",bundle);
                        getActivity().startActivityForResult(intentApp, 1);

                    }
                    catch(Exception e) {
                        e.getStackTrace();
                    }
                }
            });

            listViewLegislatorSenate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ////Toast.makeText(getContext(),"clicked", //Toast.LENGTH_SHORT);
                    try {
                        JSONObject jsonObject = sortedJsonArraySenate.getJSONObject(i);
                        Intent intentApp = new Intent(getActivity(),LegislatorDetails.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("jsonObject",jsonObject.toString());
                        intentApp.putExtra("jsonObject",bundle);
                        getActivity().startActivityForResult(intentApp, 1);

                    }
                    catch(Exception e) {
                        e.getStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static class CustomerAdapter extends ArrayAdapter<String> {
        Context c;
        String[] images = {};
        String[] names = {};
        String[] psd = {};
        LayoutInflater inflater;

        public CustomerAdapter(Context context, String[] images, String[] names, String[] psd) {
            super(context, R.layout.leg_list_item, names);
            this.c = context;
            this.images = images;
            this.names = names;
            this.psd = psd;
        }

        public class viewHolder {
            TextView nameView;
            TextView psdView;
            ImageView imageView;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.leg_list_item, null);

            }
            final viewHolder holder = new viewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            holder.psdView = (TextView) convertView.findViewById(R.id.partyState);
            holder.imageView = (ImageView) convertView.findViewById(R.id.legImage);


            Picasso.with(this.c).load(images[position]).into(holder.imageView);
            holder.psdView.setText(psd[position]);
            holder.nameView.setText(names[position]);

            return convertView;
        }
    }



}
