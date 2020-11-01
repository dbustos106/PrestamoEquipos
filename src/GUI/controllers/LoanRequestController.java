package GUI.controllers;

import Control.RealizarPrestamo;
import Control.RealizarDevolucion;
import Entidad.Computador;
import Entidad.Programa;
import Entidad.Usuario;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


/**
 * FXML Controller class
 *
 * @author ion
 */
public class LoanRequestController implements Initializable {

    //quizas metodo estatico
    RealizarDevolucion RD = new RealizarDevolucion();
    RealizarPrestamo RP = new RealizarPrestamo();
    SessionHolder sessionHolder = SessionHolder.getInstance();
    
   

    @FXML
    private TextField searchProgramTF;
    @FXML
    private Button searchComputersBtn;
    @FXML
    private TableView<Programa> availableProgramsTable;
    @FXML
    private TableView<Programa> selectedProgramsTable;
    @FXML
    private TableView<ComputerRow> availableComputersTable;
    @FXML
    private Button askLoanBtn;
    @FXML
    private Button rightArrowBtn;
    @FXML
    private Button leftArrowBtn;
    @FXML
    private Button backHomeBtn;
    @FXML
    private Button userInfoBtn;

    private ObservableList<Programa> programList = FXCollections.observableArrayList();
    private ObservableList<Programa> selectedProgramList = FXCollections.observableArrayList();
    private ObservableList<ComputerRow> computerList = FXCollections.observableArrayList();

    private int idComputerSelected;
    
    void changeScene(ActionEvent event, String fxml) throws IOException{
        Parent newParent = FXMLLoader.load(getClass().getResource(fxml));
        Scene newScene = new Scene(newParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(newScene);
        window.show();
    }
    
    //Buscador de programas
    FilteredList<Programa> filteredPrograms = new FilteredList<>(programList, b -> true);

    public void searchProgram() {

        searchProgramTF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredPrograms.setPredicate(programa -> {
                String lowerCaseFilter = newValue.toLowerCase();
                if (programa.getNombre().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });

            SortedList<Programa> sortedData = new SortedList<>(filteredPrograms);
            sortedData.comparatorProperty().bind(availableProgramsTable.comparatorProperty());
            availableProgramsTable.setItems(sortedData);
        });
    }

    @FXML
    void rightArrowBtnAction(ActionEvent event) {
        Programa programSelected = availableProgramsTable.getSelectionModel().getSelectedItem();
        addProgram(programSelected);
    }

    @FXML
    void leftArrowBtnAction(ActionEvent event) {
        Programa programSelected = selectedProgramsTable.getSelectionModel().getSelectedItem();
        unselectProgram(programSelected);
    }
    
    @FXML
    private Label warningText;
    
    @FXML
    private AnchorPane computerTableSection;

    @FXML
    void AskLoanBtnAction(ActionEvent event) {

        Usuario user = sessionHolder.getUser();
        Computador selectedComputer = new Computador();
        selectedComputer.setId(idComputerSelected);
        ArrayList<Programa> programList = new ArrayList<>();
        
        selectedProgramList.forEach(p -> {
            programList.add(p);
        });
        
        if(user.getId()==null){
            warningText.setText("Error relacionado a la sesion");
            System.out.println("Error relacionado a la sesion.");
        }
        
        if (idComputerSelected != 0) {
            try {
                LoanDataHolder holder = LoanDataHolder.getInstance();
                
                holder.setComputer(selectedComputer);
                holder.setUser(user);
                holder.setPrograms(programList);
                holder.setRow(availableComputersTable.getSelectionModel().getSelectedItem());

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/GUI/views/confirmRequest.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 437 , 209);
                Stage stagePop = new Stage();
                stagePop.setTitle("Confirmar prestamo");
                stagePop.setScene(scene);
                stagePop.showAndWait();
                
                //Vuelve al home si se hizo un prestamo.
                
                if (RD.isInactivity(user)) {
                    changeScene(event,"/GUI/views/studentHome.fxml");
                }

            } catch (IOException e) {
                System.err.println(String.format("Error: %s", e.getMessage()));
            }
        } else {
            System.out.println("Olvidaste seleccionar un computador");
            warningText.setText("Olvidaste seleccionar un computador");
        }

    }

    @FXML
    void backHomeBtn(ActionEvent event) throws IOException {
        Parent newParent = FXMLLoader.load(getClass().getResource("/GUI/views/studentHome.fxml"));
        Scene newScene = new Scene(newParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(newScene);
        window.show();
    }


    
    void insertComputers() {

        /*Solamente llena tabla de computadores.
         *crea columnas y selecciona el atributo de Programa
        
         */
        TableColumn computerIdCol = new TableColumn("Id");
        computerIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn nombreEdificioCol = new TableColumn("Nombre Edificio");
        nombreEdificioCol.setCellValueFactory(new PropertyValueFactory("nombreEdificio"));
        TableColumn IdEdificioCol = new TableColumn("Id Edificio");
        IdEdificioCol.setCellValueFactory(new PropertyValueFactory("idEdificio"));
        TableColumn nombreSalaCol = new TableColumn("Codigo Sala");
        nombreSalaCol.setCellValueFactory(new PropertyValueFactory("nombreSala"));
        

        //asigna la lista de items y las columnas a la TableView
        availableComputersTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        availableComputersTable.getColumns().addAll(computerIdCol, nombreEdificioCol, IdEdificioCol, nombreSalaCol);
        availableComputersTable.setItems(computerList);
        
    }

    @FXML
    void searchComputersBtnAction(ActionEvent event) {

        computerTableSection.setVisible(true);
        computerList.clear();
        availableComputersTable.refresh();
        ArrayList<Programa> selectedProgramArr = new ArrayList<>();
        selectedProgramList.forEach(p -> {
            selectedProgramArr.add(p);
        });

        ArrayList<String[]> availableComputersInfo = RP.getInfoComputers(selectedProgramArr);

        availableComputersInfo.forEach(computer -> {
            System.out.println(computer[0] + "" + computer[1] + "" + computer[2] + "" + computer[3]);
            ComputerRow temp = new ComputerRow(computer[0], computer[1], computer[2], computer[3]);
            if (!computerList.contains(temp)) {
                computerList.add(temp);
            }
        });

        availableComputersTable.refresh();

        /* 
         * Codigo para actualizar la tabla de computadores instanteneamente tras seleccionar los programas.
        selectedProgramList.addListener(new ListChangeListener<Programa>() {
            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends Programa> pChange) {

                ComputerRow test = new ComputerRow("1", "texto1", "textto2", "texto3");
                computerList.add(test);
                availableComputersTable.setItems(computerList);
                availableComputersTable.refresh();   
            }
        }); */
    }

    void addProgram(Programa p) {
        if (!selectedProgramList.contains(p)) {
            selectedProgramList.add(p);
            selectedProgramsTable.refresh();
            initActions();
        }

    }

    void unselectProgram(Programa p) {
        if (selectedProgramList.contains(p)) {
            selectedProgramList.remove(p);
            initActions();
        }
    }

    @FXML
    void userInfoBtnAction(ActionEvent event) {
    }

    void insertAvailablePrograms() {


        //crea columnas y selecciona el atributo de Programa
        TableColumn programIdCol = new TableColumn("Id");
        programIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn programNameCol = new TableColumn("Programa");
        programNameCol.setCellValueFactory(new PropertyValueFactory("nombre"));
        TableColumn programVersionCol = new TableColumn("Version");
        programVersionCol.setCellValueFactory(new PropertyValueFactory("version"));

        //asigna la lista de items y las columnas a la TableView
        availableProgramsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        availableProgramsTable.setItems(programList);
        availableProgramsTable.getColumns().addAll(programIdCol, programNameCol, programVersionCol);

        RP.getAllPrograms().forEach(p -> {
            programList.add(p);
        });

    }

    void insertSelectedPrograms() {

        //crea columnas y selecciona el atributo de Programa
        TableColumn programIdCol = new TableColumn("Id");
        programIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn programNameCol = new TableColumn("Programa");
        programNameCol.setCellValueFactory(new PropertyValueFactory("nombre"));
        TableColumn programVersionCol = new TableColumn("Version");
        programVersionCol.setCellValueFactory(new PropertyValueFactory("version"));

        selectedProgramsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        selectedProgramsTable.setItems(selectedProgramList);
        selectedProgramsTable.getColumns().addAll(programIdCol, programNameCol, programVersionCol);
    }

    void initActions() {

        /*
         *Cumplen la misma función que las flechas, se mantienen por usabilidad.
         */
        availableProgramsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    System.out.println(availableProgramsTable.getSelectionModel().getSelectedItem());
                    Programa programSelected = availableProgramsTable.getSelectionModel().getSelectedItem();
                    addProgram(programSelected);
                }
            }
        });

        selectedProgramsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    System.out.println(availableProgramsTable.getSelectionModel().getSelectedItem());
                    Programa programSelected = availableProgramsTable.getSelectionModel().getSelectedItem();
                    unselectProgram(programSelected);
                }
            }
        });

        availableComputersTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 1) {
                    idComputerSelected = Integer.parseInt(availableComputersTable.getSelectionModel().getSelectedItem().getId());
                    System.out.println("Id del computador seleccionado " + availableComputersTable.getSelectionModel().getSelectedItem().getId());
                }
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        insertSelectedPrograms();
        initActions();
        insertComputers();

        new Thread(new Runnable() {
            @Override
            public void run() {
                insertAvailablePrograms();
                searchProgram();
                   /* Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            
                        }
                    });*/
                }
            }).start();  

    }
}
