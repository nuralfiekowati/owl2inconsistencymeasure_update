package owl2inconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.impl.blackbox.checker.InconsistentOntologyExplanationGeneratorFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyRenameException;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.TimeOutException;

import uk.ac.manchester.cs.jfact.JFactFactory;

class Scoring {

	static int Ksize;
	static float sizeOfMI;
	static float I_ir;

	public static void Is_measure(Set<Explanation<OWLAxiom>> explanations, HashSet<OWLAxiom> ontologyAxiomSet) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_Is.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			System.out.println("15. SCORING INCONSISTENCY MEASURE I_s: ");

			OWLReasonerFactory rf7 = new JFactFactory(); // for jfact
			OWLOntologyManager manager7 = OWLManager.createOWLOntologyManager();
			OWLOntology AxiomOntology7;
			OWLDataFactory df7 = manager7.getOWLDataFactory();
			AddAxiom addAxiom7;
			Set<OWLAxiom> axiomsToRemove7;
			int sizeOfMIOfK, sizeOfMIOfKMinusAlpha;
			float scoringValue;

			for (OWLAxiom alpha : ontologyAxiomSet) {
				System.out.println("Alpha: " + alpha);

				manager7 = OWLManager.createOWLOntologyManager();
				AxiomOntology7 = manager7.createOntology();

				axiomsToRemove7 = AxiomOntology7.getAxioms();

				if (axiomsToRemove7 != null) {
					manager7.removeAxioms(AxiomOntology7, axiomsToRemove7);
				}

				for (OWLAxiom alpha2 : ontologyAxiomSet) {
					addAxiom7 = new AddAxiom(AxiomOntology7, alpha2);
					manager7.applyChange(addAxiom7);
				}

				OWLReasoner reasoner7 = rf7.createReasoner(AxiomOntology7); // for
																			// hermit
																			// and
																			// JFact
				System.out.println("========================================");

				RemoveAxiom removeAxiom7 = new RemoveAxiom(AxiomOntology7, alpha);
				manager7.applyChange(removeAxiom7);

				Set<OWLAxiom> kminusalphaaxioms = AxiomOntology7.getAxioms();
				System.out.println("Size of K minus axiom alpha: " + kminusalphaaxioms.size());
				for (OWLAxiom memberOfkminusalphaaxioms : kminusalphaaxioms) {
					System.out.println("Member of K minus axiom alpha: " + memberOfkminusalphaaxioms);
				}

				System.out.println("Is K minus alpha consistent? " + reasoner7.isConsistent());

				ExplanationGenerator<OWLAxiom> explainInconsistency7 = new InconsistentOntologyExplanationGeneratorFactory(
						rf7, 1000000000000000000L).createExplanationGenerator(AxiomOntology7);

				Set<Explanation<OWLAxiom>> explanations7 = explainInconsistency7
						.getExplanations(df7.getOWLSubClassOfAxiom(df7.getOWLThing(), df7.getOWLNothing()), 941); // set
																													// the
																													// limit
																													// of
																													// entailment
				System.out.println("Explanation of inconsistency K minus alpha: " + explanations7);

				sizeOfMIOfK = explanations.size();
				sizeOfMIOfKMinusAlpha = explanations7.size();

				System.out.println("Size of MI of K: " + sizeOfMIOfK);
				System.out.println("Size of MI of K minus alpha: " + sizeOfMIOfKMinusAlpha);

				scoringValue = sizeOfMIOfK - sizeOfMIOfKMinusAlpha;

				System.out.println("(Size of MI of K) - (Size of MI of K minus alpha): " + scoringValue);

				System.out.println("========================================");

				System.out.println("I_s(K," + alpha + ") = " + scoringValue);
				System.out.println("-----------------------------------------------");
			}

		} catch (OWLOntologyRenameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeOutException f) {
			// TODO Auto-generated catch block
			f.printStackTrace();
		} catch (ReasonerInterruptedException g) {
			// TODO Auto-generated catch block
			g.printStackTrace();
		} catch (OWLOntologyCreationException h) {
			// TODO Auto-generated catch block
			h.printStackTrace();
		} catch (FileNotFoundException i) {
			i.printStackTrace();
		}

		TotalTimeExecution.totalTime(startTime);

	}

}
