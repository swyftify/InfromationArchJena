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
import org.apache.jena.ontology.TransitiveProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.thrift.wire.RDF_Decimal;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.datatypes.xsd.*;
import org.apache.jena.datatypes.xsd.impl.XSDFloat;
import org.apache.log4j.BasicConfigurator;

public class CreateModel {

    public static String ontologiesBase = "http://lab.Jena.Kdeg.ie/";

    public static String relationshipBase = "http://relationships.lab.Jena.Kdeg.ie/";

    public static String baseNs;

    public static String ontologyName = "Ontology1";

    public static OntModel ontology;

    public static void createModel() throws IOException {

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
        LowSeverity.addLabel("Low", "en");
        HighSeverity.addLabel("High", "en");
        
        OntProperty  containedIn =  ontology.createObjectProperty(baseNs + "containedIn");
        OntProperty  contains =  ontology.createObjectProperty(baseNs + "contains");
        OntProperty geometry =  ontology.createObjectProperty(baseNs + "geometry");
        OntProperty  hasSeverity =  ontology.createObjectProperty(baseNs + "hasSeverity");
        OntProperty coords = ontology.createDatatypeProperty(baseNs + "coords");
        OntProperty  hasNumber =  ontology.createDatatypeProperty(baseNs + "hasNumber");
        OntProperty  hasLong =  ontology.createDatatypeProperty(baseNs + "hasLong");
        OntProperty  hasLat =  ontology.createDatatypeProperty(baseNs + "hasLat");
        OntProperty hasCrime = ontology.createObjectProperty(baseNs + "hasCrime");
        OntProperty occurredIn = ontology.createObjectProperty(baseNs + "occurredIn");
        
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
        hasSeverity.addRange(CrimeSeverity);
        
        hasNumber.addDomain(Crime);

        contains.setInverseOf(containedIn);
        ontology.add(contains, RDF.type, OWL.TransitiveProperty);
        ontology.add(containedIn, RDF.type, OWL.TransitiveProperty);
        contains.addDomain(County);
        contains.addRange(GardaStation);
        containedIn.addDomain(County);
                
        geometry.addDomain(County);
        geometry.addRange(CountyBoundary);
        
        coords.addDomain(CountyBoundary);
        coords.addLiteral(coords, 0.0);
             
        hasCrime.addDomain(GardaStation);
        hasCrime.addRange(Crime);
        hasCrime.setInverseOf(occurredIn);
        
        hasLong.addDomain(GardaStation);
        
        
        hasLat.addDomain(GardaStation);
        
        try {
            writeToFile("ontology" + ".owl");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        
        // FILE READ
       FileReader f = new FileReader("dataset.csv");
       BufferedReader reader = new BufferedReader(f);
       
       String line = "";
       line = reader.readLine();
       while((line = reader.readLine()) != null){
    	   String[] lineSplit = line.split(",");
    	   lineSplit[0] = lineSplit[0].replace(" ", "");
    	   Individual station = ontology.createIndividual(baseNs+lineSplit[0],GardaStation);
    	   station.addLabel(lineSplit[0], "en");
    	   
    	   station.addProperty(hasLong, lineSplit[2]);
    	   station.addProperty(hasLat, lineSplit[3]);
    	   
    	   Individual cr = ontology.createIndividual(baseNs+lineSplit[0]+"Assaults", Assaults);
    	   cr.addLabel("Attempts or threats to murder, assaults, harassments and related offences", "en");
    	   cr.addProperty(hasSeverity, HighSeverity);
    	   cr.addProperty(hasNumber, lineSplit[4]);
    	   station.addProperty(hasCrime, cr);
    	   
    	   Individual danger = ontology.createIndividual(baseNs+lineSplit[0]+"DangerousActs", DangerousActs);
    	   danger.addLabel("Dangerous or negligent acts", "en");
    	   danger.addProperty(hasNumber, lineSplit[5]);
    	   danger.addProperty(hasSeverity, HighSeverity);
    	   station.addProperty(hasCrime, danger);
    	   
    	   Individual kidnapping = ontology.createIndividual(baseNs+lineSplit[0]+"Kidnapping", Kidnapping);
    	   kidnapping.addLabel("Kidnapping and related offences", "en");
    	   kidnapping.addProperty(hasNumber, lineSplit[6]);
    	   kidnapping.addProperty(hasSeverity, HighSeverity);
    	   station.addProperty(hasCrime, kidnapping);
    	   
    	   Individual robery = ontology.createIndividual(baseNs+lineSplit[0]+"Robbery", Robbery);
    	   robery.addLabel("Robbery, extortion and hijacking offences", "en");
    	   robery.addProperty(hasNumber, lineSplit[7]);
    	   robery.addProperty(hasSeverity, HighSeverity);
    	   station.addProperty(hasCrime, robery);
    	   
    	   Individual burgluary = ontology.createIndividual(baseNs+lineSplit[0]+"Burglary", Burglary);
    	   burgluary.addLabel("Burglary and related offences", "en");
    	   burgluary.addProperty(hasNumber, lineSplit[8]);
    	   burgluary.addProperty(hasSeverity, HighSeverity);
    	   station.addProperty(hasCrime, burgluary);
    	   
    	   Individual theft = ontology.createIndividual(baseNs+lineSplit[0]+"Theft", Theft);
    	   theft.addLabel("Theft and related offences", "en");
    	   theft.addProperty(hasNumber, lineSplit[9]);
    	   theft.addProperty(hasSeverity, LowSeverity);
    	   station.addProperty(hasCrime, theft);
    	   
    	   Individual fraud = ontology.createIndividual(baseNs+lineSplit[0]+"Fraud", Fraud);
    	   fraud.addLabel("Fraud, deception and related offences", "en");
    	   fraud.addProperty(hasNumber, lineSplit[10]);
    	   fraud.addProperty(hasSeverity, LowSeverity);
    	   station.addProperty(hasCrime, fraud);
    	   
    	   Individual drugs = ontology.createIndividual(baseNs+lineSplit[0]+"Drugs", Drugs);
    	   drugs.addLabel("Controlled drug offences", "en");
    	   drugs.addProperty(hasNumber, lineSplit[11]);
    	   drugs.addProperty(hasSeverity, LowSeverity);
    	   station.addProperty(hasCrime, drugs);
    	   
    	   Individual weapons = ontology.createIndividual(baseNs+lineSplit[0]+"Weapons", Weapons);
    	   weapons.addLabel("Weapons and Explosives Offences", "en");
    	   weapons.addProperty(hasNumber, lineSplit[12]);
    	   weapons.addProperty(hasSeverity, HighSeverity);
    	   station.addProperty(hasCrime, weapons);
    	   
    	   Individual damagedproperty = ontology.createIndividual(baseNs+lineSplit[0]+"DamagedProperty", DamagedProperty);
    	   damagedproperty.addLabel("Damage to property and to the environment", "en");
    	   damagedproperty.addProperty(hasNumber, lineSplit[13]);
    	   damagedproperty.addProperty(hasSeverity, LowSeverity);
    	   station.addProperty(hasCrime, damagedproperty);
    	   
    	   Individual publicOffences = ontology.createIndividual(baseNs+lineSplit[0]+"PublicOrderOffences", PublicOrderOffences);
    	   publicOffences.addLabel("Public order and other social code offences", "en");
    	   publicOffences.addProperty(hasNumber, lineSplit[14]);
    	   publicOffences.addProperty(hasSeverity, LowSeverity);
    	   station.addProperty(hasCrime, publicOffences);
    	   
    	   Individual governmentOffences = ontology.createIndividual(baseNs+lineSplit[0]+"GovernmentOffences", GovernmentOffences);
    	   governmentOffences.addLabel("Offences against government, justice procedures and organisation of crime", "en");
    	   governmentOffences.addProperty(hasNumber, lineSplit[15]);
    	   governmentOffences.addProperty(hasSeverity, HighSeverity);
    	   station.addProperty(hasCrime, governmentOffences);
    	   
       }
       

       try {
           writeToFile("data" + ".owl");
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }

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
