package edu.mcw.scge.indexer.dao.delivery;

import edu.mcw.scge.indexer.model.DeliveryObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeliveryDao {
    public List<DeliveryObject> getIndexObjects(){
        List<DeliveryObject> objects= new ArrayList<>();
        DeliveryObject o1=new DeliveryObject();
        o1.setAnimalModels(new ArrayList<>(Arrays.asList("Ai9","mdx", "Ai9 x Pax7-nGFP")));
        o1.setOrganism("Mouse");
        o1.setEditor("SaCas9");
        o1.setEditTypes(new ArrayList<>(Arrays.asList("deletion", "non-homologous end joining (NHEJ)")));
        o1.setDeliveryVehicles(new ArrayList<>(Arrays.asList("adeno-associated virus (AAV)")));
        o1.setTargetTissue(new ArrayList<>(Arrays.asList("muscle")) );
        o1.setRouteOfAdministration(new ArrayList<>(Arrays.asList("IV injection"))) ;
        o1.setAllelesUsed(new ArrayList<>(Arrays.asList("5’ ACGAAGTTATATTAAGGGTT (CCGGAT)")));
        o1.setPi("Asokan");
        objects.add(o1);
        //==========================
        DeliveryObject o2=new DeliveryObject();
        o2.setAnimalModels(new ArrayList<>(Arrays.asList("tdTflox/flox9","Atoh1-GFP")));
        o2.setOrganism("Mouse");
        o2.setEditor("SpCas9");
        o2.setEditTypes(new ArrayList<>(Arrays.asList("deletion", "non-homologous end joining (NHEJ)")));
        o2.setDeliveryVehicles(new ArrayList<>(Arrays.asList("nanoparticle (bioreducible lipid nanoparticles)")));
        o2.setTargetTissue(new ArrayList<>(Arrays.asList("inner ear")));
        o2.setRouteOfAdministration(new ArrayList<>(Arrays.asList("Direct injection"))) ;
        o2.setAllelesUsed(new ArrayList<>(Arrays.asList("sgRNA276: AAAGAATTGATTTGATACCG", "sgRNA280: GTATGCTATACGAAGTTATT")));
        o2.setPi("Chen");
        objects.add(o2);
        //==========================
        DeliveryObject o3=new DeliveryObject();
        o3.setAnimalModels(new ArrayList<>(Arrays.asList("hSYN1-Cre","EGFP")));
        o3.setOrganism("Mouse");
        o3.setEditor("SaCas9");
        o3.setEditTypes(new ArrayList<>(Arrays.asList("deletion", "non-homologous end joining (NHEJ)")));
        o3.setDeliveryVehicles(new ArrayList<>(Arrays.asList("adeno-associated virus (AAV)")));
        o3.setTargetTissue(new ArrayList<>(Arrays.asList("brain")));
        o3.setRouteOfAdministration(new ArrayList<>(Arrays.asList("IV injection"))) ;
        o3.setPi("Deverman");
        objects.add(o3);
        //==========================
        DeliveryObject o4=new DeliveryObject();
        o4.setAnimalModels(new ArrayList<>(Arrays.asList("LSL-Tomato transgenic mouse")));
        o4.setOrganism("Mouse");
        o4.setEditor("SpCas9");
        o4.setEditTypes(new ArrayList<>(Arrays.asList("deletion", "non-homologous end joining (NHEJ)")));
        o4.setDeliveryVehicles(new ArrayList<>(Arrays.asList("adeno-associated virus (AAV)","nanoparticles")));
        o4.setTargetTissue(new ArrayList<>(Arrays.asList("lung")));
        o4.setRouteOfAdministration(new ArrayList<>(Arrays.asList("IV injection", "inhalation (nebularization"))) ;
        o4.setAllelesUsed(new ArrayList<>(Arrays.asList("5’ GTATGCTATACGAAGTTATT (AGG)")));
        o4.setPi("Gao");
        objects.add(o4);

        //==========================
        DeliveryObject o5=new DeliveryObject();
        o5.setAnimalModels(new ArrayList<>(Arrays.asList("Ai9")));
        o5.setOrganism("Mouse");
        o5.setEditor("SpCas9");
        o5.setEditTypes(new ArrayList<>(Arrays.asList("deletion", "non-homologous end joining (NHEJ)")));
        o5.setDeliveryVehicles(new ArrayList<>(Arrays.asList("engineered RBC-EV")));
        o5.setTargetTissue(new ArrayList<>(Arrays.asList("blood")));
        o5.setRouteOfAdministration(new ArrayList<>(Arrays.asList("IV or IP injection targeting bone marrow"))) ;
        o5.setAllelesUsed(new ArrayList<>(Arrays.asList("sgRNA298: AAGTAAAACCTCTACAAATG")));
        o5.setPi("Ghiran");
        objects.add(o5);

        //==========================
        DeliveryObject o6=new DeliveryObject();
        o6.setAnimalModels(new ArrayList<>(Arrays.asList("Ai14")));
        o6.setOrganism("Mouse");
        o6.setEditor("SpCas9");
        o6.setEditTypes(new ArrayList<>(Arrays.asList("deletion", "non-homologous end joining (NHEJ)")));
        o6.setDeliveryVehicles(new ArrayList<>(Arrays.asList("ribonucleic protein (RNP)","Nanoparticles/capsules")));
        o6.setTargetTissue(new ArrayList<>(Arrays.asList("brain (neurons)")));
        o6.setRouteOfAdministration(new ArrayList<>(Arrays.asList("intracerebrial or IV with glucose monitoring"))) ;
        o6.setAllelesUsed(new ArrayList<>(Arrays.asList("sgRNA298: AAGTAAAACCTCTACAAATG")));
        o6.setPi("Gong");
        objects.add(o6);
        //==========================
        DeliveryObject o7=new DeliveryObject();
        o7.setAnimalModels(new ArrayList<>(Arrays.asList("ROSAmT/mG mouse63")));
        o7.setOrganism("Mouse");
        o7.setEditor("Cas12a/MAD7");
        o7.setEditTypes(new ArrayList<>(Arrays.asList("deletion", "non-homologous end joining (NHEJ)")));
        o7.setDeliveryVehicles(new ArrayList<>(Arrays.asList("ribonucleic protein (RNP) amphiphilic peptides")));
        o7.setTargetTissue(new ArrayList<>(Arrays.asList("lung")));
        o7.setRouteOfAdministration(new ArrayList<>(Arrays.asList("intranasal infusion"))) ;
        o7.setAllelesUsed(new ArrayList<>(Arrays.asList("5’ (TTTG) GCAAAGAATTGATTTGATACCGC")));
        o7.setPi("McCray");
        objects.add(o7);

        //==========================
        DeliveryObject o8=new DeliveryObject();
        o8.setAnimalModels(new ArrayList<>(Arrays.asList("β-globin/green fluorescent protein (GFP) mouse")));
        o8.setOrganism("Mouse");
        o8.setEditor("peptide nucleic acid (PNA)");
        o8.setEditTypes(new ArrayList<>(Arrays.asList("gene replacement via homologous recombination HR)")));
        o8.setDeliveryVehicles(new ArrayList<>(Arrays.asList("peptide nucleic acid","nucleic protein")));
        o8.setTargetTissue(new ArrayList<>(Arrays.asList("blood", "lung")));
        o8.setRouteOfAdministration(new ArrayList<>(Arrays.asList("IV (blood)", "inhalation (lung)"))) ;
        o8.setAllelesUsed(new ArrayList<>(Arrays.asList("PNA homopurine target site: GGAGAGGGAGAGGGAGA ","PNA Edit Site: ACTCACGGGGTGCAGTGCTTCGG")));
        o8.setPi("Saltzman");
        objects.add(o8);

        //==========================
        DeliveryObject o9=new DeliveryObject();
        o9.setAnimalModels(new ArrayList<>(Arrays.asList("TLR2.0/Cas9 mouse")));
        o9.setOrganism("Mouse");
        o9.setEditor("SpCas9");
        o9.setEditTypes(new ArrayList<>(Arrays.asList("deletion", "non-homologous end joining (NHEJ)")));
        o9.setDeliveryVehicles(new ArrayList<>(Arrays.asList("ribonucleic protein RNP and adeno-associated virus AAV","engineered modified nucleic acids")));
        o9.setTargetTissue(new ArrayList<>(Arrays.asList("brain", "muscle", "kidney")));
        o9.setRouteOfAdministration(new ArrayList<>(Arrays.asList("brain (ICV and/or intrathecal) with glucose monitoring", "muscle, kidney  (IV or SC)"))) ;
        o9.setAllelesUsed(new ArrayList<>(Arrays.asList("sgRosa26-2: CGCCCATCTTCTAGAAAGAC")));
        o9.setPi("Sontheimer");
        objects.add(o9);

        //==========================
        DeliveryObject o10=new DeliveryObject();
       // o10.setAnimalModels(new ArrayList<>(Arrays.asList("TLR2.0/Cas9 mouse")));
     //   o10.setOrganism("");
        o10.setEditor("Cas9");
        o10.setDeliveryVehicles(new ArrayList<>(Arrays.asList("ribonucleic protein RNP","MRI-guided delivery")));
        o10.setTargetTissue(new ArrayList<>(Arrays.asList("brain")));
        o10.setRouteOfAdministration(new ArrayList<>(Arrays.asList("intracerebral infusion"))) ;
      //  o10.setAllelesUsed(new ArrayList<>(Arrays.asList("sgRosa26-2: CGCCCATCTTCTAGAAAGAC")));
        o10.setPi("Bankiewicz");
        objects.add(o10);

        //==========================
        DeliveryObject o11=new DeliveryObject();
        o11.setEditor("Cas9");
        o11.setDeliveryVehicles(new ArrayList<>(Arrays.asList("engineered velcro AAV vectors")));
        o11.setTargetTissue(new ArrayList<>(Arrays.asList("endothelium (multiple tissue types)")));
        o11.setRouteOfAdministration(new ArrayList<>(Arrays.asList("IV injection"))) ;
        //  o10.setAllelesUsed(new ArrayList<>(Arrays.asList("sgRosa26-2: CGCCCATCTTCTAGAAAGAC")));
        o11.setPi("Bao");
        objects.add(o11);

        //==========================
        DeliveryObject o12=new DeliveryObject();
        o12.setEditor("Cas9");
        o12.setDeliveryVehicles(new ArrayList<>(Arrays.asList("VLP-RNP")));
        o12.setTargetTissue(new ArrayList<>(Arrays.asList("hematopoietic stem and progenitor cells (HSPC)")));
      //  o12.setRouteOfAdministration(new ArrayList<>(Arrays.asList("IV injection"))) ;
        //  o10.setAllelesUsed(new ArrayList<>(Arrays.asList("sgRosa26-2: CGCCCATCTTCTAGAAAGAC")));
        o12.setPi("Chaikof");
        objects.add(o12);

        //==========================
        DeliveryObject o13=new DeliveryObject();

        o13.setEditor("Cas9");
        o13.setDeliveryVehicles(new ArrayList<>(Arrays.asList("adenovirus")));
        o13.setTargetTissue(new ArrayList<>(Arrays.asList("endothelium (vascular)")));
        o13.setRouteOfAdministration(new ArrayList<>(Arrays.asList("IV injection"))) ;

        o13.setPi("Curiel");
        objects.add(o13);

        //==========================
        DeliveryObject o14=new DeliveryObject();

        o14.setEditor("Cas9");
        o14.setDeliveryVehicles(new ArrayList<>(Arrays.asList("nucleic protein","zinc finger nuclease")));
        o14.setTargetTissue(new ArrayList<>(Arrays.asList("hematopoietic stem and progenitor cells (HSPC)")));

        o14.setPi("Dahlman");
        objects.add(o14);
        //==========================
        DeliveryObject o15=new DeliveryObject();

        o15.setEditor("Cas9");
        o15.setDeliveryVehicles(new ArrayList<>(Arrays.asList("ribonucleic protein")));
        o15.setTargetTissue(new ArrayList<>(Arrays.asList("GI tract")));

        o15.setPi("Lam");
        objects.add(o15);
        //==========================
        DeliveryObject o16=new DeliveryObject();

        o16.setEditor("Cas9");
        o16.setDeliveryVehicles(new ArrayList<>(Arrays.asList("adeno-associated virus","focused ultrasound")));
        o16.setTargetTissue(new ArrayList<>(Arrays.asList("brain")));
        o16.setRouteOfAdministration(new ArrayList<>(Arrays.asList("intracranial injection"))) ;

        o16.setPi("Leong");
        objects.add(o16);
        //==========================
        DeliveryObject o17=new DeliveryObject();

        o17.setEditor("Cas9");
        o17.setDeliveryVehicles(new ArrayList<>(Arrays.asList("lentiviral-based nanoscale protein delivery (nanoPOD)")));

        o17.setTargetTissue(new ArrayList<>(Arrays.asList("lung","GI tract")));

        o17.setPi("Tilton");
        objects.add(o17);
        //==========================
        DeliveryObject o18=new DeliveryObject();

        o18.setEditor("Cas9");
        o18.setDeliveryVehicles(new ArrayList<>(Arrays.asList("ribonucleic protein","antibody targeted complex to OKT3")));

        o18.setTargetTissue(new ArrayList<>(Arrays.asList("T cells")));

        o18.setPi("Wilson");
        objects.add(o18);
        //==========================
        DeliveryObject o19=new DeliveryObject();

        o19.setEditor("Cas9");
        o19.setDeliveryVehicles(new ArrayList<>(Arrays.asList("lentiviral-like particles","antibody targeting to CD7")));

        o19.setTargetTissue(new ArrayList<>(Arrays.asList("T cells")));

        o19.setPi("Yi");
        objects.add(o19);
        //==========================
        DeliveryObject o20=new DeliveryObject();

        o20.setEditor("Cas9");
        o20.setDeliveryVehicles(new ArrayList<>(Arrays.asList("nanoparticle")));
        o20.setTargetTissue(new ArrayList<>(Arrays.asList("central nervous system")));
        o20.setRouteOfAdministration(new ArrayList<>(Arrays.asList("intracranial injection","IV injection"))) ;

        o20.setPi("Zhou");
        objects.add(o20);

        return objects;
    }
}
