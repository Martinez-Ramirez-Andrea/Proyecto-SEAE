<<<<<<< HEAD
package edu.ues.seae.Proyecto_seae.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ues.seae.Proyecto_seae.model.SeaeModel;
import edu.ues.seae.Proyecto_seae.service.SeaeService;

@RestController
@CrossOrigin(origins = "*")
public class SeaeController {

    @Autowired
    private SeaeService seaeService;

    // ─── ENDPOINT VPN ────────────────────────────────────
    @PostMapping("/vpn")
    public Map<String, Object> calcularVPN(@RequestBody SeaeModel datos) {
        double resultado = seaeService.calcularVPN(datos);
        String decision = seaeService.decisionVPN(resultado);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("metodo", "Valor Presente Neto (VPN)");
        respuesta.put("resultado", resultado);
        respuesta.put("decision", decision);
        respuesta.put("unidad", "$");
        return respuesta;
    }

    // ─── ENDPOINT CAE ────────────────────────────────────
    @PostMapping("/cae")
    public Map<String, Object> calcularCAE(@RequestBody SeaeModel datos) {
        double resultado = seaeService.calcularCAE(datos);
        String decision = seaeService.decisionCAE(resultado);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("metodo", "Costo Anual Equivalente (CAE)");
        respuesta.put("resultado", resultado);
        respuesta.put("decision", decision);
        respuesta.put("unidad", "$/año");
        return respuesta;
    }

    // ─── ENDPOINT TIR ────────────────────────────────────
    @PostMapping("/tir")
    public Map<String, Object> calcularTIR(@RequestBody SeaeModel datos) {
        double resultado = seaeService.calcularTIR(datos);
        String decision = seaeService.decisionTIR(resultado, datos.getTrema());

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("metodo", "Tasa Interna de Retorno (TIR)");
        respuesta.put("resultado", resultado);
        respuesta.put("decision", decision);
        respuesta.put("unidad", "%");
        return respuesta;
    }

    // ─── ENDPOINT COMPARAR ───────────────────────────────
    @PostMapping("/comparar")
    public Map<String, Object> comparar(@RequestBody Map<String, SeaeModel> datos) {
        SeaeModel alternativaA = datos.get("alternativaA");
        SeaeModel alternativaB = datos.get("alternativaB");

        double vpnA = seaeService.calcularVPN(alternativaA);
        double vpnB = seaeService.calcularVPN(alternativaB);

        String seleccionada = vpnA >= vpnB ? "Alternativa A" : "Alternativa B";

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("vpnAlternativaA", vpnA);
        respuesta.put("vpnAlternativaB", vpnB);
        respuesta.put("seleccionada", seleccionada);
        respuesta.put("razon", "Se selecciona la de mayor VPN");
        return respuesta;
    }
=======
package edu.ues.seae.Proyecto_seae.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ues.seae.Proyecto_seae.model.SeaeModel;
import edu.ues.seae.Proyecto_seae.service.SeaeService;

@RestController
@CrossOrigin(origins = "*")
public class SeaeController {

    @Autowired
    private SeaeService seaeService;

    // ─── ENDPOINT VPN ────────────────────────────────────
    @PostMapping("/vpn")
    public Map<String, Object> calcularVPN(@RequestBody SeaeModel datos) {
        double resultado = seaeService.calcularVPN(datos);
        String decision = seaeService.decisionVPN(resultado);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("metodo", "Valor Presente Neto (VPN)");
        respuesta.put("resultado", resultado);
        respuesta.put("decision", decision);
        respuesta.put("unidad", "$");
        return respuesta;
    }

    // ─── ENDPOINT CAE ────────────────────────────────────
    @PostMapping("/cae")
    public Map<String, Object> calcularCAE(@RequestBody SeaeModel datos) {
        double resultado = seaeService.calcularCAE(datos);
        String decision = seaeService.decisionCAE(resultado);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("metodo", "Costo Anual Equivalente (CAE)");
        respuesta.put("resultado", resultado);
        respuesta.put("decision", decision);
        respuesta.put("unidad", "$/año");
        return respuesta;
    }

    // ─── ENDPOINT TIR ────────────────────────────────────
    @PostMapping("/tir")
    public Map<String, Object> calcularTIR(@RequestBody SeaeModel datos) {
        double resultado = seaeService.calcularTIR(datos);
        String decision = seaeService.decisionTIR(resultado, datos.getTrema());

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("metodo", "Tasa Interna de Retorno (TIR)");
        respuesta.put("resultado", resultado);
        respuesta.put("decision", decision);
        respuesta.put("unidad", "%");
        return respuesta;
    }

    // ─── ENDPOINT COMPARAR ───────────────────────────────
    @PostMapping("/comparar")
    public Map<String, Object> comparar(@RequestBody Map<String, SeaeModel> datos) {
        SeaeModel alternativaA = datos.get("alternativaA");
        SeaeModel alternativaB = datos.get("alternativaB");

        double vpnA = seaeService.calcularVPN(alternativaA);
        double vpnB = seaeService.calcularVPN(alternativaB);

        String seleccionada = vpnA >= vpnB ? "Alternativa A" : "Alternativa B";

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("vpnAlternativaA", vpnA);
        respuesta.put("vpnAlternativaB", vpnB);
        respuesta.put("seleccionada", seleccionada);
        respuesta.put("razon", "Se selecciona la de mayor VPN");
        return respuesta;
    }
>>>>>>> 4b80e641dfec8778850ebbc7ebcf1f3ff084805f
}