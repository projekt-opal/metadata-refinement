# OPAL metadata extraction

This OPAL component extracts metadata from natural language texts.

* **Named Entity Recognition** based on [FOX](https://github.com/dice-group/FOX)
* **Language Detection** based on [Apache OpenNLP](https://opennlp.apache.org/)

## Installation

* Download and install [OPAL common](https://github.com/projekt-opal/common):  
  `mvn install`
* Create a JAR file:  
  `mvn package`
* Start the webservices:  
  `java -jar metadata-extraction-service.jar`

## Examples

* Language Detection:  
    * [http://localhost:9080/metadata/lang/getIso369_3?text=Sprachen lernen](http://localhost:9080/metadata/lang/getIso369_3?text=Sprachen%20lernen)  
    * [http://localhost:9080/metadata/lang/addToModel?turtleBytes=...](http://localhost:9080/metadata/lang/addToModel?turtleBytes=...)
* Named Entity Recognition:  
    * [http://localhost:9080/metadata/fox/getTurtle?text=A.%20Einstein%20was%20born%20in%20Ulm.&lang=en](http://localhost:9080/metadata/fox/getTurtle?text=A.%20Einstein%20was%20born%20in%20Ulm.&lang=en)
    * [http://localhost:9080/metadata/fox/getLocationNames?text=A.%20Einstein%20was%20born%20in%20Ulm.&lang=en](http://localhost:9080/metadata/fox/getLocationNames?text=A.%20Einstein%20was%20born%20in%20Ulm.&lang=en)
    * [http://localhost:9080/metadata/fox/getLocationUris?text=A.%20Einstein%20was%20born%20in%20Ulm.&lang=en](http://localhost:9080/metadata/fox/getLocationUris?text=A.%20Einstein%20was%20born%20in%20Ulm.&lang=en)

## Notes

* If there is no FOX endpoint available, the Dockerfile in directory 'docker' can be used.