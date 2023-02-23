"use strict";
import { byId, setText } from "./util.js";

const werknemer = JSON.parse(sessionStorage.getItem("werknemer"));
const naam = `${werknemer.voornaam} ${werknemer.familienaam}`;
document.querySelector("title").innerText = naam;
//setText("voornaam", werknemer.voornaam);
//setText("familienaam", werknemer.familienaam);
setText("naam", naam);
const img = byId("foto");
img.alt = naam;
img.src = `images/${werknemer.id}.jpg`;
setText("nummer", werknemer.id);
setText("budget", werknemer.budget);