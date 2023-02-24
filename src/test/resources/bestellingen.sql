insert into bestellingen(werknemerId, omschrijving, bedrag, moment)
values ((select id from werknemers where voornaam = 'testvoornaam1'), 'testbestelling1', 2, now()),
       ((select id from werknemers where voornaam = 'testvoornaam1'), 'testbestelling2', 4, now());