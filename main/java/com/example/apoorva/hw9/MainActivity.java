package com.example.apoorva.hw9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private JSONObject legislator_json;
    private JSONObject bills_json;
    private JSONObject committees_json;

    public static JSONObject fav_legislator_json;
    public static JSONObject fav_bill_json;
    public static JSONObject fav_committee_json;

    String backFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if( !getIntent().getBundleExtra("jsonObject").isEmpty() ){
//            backFrom = getIntent().getBundleExtra("jsonObject").getString("callFrom");
//        }else{
//            backFrom = "";
//        }

        String[] params = {"l", "http://lowcost-env.yz33cmedka.us-west-2.elasticbeanstalk.com?cong_db=l"};

        new AsyncTasks().execute(params);

        SharedPreferences settings = getSharedPreferences("FavFile", 0);
        try {
            fav_legislator_json = new JSONObject(settings.getString("legislator", "{}"));
            fav_bill_json = new JSONObject(settings.getString("bill", "{}"));
            fav_committee_json = new JSONObject(settings.getString("committee", "{}"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Begin initialise - set legislators in nav bar, load legislture fragmeent
        navigationView.getMenu().getItem(0).setChecked(true);
        //Toast.makeText(this, "Legislators", //Toast.LENGTH_SHORT).show();
        setTitle(R.string.leg_heading);
    }

   /* protected void onResume() {
        super.onResume();

        FragmentManager fragmentManager = getSupportFragmentManager();
        int a = fragmentManager.getBackStackEntryCount();
        if(fragmentManager.getBackStackEntryCount() > 0) {
            String fragmentTag = fragmentManager.getBackStackEntryAt(0).getName();
            //Toast.makeText(getApplicationContext(), fragmentTag, //Toast.LENGTH_SHORT).show();
        }

    }
*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_legislators) {

            //Toast.makeText(this, "Legislators", //Toast.LENGTH_SHORT).show();
            setTitle(R.string.leg_heading);
            LegislatorFragment legislatorFragment = new LegislatorFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, legislatorFragment).commit();

        } else if (id == R.id.nav_bills) {

            if(bills_json == null) {
                String[] params = {"b", "http://lowcost-env.bdeukec6z7.us-west-2.elasticbeanstalk.com/?bills=true"};
                new AsyncTasks().execute(params);
            } else {
                setTitle(R.string.bills_heading);
                BillsFragment billsFragment = new BillsFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, billsFragment).commit();
            }


        } else if (id == R.id.nav_committee) {

            if(committees_json == null) {
                String[] params = {"c", "http://lowcost-env.yz33cmedka.us-west-2.elasticbeanstalk.com?cong_db=cfull"};
                new AsyncTasks().execute(params);
            } else {
                setTitle(R.string.comm_heading);
                CommitteesFragment committeesFragment = new CommitteesFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, committeesFragment).commit();
            }


        } else if (id == R.id.nav_favourites) {

            //Toast.makeText(this, "Favourites", //Toast.LENGTH_SHORT).show();
            setTitle(R.string.fav_heading);
            FavouritesFragment favouriteFragment = new FavouritesFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, favouriteFragment).commit();

        } else if (id == R.id.nav_aboutme) {

            //Toast.makeText(this, "About me", //Toast.LENGTH_SHORT).show();
            Intent intentApp = new Intent(this,AboutMeActivity.class);
            this.startActivityForResult(intentApp, 1);

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public JSONObject get_legislator_json(){
        return legislator_json;
    }
    public JSONObject get_bills_json(){
        return bills_json;
    }
    public JSONObject getCommittees_json(){ return committees_json; }

    public JSONObject get_fav_legislator_json(){ return fav_legislator_json; }
    public JSONObject get_fav_bill_json(){ return fav_bill_json; }
    public JSONObject get_fav_committee_json(){ return fav_committee_json; }

    private class AsyncTasks extends AsyncTask<String, String, JSONObject> {
        private String id = "";
        @Override
        protected JSONObject doInBackground(String... params) {


            GetJSON js = new GetJSON();
            String URL = params[1];

            id = params[0];

            return js.fetchJSONObject(URL);
        }

        protected void onPostExecute(JSONObject jsonObject) {
            //legislator_json = jsonObject;
            //bills_json = jsonObject;
            //Initial fragment
            if(id.equals("l")) {
                legislator_json = jsonObject;
                setTitle(R.string.leg_heading);
                LegislatorFragment legislatorFragment = new LegislatorFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().add(R.id.fragment_container, legislatorFragment).commit();
            } else if(id.equals("b")) {
                bills_json = jsonObject;
                setTitle(R.string.bills_heading);
                BillsFragment billsFragment = new BillsFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, billsFragment).commit();
            } else if(id.equals("c")) {
                committees_json = jsonObject;
                setTitle(R.string.comm_heading);
                CommitteesFragment committeesFragment = new CommitteesFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, committeesFragment).commit();
            }

        }

    }
}
