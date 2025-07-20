package sn.groupeisi.matcompta.requests;

import lombok.Data;
import sn.groupeisi.matcompta.enums.RoleUser;

@Data
public class UtilisateurRequest {
    private String nomComplet;
    private String email;
    private String motDePasse;
    private String phone;
    private RoleUser role;

    public boolean champValide() {
        return nomComplet != null && !nomComplet.isBlank()
                && email != null && !email.isBlank()
                && motDePasse != null && !motDePasse.isBlank()
                && phone != null && !phone.isBlank()
                && estNumeroValide();
    }

    public boolean champValideSansMdp() {
        return nomComplet != null && !nomComplet.isBlank()
                && email != null && !email.isBlank()
                && phone != null && !phone.isBlank()
                && estNumeroValide();
    }


    public boolean estEmailValide() {
        return email != null && email.matches("^[A-Za-z0-9_.+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }

    public boolean estNumeroValide() {
        return phone != null && phone.matches("^[0-9]{9,15}$");
    }
}
