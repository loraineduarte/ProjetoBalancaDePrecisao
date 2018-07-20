package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Medidor;

/**
 * Created by loraine.duarte on 07/06/2018.
 */

class Medidor {

    private String numeroGeral;
    private String modelo;
    private String fabricante;
    private String tensaoNominal;
    private int correnteNominal;
    private String tipoMedidor;
    private String kdKe;
    private String rr;
    private int numElementos;
    private String classe;
    private int fios;
    private String portariaInmetro;

    public Medidor(String numeroGeral, String modelo, String fabricante, String tensaoNominal, int correnteNominal, String tipoMedidor, String kdKe,
                   String rr, int numElementos, String classe, int fios, String portariaInmetro) {


        this.numeroGeral = numeroGeral;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.tensaoNominal = tensaoNominal;
        this.correnteNominal = correnteNominal;
        this.tipoMedidor = tipoMedidor;
        this.kdKe = kdKe;
        this.rr = rr;
        this.numElementos = numElementos;
        this.classe = classe;
        this.fios = fios;
        this.portariaInmetro = portariaInmetro;
    }


    public String getNumeroGeral() {
        return numeroGeral;
    }

    public void setNumeroGeral(String numeroGeral) {
        this.numeroGeral = numeroGeral;
    }


    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getTensaoNominal() {
        return tensaoNominal;
    }

    public void setTensaoNominal(String tensaoNominal) {
        this.tensaoNominal = tensaoNominal;
    }

    public int getCorrenteNominal() {
        return correnteNominal;
    }

    public void setCorrenteNominal(int correnteNominal) {
        this.correnteNominal = correnteNominal;
    }

    public String getTipoMedidor() {
        return tipoMedidor;
    }

    public void setTipoMedidor(String tipoMedidor) {
        this.tipoMedidor = tipoMedidor;
    }

    public String getKdKe() {
        return kdKe;
    }

    public void setKdKe(String kdKe) {
        this.kdKe = kdKe;
    }

    public String getRr() {
        return rr;
    }

    public void setRr(String rr) {
        this.rr = rr;
    }

    public int getNumElementos() {
        return numElementos;
    }

    public void setNumElementos(int numElementos) {
        this.numElementos = numElementos;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public int getFios() {
        return fios;
    }

    public void setFios(int fios) {
        this.fios = fios;
    }

    public String getPortariaInmetro() {
        return portariaInmetro;
    }

    public void setPortariaInmetro(String portariaInmetro) {
        this.portariaInmetro = portariaInmetro;
    }


}
