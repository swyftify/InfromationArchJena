import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;

public class UIMain {

	public static void main(String[] args) throws IOException {

		Scanner reader = new Scanner(System.in);
		boolean isExit = false;
		String inputCounty = "";
		String getRootsQuery =
				"SELECT ?crime ?num WHERE " + "{"
						+ "<http://lab.Jena.Kdeg.ie/Ontology1#Abbeyfeale> <http://lab.Jena.Kdeg.ie/Ontology1#hasCrime> ?crime."
						+ "?crime <http://lab.Jena.Kdeg.ie/Ontology1#hasNumber> ?num." + "}";

		// CreateModel.createModel();

		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_LITE_MEM, null);
		readOntology("data.owl", model);

		traverseStart(model, null, getRootsQuery);

		// ------Default ReadModel class--------
		// ReadModel.loadAllClassesOnt("data.owl");

		while (!isExit) {
			System.out.println("Please select one of the following options:");
			System.out.println("Press [1] to get the crime with most occurences in a county");
			System.out.println("Press [2] to get number of specific crime in a county");
			System.out
					.println("Press [3] to list the offences in a specific county above a threshold number of reports");
			System.out.println(
					"Press [4] to get a list of the most popular crime for a county and each of the border counties");
			System.out.println("Press [5] to get a number of specific crime for a county and bordering counties");
			System.out.println("Press [6] to get a number of violet and non-violet crimes for a county");
			System.out.println(
					"Press [7] to list the offences in a specified county below a threshold number of reports");
			System.out.println("Press [8] to get a county with max number of specified crime");
			System.out.println("Press [9] to get number of Garda stations in a county");
			System.out.println("Press [10] to exit");
			int option = reader.nextInt();
			Scanner nextReader = new Scanner(System.in);
			switch (option) {
			case 1:
			case 4:
			case 6:
			case 9:
				System.out.println("Type in the county name:");
				inputCounty = nextReader.nextLine();
				System.out.println(inputCounty);
				break;

			case 2:
			case 5:
				System.out.println("Type in the county name: ");
				inputCounty = nextReader.nextLine();
				System.out.println();
				System.out.println("Select a crime type: ");
				String result = printCrimeList(new Scanner(System.in));
				break;

			case 3:
			case 7:
				System.out.println("Type in the county name: ");
				inputCounty = nextReader.nextLine();
				System.out.println();
				System.out.println("Specify a threashold: ");
				int threshold = nextReader.nextInt();
				break;

			case 8:
				System.out.println("Select a crime type: ");
				String resultnw = printCrimeList(new Scanner(System.in));
				break;

			case 10:
				System.out.println("Exiting");
				isExit = true;
				break;

			default:
				System.out.println("-----------Wrong Choice-------------");
				break;
			}

		}
		reader.close();
	}

	public static String printCrimeList(Scanner reader) {
		String[] crimeList = { "Attempts or threats to murder, assaults, harassments and related offences",
				"Dangerous or negligent acts", "Kidnapping and related offences",
				"Robbery, extortion and hijacking offences", "Weapons and Explosives Offences",
				"Offences against government, justice procedures and organisation of crime",
				"Public order and other social code offences", "Damage to property and to the environment",
				"Fraud, deception and related offences", "Burglary and related offences", "Theft and related offences",
				"Controlled drug offences" };

		String result = "";
		for (int i = 0; i < crimeList.length; i++) {
			System.out.println("[" + (i + 1) + "] " + crimeList[i]);
		}
		int crimeIn = reader.nextInt();
		if (crimeIn >= 1 && crimeIn <= 12) {
			System.out.println("Chosen: " + crimeList[crimeIn - 1]);
			System.out.println();
			result = crimeList[crimeIn - 1];
		} else {
			System.out.println("No choice labeled " + crimeIn);
			System.out.println();
		}

		return result;
	}

	public static void readOntology(String file, OntModel model) {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			model.read(in, "RDF/XML");
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static List<String> getRoots(OntModel model, String rootQuery) {
		List<String> roots = new ArrayList<String>();

		String getRootsQuery = rootQuery;

		Query query = QueryFactory.create(getRootsQuery);

		try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				RDFNode sub = soln.get("crime");
				RDFNode sec = soln.get("num");

				if (!sub.isURIResource())
					continue;

				roots.add(sub.toString());
				roots.add(sec.toString());
			}
		}

		return roots;
	}

	public static void traverseStart(OntModel model, String entity, String rootQuery) {
		// if starting class available
		if (entity != null) {
			traverse(model, entity, new ArrayList<String>(), 0);
		}
		// get roots and traverse each root
		else {
			List<String> roots = getRoots(model, rootQuery);

			for (int i = 0; i < roots.size(); i++) {
				traverse(model, roots.get(i), new ArrayList<String>(), 0);
			}
		}
	}

	public static void traverse(OntModel model, String entity, List<String> occurs, int depth) {
		if (entity == null)
			return;

		String queryString = "SELECT ?s WHERE { " + "?s <http://www.w3.org/2000/01/rdf-schema#subClassOf> <" + entity
				+ "> . }";

		Query query = QueryFactory.create(queryString);

		if (!occurs.contains(entity)) {
			// print depth times "\t" to retrieve an explorer tree like output
			for (int i = 0; i < depth; i++) {
				System.out.print("\t");
			}
			// print out the URI
			System.out.println(entity);

			try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
				ResultSet results = qexec.execSelect();
				while (results.hasNext()) {
					QuerySolution soln = results.nextSolution();
					RDFNode sub = soln.get("crime");
					

					if (!sub.isURIResource())
						continue;

					String str = sub.toString();

					occurs.add(entity);
					traverse(model, str, occurs, depth + 1);
					occurs.remove(entity);
				}

			}
		}

	}

}
