package com.ingeniapps.pide.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ingeniapps.pide.R;
import com.ingeniapps.pide.fragment.Empresas;
import com.ingeniapps.pide.fragment.Historial;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private Menu menuDrawer;
    private NavigationView navigationView;
    private TextView ui_hot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ui_hot = null;

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //menuDrawer = navigationView.getMenu();

        navigationView.setCheckedItem(R.id.nav_pedir_producto);
        android.support.v4.app.FragmentManager fragmentManagerr = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransactionn = fragmentManagerr.beginTransaction();
        fragmentTransactionn.replace(R.id.frame_container, new Empresas());
        fragmentTransactionn.commit();

        /*//REMOVEMOS LOS CONTADORES DE NOTICIAS
        ShortcutBadger.applyCount(this, 0); //for 1.1.4+*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.



        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.principal, menu);
        return true;



    }
*/
    private int hot_number = 0;





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass;
        switch (item.getItemId())
        {
            case R.id.nav_pedir_producto:
                fragmentClass = Empresas.class;
                break;

            case R.id.nav_historial:
                fragmentClass = Historial.class;
                break;

           /* case R.id.nav_notificaciones:
                setMenuCounter(R.id.nav_notificaciones,0);
                fragmentClass = Notificaciones.class;
                break;

            case R.id.nav_daños:
                fragmentClass = Danos.class;
                break;
*/
            default:
                fragmentClass = Empresas.class;
        }

        try
        {
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
        item.setChecked(true);
        //setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }
}
