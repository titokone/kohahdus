OHTU-PROJEKTIEN ESIMERKKIDOKUMENTIT

Tähän hakemistopuuhun on koottu TKTL:n ohjelmistotuotantoprojektien
dokumenttien rungot ja osia esimerkkiprojektin sisällöstä.
Dokumentteja saa käyttää vapaasti ohjelmistotuotantoprojektien
dokumenttien malleina.

Toistaiseksi dokumentit ovat pahasti keskeneräisiä, mutta niitä
täydennetään aika ajoin.

Esimerkkidokumentin saa käyttöön menemällä haluamansa dokumentin
hakemistoon ja kirjoittamalla komennon

       make

Komento tekee hakemiston dokumentin perusteella PostScript-tiedoston.

UUDEN DOKUMENTIN LUOMINEN

Hakemisto dokumentti sisältää yhden dokumentin tarvitsemat perustiedostot.
Tarkoitus on, ettei näihin tehdä muutoksia, vaan dokumentin kirjoitus
aloitetaan kopioimalla hakemiston tiedostot uuteen hakemistoon. Esimerkiksi
projektisuunnitelma aloitettaisiin seuraavalla komennolla:

	cp -r dokumentti projektisuunnitelma

Tämän jälkeen projektisuunnitelma-hakemisto sisältää seuraavat tiedostot:

Makefile
	Sisältää dokumentin tuottamiseen liittyviä säännöksiä. Näiden
	muuttaminen vaatii asiantuntemusta.

dokumentti.tex
	Dokumentin runko. Tänne kirjoitetaan dokumentin nimi.

liitteet.tex
	Liitteet. Perusversio ei sisällä liitteitä.

sisalto.tex
	Varsinainen sisältö. Suurin osa tekstistä tulee tänne.

versio.tex
	Versiohistoria löytyy täältä. Kun dokumentista toimitetaan versio
	asiakkaalle, vastuuhenkilölle tai ohjaajalle tarkistettavaksi, tulee
	dokumentin versionumeroa nostaa.

Huomaa, että paketin mukana tulee projektisuunnitelma-niminen hakemisto,
jossa on valmis runko projektisuunnitelmaa varten.


KÄYTTÖOHJE

Komento make tuottaa tiedoston dokumentti.ps, joka on PostScriptiä eli
tulostettava tiedosto. Seuraavat komennot ovat myös hyödyllisiä:

make xdvi
	Avaa dokumentin esikatseluun. Nopea, mutta ei osaa näyttää vaativia
	kuvia.

make ps
	Avaa dokumentin esikatseluun. Hidas, mutta näyttää dokumentin
	sellaisena kuin se tulostuu paperille.

make clean
	Poistaa tilapäistiedostot. Jos huomaat, ettei tekemäsi muutos näy,
	käytä tätä komentoa, jonka jälkeen dokumentti muodostetaan alusta
	asti uudelleen.

make dokumentti-2.ps
	Tuottaa PostScript-tiedoston, joka sisältää kaksi dokumentin sivua
	yhdellä arkilla. Säästä paperia ja käytä tätä kun tulostat omaa
	käyttöä varten.

Make-ohjelma tulee käynnistää siinä hakemistossa, missä dokumentin Makefile
sijaitsee.

Kaikille asiakirjoille yhteiset käsitteet määritellään tiedostossa
asetukset.tex, joka löytyy pohjat-hakemistosta. Asetuksina kirjataan
esimerkiksi projektin nimi ja projektiryhmän jäsenet. Samasta hakemistosta
löytyy tiedosto lahteet.bib, joka sisältää eri dokumenttien lähdeviitteet.

Dokumenttipohja perustuu Tieteellisen kirjoittamisen kurssin vastaavaan
tyylitiedostoon. Pohjaa kehitetään kevään 2004 aikana opiskelijoilta saadun
palautteen pohjalta. Kehitysideoita voi lähettää sähköpostilla osoitteeseen
ohtu@cs.helsinki.fi.
