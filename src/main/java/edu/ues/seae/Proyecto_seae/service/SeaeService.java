package edu.ues.seae.Proyecto_seae.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.ues.seae.Proyecto_seae.model.SeaeModel;

@Service
public class SeaeService {

    // ─── CALCULAR VPN ─────────────────────────────────────
    public double calcularVPN(SeaeModel datos) {
        double vpn = -datos.getInversionInicial();
        List<Double> flujos = datos.getFlujosCaja();
        double tasa = datos.getTasaDescuento();

        for (int k = 1; k <= flujos.size(); k++) {
            vpn += flujos.get(k - 1) / Math.pow(1 + tasa, k);
        }

        return Math.round(vpn * 100.0) / 100.0;
    }

    // ─── CALCULAR CAE ─────────────────────────────────────
    public double calcularCAE(SeaeModel datos) {
        double I = datos.getInversionInicial();
        double S = datos.getValorSalvamento();
        double i = datos.getTrema();
        int N = datos.getVidaUtil();
        double E = datos.getCostosAnuales();
        double R = datos.getIngresosAnuales();

        double factorAP = (i * Math.pow(1 + i, N)) / (Math.pow(1 + i, N) - 1);
        double factorAF = i / (Math.pow(1 + i, N) - 1);
        double RC = I * factorAP - S * factorAF;
        double cae = R - E - RC;

        return Math.round(cae * 100.0) / 100.0;
    }

    // ─── CALCULAR TIR ─────────────────────────────────────
    public double calcularTIR(SeaeModel datos) {
        double tir = buscarTIR(datos.getInversionInicial(), datos.getFlujosCaja());
        return Math.round(tir * 10000.0) / 100.0;
    }

    private double buscarTIR(double inversion, List<Double> flujos) {
        double low = -0.99;
        double high = 10.0;

        for (int i = 0; i < 1000; i++) {
            double mid = (low + high) / 2;
            double vpn = calcularVPNInterno(inversion, flujos, mid);

            if (Math.abs(vpn) < 0.0001) return mid;
            if (vpn > 0) low = mid;
            else high = mid;
        }

        return (low + high) / 2;
    }

    private double calcularVPNInterno(double inversion, List<Double> flujos, double tasa) {
        double vpn = -inversion;
        for (int k = 1; k <= flujos.size(); k++) {
            vpn += flujos.get(k - 1) / Math.pow(1 + tasa, k);
        }
        return vpn;
    }

    // ─── DECISIONES ───────────────────────────────────────
    public String decisionVPN(double valor) {
        return valor >= 0
            ? " ACEPTAR - El proyecto es rentable"
            : " RECHAZAR - El proyecto no es rentable";
    }

    public String decisionCAE(double valor) {
        return valor >= 0
            ? " ACEPTAR - El proyecto es rentable"
            : " RECHAZAR - El proyecto no es rentable";
    }

    public String decisionTIR(double tir, double trema) {
        return tir >= (trema * 100)
            ? " ACEPTAR - TIR supera la TREMA"
            : " RECHAZAR - TIR no supera la TREMA";
    }
}