package com.example.apoorva.hw9;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment {

    JSONObject fav_leg = MainActivity.fav_legislator_json;
    JSONObject fav_bill = MainActivity.fav_bill_json;
    JSONObject fav_comm = MainActivity.fav_committee_json;
    View viewg;
    String bioguideIds[] = new String[600];
    String committeeIds[] = new String[600];
    String billIds[] = new String[600];
    HashMap<String,Integer> indexMap= new HashMap<String,Integer>();
    ListView favLegislatorList ;
    LinearLayout sideIndexFavLegislator;

    public FavouritesFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {

        super.onResume();


        if( fav_leg.length() == 0){
            ListView listView= (ListView) viewg.findViewById(R.id.fav_legislator_listing);
            listView.setAdapter(null);
        }else{
            populate_legislator( viewg, fav_leg );
        }
        if( fav_bill.length() == 0 ){
            ListView listView= (ListView) viewg.findViewById(R.id.fav_bill_listing);
            listView.setAdapter(null);
        }else{
            populate_bill( viewg, fav_bill );
        }
        if( fav_comm.length() == 0 ){
            ListView listView= (ListView) viewg.findViewById(R.id.fav_committee_listing);
            listView.setAdapter(null);
        }else{
            populate_committee( viewg, fav_comm );
        }


  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Toast.makeText(getActivity().getApplicationContext(), "oncreate", Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        View favView =  inflater.inflate(R.layout.fragment_favourites, container, false);
        viewg = favView;
        //MainActivity obj = new MainActivity();

        TabHost host = (TabHost) favView.findViewById(R.id.tabHost4);
        favLegislatorList=(ListView) favView.findViewById(R.id.fav_legislator_listing);
        sideIndexFavLegislator=(LinearLayout) favView.findViewById(R.id.side_index);

        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("LEGISLATORS");
        spec.setContent(R.id.tab9);
        spec.setIndicator("LEGISLATORS");
        host.addTab(spec);
        populate_legislator( favView, fav_leg );

        if(indexMap!=null && indexMap.size()>0){
            List<String> indexList1 = new ArrayList<String>(indexMap.keySet());
            Collections.sort(indexList1);
            for (String index : indexList1) {
                TextView textView1 = (TextView) inflater.inflate(
                        R.layout.item_side, null);
                Log.i("TAG_TEST","creating index for "+ index);
                textView1.setText(index);
                textView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView selectedIndex = (TextView) view;
                        Log.i("MY_TAG","scrolling to position "+indexMap.get(selectedIndex.getText()));

                        favLegislatorList.setSelection(indexMap.get(selectedIndex.getText()));

                    }
                });

                sideIndexFavLegislator.addView(textView1);
            }
        }

        //Tab 2
        spec = host.newTabSpec("BILLS");
        spec.setContent(R.id.tab10);
        spec.setIndicator("BILLS");
        host.addTab(spec);
        populate_bill(favView,fav_bill);

        //Tab 3
        spec = host.newTabSpec("COMMITTEES");
        spec.setContent(R.id.tab11);
        spec.setIndicator("COMMITTEES");
        host.addTab(spec);
        populate_committee( favView, fav_comm);

        TabWidget widget = host.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);
            v.setBackgroundResource(R.drawable.each_tab_background);
        }

        //Inflate the layout for this fragment
        return favView;
    }

  /*  @Override
    public void onResume() {
        Toast.makeText(getActivity().getApplicationContext(), "oncreate", Toast.LENGTH_SHORT).show();
    }*/

    public  void populate_legislator(View v, final JSONObject obj ) {
//        Log.d("","");
        String district,bioguide_id;
        int count = 0;
        JSONObject obj2= new JSONObject();
        LinkedHashMap<String,JSONObject> hashMap = new LinkedHashMap<String,JSONObject>();

        String[] images = new String[obj.length()];
        String[] names = new String[obj.length()];
        String[] psd = new String[obj.length()];
        Iterator<String> sortIterator = obj.keys();
        ListView listView= (ListView) v.findViewById(R.id.fav_legislator_listing);

        ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();

        while(sortIterator.hasNext()) {
            try {
                String keyValue = sortIterator.next();
                Object jsonObject =  new JSONObject((String)obj.get(keyValue));
                jsonObjects.add((JSONObject) jsonObject);
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        Collections.sort( jsonObjects, new Comparator<JSONObject>() {
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
                if(valA.compareTo(valB)==0){

                    String lastA = new String();
                    String lastB = new String();

                    try {
                        lastA= (String)a.get(KEY_NAME);
                        lastB = (String) b.get(KEY_NAME);
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



        for(int j=0;j<jsonObjects.size();j++) {
            String lastName=null;
            try{
                hashMap.put(jsonObjects.get(j).getString("bioguide_id"),jsonObjects.get(j));
                lastName= jsonObjects.get(j).getString("last_name");
            } catch (Exception e){

            }


            if(lastName!=null) {

                String index = lastName.substring(0, 1);
                if (indexMap.get(index) == null)
                    indexMap.put(index, j);

            }

        }





        Iterator<String> i = hashMap.keySet().iterator();

        while (i.hasNext()){
            String key = i.next();
            try {
                final JSONObject each = hashMap.get(key);
                if(!(each.isNull("district"))){
                    district=each.getString("district");
                }else{
                    district="NA";
                }
                bioguide_id = each.getString("bioguide_id");

                images[count] = "https://theunitedstates.io/images/congress/225x275/" + bioguide_id + ".jpg";;
                names[count] = each.getString("last_name") + ", " + each.getString("first_name");;
                psd[count] = "(" + each.getString("party") + ")" + each.getString("state_name") + " - " + "District " + district;
                bioguideIds[count] = bioguide_id;

                LegislatorFragment legislatorFragment = new LegislatorFragment();
                LegislatorFragment.CustomerAdapter adapter =  new LegislatorFragment.CustomerAdapter(getContext(),images,names,psd);
                listView.setAdapter(adapter);
                count++;


                Log.d("","");
                //System.out.println( val );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(getContext(),"clicked", Toast.LENGTH_SHORT);
                    try {
                        String bioguideID = bioguideIds[i];
                        String value =(String) obj.get(bioguideID);
                        Intent intentApp = new Intent(getActivity(),LegislatorDetails.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("jsonObject",value);
                        intentApp.putExtra("jsonObject",bundle);
                        getActivity().startActivityForResult(intentApp, 1);                        }
                    catch(Exception e) {
                        e.getStackTrace();
                    }
                }
            });

        }

    }
    public void populate_committee(View v, final JSONObject obj){
        String[] id = new String[obj.length()];
        String[] fullname = new String[obj.length()];
        String[] chamber = new String[obj.length()];
        int count = 0;
        ListView listView= (ListView) v.findViewById(R.id.fav_committee_listing);
        Iterator<String> sortIterator = obj.keys();
        ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        LinkedHashMap<String,JSONObject> committeeSort = new LinkedHashMap<String,JSONObject>();

        while(sortIterator.hasNext()) {
            try {
                String keyValue = sortIterator.next();
                Object jsonObject =  new JSONObject((String)obj.get(keyValue));
                jsonObjects.add((JSONObject) jsonObject);
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        Collections.sort( jsonObjects, new Comparator<JSONObject>() {
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
                if(valA.compareTo(valB)==0){

                    String lastA = new String();
                    String lastB = new String();

                    try {
                        lastA= (String)a.get(KEY_NAME);
                        lastB = (String) b.get(KEY_NAME);
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

        for(int j=0;j<jsonObjects.size();j++) {
            String lastName=null;
            try{
                committeeSort.put(jsonObjects.get(j).getString("committee_id"),jsonObjects.get(j));
                lastName= jsonObjects.get(j).getString("last_name");
            } catch (Exception e){

            }


            if(lastName!=null) {

                String index = lastName.substring(0, 1);
                if (indexMap.get(index) == null)
                    indexMap.put(index, j);

            }

        }





        Iterator<String> i = committeeSort.keySet().iterator();


        while (i.hasNext()) {
            String key = i.next();
            try {
                final JSONObject each = committeeSort.get(key);
                id[count] = each.getString("committee_id");
                fullname[count] = each.getString("name");
                chamber[count] = each.getString("chamber");
                committeeIds[count] = each.getString("committee_id");

                CommitteesFragment.CustomerAdapter adapter=new CommitteesFragment.CustomerAdapter(getContext(),id,fullname,chamber);
                listView.setAdapter(adapter);

                count++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(getContext(),"clicked", Toast.LENGTH_SHORT);
                    try {
                        Intent intentApp = new Intent(getActivity(),CommitteeDetails.class);
                        Bundle bundle = new Bundle();
                        String value = (String)obj.get(committeeIds[i]);
                        bundle.putString("jsonObject",value);
                        intentApp.putExtra("jsonObject",bundle);
                        getActivity().startActivityForResult(intentApp, 1);

                    }
                    catch(Exception e) {
                        e.getStackTrace();
                    }
                }
            });
        }
        Log.d("","");
    }
    public void populate_bill( View v, final JSONObject obj ){
//        Log.d("","");
        String district, bioguide_id;
        int count = 0;
        String[] billID = new String[obj.length()];
        String[] title = new String[obj.length()];
        String[] introducedOn = new String[obj.length()];
        ListView listView= (ListView) v.findViewById(R.id.fav_bill_listing);
        Iterator<String> sortIterator = obj.keys();
        ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        LinkedHashMap<String,JSONObject> billsSort = new LinkedHashMap<String,JSONObject>();

        while(sortIterator.hasNext()) {
            try {
                String keyValue = sortIterator.next();
                Object jsonObject =  new JSONObject((String)obj.get(keyValue));
                jsonObjects.add((JSONObject) jsonObject);
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        Collections.sort( jsonObjects, new Comparator<JSONObject>() {
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

        for(int j=0;j<jsonObjects.size();j++) {
            String lastName=null;
            try{
                billsSort.put(jsonObjects.get(j).getString("bill_id"),jsonObjects.get(j));
                lastName= jsonObjects.get(j).getString("last_name");
            } catch (Exception e){}

            if(lastName!=null) {

                String index = lastName.substring(0, 1);
                if (indexMap.get(index) == null)
                    indexMap.put(index, j);
            }
        }
        Iterator<String> i = billsSort.keySet().iterator();




        while (i.hasNext()){
            String key = i.next();
            try {
                final JSONObject each = billsSort.get(key);
                if(!(each.isNull("short_title"))){
                    district=each.getString("short_title");
                }else{
                    district=each.getString("official_title");
                }

                billID[count] = each.getString("bill_id");
                billIds[count] = each.getString("bill_id");
                title[count] = district;
                introducedOn[count] = each.getString("introduced_on");

                BillsFragment.BillsCustomAdapter adapter =  new BillsFragment.BillsCustomAdapter(getContext(),billID,title,introducedOn);
                listView.setAdapter(adapter);
                count++;

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //Toast.makeText(getContext(),"clicked", Toast.LENGTH_SHORT);
                        try {
                            Intent intentApp = new Intent(getActivity(),BillDetails.class);
                            Bundle bundle = new Bundle();
                            String value = (String)obj.get(billIds[i]);
                            bundle.putString("jsonObject",value);
                            bundle.putString("callFrom", "favorites");
                            intentApp.putExtra("jsonObject",bundle);
//                            intentApp.putExtra("callFrom", "Favorites");
                            getActivity().startActivityForResult(intentApp, 1);
                        }
                        catch(Exception e) {
                            e.getStackTrace();
                        }
                    }
                });

                Log.d("","");
                //System.out.println( val );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
