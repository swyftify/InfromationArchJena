import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.jena.ontology.MaxCardinalityRestriction;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.log4j.BasicConfigurator;

public class CreateModel {

    public static String ontologiesBase = "http://lab.Jena.Kdeg.ie/";

    public static String relationshipBase = "http://relationships.lab.Jena.Kdeg.ie/";

    public static String baseNs;

    public static String ontologyName = "Ontology1";

    public static OntModel ontology;

    public static void main(String args[]) {

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
        OntClass CrimeType = ontology.createClass(baseNs + "CrimeType");
        OntClass CrimeSeverity = ontology.createClass(baseNs + "CrimeSeverity");
       
        OntProperty  containedIn =  ontology.createObjectProperty(baseNs + "containedIn");
        OntProperty  contains =  ontology.createObjectProperty(baseNs + "contains");
        contains.setInverseOf(containedIn);
        contains.addDomain(Province);
        contains.addRange(Division);
        containedIn.addDomain(Division);
        containedIn.addRange(Province);
       
        contains.addDomain(Division);
        contains.addRange(County);
        containedIn.addDomain(County);
        containedIn.addRange(Division);
       
        OntProperty geometry =  ontology.createObjectProperty(baseNs + "geometry");
        geometry.addDomain(County);
        geometry.addRange(CountyBoundary);
       
        OntProperty coords = ontology.createObjectProperty(baseNs + "coords");
        coords.addDomain(CountyBoundary);
        coords.addLiteral(coords, 0.0);
       
        contains.addDomain(County);
        contains.addRange(GardaStation);
        containedIn.addDomain(GardaStation);
        containedIn.addRange(County);
       
        OntProperty hasOccurred = ontology.createObjectProperty(baseNs + "hasOccurred");
        hasOccurred.addDomain(GardaStation);
        hasOccurred.addRange(Crime);
       
        
//        OntClass provinceRestriction = ontology.createCardinalityRestriction(null, contains,1);
//        Province.addSuperClass(provinceRestriction);
        
        
        long crimeNumber = 0;
        OntProperty  hasNumber =  ontology.createObjectProperty(baseNs + "hasNumber");
        hasNumber.addDomain(Crime);
        hasNumber.addLiteral(hasNumber, crimeNumber);

        OntProperty  hasCrimeDetails =  ontology.createObjectProperty(baseNs + "hasCrimeDetails");
        hasCrimeDetails.addDomain(Crime);
        hasCrimeDetails.addRange(CrimeType);
        
        OntProperty  hasType =  ontology.createObjectProperty(baseNs + "hasType");
        hasType.addDomain(CrimeType);
        hasType.addLiteral(hasType, "");
        
        OntProperty  hasSeverity =  ontology.createObjectProperty(baseNs + "hasSeverity");
        hasSeverity.addDomain(CrimeType);
        hasSeverity.addRange(CrimeSeverity);
        
        OntProperty  is =  ontology.createObjectProperty(baseNs + "is");
        is.addDomain(CrimeSeverity);
        is.addLiteral(is, "");
        
       
////       
//        OntProperty  chasedby =  ontology.createObjectProperty(baseNs + "chased_by");
//        chasedby.addInverseOf(chases);
//       
//        OntProperty  runsAfter =  ontology.createObjectProperty(baseNs + "runs_after");
//        runsAfter.addEquivalentProperty(chases);
//       
//        OntProperty  eats =  ontology.createObjectProperty(baseNs + "eats");
//        chases.addSubProperty(eats);
        
//        Individual Garfield = Cat.createIndividual(Cat+"Garfield");
//        Individual Snoopy = Dog.createIndividual();
    
       
       
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
