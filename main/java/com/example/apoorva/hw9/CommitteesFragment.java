package com.example.apoorva.hw9;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class CommitteesFragment extends android.support.v4.app.Fragment {


    ArrayList<HashMap<String, String>> committeeList;
    ArrayList<String> senate_Ids,senate_Chambers,senate_Names,joint_Ids,joint_Chambers,joint_Names;
    String[] Senate_Ids,Senate_Chambers,Senate_Names,Joint_Ids,Joint_Chambers,Joint_Names;

    public CommitteesFragment() {
        // Required empty public constructor
    }


    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View commview = inflater.inflate(R.layout.fragment_committees, container, false);

        committeeList = new ArrayList<>();
        MainActivity act = (MainActivity) getActivity();
        JSONObject committee = act.getCommittees_json();

        TabHost host = (TabHost)commview.findViewById(R.id.tabHost3);

        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("HOUSE");
        spec.setContent(R.id.tab6);
        spec.setIndicator("HOUSE");
        host.addTab(spec);
        populateListview(commview, committee);


        //Tab 2
        spec = host.newTabSpec("SENATE");
        spec.setContent(R.id.tab7);
        spec.setIndicator("SENATE");
        host.addTab(spec);
        ListView listView= (ListView) commview.findViewById(R.id.senateView);
        CommitteesFragment.CustomerAdapter adapter=new CommitteesFragment.CustomerAdapter(getContext(),Senate_Ids,Senate_Names,Senate_Chambers);
        listView.setAdapter(adapter);

        //Tab 2
        spec = host.newTabSpec("JOINT");
        spec.setContent(R.id.tab8);
        spec.setIndicator("JOINT");
        host.addTab(spec);
        ListView listView2 = (ListView) commview.findViewById(R.id.jointView);
        CommitteesFragment.CustomerAdapter adapter2 =new CommitteesFragment.CustomerAdapter(getContext(),Joint_Ids,Joint_Names,Joint_Chambers);
        listView2.setAdapter(adapter2);


        TabWidget widget = host.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);
            v.setBackgroundResource(R.drawable.each_tab_background);
        }

        //Inflate the layout for this fragment
        return commview;
    }
    String fullname, party, state, district, partyState, bioguide_id, imageURL;
    String id,name,chamber;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void populateListview(View v, final JSONObject committee) {
        int k = 0, l = 0;
        final JSONObject commValues = new JSONObject();
        try {
            int count = 0;
            String fullname, party, state, district, partyState, bioguide_id, imageURL;

            final ArrayList<String> houseIds,houseNames,houseChambers;
            String[] HouseIds,HouseNames,HouseChambers;
            final JSONArray houseArray = committee.getJSONArray("results");
            for(int i=0;i<houseArray.length();i++){
                commValues.put(houseArray.getJSONObject(i).getString("committee_id"),houseArray.getJSONObject(i));
            }
            //sort committe based on committe name
            final JSONArray sortedComm = new JSONArray();
            final List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < houseArray.length(); i++) {
                jsonValues.add(houseArray.getJSONObject(i));
            }
            Collections.sort( jsonValues, new Comparator<JSONObject>() {
                //You can change "Name" with "ID" if you want to sort by ID
                private static final String KEY_NAME = "name";

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
                /*    if(valA.compareTo(valB)==0){

                        String lastA = new String();
                        String lastB = new String();

                        try {
                            lastA= (String)a.get("last_name");
                            lastB = (String) b.get("last_name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return lastA.compareTo(lastB);

                    }*/

                    return valA.compareTo(valB);
                    //if you want to change the sort order, simply use the following:
                    //return -valA.compareTo(valB);
                }
            });
           /* for (int i = 0; i < houseArray.length(); i++) {
                sortedComm.put(jsonValues.get(i));
            }
            final JSONArray senateArray = sortedComm;
            final JSONArray jointArray = sortedComm;
*/
            houseIds = new ArrayList<String>();
            houseNames = new ArrayList<String>();
            houseChambers = new ArrayList<String>();

            senate_Ids = new ArrayList<String>();
            senate_Names = new ArrayList<String>();
            senate_Chambers = new ArrayList<String>();

            joint_Ids = new ArrayList<String>();
            joint_Names = new ArrayList<String>();
            joint_Chambers = new ArrayList<String>();
            while (count < jsonValues.size()) {

                JSONObject JO = jsonValues.get(count);
                id = JO.getString("committee_id");
                fullname = JO.getString("name");
                chamber = JO.getString("chamber");

                if(JO.getString("chamber").equals("house")) {
                    houseIds.add(id);
                    houseNames.add(fullname);
                    houseChambers.add(chamber);
                }
                if(JO.getString("chamber").equals("senate")) {

                    senate_Ids.add(id);
                    senate_Names.add(fullname);
                    senate_Chambers.add(chamber);
                    k++;
                } else if(JO.getString("chamber").equals("joint")) {

                    joint_Ids.add(id);
                    joint_Names.add(fullname);
                    joint_Chambers.add(chamber);
                    l++;
                }

                count++;
            }
            ListView listView= (ListView) v.findViewById(R.id.houseView);

            HouseIds = new String[houseIds.size()];
            HouseIds = houseIds.toArray(HouseIds);
            HouseNames = new String[houseNames.size()];
            HouseNames = houseNames.toArray(HouseNames);
            HouseChambers = new String[houseChambers.size()];
            HouseChambers = houseChambers.toArray(HouseChambers);


            Senate_Ids = new String[senate_Ids.size()];
            Senate_Ids = senate_Ids.toArray(Senate_Ids);
            Senate_Names = new String[senate_Names.size()];
            Senate_Names = senate_Names.toArray(Senate_Names);
            Senate_Chambers = new String[senate_Chambers.size()];
            Senate_Chambers = senate_Chambers.toArray(Senate_Chambers);

            Joint_Ids = new String[joint_Ids.size()];
            Joint_Ids = joint_Ids.toArray(Joint_Ids);
            Joint_Names = new String[joint_Names.size()];
            Joint_Names = joint_Names.toArray(Joint_Names);
            Joint_Chambers = new String[joint_Chambers.size()];
            Joint_Chambers = joint_Chambers.toArray(Joint_Chambers);

            CommitteesFragment.CustomerAdapter adapter=new CustomerAdapter(getContext(),HouseIds,HouseNames,HouseChambers);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(getContext(),"clicked", Toast.LENGTH_SHORT);
                    try {
                        JSONObject jsonObject = (JSONObject) commValues.get(houseIds.get(i));
                        Intent intentApp = new Intent(getActivity(),CommitteeDetails.class);
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
            ListView listView2 = (ListView) v.findViewById(R.id.jointView);
            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(getContext(),"clicked", Toast.LENGTH_SHORT);
                    try {
                        JSONObject jsonObject = (JSONObject) commValues.get(joint_Ids.get(i));
                        Intent intentApp = new Intent(getActivity(),CommitteeDetails.class);
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


            ListView listViewSenate= (ListView) v.findViewById(R.id.senateView);
                listViewSenate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(getContext(),"clicked", Toast.LENGTH_SHORT);
                    try {
                        JSONObject jsonObject = (JSONObject) commValues.get(senate_Ids.get(i));
                        Intent intentApp = new Intent(getActivity(),CommitteeDetails.class);
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
            super(context, R.layout.committee_list_item, names);
            this.c = context;
            this.images = images;
            this.names = names;
            this.psd = psd;
        }

        public class viewHolder {
            TextView nameView;
            TextView chamberView;
            TextView commIDView;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.committee_list_item, null);

            }
            final CommitteesFragment.CustomerAdapter.viewHolder holder = new CommitteesFragment.CustomerAdapter.viewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            holder.chamberView = (TextView) convertView.findViewById(R.id.partyState);
            holder.commIDView = (TextView) convertView.findViewById(R.id.committeeID);

            holder.commIDView.setText(images[position]);
            holder.nameView.setText(psd[position]);
            holder.chamberView.setText(names[position]);

            return convertView;
        }
    }
}
