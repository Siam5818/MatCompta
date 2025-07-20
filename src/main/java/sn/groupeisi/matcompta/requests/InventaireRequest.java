package sn.groupeisi.matcompta.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InventaireRequest {
    @NotBlank(message = "Le libellé est obligatoire")
    private String libelle;

    private String departement;
    
    private LocalDate dateInventaire;
}
