# OPAL metadata extraction

This OPAL component extracts metadata from natural language texts.

* **Language Detection** based on [Apache OpenNLP](https://opennlp.apache.org/)
* **Named Entity Recognition** based on [FOX](https://github.com/dice-group/FOX)

## Installation

* Download and install [OPAL common](https://github.com/projekt-opal/common):  
  `mvn install`
* Create a JAR file:  
  `mvn package`
* Start the webservices:  
  `java -jar metadata-extraction-service.jar`

## Webservice examples

* Language Detection:  
    * Returns detected language of plain text. ISO 639-3 codes (3 characters) are returned.  
[http://localhost:9080/metadata/lang?text=Sprachen lernen](http://localhost:9080/metadata/lang?text=Sprachen%20lernen)
    * Detects languages of metadata of datasets and adds them to model.  
[http://localhost:9080/metadata/lang/model?turtleBytes=...](http://localhost:9080/metadata/lang/model?turtleBytes=...)
* Named Entity Recognition:  
    * Returns FOX result in TURTLE format.  
[http://localhost:9080/metadata/fox?text=A. Einstein was born in Ulm.&lang=en](http://localhost:9080/metadata/fox?text=A.%20Einstein%20was%20born%20in%20Ulm.&lang=en)
    * Returns location names as JSON array.  
[http://localhost:9080/metadata/fox/location/names?text=Paderborn und Bad Oeynhausen sind in NRW&lang=de](http://localhost:9080/metadata/fox/location/names?text=Paderborn%20und%20Bad%20Oeynhausen%20sind%20in%20NRW&lang=de)
    * Returns location URIs as JSON array.  
[http://localhost:9080/metadata/fox/location/uris?text=Paderborn und Bad Oeynhausen sind in NRW&lang=de](http://localhost:9080/metadata/fox/location/uris?text=Paderborn%20und%20Bad%20Oeynhausen%20sind%20in%20NRW&lang=de)

## Notes

* If there is no FOX endpoint available, the Dockerfile in directory 'docker' can be used.