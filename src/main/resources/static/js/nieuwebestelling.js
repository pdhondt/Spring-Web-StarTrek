"use strict";
import { byId, setText, toon, verberg } from "./util.js";

const werknemer = JSON.parse(sessionStorage.getItem("werknemer"));
const naam = `${werknemer.voornaam} ${werknemer.familienaam}`;
document.querySelector("title").innerText = `Bestelling voor ${naam}`;
setText("naam", `Bestelling voor ${naam}`);
setText("naarWerknemer", naam);

byId("bestel").onclick = async function () {
    verbergFouten();
    const omschrijvingInput = byId("omschrijving");
    if (! omschrijvingInput.checkValidity()) {
        toon("omschrijvingFout");
        omschrijvingInput.focus();
        return;
    }
    const bedragInput = byId("bedrag");
    if (! bedragInput.checkValidity()) {
        toon("bedragFout");
        bedragInput.focus();
        return;
    }
    const bestelling =
        {
            omschrijving: omschrijvingInput.value,
            bedrag: bedragInput.value
        }
    await bestel(werknemer.id, bestelling)
}

function verbergFouten() {
    verberg("omschrijvingFout");
    verberg("bedragFout");
    verberg("conflict");
    verberg("storing");
    verberg("nietGevonden");
}

async function bestel(werknemerId, bestelling) {
    verberg("nietGevonden");
    verberg("conflict");
    verberg("storing");
    const response = await fetch(`werknemers/${werknemerId}/nieuwebestelling`,
        {
            method: "POST",
            headers: {'Content-Type': "application/json"},
            body: JSON.stringify(bestelling)
        })
    if (response.ok) {
        window.location = "vorigebestellingen.html";
    } else {
        switch (response.status) {
            case 404:
                toon("nietGevonden");
                break;
            case 409:
                const responseBody = await response.json();
                setText("conflict", responseBody.message);
                toon("conflict");
                break;
            default:
                toon("storing");
        }
    }
}