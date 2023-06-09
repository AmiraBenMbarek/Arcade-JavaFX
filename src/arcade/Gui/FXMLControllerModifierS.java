/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arcade.Gui;

import arcade.Entities.Evenement;
import arcade.Entities.Sponsor;
import arcade.Service.ServiceEvenement;
import arcade.Service.ServiceSponsor;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author zeine
 */
public class FXMLControllerModifierS {
    
      
    @FXML
    private TextField TFNomSp;
    @FXML
    private TextField TFNumTel;
    @FXML
    private TextField TFemail;
    @FXML
    private TextArea TAadresse;
    @FXML
    private TextField TFLogo;
    @FXML
    private TextField TFdomaine;
    @FXML
    private TextField TFmontant;
    @FXML
    private SplitMenuButton SPMeventFK;
    
    @FXML
    private Button chooseLogo;
    
    private Sponsor sponsor;
    
    @FXML
        private void chooseLogo(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir un logo");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );
            File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
            if (file != null) {
                TFLogo.setText(file.getAbsolutePath());
            }
        }
    
    
    public void setSponsor(Sponsor sponsor) {
      
        this.sponsor = sponsor;
        TFNomSp.setText(sponsor.getNomSponsor());
        TFNumTel.setText(String.valueOf(sponsor.getNumTelSponsor()));
        TFemail.setText(sponsor.getEmailSponsor());
        TAadresse.setText(sponsor.getAdresseSponsor());
        TFLogo.setText(String.valueOf(sponsor.getLogoSponsor()));
        TFdomaine.setText(sponsor.getDomaineSponsor());
        TFmontant.setText(String.valueOf(sponsor.getMontantSponsoring()));
        SPMeventFK.setText(String.valueOf(sponsor.getIDEventsFK()));
}

    @FXML
    private void modifier(ActionEvent event) throws IOException {
        
          if (TFNomSp.getText().isEmpty() || TFNumTel.getText().isEmpty() || TFemail.getText().isEmpty() 
                || TAadresse.getText().isEmpty() || TFLogo.getText().isEmpty() || TFdomaine.getText().isEmpty()
                || TFmontant.getText().isEmpty()|| SPMeventFK.getText().equals("Select Event")) 
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("ce champs ne peut pas etre vide");
                alert.showAndWait();
                return;
               }
               
                  
                String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
                if (!TFemail.getText().matches(emailRegex)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("L'adresse e-mail n'est pas valide");
                    alert.showAndWait();
                    return;
                }

                
                   
            int NumTelSponsor;
            try {
                NumTelSponsor = Integer.parseInt(TFNumTel.getText());
                if (NumTelSponsor <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Numero de telephone doit etre un entier positive");
                alert.showAndWait();
                return;
            }

    
            float MontantSponsoring;
            try {
                MontantSponsoring = Float.parseFloat(TFmontant.getText());
                if (MontantSponsoring <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Montant sponsoring doit etre un entier positive");
                alert.showAndWait();
                return;
            }


                Sponsor s = new Sponsor();
                s.setNomSponsor(TFNomSp.getText());
                s.setNumTelSponsor(Integer.valueOf(TFNumTel.getText()));
                String logoPath = TFLogo.getText();
                    if (logoPath == null || logoPath.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText(null);
                        alert.setContentText("Veuillez choisir un logo");
                        alert.showAndWait();
                        return;
                    }
                s.setLogoSponsor(logoPath);
                s.setAdresseSponsor(TAadresse.getText());
                s.setEmailSponsor(TFemail.getText());
                s.setMontantSponsoring(Float.valueOf(TFmontant.getText()));
                s.setDomaineSponsor(TFdomaine.getText());
                s.setIDEventsFK(Integer.valueOf(sponsor.getIDEventsFK()));


                ServiceSponsor sp = new ServiceSponsor();
                sp.modifier(sponsor.getId(), s);


                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Succés");
                    alert.setHeaderText(null);
                    alert.setContentText("Sponsor modifié");
                    alert.showAndWait();

    
     }
    
   private FXMLSponsorController SponsorController;

    public void setFXMLSponsorController(FXMLSponsorController SponsorController) {
        this.SponsorController = SponsorController;
    }
    @FXML
    private Button backButtonEdit;



   
        
    public void initialize() {
    
            
            chooseLogo.setOnAction(this::chooseLogo);
             ServiceEvenement serviceEvenement = new ServiceEvenement();
            List<Evenement> evenements = serviceEvenement.afficher();
            for (Evenement evenement : evenements) {
                MenuItem menuItem = new MenuItem(evenement.getNomEvent());
                menuItem.setId(String.valueOf(evenement.getId()));
                menuItem.setOnAction(event -> {
                    SPMeventFK.setText(evenement.getNomEvent());
                    SPMeventFK.setId(String.valueOf(evenement.getId()));
                });
                SPMeventFK.getItems().add(menuItem);
            }
            
}

    @FXML
    public void cancelButton(ActionEvent event)
    {
        try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
                        Parent root = loader.load();
                        HomeController controller = loader.getController();
                        controller.changePage("Sponsor");

                        TFNomSp.getScene().setRoot(root);

                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
    }
    

}
  
    

