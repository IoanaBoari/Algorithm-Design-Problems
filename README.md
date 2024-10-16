// Boari Ioana-Ruxandra 322CD

            TEMA 1 PA - TEHNICI DE PROGRAMARE (SI GIGEL)

Pentru realizarea temei am ales sa folosesc limbajul Java, deoarece imi este mai familiar
in urma cursului de POO din semestrul trecut. Pentru a implementa exercitiile am ales sa folosesc
scheletul de la laborator pentru citire si scriere, iar rezolvarile le-am facut in metoda getResult().
Tema a avut o dificultate potrivita si mi-a placut ca nu a fost asa mult de scris, ci mai mult de gandit.

Am avut diferente intre rularea pe local si cea pe vmchecker, dar am rezolvat implementeand mai eficient citirea 
folosind clasa MyScanner sugerata pe ocw.

Problema 1: Alimentare Servere
    Pentru a rezolva aceasta cerinta am ales sa folosesc o cautare binara intre curentul minim si cel maxim.
    La inceput retin curentul minim si cel maxim in timp ce fac citirea datelor in metoda readInput().
    Apoi, in metoda getResult() fac cautarea binara intre acesti curenti astfel: calculez puterile
    pentru fiecare server folosind drept curent pe cel minim, respectiv pe cel maxim. 
    Folosesc 2 arrayList in care sa stochez aceste rezultate. Din fiecare arrayList extrag minimul, 
    deoarece puterea sistemului este limitata de cea mai mica putere individuala, si il compar cu 
    puterea maxima descoperita pana la pasul curent. min_power corespunde curentului minim 
    intre care se face cautarea binara si max_power corespunde curentului maxim. In functie de care 
    dintre aceste puteri este mai mare se actualizeaza limitele intre care se face cautarea binara.
    Daca puterile sunt egale inseamna ca vom gasi curentul optim la mijlocul intervalului.
    Apoi calculam puterile pentru toate serverele cu acest curent optim si returnam puterea cea mai mica.

    Complexitate: O(n log m), unde m reprezinta diferenta maxima dintre curentii din input
                - complexitatea este data de cautarea binara

Problema 2: Colorare
    Pentru a rezolva aceasta cerinta am ales sa fac calculul pe masura ce citesc datele de intrare.
    In functie de numarul de zone si de directia acestora am calculat in cate moduri diferite 
    se poate colora tabloul, avand in vedere constrangerile impuse de zona anterioara.
    Pentru prima zona nu exista nicio constrangere, asadar daca aceasta este orizontala 
    exista 6 moduri in care se poate colora(Aranjamente de 3 luate cate 2), iar daca este verticala
    exista 3 moduri(Aranjamente de 3 luate cate 1). Apoi zonele urmatoare depind de tipul zonei anterioare.
    Pentru a evita depasirea valorilor si pentru a realiza un calcul mai eficient am ales sa folosesc 
    metoda Montgomery pentru ridicarea la putere si inmultirea modulara. 
    Am cautat pe internet mai multe metode pentru a eficientiza ridicarea la putere doarece 
    simpla folosire a lui pow() nu a fost suficienta. 
    https://cp-algorithms.com/algebra/montgomery_multiplication.html in urma citirii acestui articol 
    am implementat metoda Mongomery.

    Complexitate: O(k), unde k reprezinta numarul de zone din tablou
                - complexitatea este data de metoda de citire deoarece operatiile se executa in timp ce se citesc datele

Problema 3: Compresie
    Pentru a rezolva aceasta cerinta am ales sa folosesc sume partiale si un arrayFinal 
    in care sa retin sirul format pentru e returna direct lungimea sa.
    Astfel parcurg cele 2 array-uri si initializez sumele cu primul element din array. 
    Daca sumele sunt egale adaug suma in array-ul final, trec la urmatorul numar in ambele array-uri 
    si setez sumele pe 0. Daca suma pentru A este mai mare decat cea pentru B, 
    adun urmatorul element din array-ul B la suma pentru B si pentru array-ul A nu fac nimic. 
    Se aplica aceeasi metoda si in cazul in care suma din B este mai mare decat cea din A.
    In final, daca nu s-au terminat ambele siruri inseamna ca nu s-au putut forma siruri egale asadar se returneaza -1.
    Altfel se returneaza dimensiunea array ului final in care s-a retinut sirul.

    Complexitate: O(n + m), unde n este dimensiunea array ului A si m dimensiunea array ului B
                - complexitatea este data de metoda de citire deoarece metoda getResult() are drept complexitate min(n,m)

Problema 4: Criptat
    Pentru a rezolva aceasta cerinta am folosit un HashMap pentru a retine toate literele 
    si nr de apritii al acestora in toate cuvintele. In metoda getResult() folosesc string-ul maxPassword
    pentru a retine parola maxima posibila. Parcurg toate literele in ordinea descrescatoare dupa valorile 
    din HashMap. Apoi sortez cuvintele dupa raportul dintre litera dominanta si numarul de litere al cuvantului.
    Adaug la parola toate cuvintele pentru care se respecta conditia de dominanta. 
    In final returnez lungimea celei mai lungi parole.

    Complexitate: O(n log n), unde n este numarul de cuvinte 
                - complexitatea este data de sortarea cuvintelor din metoda getResult()

Problema 5: Oferta
    Pentru a rezolva aceasta cerinta am folosit un vector dp pentru a retine pe pozitia i 
    pretul cel mai bun pentru primele i+1 produse. Am calculat separat pretul pentru primele 3 produse
    apoi am parcurs toate produsele si am generat cele 3 variante posibile de pret 
    si am ales-o pe cea mai buna. 
    Varianta 1 reprezinta adunarea pretului produsului curent la cel mai bun pret obtinut anterior.
    Varianta 2 reprezinta aplicarea reducerii pentru 2 produse pe ultimele 2 si
    adunarea rezultatului la cel mai bun pret pentru i-2 produse.
    Varianta 3 reprezinta aplicarea reducerii pentru 3 produse pe ultimele 3 si
    adunarea rezultatului la cel mai bun pret pentru i-3 produse.
    In final returnez pretul de pe pozitia n-1 deoarece acolo se afla pretul cel mai bun pentru cele n produse.

    Complexitate: O(n), unde n reprezinta numarul de produse
                - cmplexitatea provine din metoda getResult() in care se construieste vectoul dp cu preturile optime