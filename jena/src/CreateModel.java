import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.datatypes.xsd.*;
import org.apache.log4j.BasicConfigurator;

public class CreateModel {

    public static String ontologiesBase = "http://lab.Jena.Kdeg.ie/";

    public static String relationshipBase = "http://relationships.lab.Jena.Kdeg.ie/";

    public static String baseNs;

    public static String ontologyName = "Ontology1";

    public static OntModel ontology;

    public static void main(String args[]) throws IOException {

	//ontologyName=args[0];
        baseNs = ontologiesBase + ontologyName + "#";
        ontology = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF);
        BasicConfigurator.configure();

        OntClass Province = ontology.createClass(baseNs + "Province");
        OntClass Division = ontology.createClass(baseNs + "Division");
        OntClass County= ontology.createClass(baseNs + "County");
        OntClass GardaStation = ontology.createClass(baseNs + "GardaStation");
        OntClass Crime = ontology.createClass(baseNs + "Crime");
        OntClass CountyBoundary = ontology.createClass(baseNs + "CountyBoundary");
        OntClass CrimeSeverity = ontology.createClass(baseNs + "CrimeSeverity");
        
        OntClass Assaults = ontology.createClass(baseNs + "Assaults");
        OntClass DangerousActs = ontology.createClass(baseNs + "DangerousActs");
        OntClass Kidnapping = ontology.createClass(baseNs + "Kidnapping");
        OntClass Robbery = ontology.createClass(baseNs + "Robbery");
        OntClass Burglary = ontology.createClass(baseNs + "Burglary");
        OntClass Theft = ontology.createClass(baseNs + "Theft");
        OntClass Fraud = ontology.createClass(baseNs + "Fraud");
        OntClass Drugs = ontology.createClass(baseNs + "Drugs");
        OntClass Weapons = ontology.createClass(baseNs + "Weapons");
        OntClass DamagedProperty = ontology.createClass(baseNs + "DamagedProperty");
        OntClass PublicOrderOffences = ontology.createClass(baseNs + "PublicOrderOffences");
        OntClass GovernmentOffences = ontology.createClass(baseNs + "GovernmentOffences");
        
        OntProperty  containedIn =  ontology.createObjectProperty(baseNs + "containedIn");
        OntProperty  contains =  ontology.createObjectProperty(baseNs + "contains");
        OntProperty geometry =  ontology.createObjectProperty(baseNs + "geometry");
        OntProperty  hasSeverity =  ontology.createObjectProperty(baseNs + "hasSeverity");
        OntProperty coords = ontology.createObjectProperty(baseNs + "coords");
        OntProperty hasOccurred = ontology.createObjectProperty(baseNs + "hasOccurred");
        OntProperty  hasNumber =  ontology.createObjectProperty(baseNs + "hasNumber");
        OntProperty  is =  ontology.createObjectProperty(baseNs + "is");
        OntProperty  hasX =  ontology.createObjectProperty(baseNs + "hasX");
        OntProperty  hasY =  ontology.createObjectProperty(baseNs + "hasY");
        OntProperty hasCrime = ontology.createObjectProperty(baseNs + "hasCrime");
        //TODO domain range for hascrime
        
        Crime.addSubClass(Assaults);
        Crime.addSubClass(DangerousActs);
        Crime.addSubClass(Kidnapping);
        Crime.addSubClass(Robbery);
        Crime.addSubClass(Burglary);
        Crime.addSubClass(Theft);
        Crime.addSubClass(Fraud);
        Crime.addSubClass(Drugs);
        Crime.addSubClass(Weapons);
        Crime.addSubClass(DamagedProperty);
        Crime.addSubClass(PublicOrderOffences);
        Crime.addSubClass(GovernmentOffences);
        
        hasSeverity.addDomain(Assaults);
        hasSeverity.addDomain(DangerousActs);
        hasSeverity.addDomain(Kidnapping);
        hasSeverity.addDomain(Robbery);
        hasSeverity.addDomain(Burglary);
        hasSeverity.addDomain(Theft);
        hasSeverity.addDomain(Fraud);
        hasSeverity.addDomain(Drugs);
        hasSeverity.addDomain(Weapons);
        hasSeverity.addDomain(DamagedProperty);
        hasSeverity.addDomain(PublicOrderOffences);
        hasSeverity.addDomain(GovernmentOffences);
        
        hasNumber.addDomain(Assaults);
        hasNumber.addDomain(DangerousActs);
        hasNumber.addDomain(Kidnapping);
        hasNumber.addDomain(Robbery);
        hasNumber.addDomain(Burglary);
        hasNumber.addDomain(Theft);
        hasNumber.addDomain(Fraud);
        hasNumber.addDomain(Drugs);
        hasNumber.addDomain(Weapons);
        hasNumber.addDomain(DamagedProperty);
        hasNumber.addDomain(PublicOrderOffences);
        hasNumber.addDomain(GovernmentOffences);
       

        contains.setInverseOf(containedIn);
        contains.addDomain(Province);
        contains.addRange(Division);
        containedIn.addDomain(Division);
        containedIn.addRange(Province);
       
        contains.addDomain(Division);
        contains.addRange(County);
        containedIn.addDomain(County);
        containedIn.addRange(Division);
       
        
        geometry.addDomain(County);
        geometry.addRange(CountyBoundary);
       
        
        coords.addDomain(CountyBoundary);
        coords.addLiteral(coords, 0.0);
       
        contains.addDomain(County);
        contains.addRange(GardaStation);
        containedIn.addDomain(GardaStation);
        containedIn.addRange(County);
             
        hasOccurred.addDomain(GardaStation);
        hasOccurred.addRange(Crime);
        
        is.addDomain(CrimeSeverity);
        is.addLiteral(is, "");
        
        
        hasX.addDomain(GardaStation);
        
        
        hasY.addDomain(GardaStation);
        
        
        // FILE READ
       FileReader f = new FileReader("dataset.csv");
       BufferedReader reader = new BufferedReader(f);
       
       String line = "";
      
       while((line = reader.readLine()) != null){
    	   String[] lineSplit = line.split(",");
    	   Individual station = ontology.createIndividual(baseNs+lineSplit[0],GardaStation);
    	   Individual division = ontology.createIndividual(baseNs + lineSplit[1],Division);
    	   station.addProperty(hasX, lineSplit[2]);
    	   station.addProperty(hasY, lineSplit[3]);
    	   Individual cr = ontology.createIndividual(baseNs+lineSplit[0]+"Assaults", Assaults);
    	   cr.addProperty(hasNumber, lineSplit[4]);
    	   station.addProperty(hasCrime, cr);
    	   
    	   Individual danger = ontology.createIndividual(baseNs+lineSplit[0]+"DangerousActs", DangerousActs);
    	   danger.addProperty(hasNumber, lineSplit[5]);
    	   station.addProperty(hasCrime, danger);
    	   
    	   
    	   
       }
       
        try {
            writeToFile(ontologyName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        try 
//        {
//			ReadModel.loadAllClassesOnt(ontologyName);
//		} 
//        catch (FileNotFoundException e) 
//		{
//			e.printStackTrace();
//		}

    }

    public static void writeToFile(String filename)
            throws FileNotFoundException {
        try {
            ontology.write(new FileOutputStream(new File(filename)),
                    "RDF/XML-ABBREV");
            System.out.println("Ontology written to file.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

} 
