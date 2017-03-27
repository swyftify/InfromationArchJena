import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.jena.ontology.Individual;
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
        ontology = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        BasicConfigurator.configure();

        OntClass Province = ontology.createClass(baseNs + "Province");
        OntClass Division = ontology.createClass(baseNs + "Division");
        OntClass County= ontology.createClass(baseNs + "County");
        OntClass GardaStation = ontology.createClass(baseNs + "GardaStation");
        OntClass Crime = ontology.createClass(baseNs + "Crime");
        OntClass CountyBoundary = ontology.createClass(baseNs + "CountyBoundary");
        OntClass CrimeType = ontology.createClass(baseNs + "CrimeType");
        OntClass CrimeSeverity = ontology.createClass(baseNs + "CrimeSeverity");
        

//        Dog.addDisjointWith(Cat);
        
        OntProperty  containedIn =  ontology.createObjectProperty(baseNs + "containedIn");
        OntProperty  contains =  ontology.createObjectProperty(baseNs + "contains");
        contains.setInverseOf(containedIn);
        contains.addDomain(Province);
        contains.addRange(Division);
        
        OntClass provinceRestriction = ontology.createCardinalityQRestriction(null, containedIn,1 ,Province);
        Province.addSuperClass(provinceRestriction);
       
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

        try 
        {
			ReadModel.loadAllClassesOnt(ontologyName);
		} 
        catch (FileNotFoundException e) 
		{
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
