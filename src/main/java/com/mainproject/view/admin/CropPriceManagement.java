package com.mainproject.view.admin;

import com.mainproject.controller.CropPriceController;
import com.mainproject.model.CropPrice;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class CropPriceManagement {
    private final Stage stage; private final AdminDashboard dashboard; private final CropPriceController controller=new CropPriceController();
    private BorderPane root; private TableView<CropPrice> table; private ComboBox<String> state,district,market; private Label status;
    public CropPriceManagement(Stage stage,AdminDashboard dashboard){this.stage=stage;this.dashboard=dashboard;}
    public void show(){root=new BorderPane();root.setStyle("-fx-background-color:"+AdminCommon.BG+";");root.setLeft(AdminCommon.sidebar(stage,dashboard,"Crop Price Management"));root.setTop(AdminCommon.topBar("Crop Price Management",()->AdminCommon.collapse(root),this::load));root.setCenter(content());stage.getScene().setRoot(root);loadStates();}
    private VBox content(){VBox box=new VBox(15);box.setPadding(new Insets(20));HBox filters=new HBox(10);state=new ComboBox<>();district=new ComboBox<>();market=new ComboBox<>();state.setPromptText("State");district.setPromptText("District");market.setPromptText("Market");Button search=new Button("Load Prices");search.setStyle("-fx-background-color:"+AdminCommon.GREEN+";-fx-text-fill:white;");state.setOnAction(e->{district.getItems().clear();market.getItems().clear();if(state.getValue()!=null)loadDistricts(state.getValue());});district.setOnAction(e->{market.getItems().clear();if(state.getValue()!=null&&district.getValue()!=null)loadMarkets(state.getValue(),district.getValue());});search.setOnAction(e->load());filters.getChildren().addAll(state,district,market,search);table=new TableView<>();table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);table.getColumns().addAll(col("State","state"),col("District","district"),col("Market","market"),col("Commodity","commodity"),col("Variety","variety"),col("Arrival Date","arrivalDate"),col("Min Price","minPrice"),col("Max Price","maxPrice"),col("Modal Price","modalPrice"));status=new Label("Loading...");VBox card=new VBox(10,filters,table,status);card.setPadding(new Insets(18));AdminCommon.card(card);box.getChildren().add(card);return box;}
    private <T> TableColumn<CropPrice,T> col(String title,String prop){TableColumn<CropPrice,T> c=new TableColumn<>(title);c.setCellValueFactory(new PropertyValueFactory<>(prop));return c;}
    private void loadStates(){new Thread(()->{List<String> x=controller.getStates();Platform.runLater(()->state.setItems(FXCollections.observableArrayList(x)));}).start();}
    private void loadDistricts(String s){new Thread(()->{List<String> x=controller.getDistricts(s);Platform.runLater(()->district.setItems(FXCollections.observableArrayList(x)));}).start();}
    private void loadMarkets(String s,String d){new Thread(()->{List<String> x=controller.getMarkets(s,d);Platform.runLater(()->market.setItems(FXCollections.observableArrayList(x)));}).start();}
    private void load(){String s=state.getValue(),d=district.getValue(),m=market.getValue();status.setText("Loading crop prices...");new Thread(()->{List<CropPrice> x=controller.getCropPrices(s,d,m);Platform.runLater(()->{table.setItems(FXCollections.observableArrayList(x));status.setText("Loaded "+x.size()+" records from Data.gov.in");});}).start();}
}
