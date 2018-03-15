package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

public class SituacoesObservadasActivity extends AppCompatActivity {

    RadioButton sujeiraInterna, sinaisCarbonizacao, ranhurasDisco, neutroCarbonizado, marcasParafusos, mancaisDeslocados, ledIntermitente, ledApagado, ledDisparado, ledAceso, elementoDescentralizado,
            componentesQueimados, circuitoDefeituoso, bobinaInterrompida, blocoCarbonizados, placaSolta, placaAmassada;
    String sujeiraInternaStatus, sinaisCarbonizacaoStatus, ranhurasDiscoStatus, neutroCarbonizadoStatus, marcasParafusosStatus, mancaisDeslocadosStatus, ledIntermitenteStatus, ledApagadoStatus, ledDisparadoStatus,
            ledAcesoStatus, elementoDescentralizadoStatus, componentesQueimadosStatus, circuitoDefeituosoStatus, bobinaInterrompidaStatus, blocoCarbonizadosStatus, placaSoltaStatus, placaAmassadaStatus;
    String todasObservacoes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situacoes_observadas);

        Log.d("SITUAÇÕES OBSERVADAS", String.valueOf(Hawk.count()));

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase8);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Hawk.delete("SituacoesObservadas");

                sujeiraInterna = findViewById(R.id.SujeiraInterna);
                sinaisCarbonizacao = findViewById(R.id.sinaisCarbonizacao);
                ranhurasDisco = findViewById(R.id.RanhurasDisco);
                neutroCarbonizado = findViewById(R.id.NeutroCarbonizado);
                marcasParafusos = findViewById(R.id.MarcasParafusos);
                mancaisDeslocados = findViewById(R.id.MancaisDeslocados);
                ledIntermitente = findViewById(R.id.ledIntermitente);
                ledApagado = findViewById(R.id.LedApagado);
                ledDisparado = findViewById(R.id.LedDisparado);
                ledAceso = findViewById(R.id.LedAcesoDireto);
                elementoDescentralizado = findViewById(R.id.elementoDescentralizado);
                componentesQueimados = findViewById(R.id.ComponenteQueimado);
                circuitoDefeituoso = findViewById(R.id.CircuitoDefeituoso);
                bobinaInterrompida = findViewById(R.id.BobinaInterrompida);
                blocoCarbonizados = findViewById(R.id.BlocosCarbonizados);
                placaSolta = findViewById(R.id.PlacaSolta);
                placaAmassada = findViewById(R.id.PlacaAmassada);


                todasObservacoes = "";
                if (sujeiraInterna.isChecked()) {
                    sujeiraInternaStatus = "Sujeira interna - atrito";
                    todasObservacoes = todasObservacoes + sujeiraInternaStatus;

                }
                if (sinaisCarbonizacao.isChecked()) {
                    sinaisCarbonizacaoStatus = "Sinais de carbonização";
                    todasObservacoes = todasObservacoes + " - " + sinaisCarbonizacaoStatus;

                }
                if (ranhurasDisco.isChecked()) {
                    ranhurasDiscoStatus = "Ranhuras no disco do elemento móvel";
                    todasObservacoes = todasObservacoes + " - " + ranhurasDiscoStatus;

                }
                if (neutroCarbonizado.isChecked()) {
                    neutroCarbonizadoStatus = "Neutro Carbonizado";
                    todasObservacoes = todasObservacoes + " - " + neutroCarbonizadoStatus;

                }
                if (marcasParafusos.isChecked()) {
                    marcasParafusosStatus = "Marca nos parafusos dos mancais";
                    todasObservacoes = todasObservacoes + " - " + marcasParafusosStatus;

                }
                if (mancaisDeslocados.isChecked()) {
                    mancaisDeslocadosStatus = "Mancais deslocados";
                    todasObservacoes = todasObservacoes + " - " + mancaisDeslocadosStatus;

                }
                if (ledIntermitente.isChecked()) {
                    ledIntermitenteStatus = "LED intermitente";
                    todasObservacoes = todasObservacoes + " - " + ledIntermitenteStatus;

                }
                if (ledApagado.isChecked()) {
                    ledApagadoStatus = "LED apagado/ não emite pulsos";
                    todasObservacoes = todasObservacoes + " - " + ledApagadoStatus;

                }
                if (ledDisparado.isChecked()) {
                    ledDisparadoStatus = "LED disparado";
                    todasObservacoes = todasObservacoes + " - " + ledDisparadoStatus;

                }
                if (ledAceso.isChecked()) {
                    ledAcesoStatus = "LEd aceso direto";
                    todasObservacoes = todasObservacoes + " - " + ledAcesoStatus;

                }
                if (elementoDescentralizado.isChecked()) {
                    elementoDescentralizadoStatus = "Elemento móvel descentralizado";
                    todasObservacoes = todasObservacoes + " - " + elementoDescentralizadoStatus;

                }
                if (componentesQueimados.isChecked()) {
                    componentesQueimadosStatus = "Componentes eletrônicos queimados";
                    todasObservacoes = todasObservacoes + " - " + componentesQueimadosStatus;

                }
                if (circuitoDefeituoso.isChecked()) {
                    circuitoDefeituosoStatus = "Circuito eletrônico defeituoso";
                    todasObservacoes = todasObservacoes + " - " + circuitoDefeituosoStatus;

                }
                if (bobinaInterrompida.isChecked()) {
                    bobinaInterrompidaStatus = "Bobina interrompida sem causa detectada";
                    todasObservacoes = todasObservacoes + " - " + bobinaInterrompidaStatus;

                }
                if (blocoCarbonizados.isChecked()) {
                    blocoCarbonizadosStatus = "Bloco de terminais carbonizados";
                    todasObservacoes = todasObservacoes + " - " + blocoCarbonizadosStatus;

                }
                if (placaSolta.isChecked()) {
                    placaSoltaStatus = "Placa solta";
                    todasObservacoes = todasObservacoes + " - " + placaSoltaStatus;

                }
                if (placaAmassada.isChecked()) {
                    placaAmassadaStatus = "Placa de identificação amassada";
                    todasObservacoes = todasObservacoes + " - " + placaAmassadaStatus;
                }

                Hawk.put("SituacoesObservadas", todasObservacoes);

                abrirInformacoesComplementares();
            }
        });


    }

    private void abrirInformacoesComplementares() {
        Intent intent = new Intent(this, InformacoesComplementaresActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
