This app read from a JSON file the places where you wanna show the TSP

The app use:

* [Jackson](https://github.com/FasterXML/jackson) to convert the File input into object and have better performance
* [Lombok](https://projectlombok.org) to skip the setter and getters writes
* [Google Maps Distance Matrix API](https://developers.google.com/maps/documentation/distance-matrix/?hl=ES) to get the distances and times
* [JSON](http://www.json.org/json-es.html) to read the result of the Google maps distance Matrix API

##Requeriments

**Java 8**
**Internet Connection**
**Google Maps Distance Matrix API KEY**
**File With point**

The files 
Must have a file with the points of the route example:

[{
	"nombre": "Galerias Toluca",
	"x": 19.289925,
	"y": -99.622425
}]