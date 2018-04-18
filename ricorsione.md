# Ricorsione
Sapendo che nel database sono presenti 3 città, supponiamo che un tecnico debba compiere delle analisi tecniche della durata di un giorno in ciascuna citta. Le analisi hanno un costo per ogni giornata, determinato dalla somma di due contributi: un fattore costante (di valore 100) ogniqualvolta il tecnico si deve spostare da una città ad un’altra in due giorni successivi, ed un fattore variabile pari al valore numerico dell’umidità della città nel giorno considerato. Si trovi la sequenza delle città da visitare nei primi 15 giorni del mese selezionato, tale da minimizzare il costo complessivo rispettando i seguenti vincoli:
- Nei primi 15 giorni del mese, tutte le città devono essere visitate almeno una volta
- In nessuna città si possono trascorrere più di 6 giornate (anche non consecutive)
- Scelta una città, il tecnico non si può spostare prima di aver trascorso 3 giorni consecutivi.

#### Problema : Soluzione che minimizza costi
Dati 
* 3 città 
* costi in umidità ==> mi serve sapere l'umidità
* nei primi 15 giorni del mese==> mi serve sapere il mese
Bisogna trovare la sequenza di città da visitare

#### Livello
Numero di città già inserite nella sequenza

#### Soluzione Parziale
* Parte iniziale della sequenza, già controllata
* Scelte città per numero di giorni pari al livello

#### Soluzione Completa
Sequenza di città per 15 giorni che minimizza costi

#### Validità soluzione parziale
* Tutte le città devono esser visitate almeno una volta ==> contatoreCitta>0
* Non si può stare più di 6 giorni in ogni città==> contatoreCitta<=6
* Si deve stare almeno 3 giorni consecutivi in ogni città

#### Validità soluzione Completa
* Deve contenere sequenza di 15 elementi ==> List<> sequenza....sequenza.size==15
* costoSoluzione deve essere il minore tra i costiParziali

#### Generazione soluzione di livello +1 dal livello corrente
Partendo dalla soluzione (parziale) di livello corrente, bisogna:
* aggiungere alla parziale una città 
  * nuova se si è sostati già 6 giorni nella precedente
  * altrimenti la stessa
* testare se conviene in termini di costo e se rispetta i controlli di parziale
* se ok, ricorsione, *altrimenti* la rimuovo
* se ok, entra nella parziale, *altrimenti* deve esser rimossa

#### Struttura dati per memorizzare soluzione
* Lista di città (parziale o soluzione)
* livello
* mese per prendere i costi nei suoi primi 15 giorni
