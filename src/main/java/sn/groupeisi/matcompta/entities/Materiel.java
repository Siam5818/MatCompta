package sn.groupeisi.matcompta.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;
import sn.groupeisi.matcompta.enums.EtatMateriel;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Materiel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String libelle;

    @Column(nullable = false)
    private String reference;

    @Column(nullable = false)
    private LocalDate dateAchat;

    @Min(value = 0, message = "La valeur d'achat ne peut pas être négative")
    private double valeurAchat;

    @Min(value = 1, message = "La durée d'amortissement doit être d'au moins 1 mois")
    private int dureeAmortissement;

    @Column
    private String emplacement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EtatMateriel etat;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @ManyToOne
    @JoinColumn(name = "inventaire_id")
    private InventaireComptable inventaireComptable;

    public LocalDate getDateFinAmortissement() {
        return dateAchat.plusMonths(dureeAmortissement);
    }
}
