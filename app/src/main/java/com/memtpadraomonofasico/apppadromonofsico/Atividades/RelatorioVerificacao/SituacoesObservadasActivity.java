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
            ledAcesoStatus, elementoDescentralizadoStatus, componentesQueimadosStatus, circuitoDefeituosoStatus, bobinaInterrompidaStatus, blocoCarbonizadosStatus, placaSoltaStatus, placaAmassadaStatus ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situacoes_observadas);



        Log.d("SITUAÇÕES OBSERVADAS", String.valueOf(Hawk.count()));

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase8);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Hawk.delete("sujeiraInterna");
                Hawk.delete("sinaisCarbonizacao");
                Hawk.delete("ranhurasDisco");
                Hawk.delete("neutroCarbonizado");
                Hawk.delete("marcasParafusos");
                Hawk.delete("mancaisDeslocados");
                Hawk.delete("ledIntermitente");
                Hawk.delete("ledApagado");
                Hawk.delete("ledDisparado");
                Hawk.delete("ledAceso");
                Hawk.delete("elementoDescentralizado");
                Hawk.delete("componentesQueimados");
                Hawk.delete("circuitoDefeituoso");
                Hawk.delete("bobinaInterrompida");
                Hawk.delete("blocoCarbonizados");
                Hawk.delete("placaSolta");
                Hawk.delete("placaAmassada");

                sujeiraInterna = findViewById(R.id.SujeiraInterna);
                sinaisCarbonizacao = findViewById(R.id.sinaisCarbonizacao);
                ranhurasDisco = findViewById(R.id.RanhurasDisco);
                neutroCarbonizado = findViewById(R.id.NeutroCarbonizado);
                marcasParafusos  = findViewById(R.id.MarcasParafusos);
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


                if(sujeiraInterna.isChecked()){
                    sujeiraInternaStatus = "Sujeira interna - atrito";
                    Hawk.put("sujeiraInterna",sujeiraInternaStatus);

                }
                if (sinaisCarbonizacao.isChecked()){
                    sinaisCarbonizacaoStatus = "Sinais de carbonização";
                    Hawk.put("sinaisCarbonizacao",sinaisCarbonizacaoStatus);

                }
                if (ranhurasDisco.isChecked()){
                    ranhurasDiscoStatus = "Ranhuras no disco do elemento móvel";
                    Hawk.put("ranhurasDisco",ranhurasDiscoStatus);

                }
                if (neutroCarbonizado.isChecked()){
                    neutroCarbonizadoStatus = "Neutro Carbonizado";
                    Hawk.put("neutroCarbonizado",neutroCarbonizadoStatus);

                }
                if (marcasParafusos.isChecked()){
                    marcasParafusosStatus = "Marca nos parafusos dos mancais";
                    Hawk.put("marcasParafusos",marcasParafusosStatus);

                }
                if (mancaisDeslocados.isChecked()){
                    mancaisDeslocadosStatus = "Mancais deslocados";
                    Hawk.put("mancaisDeslocados",mancaisDeslocadosStatus);

                }
                if (ledIntermitente.isChecked()){
                    ledIntermitenteStatus = "LED intermitente";
                    Hawk.put("ledIntermitente",ledIntermitenteStatus);

                }
                if (ledApagado.isChecked()){
                    ledApagadoStatus = "LED apagado/ não emite pulsos";
                    Hawk.put("ledApagado",ledApagadoStatus);

                }
                if (ledDisparado.isChecked()){
                    ledDisparadoStatus = "LED disparado";
                    Hawk.put("ledDisparado",ledDisparadoStatus);

                }
                if (ledAceso.isChecked()){
                    ledAcesoStatus = "LEd aceso direto";
                    Hawk.put("ledAceso",ledAcesoStatus);

                }
                if (elementoDescentralizado.isChecked()){
                    elementoDescentralizadoStatus = "Elemento móvel descentralizado";
                    Hawk.put("elementoDescentralizado",elementoDescentralizadoStatus);

                }
                if (componentesQueimados.isChecked()){
                    componentesQueimadosStatus = "Componentes eletrônicos queimados";
                    Hawk.put("componentesQueimados",componentesQueimadosStatus);

                }
                if (circuitoDefeituoso.isChecked()){
                    circuitoDefeituosoStatus = "Circuito eletrônico defeituoso";
                    Hawk.put("circuitoDefeituoso",circuitoDefeituosoStatus);

                }
                if (bobinaInterrompida.isChecked()){
                    bobinaInterrompidaStatus = "Bobina interrompida sem causa detectada";
                    Hawk.put("bobinaInterrompida",bobinaInterrompidaStatus);

                }
                if (blocoCarbonizados.isChecked()){
                    blocoCarbonizadosStatus = "Bloco de terminais carbonizados";
                    Hawk.put("blocoCarbonizados",blocoCarbonizadosStatus);

                }
                if (placaSolta.isChecked()){
                    placaSoltaStatus = "Placa solta";
                    Hawk.put("placaSolta",placaSoltaStatus);

                }
                if (placaAmassada.isChecked()){
                    placaAmassadaStatus  = "Placa de identificação amassada";
                    Hawk.put("placaAmassada",placaAmassadaStatus);
                }

                abrirInformacoesComplementares();
            }
        });


    }

    private void abrirInformacoesComplementares() {
        Intent intent = new Intent(this, InformacoesComplementaresActivity.class);
        startActivity(intent);
    }
}
