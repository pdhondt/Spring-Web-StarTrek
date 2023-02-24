"use strict";
import { byId, toon, setText } from "./util.js";

const werknemer = JSON.parse(sessionStorage.getItem("werknemer"));
const naam = `${werknemer.voornaam} ${werknemer.familienaam}`;
document.querySelector("title").innerText = `Bestellingen van ${naam}`;
setText("naam", `Bestellingen van ${naam}`);
setText("naarWerknemer", naam);

const response = await fetch(`werknemers/${werknemer.id}/bestellingen`);
if (response.ok) {
    const bestellingen = await response.json();
    const bestellingenBody = byId("bestellingenBody");
    for (const bestelling of bestellingen) {
        const tr = bestellingenBody.insertRow();
        tr.insertCell().innerText = bestelling.id;
        tr.insertCell().innerText = bestelling.omschrijving;
        tr.insertCell().innerText = bestelling.bedrag;
        tr.insertCell().innerText = new Date(bestelling.moment).toLocaleString("nl-BE");
    }
} else {
    toon("storing");
}