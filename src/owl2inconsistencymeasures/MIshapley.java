package owl2inconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import java.text.DecimalFormat;

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

class MIshapley {

	public static void MIshapley_measure(OWLDataFactory df, HashSet<OWLAxiom> ontologyAxiomSet) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_MIVshapley.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			System.out.println("14. MIVshapley INCONSISTENCY MEASURE MI_shapley: ");

			double shapleyValue, shapleyVal;
			// long shapleyValue, shapleyVal;
			int c, n;
			// System.out.println("Factorial of 3: " + factorial(3));

			OWLReasonerFactory rf4 = new JFactFactory(); // for jfact
			OWLOntologyManager manager4 = OWLManager.createOWLOntologyManager();
			OWLOntology AxiomOntology = manager4.createOntology();
			OWLDataFactory df4 = manager4.getOWLDataFactory();
			AddAxiom addAxiom4;
			Set<OWLAxiom> axiomsToRemove4;
			int sizeOfMIOfC, sizeOfMIOfCMinusAlpha;
			// float factorial1, factorial2, factorial3, firstComputation,
			// secondComputation;
			long factorial1, factorial2, factorial3;
			double firstComputation, secondComputation;

			for (OWLAxiom alpha : ontologyAxiomSet) {
				System.out.println("Alpha: " + alpha);
				shapleyValue = 0;
				shapleyVal = 0;
				for (Set<OWLAxiom> s : PowerSetCount.powerSet(ontologyAxiomSet)) {
					c = s.size();
					n = ontologyAxiomSet.size();

					manager4 = OWLManager.createOWLOntologyManager();
					AxiomOntology = manager4.createOntology();

					axiomsToRemove4 = AxiomOntology.getAxioms();
					// for (OWLAxiom ax : axiomsToRemove)
					if (axiomsToRemove4 != null) {
						manager4.removeAxioms(AxiomOntology, axiomsToRemove4);
					}

					for (OWLAxiom axiomOfS : s) {
						addAxiom4 = new AddAxiom(AxiomOntology, axiomOfS);
						manager4.applyChange(addAxiom4);
					}

					OWLReasoner reasoner4 = rf4.createReasoner(AxiomOntology); // for
																				// hermit
																				// and
																				// JFact

					System.out.println("========================================");

					// //for hermit and JFact
					System.out.println("C: " + s);
					System.out.println("Is C consistent? " + reasoner4.isConsistent());

					ExplanationGenerator<OWLAxiom> explainInconsistency4 = new InconsistentOntologyExplanationGeneratorFactory(
							rf4, 1000000000000000000L).createExplanationGenerator(AxiomOntology);

					Set<Explanation<OWLAxiom>> explanations4 = explainInconsistency4
							.getExplanations(df4.getOWLSubClassOfAxiom(df4.getOWLThing(), df4.getOWLNothing()), 941); // set
																														// the
																														// limit
																														// of
																														// entailment
					System.out.println("Explanation of inconsistency of C: " + explanations4);

					RemoveAxiom removeAxiom = new RemoveAxiom(AxiomOntology, alpha);
					manager4.applyChange(removeAxiom);

					Set<OWLAxiom> cminusalphaaxioms = AxiomOntology.getAxioms();
					for (OWLAxiom memberOfcminusalphaaxioms : cminusalphaaxioms) {
						System.out.println("Member of C minus alpha axioms: " + memberOfcminusalphaaxioms);
					}

					sizeOfMIOfC = explanations4.size();

					System.out.println("Size of MI of C: " + sizeOfMIOfC);

					System.out.println("Is C minus alpha consistent? " + reasoner4.isConsistent());

					ExplanationGenerator<OWLAxiom> explainInconsistency5 = new InconsistentOntologyExplanationGeneratorFactory(
							rf4, 1000000000000000000L).createExplanationGenerator(AxiomOntology);

					Set<Explanation<OWLAxiom>> explanations5 = explainInconsistency5
							.getExplanations(df.getOWLSubClassOfAxiom(df4.getOWLThing(), df4.getOWLNothing()), 941); // set
																														// the
																														// limit
																														// of
																														// entailment
					System.out.println("Explanation of inconsistency C minus alpha: " + explanations5);

					sizeOfMIOfCMinusAlpha = explanations5.size();

					System.out.println("Size of MI of C minus alpha: " + sizeOfMIOfCMinusAlpha);

					System.out.println("Size of n: " + n);
					System.out.println("Size of c: " + c);

					factorial1 = FactorialCount.factorial(c - 1);
					factorial2 = FactorialCount.factorial(n - c);
					factorial3 = FactorialCount.factorial(n);
					firstComputation = ((double) factorial1 * (double) factorial2) / (double) factorial3;
					secondComputation = (double) sizeOfMIOfC - (double) sizeOfMIOfCMinusAlpha;

					System.out.println("factorial(c-1): " + new DecimalFormat("####.##").format((double) factorial1));
					System.out.println("factorial(n-c): " + new DecimalFormat("####.##").format((double) factorial2));
					System.out.println("factorial(n): " + new DecimalFormat("####.##").format((double) factorial3));
					System.out.println("First Computation: " + new DecimalFormat("####.##").format(firstComputation));
					System.out.println("(Size of MI of C) - (Size of MI of C minus alpha): "
							+ new DecimalFormat("####.##").format(secondComputation));

					shapleyVal = firstComputation * secondComputation;
					System.out.println("Shapley value: " + new DecimalFormat("####.##").format(shapleyVal));
					shapleyValue = shapleyValue + shapleyVal;
					System.out.println("Sum of Shapley Value = " + new DecimalFormat("####.##").format(shapleyValue));
					System.out.println("========================================");
				}
				System.out.println("S^(I_MI)(K," + alpha + ") = " + new DecimalFormat("####.##").format(shapleyValue));
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
