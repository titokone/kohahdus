OHTU-PROJEKTIEN ESIMERKKIDOKUMENTIT

T�h�n hakemistopuuhun on koottu TKTL:n ohjelmistotuotantoprojektien
dokumenttien rungot ja osia esimerkkiprojektin sis�ll�st�.
Dokumentteja saa k�ytt�� vapaasti ohjelmistotuotantoprojektien
dokumenttien malleina.

Toistaiseksi dokumentit ovat pahasti keskener�isi�, mutta niit�
t�ydennet��n aika ajoin.

Esimerkkidokumentin saa k�ytt��n menem�ll� haluamansa dokumentin
hakemistoon ja kirjoittamalla komennon

       make

Komento tekee hakemiston dokumentin perusteella PostScript-tiedoston.

UUDEN DOKUMENTIN LUOMINEN

Hakemisto dokumentti sis�lt�� yhden dokumentin tarvitsemat perustiedostot.
Tarkoitus on, ettei n�ihin tehd� muutoksia, vaan dokumentin kirjoitus
aloitetaan kopioimalla hakemiston tiedostot uuteen hakemistoon. Esimerkiksi
projektisuunnitelma aloitettaisiin seuraavalla komennolla:

	cp -r dokumentti projektisuunnitelma

T�m�n j�lkeen projektisuunnitelma-hakemisto sis�lt�� seuraavat tiedostot:

Makefile
	Sis�lt�� dokumentin tuottamiseen liittyvi� s��nn�ksi�. N�iden
	muuttaminen vaatii asiantuntemusta.

dokumentti.tex
	Dokumentin runko. T�nne kirjoitetaan dokumentin nimi.

liitteet.tex
	Liitteet. Perusversio ei sis�ll� liitteit�.

sisalto.tex
	Varsinainen sis�lt�. Suurin osa tekstist� tulee t�nne.

versio.tex
	Versiohistoria l�ytyy t��lt�. Kun dokumentista toimitetaan versio
	asiakkaalle, vastuuhenkil�lle tai ohjaajalle tarkistettavaksi, tulee
	dokumentin versionumeroa nostaa.

Huomaa, ett� paketin mukana tulee projektisuunnitelma-niminen hakemisto,
jossa on valmis runko projektisuunnitelmaa varten.


K�YTT�OHJE

Komento make tuottaa tiedoston dokumentti.ps, joka on PostScripti� eli
tulostettava tiedosto. Seuraavat komennot ovat my�s hy�dyllisi�:

make xdvi
	Avaa dokumentin esikatseluun. Nopea, mutta ei osaa n�ytt�� vaativia
	kuvia.

make ps
	Avaa dokumentin esikatseluun. Hidas, mutta n�ytt�� dokumentin
	sellaisena kuin se tulostuu paperille.

make clean
	Poistaa tilap�istiedostot. Jos huomaat, ettei tekem�si muutos n�y,
	k�yt� t�t� komentoa, jonka j�lkeen dokumentti muodostetaan alusta
	asti uudelleen.

make dokumentti-2.ps
	Tuottaa PostScript-tiedoston, joka sis�lt�� kaksi dokumentin sivua
	yhdell� arkilla. S��st� paperia ja k�yt� t�t� kun tulostat omaa
	k�ytt�� varten.

Make-ohjelma tulee k�ynnist�� siin� hakemistossa, miss� dokumentin Makefile
sijaitsee.

Kaikille asiakirjoille yhteiset k�sitteet m��ritell��n tiedostossa
asetukset.tex, joka l�ytyy pohjat-hakemistosta. Asetuksina kirjataan
esimerkiksi projektin nimi ja projektiryhm�n j�senet. Samasta hakemistosta
l�ytyy tiedosto lahteet.bib, joka sis�lt�� eri dokumenttien l�hdeviitteet.

Dokumenttipohja perustuu Tieteellisen kirjoittamisen kurssin vastaavaan
tyylitiedostoon. Pohjaa kehitet��n kev��n 2004 aikana opiskelijoilta saadun
palautteen pohjalta. Kehitysideoita voi l�hett�� s�hk�postilla osoitteeseen
ohtu@cs.helsinki.fi.
