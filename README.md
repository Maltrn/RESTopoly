# RESTopoly

TODO bis 16.12:
1. Die Clientseite erweitern, sodass Events erstellt werden können usw.
2. Die ganzen neuen Funktionen testen
3. Die ganzen Banken betreffenden Services transaktional machen und Aufgabe 3.4B

Probleme/Unklarheiten:
Soll der Client nun der richtige Client für das endgültige Spiel sein oder die Vollmacht über das Spiel haben also auch transferFromTo aufrufen können usw.
Soll der Client mehrere Spiele erzeugen können und mehrere Spieler, um das ganze besser testen zu können oder wie im späteren "realen" Umfeld nur jeweils eins?
Wenn jeder Client einen eigenen Service anbieten soll unter einer URL dann wahrscheinlich nur ein Spiel und Spieler. 
Wie soll die Spieler Service URI aussehen/wo soll man die hernehmen, damit die Kommunikation mit den anderen Services klappt?
Wie soll man die URI flexibel aus dem YellowPage Service auslesen können, wenn die ID der Services jedes mal eine höhere ist sobald man eine neue Version der Services hochläd?
(Da sind bestimmt noch mehr Dinge die mir gerade nicht einfallen)
