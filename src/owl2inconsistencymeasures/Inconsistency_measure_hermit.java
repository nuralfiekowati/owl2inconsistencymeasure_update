package owl2inconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationException;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorInterruptedException;
import org.semanticweb.owl.explanation.impl.blackbox.checker.InconsistentOntologyExplanationGeneratorFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.TimeOutException;

//import uk.ac.manchester.cs.jfact.JFactFactory;
//import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
//import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
//import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;

public class Inconsistency_measure_hermit {

	static Set<OWLAxiom> arrayOfExplanation = null;
	static Set<Set<OWLAxiom>> arrayOfExplanationSet = new HashSet<Set<OWLAxiom>>(3000000, 1000000F);
	static HashSet<OWLAxiom> MIKAxiomSet = new HashSet<OWLAxiom>();
	static HashSet<OWLAxiom> topBottom = new HashSet<OWLAxiom>();
	static HashSet<OWLClass> MIKClassSet = new HashSet<OWLClass>();
	static HashSet<OWLNamedIndividual> MIKIndividualSet = new HashSet<OWLNamedIndividual>();
	static HashSet<OWLObjectProperty> MIKObjectPropertySet = new HashSet<OWLObjectProperty>();
	static Set<OWLClass> inconsistentClass = null;
	static Set<OWLNamedIndividual> inconsistentIndividual = null;
	static Set<OWLObjectProperty> inconsistentObjectProperty = null;
	static HashSet<OWLAxiom> ontologyAxiomSet = new HashSet<OWLAxiom>(3000000, 1000000F);

	public static void main(String[] args) throws Exception {

		try {
			File inputOntologyFile = new File("examples/knowledgebaseK4.owl");

			ReasonerFactory rf = new ReasonerFactory(); // for hermit

			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

			OWLDataFactory df = manager.getOWLDataFactory();

			OWLOntology ontology = manager.loadOntologyFromOntologyDocument(inputOntologyFile);

			Configuration configuration = new Configuration(); // for hermit
			configuration.throwInconsistentOntologyException = false; // for
																		// hermit

			OWLReasoner reasoner = rf.createReasoner(ontology, configuration); // for
																				// hermit
																				// and
																				// jfact

			System.out.println(
					"Is ontology (file name: " + inputOntologyFile + ") consistent? " + reasoner.isConsistent());

			ExplanationGenerator<OWLAxiom> explainInconsistency = new InconsistentOntologyExplanationGeneratorFactory(
					rf, 1000000000000000000L).createExplanationGenerator(ontology); // modified

			Set<Explanation<OWLAxiom>> explanations = explainInconsistency
					.getExplanations(df.getOWLSubClassOfAxiom(df.getOWLThing(), df.getOWLNothing()), 941); // set
																											// the
																											// limit
																											// of
																											// entailment

			System.out.println("Explanation of inconsistency (MI(K)): " + explanations);

			SizeOfK.sizeK(ontologyAxiomSet);
			ManageOWL.owlsetmanager(ontology, ontologyAxiomSet);

			System.out.println("                                   ");
			System.out.println("===============================================================");
			System.out.println("==============INCONSISTENCY MEASURES FOR ONTOLOGY==============");
			System.out.println("===============================================================");

			// Drastic_jfact.Drastic_measure(reasoner); // limit of
			// entailment
			System.out.println("Explanation of inconsistency (MI(K)): " + explanations);

			for (Explanation<OWLAxiom> explanation : explanations) { // explanation
																		// is M
																		// in
																		// MI(K),
																		// while
																		// explanations
																		// is
																		// MI(K).

				arrayOfExplanation = explanation.getAxioms(); // arrayOfExplanation
																// is M in MI(K)
				System.out.println("-----------------------------------------------------------------------------");
				System.out.println("MI(K) subset: " + arrayOfExplanation);
				arrayOfExplanationSet.add(arrayOfExplanation); // arrayOfExplanationSet
																// is MI(K)set
				System.out.println("-----------------------------------------------------------------------------");

				System.out.println("Axioms causing the inconsistency: ");
				for (OWLAxiom causingAxiom : arrayOfExplanation) {
					System.out.println(causingAxiom);
					MIKAxiomSet.add(causingAxiom);
					if ((causingAxiom.isBottomEntity() == true) || (causingAxiom.isTopEntity() == true)) {
						topBottom.add(causingAxiom);
					}

					inconsistentClass = (Set<OWLClass>) causingAxiom.getClassesInSignature();
					for (OWLClass theClass : inconsistentClass) {
						MIKClassSet.add(theClass);
					}
					inconsistentIndividual = (Set<OWLNamedIndividual>) causingAxiom.getIndividualsInSignature();
					for (OWLNamedIndividual theIndividual : inconsistentIndividual) {

						MIKIndividualSet.add(theIndividual);
					}
					inconsistentObjectProperty = (Set<OWLObjectProperty>) causingAxiom.getObjectPropertiesInSignature();
					for (OWLObjectProperty theObjectProperty : inconsistentObjectProperty) {
						MIKObjectPropertySet.add(theObjectProperty);
					}
				}

			}

			System.out.println("theClass: " + MIKClassSet);

			Drastic.Id_measure(reasoner); // limit of
			MI.Imi_measure(ontology, explanations);
			MIc.Imic_measure(arrayOfExplanation, explanations);
			Df.Idf_measure(ontologyAxiomSet, arrayOfExplanation, explanations);
			Problematic.Ip_measure(MIKAxiomSet);
			IR.Iir_measure(explanations, ontologyAxiomSet);
			MC.Imc_measure(explanations, ontologyAxiomSet);
			Nc.Inc_measure(ontologyAxiomSet);
			Mv.Imv_measure(MIKClassSet, MIKIndividualSet, MIKObjectPropertySet, ontologyAxiomSet);
			IDmcs.IDmcs_measure(MIKClassSet, MIKIndividualSet, MIKObjectPropertySet, ontologyAxiomSet);
			MIVd.MIVd_measure(MIKAxiomSet, ontologyAxiomSet);
			MIVsharp.MIVsharp_measure(arrayOfExplanationSet, ontologyAxiomSet);
			MIVc.MIVc_measure(arrayOfExplanationSet, ontologyAxiomSet);
			MIshapley.MIshapley_measure(df, ontologyAxiomSet);
			Scoring.Is_measure(explanations, ontologyAxiomSet);

			System.out.println("***************************************************************");

		} catch (NoSuchElementException e) {
			System.out.println("NoSuchElementException: " + e.getMessage());
		} catch (InconsistentOntologyException f) {
			System.out.println("InconsistentOntologyException: " + f.getMessage());
		} catch (FileNotFoundException g) {
			System.out.println("FileNotFoundException: " + g.getMessage());
		} catch (OWLOntologyCreationException g) {
			System.out.println("InconsistentOntologyException: " + g.getMessage());
		} catch (ExplanationGeneratorInterruptedException h) {
			System.out.println("ExplanationGeneratorInterruptedException: " + h.getMessage());
		} catch (ReasonerInterruptedException i) {
			System.out.println("ReasonerInterruptedException: " + i.getMessage());
		} catch (ExplanationException k) {
			System.out.println("ExplanationException: " + k.getMessage());
		} catch (TimeOutException l) {
			System.out.println("TimeOutException: " + l.getMessage());
		} catch (OutOfMemoryError m) {
			System.out.println("OutOfMemoryError: " + m.getMessage());
		}

	}

}
