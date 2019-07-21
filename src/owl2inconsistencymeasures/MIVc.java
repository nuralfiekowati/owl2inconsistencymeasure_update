package owl2inconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import java.text.DecimalFormat;

import org.semanticweb.owlapi.model.OWLAxiom;

class MIVc {

	public static void MIVc_measure(Set<Set<OWLAxiom>> arrayOfExplanationSet, HashSet<OWLAxiom> ontologyAxiomSet) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_MIVc.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			System.out.println("13. MIVc INCONSISTENCY MEASURE MIV_c: ");

			float onePerMsize, MIVc = 0;

			for (OWLAxiom axiomInK3 : ontologyAxiomSet) {
				MIVc = 0;
				for (Set<OWLAxiom> MinMIK : arrayOfExplanationSet) {
					if (MinMIK.contains(axiomInK3)) {
						onePerMsize = (float) 1 / (MinMIK.size());
						MIVc = MIVc + onePerMsize;
					}
				}

				System.out.println("MIV_c(K," + axiomInK3 + ") = " + new DecimalFormat("####.##").format(MIVc));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TotalTimeExecution.totalTime(startTime);

	}

}
