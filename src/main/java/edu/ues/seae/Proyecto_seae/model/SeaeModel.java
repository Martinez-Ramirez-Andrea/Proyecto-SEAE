package edu.ues.seae.Proyecto_seae.model;

import java.util.List;

public class SeaeModel {

    private double inversionInicial;
    private double tasaDescuento;
    private double trema;
    private List<Double> flujosEfectivo;
    private double valorSalvamento;
    private int vidaUtil;
    private double costosAnuales;
    private double ingresosAnuales;

    public double getInversionInicial() {
        return inversionInicial;
    }

    public void setInversionInicial(double inversionInicial) {
        this.inversionInicial = inversionInicial;
    }

    public double getTasaDescuento() {
        return tasaDescuento;
    }

    public void setTasaDescuento(double tasaDescuento) {
        this.tasaDescuento = tasaDescuento;
    }

    public double getTrema() {
        return trema;
    }

    public void setTrema(double trema) {
        this.trema = trema;
    }

    public List<Double> getFlujosEfectivo() {
        return flujosEfectivo;
    }

    public void setFlujosEfectivo(List<Double> flujosEfectivo) {
        this.flujosEfectivo = flujosEfectivo;
    }

    public double getValorSalvamento() {
        return valorSalvamento;
    }

    public void setValorSalvamento(double valorSalvamento) {
        this.valorSalvamento = valorSalvamento;
    }

    public int getVidaUtil() {
        return vidaUtil;
    }

    public void setVidaUtil(int vidaUtil) {
        this.vidaUtil = vidaUtil;
    }

    public double getCostosAnuales() {
        return costosAnuales;
    }

    public void setCostosAnuales(double costosAnuales) {
        this.costosAnuales = costosAnuales;
    }

    public double getIngresosAnuales() {
        return ingresosAnuales;
    }

    public void setIngresosAnuales(double ingresosAnuales) {
        this.ingresosAnuales = ingresosAnuales;
    }
}