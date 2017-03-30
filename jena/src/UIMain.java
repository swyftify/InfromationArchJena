import java.util.Scanner;

public class UIMain {

	public static void main(String[] args) {

		Scanner reader = new Scanner(System.in);
		boolean isExit = false;
		String inputCounty = "";

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

}
