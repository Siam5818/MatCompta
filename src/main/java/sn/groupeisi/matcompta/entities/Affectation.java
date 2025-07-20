package sn.groupeisi.matcompta.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import sn.groupeisi.matcompta.enums.EtatMateriel;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Affectation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Materiel materiel;

    @ManyToOne(optional = false)
    private Utilisateur employe;

    @Column(nullable = false)
    private LocalDate dateAffectation = LocalDate.now();

    @Column()
    private LocalDate dateRetour;

    @Enumerated(EnumType.STRING)
    private EtatMateriel etatMaterielAvant;

    @Enumerated(EnumType.STRING)
    private EtatMateriel etatMaterielApres;

    private String commentaire;
}
