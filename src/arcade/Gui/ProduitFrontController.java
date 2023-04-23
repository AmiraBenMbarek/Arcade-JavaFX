/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arcade.Gui;

import arcade.Entities.Categorie;
import arcade.Entities.Produit;
import arcade.Service.CategorieService;
import arcade.Service.ProduitService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import arcade.Service.WishlistService;

/**
 * FXML Controller class
 *
 * @author Amira
 */
public class ProduitFrontController implements Initializable{
    @FXML
    public FlowPane content;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ProduitService ps = new ProduitService();
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeFront.fxml"));

                //Parent root = loader.load();
                HomeFrontController cont = loader.getController();
/*                int c = cont.getCat("");
                System.out.println("qsdqsdqsd:" + c);
                System.out.println("qsdqsdqsd:");*/
            try {

            List<Produit> products = ps.afficher();

        
            for (Produit p : products) {

                FXMLLoader item = new FXMLLoader(getClass().getResource("ProduitCard.fxml"));
                try {
                    Parent root2 = item.load();
                    ProduitCardController itemController = item.getController();

                    itemController.setNomProduit(p.getNomProduit());
                    itemController.setPrix(p.getPrix() + "");
                    itemController.setId(p.getId() + "");
                    itemController.setImage("http://127.0.0.1:8000/eshop/produit/" + p.getImage());

                    WishlistService ws=new WishlistService();
                    if( ws.afficher().contains(p.getId())){
                    itemController.setWishlist("http://127.0.0.1:8000/eshop/" +"full.png");
                    }
                    
                    content.getChildren().add(root2);
                } catch (IOException ex) {
                    Logger.getLogger(ProduitFrontController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProduitFrontController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        } catch (Exception ex) {
                    Logger.getLogger(ProduitFrontController.class.getName()).log(Level.SEVERE, null, ex);
                }
    }    
}
