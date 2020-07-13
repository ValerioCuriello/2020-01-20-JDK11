package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import it.polito.tdp.artsmia.model.Arco;
import it.polito.tdp.artsmia.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola artisti connessi");
    	for(Arco a : this.model.getConnessi(boxRuolo.getValue())) {
    		txtResult.appendText(a + "\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
	    	txtResult.clear();
	    	txtResult.appendText("Calcola percorso");
	    	String ruolo = boxRuolo.getValue();
	    	txtResult.clear();
	    	try {
	    		int id = Integer.parseInt(txtArtista.getText());
	    		if(txtArtista.getText()!=null &&  Pattern.matches(".*[0-9].*",txtArtista.getText())) {
			    	if(this.model.getVerificaIdentita(id,ruolo )==1) {
			    		    this.model.getCamminolungo(id);			    		}
			    	else {
			    		txtResult.appendText("Non esiste un'artista con il ruolo e il numero selezionato");
			    	}
		    	}
	    		else {
	    			txtResult.appendText("Devi inserire un numero");
	    		}
	    	}
	    	catch(NumberFormatException n) {
	    		txtResult.appendText("Il faut utiliser un numero!");
	    	}
		    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Crea grafo");
    	String ruolo = boxRuolo.getValue();
    	System.out.println(ruolo);
    	this.model.creaGrafo(ruolo);
    	txtResult.appendText("Vertici: " + this.model.nVertici());
    	txtResult.appendText("Archi: " + this.model.nArchi());
    	System.out.println("Vertici: " + this.model.nVertici());
    	System.out.println("Archi: " + this.model.nArchi());
    	
    }

    public void setModel(Model model) {
    	this.model = model;
    	ObservableList<String> ruoli = FXCollections.observableList(this.model.getRuoli());
    	boxRuolo.setItems(ruoli);
    	boxRuolo.setValue(ruoli.get(0));
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
