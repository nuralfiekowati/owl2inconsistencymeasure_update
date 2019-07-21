package owl2inconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;

import org.semanticweb.owlapi.model.OWLAxiom;

class MIVd {

	public static void MIVd_measure(HashSet<OWLAxiom> MIKAxiomSet, HashSet<OWLAxiom> ontologyAxiomSet) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_MIVd.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			System.out.println("11. MIVd INCONSISTENCY MEASURE MIV_d: ");
			for (OWLAxiom axiomInK : ontologyAxiomSet) {
				if (MIKAxiomSet.contains(axiomInK) == true) {
					System.out.println("MIV_D(K," + axiomInK + ") = 1");
				} else if (MIKAxiomSet.contains(axiomInK) == false) {
					System.out.println("MIV_D(K," + axiomInK + ") = 0");
				}
			}

			System.out.println("***************************************************************");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TotalTimeExecution.totalTime(startTime);
	}

}
