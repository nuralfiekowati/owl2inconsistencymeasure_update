package owl2inconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

class MIVsharp_jfact {

	public static void mivsharp_measure(Set<Set<OWLAxiom>> arrayOfExplanationSet, HashSet<OWLAxiom> ontologyAxiomSet) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_jfact_MIVsharp_K3.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			System.out.println("12. MIV_Sharp INCONSISTENCY MEASURE: ");

			Set<Set<OWLAxiom>> SetOfMinMIK = new HashSet<Set<OWLAxiom>>();

			for (OWLAxiom axiomInK2 : ontologyAxiomSet) {
				SetOfMinMIK.clear();
				for (Set<OWLAxiom> MinMIK : arrayOfExplanationSet) {
					if (MinMIK.contains(axiomInK2)) {
						SetOfMinMIK.add(MinMIK);
					}
				}
				System.out.println("MIV_Sharp(K," + axiomInK2 + ") = " + SetOfMinMIK.size());

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TotalTimeExecution.totalTime(startTime);

	}

}
