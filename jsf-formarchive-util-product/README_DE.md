# JSF Form Archiv Nutzen

Mit diesem Nutzen kannst du nehmen Schnappschüsse von eure JSF UIs und so Archiv
in einem Nutzer-freundlichen Weg #welche (Nutzer) Inputs waren gemacht an gewiss
Punkte #fristgemäß.

- Nach einer Form hat gewesen unterzogen du willst finden den geschaffenen
  Schnappschuss Image auf das Fall Detail Seite.
- Der Task Name will sein genommen wie Image Namen.

## Demo

Einfach unterzieh die Form und du willst einen Schnappschuss von der Form finden
in die Fall Dokumente auf das Fall Detail Seite.

![Archiviert-Sample-Zwiegespräch](images/ArchivedSampleDialog.png)

![Fall-Dokumente-Zwiegespräch](images/CaseDocuments.png)

## Einrichtung

Zu nützen von diesem Nutzen du musst adaptieren dem "unterziehen" commandButton
in den Zwiegesprächen in eurem Arbeitsgang jener du möchtest haben archiviert.
Wechsel das #voreingestellt commandButton, jener darf #aussehen
  ```
  <p:commandButton actionListener="#{logic.close()}" value="Proceed" update="form" icon="pi pi-check" />
  ```
Zu
  ```
  <p:commandButton oncomplete="if(!args.validationFailed){saveCanvas()}" value="Proceed" />
  <ic:jsf.form.archive.util.SaveCanvas form="form" submitListener="#{logic.close()}" />
  ```
Vielleicht möchtest du schaffen einen neuen Ausblick Tippt ein die #HTML Vorlage
Präferenzen in eurem Designer mit dieser Abänderung.
