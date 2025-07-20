package sn.groupeisi.matcompta.requests;

import lombok.Data;
import sn.groupeisi.matcompta.enums.EtatMateriel;

import java.time.LocalDate;

@Data
public class AffectationRequest {
    private Long materielId;
    private Long employeId;
    private LocalDate dateAffectation;
    private LocalDate dateRetour;
    private EtatMateriel etatMaterielAvant;
    private EtatMateriel etatMaterielApres;
    private String commentaire;

    public boolean champsValides() {
        return materielId != null && employeId != null && dateAffectation != null;
    }
}
