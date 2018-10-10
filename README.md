# 1. Propos

La bibliothèque jours-feries fournit la liste des jours fériés et des jours fermés à l'État de Genève.

# 2. Description

La classe ``JoursFeriesService`` permet de connaître les jours fériés et fermés à l'État de Genève.

Jours fériés légaux :
- Nouvel An : le 1er janvier
- Vendredi-saint : 2 jours avant Pâques
- lundi de Pâques : 1 jour après Pâques
- jeudi de l'Ascension : 39 jours après Pâques
- lundi de Pentecôte : 50 jours après Pâques
- fête nationale : le 1er août
- jeûne genevois : le jeudi qui suit le premier dimanche de septembre
- Noël : le 25 décembre,
- Restauration de la République : le 31 décembre

Jours fermés à l'État :
- le 1er mai
- le lundi suivant le 1er août, si celui-ci tombe un dimanche
- les 24, 26, 27, 28, 29 et 30 décembre

Cette liste des jours fermés est indicative, car elle doit être confirmée chaque année par le Conseil d'État.

Les jours fermés sont des jours fériés supplémentaires pour les employés de l'État.
Les méthodes incluant les jours fermés contiennent le mot "Etat" dans leur nom (``getJoursEtatFermesDuMois()``, 
``isJourEtatFerie()``....).
Les jours ouvrables sont les jours qui ne sont pas fériés et qui ne sont ni samedi ni dimanche. De nouveau, 
on a la notion de jour ouvrable général et de jour ouvrable à l'État de Genève (méthodes ``isJourOuvrable()`` et
``isJourEtatOuvrable()``).

Les méthodes existent en 2 exemplaires : le premier avec des dates aux formats java.util.Date et le second
avec des dates au format DTD, c'est-à-dire yyyyMMdd.

Deux méthodes auxiliaires testent si la date est un samedi ou un dimanche.

# 3. Construction

La bibliothèque peut être assemblée via Maven par la commande

```mvn clean install```

# 4. Exécution

Le fichier .jar créé ci-dessus peut ensuite être intégré à toute application Java 8+.

Lors de l'exécution, il faut fournir le fichier contenant les jours fermés, au moyen d'une variable de la JVM 
``jours-feries.config.file``. Par exemple :
```
-Djours-feries.config.file=C:\tmp\jours-feries.properties
```

Dans ce fichier, le format à respecter est :
```
JOURS_FERMETURE_ETAT_[ANNÉE]=dd/MM;dd/MM;dd/MM....
```

Par exemple :
```
JOURS_FERMETURE_ETAT_2017=26/12;27/12;28/12;29/12
JOURS_FERMETURE_ETAT_2018=24/12;26/12;27/12;28/12
```
Le 1<sup>er</sup> mai n'a pas besoin de figurer dans ce fichier : il est automatiquement fourni comme jour fermé.
