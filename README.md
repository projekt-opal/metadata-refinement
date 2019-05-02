# OPAL metadata extraction

* **Named Entity Recognition** based on [FOX](https://github.com/dice-group/FOX)
* **Language Detection** based on [Apache OpenNLP](https://opennlp.apache.org/)

## Installation

* Create a JAR file:  
  `mvn package`
* Start the webservices:  
  `java -jar metadata-extraction-service.jar`

## Examples

* Language Detection:  
  [http://localhost:9080/metadata/lang/text?text=Sprachen lernen](http://localhost:9080/metadata/lang/text?text=Sprachen%20lernen)
* Named Entity Recognition:  
  [http://localhost:9080/metadata/fox?text=A. Einstein was born in Ulm.&lang=en](http://localhost:9080/metadata/fox?text=A.%20Einstein%20was%20born%20in%20Ulm.&lang=en)