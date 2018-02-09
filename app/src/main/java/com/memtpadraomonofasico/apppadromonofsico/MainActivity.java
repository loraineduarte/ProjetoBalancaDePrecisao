package com.memtpadraomonofasico.apppadromonofsico;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.Avaliador.CriarAvaliadorActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.Bluetooth.BluetoothActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.Medidor.CriarMedidorActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.RelatorioVerificacaoActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoVisual.InspecaoVisualActivity;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBluetooth();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void callBluetooth() {
        Intent intent = new Intent(this, BluetoothActivity.class);
        startActivity(intent);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_avaliador) {

            abrirAvaliador();
            return true;

        } else if (id == R.id.nav_medidores) {
            abrirMedidores();
            return true;

        }  else if (id == R.id.nav_relatorioVerificação) {
            abrirRelatorio();
            return true;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //Views
    private void abrirAvaliador() {
        Log.d(TAG, "Opção de avaliador");
        Intent intent = new Intent(this, CriarAvaliadorActivity.class);
        startActivity(intent);
    }

    private void abrirMedidores() {
        Log.d(TAG, "Opção de medidores");
        Intent intent = new Intent(this, CriarMedidorActivity.class);
        startActivity(intent);
    }

    private void abrirRelatorio() {

        Log.d(TAG, "Relatório");
        Intent intent = new Intent(this, RelatorioVerificacaoActivity.class);
        startActivity(intent);
    }
}
