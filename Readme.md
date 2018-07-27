# Caribou : architecture de monitoring pour micro-services

## Problematique

* R�cup�rer, centraliser et traiter les logs g�n�r�s par les micro-services.
Les logs doivent �tre r�cup�r�s dynamiquement par une application web utilisant AJAX. Il s'agit � terme de pouvoir traiter les logs d'exceptions, du garbage collector et enfin des logs de l'OS.


## Etat de l'art

La gestion des logs est un enjeu de plus en plus grand pour les entreprises. Dans le cas du monitoring de micro-services, cela permet d'isoler le plus rapidement possible les problemes au sein de l'application. 

* Stack ELK
  Maintenant appel� Elastic stack, comprend :
  * Elasticsearch, qui permet le sotckage  et l'indexation des requêtes. Il est bas� sur le moteur de recherche Apache Lucene et a pour principales caract�ristiques l'utilisation d'une base de donn�es NoSQL, une forte scalabilit�, l'utilisation d'une API REST et des temps de r�ponse compris entre 20ms et 300ms.
  * Logstash, qui assure l'analyse, le filtrage et le d�coupage des logs
  * Kibana est un dashboard permettant l'affichage des logs. 

* Splunk




## Architecture

Notre proposition d'architecture est pens�e pour �tre la plus propre et modulable possible avec l'id�e de "screaming architecture" en t�te. Nous avons fait un sch�ma UML le plus parlant possible pour que l'organisation de ce projet reste claire. Ainsi, le projet se s�pare en plusieurs parties distinctes :

* Gestion des agents :

Caribou peut soutenir plusieurs agents fonctionnant en parall�le et retient la source de chaque log.

* Gestion des logs :

L'application permet d'afficher et de trier les logs selon des expressions r�guli�res entierement choisies par l'utilisateur. Des types de logs peuvent etre pr�configur�s mais l'utilisateur peut aussi cr�er un log personnalis�

* Application web :

Bien que Caribou fonctionne en local pour l'instant, une application web a d�j� �t� cod�e avec spring MVC pour la partie Controller et Thymeleaf pour la partie de gestion des templates. Diff�rents onglets permettent de g�rer les diff�rents param�tres.

* Base de donn�es : 

Les logs sont stock�s sous une forme minimale LightLog pour �conomiser de l'espace dans une base de donn�es de type NoSQL orient�e documents MongoDB mais on peut facilement impl�menter un autre type de base de donn�es.

## Installation et lancement

Apr�s une installation de Java 8 (ou plus) et Mango en r�gle il est n�cessaire de 
* faire une installation Maven depuis le fichier pom.xml, 
* de cr�er une base de donn�e Mango,
* d'�xecuter `Caribou/target/java -jar Caribou-X.X.X-SNAPSHOT.jar`

Vous pouvez ensuite cr�er des objects Agents sur les machines o� se trouvent les logs � r�cup�rer et d'appliquer leur m�thode run(). Le fichier CAribou/TestAgnt.java contient un exemple lan�ant 2 Agents et des simulateurs de micro-services.

