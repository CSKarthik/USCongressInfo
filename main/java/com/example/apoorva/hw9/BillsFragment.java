package com.example.apoorva.hw9;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BillsFragment extends Fragment {


    public BillsFragment() {
        // Required empty public constructor
    }

    String[] active_bill_name;
    String[] active_bill_short_title;
    String[] active_bill_intro_on;

    String[] new_bill_name;
    String[] new_bill_short_title;
    String[] new_bill_intro_on;

//    ArrayList<HashMap<String, String>> BillsList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View billView = inflater.inflate(R.layout.fragment_bills, container, false);

        //BillsList = new ArrayList<>();
        MainActivity act = (MainActivity) getActivity();
        JSONObject bills = act.get_bills_json();

        TabHost host = (TabHost) billView.findViewById(R.id.tabHost2);

        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("ACTIVE BILLS");
        spec.setContent(R.id.tab4);
        spec.setIndicator("ACTIVE BILLS");
        host.addTab(spec);

        try {
            JSONObject temp = bills.getJSONObject("resultActive");
            populateListviewActive(billView, temp, 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Tab 2
        spec = host.newTabSpec("NEW BILLS");
        spec.setContent(R.id.tab5);
        spec.setIndicator("NEW BILLS");
        host.addTab(spec);
        try {
            JSONObject temp = bills.getJSONObject("resultNew");
            populateListviewNew(billView, temp, 2);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TabWidget widget = host.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);
            v.setBackgroundResource(R.drawable.each_tab_background);
        }
        //Inflate the layout for this fragment
        return billView;
    }
    public void populateListviewActive(View v, JSONObject bill, int tab_id ) {
        try {
            JSONArray jsonArr = bill.getJSONArray("results");
            ArrayList<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < jsonArr.length(); i++) {
                jsonValues.add(jsonArr.getJSONObject(i));
            }
            Collections.sort( jsonValues, new Comparator<JSONObject>() {
                //You can change "Name" with "ID" if you want to sort by ID
                private static final String KEY_NAME = "introduced_on";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();
                    Long dateTime1=0L,dateTime2=0L;

                    try {
                        valA = (String) a.get(KEY_NAME);
                        valB = (String) b.get(KEY_NAME);

                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        dateTime1 =  df.parse(valA).getTime();
                        dateTime2 = df.parse(valB).getTime();
                    }
                    catch (Exception e) {
                        //do something
                    }
                    return dateTime1.compareTo(dateTime2);
                    //if you want to change the sort order, simply use the following:
                    //return -valA.compareTo(valB);
                }
            });


            active_bill_name = new String[ jsonArr.length() ];
            active_bill_intro_on = new String[ jsonArr.length() ];
            active_bill_short_title = new String[ jsonArr.length() ];
            Collections.reverse(jsonValues);
            final ArrayList<JSONObject> jsonArray2 = jsonValues;
            for( int i=0; i< jsonValues.size(); i++) {
                JSONObject each = jsonValues.get(i);
                HashMap<String, String> eachObj = new HashMap<>();
                eachObj.put("bill_id", each.getString("bill_id"));
                eachObj.put("short_title", each.getString("short_title"));

                java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.text.SimpleDateFormat toFormat = new java.text.SimpleDateFormat("MMM dd, yyyy");
                //eachObj.put("introduced_on", toFormat.format(format.parse( each.getString("introduced_on") )));

                eachObj.put("introduced_on", each.getString("introduced_on"));

                active_bill_name[i] = each.getString("bill_id").toUpperCase();
                if( each.isNull("short_title") )
                    active_bill_short_title[i] = each.getString("official_title");
                else
                    active_bill_short_title[i] = each.getString("short_title");

                active_bill_intro_on[i] = toFormat.format(format.parse( each.getString("introduced_on")));

                //BillsList.add(eachObj);
            }
            //ListView listView= (ListView) v.findViewById(R.id.ActiveBillsList);
//            ListAdapter adapter = new SimpleAdapter(
//                    getContext(),
//                    BillsList,
//                    R.layout.bills_list_item,
//                    new String[]{"bill_id", "short_title", "introduced_on"}, new int[]{R.id.bill_id, R.id.bill_short_title, R.id.bill_intro_date});
//            listView.setAdapter(adapter);

            ListView listView= (ListView) v.findViewById(R.id.active_bills_list);
            BillsCustomAdapter adapter=new BillsCustomAdapter(getContext(),active_bill_name,active_bill_short_title,active_bill_intro_on);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(getContext(),"clicked", Toast.LENGTH_SHORT);
                    try {
                        JSONObject jsonObject = jsonArray2.get(i);
                        Intent intentApp = new Intent(getActivity(),BillDetails.class);
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
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void populateListviewNew(View v, JSONObject bill, int tab_id ) {
        try {
            JSONArray jsonArr = bill.getJSONArray("results");
            final JSONArray sortedNew = new JSONArray();
            final List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < jsonArr.length(); i++) {
                jsonValues.add(jsonArr.getJSONObject(i));
            }
            Collections.sort( jsonValues, new Comparator<JSONObject>() {
                //You can change "Name" with "ID" if you want to sort by ID
                private static final String KEY_NAME = "introduced_on";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();
                    Long dateTime1=0L,dateTime2=0L;

                    try {
                        valA = (String) a.get(KEY_NAME);
                        valB = (String) b.get(KEY_NAME);

                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        dateTime1 =  df.parse(valA).getTime();
                        dateTime2 = df.parse(valB).getTime();
                    }
                    catch (Exception e) {
                        //do something
                    }
                    return dateTime2.compareTo(dateTime1);
                    //if you want to change the sort order, simply use the following:
                    //return -valA.compareTo(valB);
                }
            });


            new_bill_name = new String[ jsonArr.length() ];
            new_bill_intro_on = new String[ jsonArr.length() ];
            new_bill_short_title = new String[ jsonArr.length() ];

            for( int i=0; i< jsonValues.size(); i++) {
                JSONObject each = jsonValues.get(i);

                new_bill_name[i] = each.getString("bill_id").toUpperCase();
                if( each.isNull("short_title") )
                    new_bill_short_title[i] = each.getString("official_title");
                else
                    new_bill_short_title[i] = each.getString("short_title");
                new_bill_intro_on[i] = each.getString("introduced_on");
            }
            ListView listView= (ListView) v.findViewById(R.id.new_bills_list);
                BillsCustomAdapter adapter=new BillsCustomAdapter(getContext(),new_bill_name,new_bill_short_title,new_bill_intro_on);
                listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(getContext(),"clicked", Toast.LENGTH_SHORT);
                    try {
                        JSONObject jsonObject = jsonValues.get(i);
                        Intent intentApp = new Intent(getActivity(),BillDetails.class);
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


    public static class BillsCustomAdapter extends ArrayAdapter<String> {
        Context c;
        String[] bill_id = {};
        String[] short_title = {};
        String[] intro_on = {};
        LayoutInflater inflater;

        public BillsCustomAdapter(Context context, String[] bill_id, String[] short_title, String[] intro_on) {
            super(context, R.layout.bills_list_item, bill_id);
            this.c = context;
            this.bill_id = bill_id;
            this.short_title = short_title;
            this.intro_on = intro_on;
        }

        public class viewHolder {
            TextView bill_id;
            TextView bill_short_title;
            TextView bill_intro_date;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.bills_list_item, null);

            }
            final viewHolder holder = new viewHolder();
            holder.bill_id = (TextView) convertView.findViewById(R.id.bill_id);
            holder.bill_short_title = (TextView) convertView.findViewById(R.id.bill_short_title);
            holder.bill_intro_date = (TextView) convertView.findViewById(R.id.bill_intro_date);

            holder.bill_short_title.setText(short_title[position]);
            holder.bill_intro_date.setText(intro_on[position]);
            holder.bill_id.setText(bill_id[position]);

            return convertView;
        }
    }
}