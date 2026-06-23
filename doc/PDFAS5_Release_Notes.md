# PDF-AS v5.0 Release vom xx.06.2026

Mit dem Major-Release PDF-AS 5.0 erfolgt eine Migration auf aktuelle Software-Infrastruktur.
Demnach sind beim Einspielen des Updates einige Änderungen notwendig.
Wir empfehlen, das Update zunächst in einer Testumgebung einzuspielen und die erfolgreiche Migration im entsprechenden Use-Case zu testen.

### Änderungen in dieser Version

- An der Kernbibliothek PDF-AS:
    - Die Multi-Stage-API für die programmatische Verwendung von PDF-AS hat sich geändert. `setCertificate` und `setSignature` führen jetzt direkt die dazugehörige Operation durch. Der Aufruf von `process` fällt weg.
    - Das Zertifikat eines MOA-Connectors (Wert `moa.sign.Certificate`) kann nun auch direkt base64-encoded angegeben werden. Hierzu wird ein Prefix `base64:` gefolgt vom Base64-encoded Zertifikat verwendet.
    - Ein Timeout oder Fehler bei der Verbindung zum MOA-Connector gibt nun einen eigens definierten PDF-AS-Fehlercode `11022` (`ERROR_SIG_CONNECT_ERROR`) zurück.
    - Java 17
    - Apache PDFBox 3.0.6
    - Update sonstiger verwendeter Programmbibliotheken:
        - logback-classic 1.5.25
        - commons-cli 1.11.0
        - commons-collections 4.5.0
        - commons-codec 1.21.0
        - jakarta.activation 2.1.4
        - jakarta.xml.bind-api 4.0.4
        - jakarta.jws 3.0.0
        - gson 2.13.2
        - org.apache.cxf 4.2.1
        
- An PDF-AS Web:
    - Umstellung von PDF-AS-Web auf Spring Boot 4.0.6. Die PDF-AS-Web-Konfigurationsdatei, angegeben über `-Dpdf-as-web.conf`, kann auch zur Angabe von Spring-Konfigurationsparametern genutzt werden.
    - Integration von Spring Boot Admin Client 4.0.4 zum einfacheren Monitoring. Dieser ist standardmäßig deaktiviert und muss, falls gewünscht, über die Konfigurationsdatei aktiviert werden.
    - Eine vollwertige JSON-API, funktionell gleichwertig zur SOAP-API, wurde hinzugefügt. Für weitere Informationen verweisen wir auf die Dokumentation zur Anbindung externer Webanwendungen an PDF-AS 5.0, sowie auf die maschinenlesbare OpenAPI-Dokumentation.
    - Für jeden MOA-Connector kann nun ein konfigurierbares Timeout gewählt werden. Setzen Sie hierzu den Wert `moal.(id).timeout` auf das gewünschte Timeout in Millisekunden.
    - Update sonstiger verwendeter Programmbibliotheken:
        - commons-text 1.14.0
        - zxing 3.5.0
        - sitemesh 3.2.1
        - hibernate 6.6.44.Final

### Durchführen eines Updates von PDF-AS Web

Das Major-Release 5.0 bringt einige Änderungen mit sich.
Aus diesem Grund soll auf jeden Fall ein vollständiges Backup der existierenden PDF-AS-Web-Umgebung durchgeführt werden, bevor das Upgrade versucht wird.

PDF-AS-Web wurde auf Java 17 migriert. PDF-As-Web 5.0 sollte also mit einem geeigneten Servlet-Container (wie z.B. Tomcat 11) betrieben werden.

PDF-AS-Web wurde auf Spring Boot migriert. Hierdurch ändert sich der notwendige Parameter für externe Logging-Einstellungen. PDF-AS wird mit einer geeigneten Standard-Konfiguration für das Logging ausgeliefert, die Logdateien der Form `pdfas.log` in den Tomcat-Logordner schreibt. Für eine Konfiguration über eine externe Konfigurationsdatei muss der frühere Parameter `logback.configurationFile` durch den Spring-Boot-Parameter `logging.config` ersetzt werden.

Der Security-Layer-Einsprungpunkt der ID Austria (`bku.mobile.url`) hat sich geändert. Die mitgelieferte Beispiel-Konfiguration beinhaltet den aktuellsten Wert (`https://service.a-trust.at/mobile/https-security-layer-request/default.aspx`). Falls eine Konfiguration einer älteren Instanz übernommen wird, sollte dieser Wert angepasst werden.

Zum verbesserten Monitoring von PDF-AS-Web wird der Spring Boot Admin Client mit ausgeliefert, um eine Integration in einen Monitoring-Server zu ermöglichen. Im Standardzustand ist dieser deaktiviert. Um ihn zu aktivieren, sollten `spring.boot.admin.client.enabled` und `spring.boot.admin.client.url` entsprechend gesetzt werden.

Der Parameter `web.upload.RequestsizeMax` wurde zur besseren Konsistenz zu `web.upload.requestsizeMax` umbenannt.

Das Releasepacket wurde um einen separaten PDF-AS-Web Build `pdf-as-web-db-*`  ergänzt welcher das im Handbuch beschriebene Datenbank-Backend für die SOAP Schnittstelle bereits inkludiert.
