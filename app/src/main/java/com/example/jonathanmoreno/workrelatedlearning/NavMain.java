package com.example.jonathanmoreno.workrelatedlearning;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jonathanmoreno.workrelatedlearning.fragments.CompaniesFragment;
import com.example.jonathanmoreno.workrelatedlearning.fragments.LocationVacancy;
import com.example.jonathanmoreno.workrelatedlearning.fragments.ResumeFragment;
import com.example.jonathanmoreno.workrelatedlearning.fragments.SubscribeFragment;
import com.example.jonathanmoreno.workrelatedlearning.fragments.ViewVacancies;
import com.example.jonathanmoreno.workrelatedlearning.util.UserSessionManager;

public class NavMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userSessionManager=new UserSessionManager(getApplicationContext());

        ViewVacancies vacancies=new ViewVacancies();
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,vacancies,"view vacanices");
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();
        }
    }

    public void showMassage(String Title,String Message)
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.show();
    }

    private void aboutApp(){
        showMassage("About this App","\n\n \t This app help students seeking internship by  \n" +
                "1.\t Enable them to stay uptodate with the vacancies\n" +
                "2.\t Enable students to apply on the app \n" +
                "3.\tCompanies can view cvs with no much effort\n" +
                ".\t All rights reserved\n"+
                "-------------------------\n\n"+
                "\t Developed by Taku 2019\n\n\n");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_main, menu);
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
            aboutApp();
            return true;
        }

       else if (id == R.id.action_logout) {
            android.app.AlertDialog.Builder alertDialogBuilder =  new android.app.AlertDialog.Builder(this)
                    .setTitle("Logout?")
                    .setMessage("Are you sure you want to Logout from " + getString(R.string.app_name) + "?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            userSessionManager.logoutUser();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.view_vacancies) {
            // Handle the camera action
            ViewVacancies vacancies=new ViewVacancies();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame,vacancies,"view vacanices");
            transaction.commit();
        } else if (id == R.id.view_vac_locations) {
            LocationVacancy locationVacancy=new LocationVacancy();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame,locationVacancy,"view location vacanices");
            transaction.commit();


        } else if (id == R.id.subscribe) {
            SubscribeFragment subscribeFragment=new SubscribeFragment();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame,subscribeFragment,"Subscribe");
            transaction.commit();

        } else if (id == R.id.companies) {
            CompaniesFragment companiesFragment=new CompaniesFragment();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame,companiesFragment,"Companies");
            transaction.commit();

        } else if (id == R.id.profile) {
            ResumeFragment resumeFragment=new ResumeFragment();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame,resumeFragment,"Resume");
            transaction.commit();
        } else if (id == R.id.about_app) {
            aboutApp();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
