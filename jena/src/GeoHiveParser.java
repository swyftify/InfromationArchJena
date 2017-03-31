import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.BasicConfigurator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

//import no.ssb.jsonstat.JsonStatModule;
//import no.ssb.jsonstat.v2.*;
//import no.ssb.jsonstat.v2.deser.*;

import java.io.FileReader;
import java.awt.geom.Path2D;
import java.io.BufferedReader;


public class GeoHiveParser {

	public static ArrayList<CountyGeometry> parsePolygons(ResultSet rs){
		ArrayList<CountyGeometry> geoms = new ArrayList<CountyGeometry>();
		while(rs.hasNext()){
			QuerySolution q = rs.next();
			Literal geoLit = q.getLiteral("geo");
			String gString = geoLit.getString();

			Literal labLit = q.getLiteral("label");
			String label = labLit.getString();
			
			if (gString.startsWith("P")){ // a POLYGON
				String trimmed = gString.substring(8);
				trimmed = trimmed.replace("(", "");
				trimmed = trimmed.replace(")", "");
				trimmed = trimmed.replace(", ", ",");

				String[] pts = trimmed.split(",");
				
				Path2D.Double path = new Path2D.Double();
				path.moveTo(Double.parseDouble(pts[0].split(" ")[0]), Double.parseDouble(pts[0].split(" ")[1]));
				for(int i = 1; i<pts.length; i++){
					path.lineTo(Double.parseDouble(pts[i].split(" ")[0]), Double.parseDouble(pts[i].split(" ")[1]));
				}
				CountyGeometry cg = new CountyGeometry(label, path, gString);
				geoms.add(cg);
				/*if(path.contains(-7.2, 53.9)){ // Should be CAVAN
					System.out.println("we're in " + label);
				}*/
			}
			else if (gString.startsWith("M")){ // a MULTIPOLYGON
				String trimmed = gString.substring(13);
				//is there a better way to do this? yes, probably
				trimmed = trimmed.replace("((", "(");
				trimmed = trimmed.replace("((", "(");
				trimmed = trimmed.replace(")", "");
				trimmed = trimmed.replace(", ", ",");
				String[] polys = trimmed.split("\\(");
				
				Path2D.Double path = new Path2D.Double();
				
				for(int i = 1;i<polys.length;i++){
					if(polys[i]=="")break;
					String[] pts = polys[i].split(",");

					path.moveTo(Double.parseDouble(pts[1].split(" ")[0]), Double.parseDouble(pts[1].split(" ")[1]));
					
					for(int j = 1; j<pts.length; j++){
						path.lineTo(Double.parseDouble(pts[j].split(" ")[0]), Double.parseDouble(pts[j].split(" ")[1]));
					}
				}
				/*if(path.contains(-6.04, 52.977)){ //Should be WICKLOW
					System.out.println("now we're in " + label);
				}*/
				CountyGeometry cg = new CountyGeometry(label, path, gString);
				geoms.add(cg);
			}
			else{
				System.out.println("ERROR parsing the wkt string -- not a POLYGON or MULTIPOLYGON");
			}
		}
		
		return geoms;
	}
	
	public static ArrayList<CountyGeometry> readDbAndParse(String pathToDb){
		Model model = ModelFactory.createDefaultModel() ;
		model.read(pathToDb) ;
		
		String qString = "SELECT ?s ?o ?p "
				+ "WHERE { "
				+ "?s ?o ?p. "
				//+ "POINT{0,2} ge:sfWithin ?county"
				+ "} LIMIT 10 ";
		
		String q2 = "prefix gh:   <http://ontologies.geohive.ie/osi#> "
				+ "prefix w3: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "prefix gs: <http://www.opengis.net/ont/geosparql#> "
				+ "SELECT DISTINCT ?label ?geo WHERE { "
				+ "?county a gh:County. "
				+ "?county w3:label ?label. "
				+ "?county gs:hasGeometry ?cGeo. "
				+ "?cGeo gs:asWKT ?geo. " 
				+ "filter(lang(?label) = \"\")"
			//	+ "FILTER (gs:sfWithin(\"POINT(-6.267112, 53.3361236)\"^^gs:wktLiteral, ?geo )) "
				+ "} LIMIT 100 ";
			
		String dbUrl = "";
				
		Query query = QueryFactory.create(q2);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		ResultSet res = qexec.execSelect();
		
		ArrayList<CountyGeometry> cgList = parsePolygons(res);
		//ResultSetFormatter.out(System.out, res, query);
		qexec.close();
		System.out.println("### DONE ###");
		return cgList;
	}
	public static void main(String args[]){
		readDbAndParse("C:\\Users\\Adam\\Documents\\Java\\Geohive\\geodb.ttl");
		
	}
	
}
