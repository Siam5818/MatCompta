package sn.groupeisi.matcompta.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InventaireComptable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String libelle;

    private String departement;

    @Column(nullable = false)
    private LocalDate dateInventaire;

    @OneToMany(mappedBy = "inventaireComptable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Materiel> materiels;
}
