const URL_BASE = "http://localhost:8080";

let contadorAnios = 1;

document.addEventListener("DOMContentLoaded", () => {

    const btnCalcular = document.getElementById("btn-calcular");
    const btnReporte = document.getElementById("btn-reporte");
    const btnAgregar = document.querySelector(".btn-secondary");
    const btnComparar = document.querySelector(".btn-compare");

    // -------- CALCULAR --------
    if (btnCalcular) {
        btnCalcular.addEventListener("click", calcularTodo);
    }

    // -------- REPORTE --------
    if (btnReporte) {
        btnReporte.addEventListener("click", async () => {

            try {
                const vpnA = getNumber("res-vpn");
                const caeA = getNumber("res-cae");
                const tirA = getNumber("res-tir");

                const vpnB = getNumber("vpn-b");

                const inversion = getValue("inversion");
                const trema = getValue("trema");

                const flujosA = getFlujos();

                const datos = {
                    inversion,
                    trema,
                    flujos: flujosA, // backend ya soporta esto
                    vpn: vpnA,
                    cae: caeA,
                    tir: tirA,
                    vpn2: vpnB
                };

                const res = await fetch(`${URL_BASE}/reporte`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(datos)
                });

                const blob = await res.blob();

                if (blob.size < 100) {
                    alert("El PDF está vacío o falló el backend");
                    return;
                }

                const url = window.URL.createObjectURL(blob);
                const a = document.createElement("a");
                a.href = url;
                a.download = "reporte.pdf";
                a.click();

            } catch (e) {
                console.error(e);
                alert("Error al generar PDF");
            }
        });
    }

    // -------- AGREGAR AÑOS --------
    if (btnAgregar) {
        btnAgregar.addEventListener("click", () => {
            contadorAnios++;

            const container = document.getElementById("flows-container");
            if (!container) return;

            const div = document.createElement("div");
            div.classList.add("flow-input");

            div.innerHTML = `
                <span>Año ${contadorAnios}:</span>
                <input type="number" placeholder="Monto">
            `;

            container.appendChild(div);
        });
    }

    // -------- COMPARAR --------
    if (btnComparar) {
        btnComparar.addEventListener("click", async () => {

            try {
                const trema = parseFloat(getValue("trema"));
                const inversion = parseFloat(getValue("inversion"));

                const flujos = getFlujos();

                const datos = {
                    inversionInicial: inversion,
                    flujosEfectivo: flujos,
                    trema: trema / 100
                };

                const datosB = {
                    inversionInicial: inversion,
                    flujosEfectivo: flujos.map(f => f * 0.5),
                    trema: trema / 100
                };

                const res = await fetch(`${URL_BASE}/comparar`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        alternativaA: datos,
                        alternativaB: datosB
                    })
                });

                const data = await res.json();

                setText("vpn-a", `$${data.vpnAlternativaA.toFixed(2)}`);
                setText("vpn-b", `$${data.vpnAlternativaB.toFixed(2)}`);

                // comparación robusta
                if (data.vpnAlternativaA > data.vpnAlternativaB) {
                    setEstado("estado-a", true);
                    setEstado("estado-b", false);
                } else {
                    setEstado("estado-a", false);
                    setEstado("estado-b", true);
                }

            } catch (e) {
                console.error(e);
                alert("Error al comparar");
            }
        });
    }

});


// ================= FUNCIONES SEGUEAS =================

function getValue(id) {
    const el = document.getElementById(id);
    return el ? el.value : 0;
}

function getNumber(id) {
    const el = document.getElementById(id);
    if (!el) return 0;
    return Number(el.innerText.replace("$", "").replace("%", "")) || 0;
}

function getFlujos() {
    const inputs = document.querySelectorAll('#flows-container input');
    return Array.from(inputs)
        .map(i => Number(i.value))
        .filter(v => !isNaN(v));
}

function setText(id, value) {
    const el = document.getElementById(id);
    if (el) el.innerText = value;
}

function setEstado(id, positivo) {
    const el = document.getElementById(id);
    if (!el) return;

    if (positivo) {
        el.innerText = "✔ Mejor opción";
        el.className = "status positive";
    } else {
        el.innerText = "✖ No recomendable";
        el.className = "status negative";
    }
}

// -------- CALCULAR --------


async function calcularTodo() {

    try {
        const trema = parseFloat(getValue("trema"));
        const inversion = parseFloat(getValue("inversion"));

        const flujos = getFlujos();

        const datosA = {
            inversionInicial: inversion,
            flujosEfectivo: flujos,
            trema: trema / 100
        };

        const datosB = {
            inversionInicial: inversion,
            flujosEfectivo: flujos.map(f => f * 0.5),
            trema: trema / 100
        };

        //  LLAMADAS PARA A Y B
        const [vpnA, caeA, tirA, vpnB, caeB, tirB] = await Promise.all([

            fetch(`${URL_BASE}/vpn`, { method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(datosA)}).then(r=>r.json()),
            fetch(`${URL_BASE}/cae`, { method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(datosA)}).then(r=>r.json()),
            fetch(`${URL_BASE}/tir`, { method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(datosA)}).then(r=>r.json()),

            fetch(`${URL_BASE}/vpn`, { method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(datosB)}).then(r=>r.json()),
            fetch(`${URL_BASE}/cae`, { method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(datosB)}).then(r=>r.json()),
            fetch(`${URL_BASE}/tir`, { method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(datosB)}).then(r=>r.json())

        ]);

        // -------- MOSTRAR A --------
        setText('res-vpn', `$${vpnA.resultado.toFixed(2)}`);
        setText('res-cae', `$${caeA.resultado.toFixed(2)}`);
        setText('res-tir', `${tirA.resultado.toFixed(2)}%`);

        // -------- MOSTRAR B --------
        setText('vpn-b', `$${vpnB.resultado.toFixed(2)}`);
        setText('cae-b', `$${caeB.resultado.toFixed(2)}`);
        setText('tir-b', `${tirB.resultado.toFixed(2)}%`);

    } catch (e) {
        console.error(e);
        alert("Error en cálculo");
    }
}