"use strict";
import { byId, setText, toon, verberg } from "./util.js";

const werknemer = JSON.parse(sessionStorage.getItem("werknemer"));
const naam = `${werknemer.voornaam} ${werknemer.familienaam}`;
document.querySelector("title").innerText = `Bestelling voor ${naam}`;
setText("naam", `Bestelling voor ${naam}`);
setText("naarWerknemer", naam);

byId("bestel").onclick = async function () {
    verbergFouten();
    var omschrijvingInput = byId("omschrijving");
    if (! omschrijvingInput.checkValidity()) {
        toon("omschrijvingFout");
        omschrijvingInput.focus();
        return;
    }
    var bedragInput = byId("bedrag");
    if (! bedragInput.checkValidity()) {
        toon("bedragFout");
        bedragInput.focus();
        return;
    }
    const bestelling =
        {
            "omschrijving": omschrijvingInput.value,
            "bedrag": bedragInput.value
        }
    await bestel(werknemer.id, bestelling)
}

function verbergFouten() {
    verberg("omschrijvingFout");
    verberg("bedragFout");
    verberg("onvoldoendeBudget");
    verberg("storing");
}

async function bestel(werknemerId, bestelling) {
    const response = await fetch(`werknemers/${werknemerId}/nieuwebestelling`,
        {
            method: "POST",
            headers: {'Content-Type': "application/json"},
            body: JSON.stringify(bestelling)
        })
    if (response.ok) {
        window.location = "vorigebestellingen.html";
    } else {
        if (response.status === 409) {
            toon("onvoldoendeBudget");
        } else {
            toon("storing");
        }
    }
}