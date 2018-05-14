package com.memtpadraomonofasico.apppadromonofsico;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.Cadastro.Avaliador.CriarAvaliadorActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.Cadastro.Medidor.CriarMedidorActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.RelatorioVerificacaoActivity;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.orhanobut.hawk.Hawk;


public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressBar myprogressBarAvaliadores;
    private ProgressBar myprogressBarMedidores;
    private TextView progressingTextViewAvaliadores;
    private TextView progressingTextViewmedidores;
    private final Handler progressHandler = new Handler();
    private int i = 0;
    String user, senha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = Hawk.get("usuario");
        senha = Hawk.get("senha" );

        final BancoController crud = new BancoController(getBaseContext());
        final Cursor cursorMedidor = crud.pegaMedidores();
        final Cursor cursorAvaliador = crud.pegaAvaliadores();
        final Cursor usuarioLogin = crud.pegaTipoUsuario(user, senha);


        Button avaliadores = findViewById(R.id.Avaliador);
        avaliadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAvaliador();
            }
        });

        Button medidores = findViewById(R.id.Medidor);
        medidores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMedidores();
            }
        });

        Button teste = findViewById(R.id.Teste);
        teste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirRelatorio();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myprogressBarAvaliadores = findViewById(R.id.progressBarAvaliadores);
        myprogressBarMedidores = findViewById(R.id.progressBarMedidores);
        progressingTextViewAvaliadores = findViewById(R.id.progress_circle_textAvaliadores);
        progressingTextViewmedidores = findViewById(R.id.progress_circle_textMedidores);

        new Thread(new Runnable() {
            public void run() {
                while (i < 100) {
                    i += 2;
                    progressHandler.post(new Runnable() {
                        public void run() {
                            myprogressBarAvaliadores.setProgress(i);
                            myprogressBarMedidores.setProgress(i);

                            progressingTextViewmedidores.setText(String.valueOf(cursorMedidor.getCount()));
                            progressingTextViewAvaliadores.setText(String.valueOf(cursorAvaliador .getCount()));
                        }
                    });
                    try {
                        Thread.sleep(300);



                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //Views
    private void abrirAvaliador() {
        Intent intent = new Intent(this, CriarAvaliadorActivity.class);
        startActivity(intent);
    }

    private void abrirMedidores() {
        Intent intent = new Intent(this, CriarMedidorActivity.class);
        startActivity(intent);
    }

    private void abrirRelatorio() {
        Intent intent = new Intent(this, RelatorioVerificacaoActivity.class);
        startActivity(intent);
    }
}
