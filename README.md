# RESTopoly

TODO bis 16.12:  
1. Verbindung zurück zum "Player Service" hinbekommen  
2. Die ganzen Banken betreffenden Services transaktional machen/refactorn und Aufgabe 3.4B  
3. Alle Services mit Kommentaren zur Übersichtlichkeit versehen  
4. (um Board Methoden usw. der Anderen Gruppe erweitern)  
  
Probleme/Unklarheiten:  
Soll der Client mehrere Spiele erzeugen können und mehrere Spieler, um das ganze besser testen zu können oder wie im späteren "realen" Umfeld nur jeweils eins?  
Wenn jeder Client einen eigenen Service anbieten soll unter einer URL dann wahrscheinlich nur ein Spiel und Spieler.   
Die Verbindung zurück zum Player Service/Client kann weder über die öffentliche IP Adresse+Port noch über die IP Adresse+Port aus den Request Informationen, die der Client an die Services sendet, aufgebaut werden.  
Wie soll man die URI flexibel aus dem Yellowpage Service auslesen können, wenn die ID der Services jedes mal eine höhere ist sobald man eine neue Version der Services hochläd?  
Die Services sind vom Client also von außen nur über https zu erreichen intern funktioniert jedoch nur http. Welche Uri soll nun an den Yellowpage Service übermittelt werden? Wenn man überall die Uri aus den Yellowpages nimmt kann momentan eine der beiden Kommunikationsformen automatisch nicht funktionieren.  
(Da sind bestimmt noch mehr Dinge die mir gerade nicht einfallen)  
