package edu.ues.seae.Proyecto_seae.controller;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@RestController
public class ReporteController {

    @PostMapping("/reporte")
    public ResponseEntity<byte[]> generarPDF(@RequestBody Map<String, Object> datos) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);

            // -------- DATOS --------
            double inversion = parse(datos.get("inversion"));
            double trema = parse(datos.get("trema"));

            double vpnA = parse(datos.get("vpn"));
            double caeA = parse(datos.get("cae"));
            double tirA = parse(datos.get("tir"));

            double vpnB = parse(datos.get("vpn2"));

            List<?> flujos = (List<?>) datos.get("flujos");
            List<?> flujosA = (List<?>) datos.get("flujosA");
            List<?> flujosB = (List<?>) datos.get("flujosB");

            // -------- TITULO --------
            doc.add(new Paragraph("INFORME DE EVALUACIÓN ECONÓMICA")
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph(" "));

            // -------- DATOS GENERALES --------
            doc.add(new Paragraph("1. DATOS GENERALES").setBold());
            doc.add(new Paragraph("Inversión Inicial: $" + inversion));
            doc.add(new Paragraph("TREMA: " + trema + "%"));

            // -------- TABLA DE FLUJOS --------
            doc.add(new Paragraph("\n2. FLUJOS DE EFECTIVO").setBold());

            Table tablaFlujos = new Table(UnitValue.createPercentArray(new float[]{30, 70}))
                    .useAllAvailableWidth();

            tablaFlujos.addHeaderCell(new Cell().add(new Paragraph("Año").setBold()));
            tablaFlujos.addHeaderCell(new Cell().add(new Paragraph("Flujo").setBold()));

            if (flujos != null && !flujos.isEmpty()) {
                for (int i = 0; i < flujos.size(); i++) {
                    tablaFlujos.addCell("Año " + (i + 1));
                    tablaFlujos.addCell("$" + parse(flujos.get(i)));
                }
            } else {
                tablaFlujos.addCell(new Cell(1,2).add(new Paragraph("No hay flujos")));
            }

            doc.add(tablaFlujos);

            // -------- PROCEDIMIENTOS --------
            doc.add(new Paragraph("\n3. PROCEDIMIENTOS").setBold());

            doc.add(new Paragraph("VPN = -Inversión + Σ (Flujo / (1 + i)^n)"));
            doc.add(new Paragraph("CAE = Distribución anual equivalente del VPN"));
            doc.add(new Paragraph("TIR = Tasa que hace VPN = 0"));

            // -------- RESULTADOS --------
            doc.add(new Paragraph("\n4. RESULTADOS").setBold());

            doc.add(new Paragraph("ALTERNATIVA A").setBold());
            doc.add(new Paragraph("VPN: $" + vpnA)
                    .setFontColor(vpnA > 0 ? ColorConstants.GREEN : ColorConstants.RED));
            doc.add(new Paragraph("CAE: $" + caeA));
            doc.add(new Paragraph("TIR: " + tirA + "%"));

            doc.add(new Paragraph("\nALTERNATIVA B").setBold());
            doc.add(new Paragraph("VPN: $" + vpnB)
                    .setFontColor(vpnB > 0 ? ColorConstants.GREEN : ColorConstants.RED));
            doc.add(new Paragraph("CAE: No disponible"));
            doc.add(new Paragraph("TIR: No disponible"));

            // -------- TABLA COMPARATIVA --------
            doc.add(new Paragraph("\n5. COMPARATIVA").setBold());

            Table comp = new Table(UnitValue.createPercentArray(new float[]{40,30,30}))
                    .useAllAvailableWidth();

            comp.addHeaderCell("Indicador");
            comp.addHeaderCell("Alternativa A");
            comp.addHeaderCell("Alternativa B");

            comp.addCell("VPN");
            comp.addCell("$" + vpnA);
            comp.addCell("$" + vpnB);

            comp.addCell("CAE");
            comp.addCell("$" + caeA);
            comp.addCell("—");

            comp.addCell("TIR");
            comp.addCell(tirA + "%");
            comp.addCell("—");

            doc.add(comp);

            // -------- CONCLUSIÓN --------
            doc.add(new Paragraph("\n6. CONCLUSIÓN").setBold());

            String decision;

            if (vpnA > vpnB) {
                decision = "La Alternativa A es más rentable (mayor VPN)";
            } else if (vpnB > vpnA) {
                decision = "La Alternativa B es más rentable (mayor VPN)";
            } else {
                decision = "Ambas alternativas tienen el mismo rendimiento";
            }

            doc.add(new Paragraph(decision)
                    .setBold()
                    .setFontColor(ColorConstants.BLUE));

            doc.close();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private double parse(Object val) {
        try {
            if (val == null) return 0;
            return Double.parseDouble(val.toString()
                    .replace("$", "")
                    .replace("%", "")
                    .replace(",", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}