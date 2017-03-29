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
        
        OntClass LowSeverity = ontology.createClass(baseNs + "LowSeverity");
        OntClass HighSeverity = ontology.createClass(baseNs + "HighSeverity");
        
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
        
        CrimeSeverity.addSubClass(HighSeverity);
        CrimeSeverity.addSubClass(LowSeverity);
        
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
        
        hasSeverity.addDomain(Crime);
        
        hasNumber.addDomain(Crime);

        contains.setInverseOf(containedIn);
       

        contains.addRange(County);
        containedIn.addDomain(County);
        
       
        
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
    	   lineSplit[0] = lineSplit[0].replace(" ", "");
    	   Individual station = ontology.createIndividual(baseNs+lineSplit[0],GardaStation);
  
    	   station.addProperty(hasX, lineSplit[2]);
    	   station.addProperty(hasY, lineSplit[3]);
    	   Individual cr = ontology.createIndividual(baseNs+lineSplit[0]+"Assaults", Assaults);
    	   cr.addProperty(hasNumber, lineSplit[4]);
    	   station.addProperty(hasCrime, cr);
    	   
    	   Individual danger = ontology.createIndividual(baseNs+lineSplit[0]+"DangerousActs", DangerousActs);
    	   danger.addProperty(hasNumber, lineSplit[5]);
    	   station.addProperty(hasCrime, danger);
    	   
    	   Individual kidnapping = ontology.createIndividual(baseNs+lineSplit[0]+"Kidnapping", Kidnapping);
    	   kidnapping.addProperty(hasNumber, lineSplit[6]);
    	   station.addProperty(hasCrime, kidnapping);
    	   
    	   Individual robery = ontology.createIndividual(baseNs+lineSplit[0]+"Robbery", Robbery);
    	   robery.addProperty(hasNumber, lineSplit[7]);
    	   station.addProperty(hasCrime, robery);
    	   
    	   Individual burgluary = ontology.createIndividual(baseNs+lineSplit[0]+"Burglary", Burglary);
    	   burgluary.addProperty(hasNumber, lineSplit[8]);
    	   station.addProperty(hasCrime, burgluary);
    	   
    	   Individual theft = ontology.createIndividual(baseNs+lineSplit[0]+"Theft", Theft);
    	   theft.addProperty(hasNumber, lineSplit[9]);
    	   station.addProperty(hasCrime, theft);
    	   
    	   Individual fraud = ontology.createIndividual(baseNs+lineSplit[0]+"Fraud", Fraud);
    	   fraud.addProperty(hasNumber, lineSplit[10]);
    	   station.addProperty(hasCrime, fraud);
    	   
    	   Individual drugs = ontology.createIndividual(baseNs+lineSplit[0]+"Drugs", Drugs);
    	   drugs.addProperty(hasNumber, lineSplit[11]);
    	   station.addProperty(hasCrime, drugs);
    	   
    	   Individual weapons = ontology.createIndividual(baseNs+lineSplit[0]+"Weapons", Weapons);
    	   weapons.addProperty(hasNumber, lineSplit[12]);
    	   station.addProperty(hasCrime, weapons);
    	   
    	   Individual damagedproperty = ontology.createIndividual(baseNs+lineSplit[0]+"DamagedProperty", DamagedProperty);
    	   damagedproperty.addProperty(hasNumber, lineSplit[13]);
    	   station.addProperty(hasCrime, damagedproperty);
    	   
    	   Individual publicOffences = ontology.createIndividual(baseNs+lineSplit[0]+"PublicOrderOffences", PublicOrderOffences);
    	   publicOffences.addProperty(hasNumber, lineSplit[14]);
    	   station.addProperty(hasCrime, publicOffences);
    	   
    	   Individual governmentOffences = ontology.createIndividual(baseNs+lineSplit[0]+"GovernmentOffences", GovernmentOffences);
    	   governmentOffences.addProperty(hasNumber, lineSplit[15]);
    	   station.addProperty(hasCrime, governmentOffences);
    	   
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
