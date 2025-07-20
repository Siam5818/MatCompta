package sn.groupeisi.matcompta.requests;

import lombok.Data;

@Data
public class CategorieRequest {
    private String nom;
    private String description;

    public boolean champsValides() {
        return nom != null && !nom.trim().isEmpty();
    }
}
