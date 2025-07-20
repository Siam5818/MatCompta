package sn.groupeisi.matcompta.requests;

import lombok.Data;
import sn.groupeisi.matcompta.enums.EtatMateriel;
import java.time.LocalDate;

@Data
public class MaterielRequest {
    private Long inventaireId;
    private String libelle;
    private String reference;
    private LocalDate dateAchat;
    private double valeurAchat;
    private int dureeAmortissement;
    private String emplacement;
    private EtatMateriel etat;
    private Long categorieId;

    public boolean champsValides() {
        return libelle != null && !libelle.isBlank()
                && reference != null && !reference.isBlank()
                && dateAchat != null
                && valeurAchat > 0
                && dureeAmortissement > 0
                && emplacement != null && !emplacement.isBlank()
                && etat != null
                && categorieId != null;
    }
}
