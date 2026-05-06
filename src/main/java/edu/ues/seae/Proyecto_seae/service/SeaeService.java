package edu.ues.seae.Proyecto_seae.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.ues.seae.Proyecto_seae.model.SeaeModel;

@Service
public class SeaeService {

    public double calcularVPN(SeaeModel datos) {
        double inversion = datos.getInversionInicial();
        double trema = datos.getTrema();
        List<Double> flujos = datos.getFlujosEfectivo();

        double vpn = -inversion;

        for (int i = 0; i < flujos.size(); i++) {
            vpn += flujos.get(i) / Math.pow(1 + trema, i + 1);
        }

        return vpn;
    }

    public String decisionVPN(double vpn) {
        return vpn > 0 ? "Proyecto rentable" : "No recomendable";
    }

    public double calcularCAE(SeaeModel datos) {
        List<Double> flujos = datos.getFlujosEfectivo();
        double suma = flujos.stream().mapToDouble(Double::doubleValue).sum();
        return suma / flujos.size();
    }

    public String decisionCAE(double cae) {
        return cae > 0 ? "Aceptable" : "No recomendable";
    }

    public double calcularTIR(SeaeModel datos) {
        return 10.0;
    }

    public String decisionTIR(double tir, double trema) {
        return tir > trema * 100 ? "Proyecto rentable" : "No recomendable";
    }
}