# Aiheen kuvaus ja määrittely #
## Luokkakaavio ##
![Luokkakaavio](https://github.com/laitilari/Texas-fold-em/blob/master/dokumentaatio/Luokkakaavio.png)
## Sekvenssikaaviot ##
![Aseta pelin nopeus](https://github.com/laitilari/Texas-fold-em/blob/master/dokumentaatio/Setting game speed.png)
![Pelaaja nostaa](https://github.com/laitilari/Texas-fold-em/blob/master/dokumentaatio/Player raise.png)
![Valmistele peli](https://github.com/laitilari/Texas-fold-em/blob/master/dokumentaatio/Prepare game.png)

## Aihe ##

*Texas Fold'em on interaktiivinen korttipeli, jonka säännöt ovat samat, kuin perinteisen Texas Hold'emin säännöt. Texas Fold'emissa pelaaja pelaa tekoälyä vastaan. Pelaajan tehtävänä on voittaa peli. Pelin voittaa, jos tekoälyltä loppuvat pelimerkit. Pelaaja häviää, jos pelaajalta itseltään loppuvat pelimerkit.*

*Pelin kehityksen tarkoituksena oli luoda niin hyvä tekoäly, että sitä ei olisi ainakaan ihan helppo voittaa. Tavoite oli kunnianhimoinen, mutta onnistui silti kohtalaisesti. Tekoäly pystyy voittamaan kokemattoman pelaajan.*

*Projektin käyttöliittymä on tavoitteesta poiketen tekstuaalinen. Projektiin käytettävä aika (7 vk), aiheen haasteellisuus ja tekijän ohjelmointikokemus sekä muut kiireet synnyttivät lopputuloksen, joka on välttävästi onnistunut ja sisältää vielä paljon parannettavaa.*

### Käyttäjä(t)

Pelaaja

#### Pelaajan toiminnot

- Pelaaja voi valita pelin nopeuden: nopea, normaali, hidas. Pelin nopeuteen vaikuttaa aloituspelimerkkien määrä.
- Pelaajalla on kaikki Texas Hold'emiin kuuluvat valinnat käytössään: fold, check, call ja raise.
- Pelin päätyttyä pelaajalla on mahdollisuus lopettaa peli tai pelata uudestaan.

## Rakennekuvaus ##

Pelin luokkarakenne koostuu tekstikäyttöliittymäluokasta, logiikkaluokista sekä domainluokista. Näiden lisäksi erillisenä domainluokan ja logiikkaluokan sekoituksena on AI-luokka.

Pelin keskiössä on Game.java, joka vastaa pelin logiikkaan liittyvistä tehtävistä. Ohjelman suoritus koostuu suurimmaksi osaksi Userinterface-luokan (UI) ja Game-luokan yhteistoiminnasta. UI pyörittää silmukoita, jotka vastaavat koko pelin toiminnan jatkamisesta, sekä yksittäisten kierrosten suorittamisesta. UI myös suorittaa kaikki pelin tulostukset.

UI kutsuu Game-luokkaa toimintojen toteuttamiseen, joka kutsuu muita luokkia tarpeen mukaan. Eräs tyypillinen ketjutus on seuraava seuraava: UI pyytää Gamelta AI:n toimintaa pelin vaiheeseen. Game välittää AI:lle pelitilanteen muuttujat, joiden pohjalta AI-luokka valitsee parhaan toiminnan tilanteeseen. AI palauttaa toiminnastaan tiedon tekstinä Game luokalle, joka tutkii AI:n vastausta, suorittaa AI:n toiminnan peliin aiheuttamat vaikutukset, sekä palauttaa UI luokalle tulostettavaksi tekstikäyttöliittymään kuvauksen AI:n toiminnasta.

UI-luokan apuna on ScannerClass-luokka, joka mahdollistaa käyttäjän syötteen lukemisen. Käyttäjän syöte välitetään Game-luokalle, joka suorittaa peliin käyttäjän toiminnasta aiheutuneet muutokset ja kutsuu muun muassa Player-luokkaa vähentämään omia pelimerkkejään käyttäjän määrittämän panostuksen verran.

Dealer-luokka toimii Game-luokan apuna korttien käsittelyssä. Game voi kutsua Dealerin kautta korttipakan luomista, korttipakan sekoitusta tai korttien nostamista korttipakasta ja niiden jakamista. Luokka PackOfCards sisältää useita luokan Card olioita, kuten myös luokka TableCards, jota Dealer kutsuu, kun pelissä edetään.

Käyttäjän vastustaja on AI, jonka toiminta on määritelty AI-luokan koodiin. AI osaa valita pelin kehittäjän näkemyksen mukaisesti parhaita pelivalintoja kuhunkin tilanteeseen. AI:n valintaan vaikuttavat Game-luokalta saadut tiedot pelin tilanteesta. Nämä tiedot sisältävät muun muassa potin koon, pöytäkorttien arvon sekä vastustajan panostuksen koon ja pelimerkkien määrän.

## Käyttöohjeet ##

### Johdanto ###

Peliä käytetään tekstikäyttöliittymän avulla, jonka Main-luokka avaa, kun ohjelma suoritetaan. Käyttöliittymään voit kirjoittaa näppäimistöllä. Saat ohjeet pelinäppäimistä käyttöliittymältä aina, kun sinulta vaaditaan toimintaa. Kirjoitettuasi valitsemaasi toimintaa vastaavan merkin käyttöliittymään, hyväksy valintasi painamalla Enter-näppäintä ja seuraa käyttöliittymästä toimintasi vaikutusta pelin tilanteeseen ja AI:n toimintaan.

### Ohjeet ###
- Käynnistä ohjelman suoritus. 
- Valitse tekstikäyttöliittymän ohjeiden mukaisesti pelin nopeus kirjoittamalla käyttöliittymään jokin annetuista vaihtoehdoista.
- Seuraa käyttöliittymän tulostuksia. Tulostukset sisältävät mm. tietoja korttien sekoituksesta, blindien maksusta ja pelipositiosta.
- Vuorollasi valitse haluamasi toiminta kirjoittamalla käyttöliittymän ohjeiden mukaisesti toimintoa vastaava merkki.
- Valintavaihtoehtosi ovat normaaleja Texas Hold'emin toimintoja, kuten raise, call/check, fold.
- Jos AI:lta loppuvat pelimerkit, olet voittanut pelin ja saat siitä ilmoituksen.
- Jos sinulta loppuvat pelimerkit, AI voittaa ja saat ilmoituksen.
- Kun peli on loppu, voit valita haluatko pelata uudestaan vai haluatko lopettaa.
