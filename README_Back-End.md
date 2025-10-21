# Angular SPA für ToDos

**Allgemeine Anmerkungen**

Generell gilt: wenn Du weitere Infos oder Klarstellungen benötigst, kannst Du mich jederzeit ansprechen. Außerdem werde ich von Zeit zu Zeit den aktuellen Fortschritt mit dir besprechen und dich so gut es geht unterstützen 😊

_**Wichtig:** Bitte erstelle das Projekt mit Sorgfalt. Es ist mir viel wichtiger zu sehen, wie Du beim Kunden arbeiten würdest, als dass Du alle Aufgaben in Rekordzeit erledigst._

**Ich wünsche viel Erfolg🍀 und hoffe es macht auch Spaß🤘**

## Deine Aufgabe

Erstelle einen einen REST-Service für ToDo-Aufgaben. 

Im Verzeichnis "[back-end](./back-end)" findest Du eine initialisierte Spring Boot Anwendung, im `pom.xml` sind bereits alle notwendigen Dependencies konfiguriert.  
Das Projekt enthält den Maven-Wrapper, d.h. sämtliche Maven-Aufrufe kannst Du einfach mit `mvnw` durchführen. Eine Installation von Maven ist nicht notwendig.

Als Basis-Technologien verwenden wir: Java, Maven, Spring Boot, JPA,  eine SQL-Datenbank (H2)

Die Anwendung soll eine OpenAPI-konforme Dokumentation bereitstellen.  
Sobald die REST-Schnittstelle mit OpenAPI-Annotationen dokumentiert wird, kann die API-Doku hier aufgerufen werden: http://localhost:8080/swagger-ui.html

Eine Beschreibung der REST-Schnittstelle findest Du in [api-docs.html](./api-docs.html)

### Spezifikation der Datenbank
Die Datenbank benötigt nur eine Tabelle

| Spalte           | nullable? | Beschreibung                       | Typ            | Beispiel                               |
|------------------|-----------|------------------------------------|----------------|----------------------------------------|
| **id**           | nein      | Primärschlüssel                    | UUID           | `deadbeef-dead-0000-0000-000000000000` |
| **name**         | nein      | Name/Titel des ToDo                | VARCHAR        | `Opa anrufen`                          |
| **created**      | nein      | Zeitpunkt der Erstellung des ToDos | OffsetDateTime |                                        |
| **lastModified** | nein      | Zeitpunkt der letzten Änderung     | OffsetDateTime |                                        |
| **dueDate**      | ja        | optionales Fälligkeitsdatum        | LocalDate      | `2024-04-24`                           |
| **details**      | ja        | optionale Beschreibung des ToDos   | LOB            | `der freut sich bestimmt ♥️`           |
| **status**       | nein      | Status des ToDo                    | Enum           | `OPEN` \| `DONE`                       |

### Datenbank des Back-Ends betrachten
Unter http://localhost:8080/h2-console/ findest Du eine grafische Oberfläche für die SQL-Datenbank.
* User: `sa`
* Pasword: `password`

### Starten der Angular SPA
Im Verzeichnis "[front-end](./front-end)" befindet sich eine Angular-Anwendung die zur Schnittstelle passt, und die Du zum manuellen Testen verwenden kannst.

Um die App zu starten, gehe in der Konsole in das `front-end`-Verzeichnis und führe folgenden Befehl aus:
```
ng serve
```
Um die App wieder zu stoppen, drückst Du einfach `STRG+C`.

Danach kannst Du die Anwendung im Browser öffnen: http://localhost:4200

**💡 Damit das Angular Front-End auf das Back-End zugreifen kann, musst Du CORS für `http://localhost:4200` erlauben.**
