package com.mainproject.view.admin;

import com.mainproject.dao.AdminFirestoreDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

public class ContentManagement {
    private final Stage stage; private final AdminDashboard dashboard; private final AdminFirestoreDAO dao=new AdminFirestoreDAO(); private BorderPane root; private TableView<Item> table; private ObservableList<Item> data=FXCollections.observableArrayList(); private TextField search;
    public ContentManagement(Stage stage,AdminDashboard dashboard){this.stage=stage;this.dashboard=dashboard;}
    public void show(){root=new BorderPane();root.setStyle("-fx-background-color:"+AdminCommon.BG+";");root.setLeft(AdminCommon.sidebar(stage,dashboard,"Content Management"));root.setTop(AdminCommon.topBar("Content Management",()->AdminCommon.collapse(root),this::load));root.setCenter(content());stage.getScene().setRoot(root);load();}
    private VBox content(){VBox box=new VBox(12);box.setPadding(new Insets(20));HBox tools=new HBox(8);search=new TextField();search.setPromptText("Search title/type...");Button add=new Button("+ Add Content");add.setStyle("-fx-background-color:"+AdminCommon.GREEN+";-fx-text-fill:white;");Button find=new Button("Search");find.setOnAction(e->filter());add.setOnAction(e->add());tools.getChildren().addAll(search,find,add);table=new TableView<>();table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);table.getColumns().addAll(col("Title","title"),col("Type","type"),col("Status","status"),col("Updated","updated"));TableColumn<Item,Void> act=new TableColumn<>("Action");act.setCellFactory(c->new TableCell<>(){Button edit=new Button("Edit"),del=new Button("Delete");HBox b=new HBox(5,edit,del);{edit.setOnAction(e->edit(getTableRow().getItem()));del.setOnAction(e->remove(getTableRow().getItem()));}protected void updateItem(Void x,boolean empty){super.updateItem(x,empty);setGraphic(empty?null:b);}});table.getColumns().add(act);VBox card=new VBox(10,tools,table);card.setPadding(new Insets(18));AdminCommon.card(card);box.getChildren().add(card);return box;}
    private TableColumn<Item,String> col(String h,String p){TableColumn<Item,String> c=new TableColumn<>(h);c.setCellValueFactory(new PropertyValueFactory<>(p));return c;}
    private void load(){data.clear();for(Map<String,Object> r:dao.getAll("adminContent"))data.add(Item.of(r));table.setItems(data);}
    private void filter(){String q=search.getText().trim().toLowerCase();ObservableList<Item> x=FXCollections.observableArrayList();for(Item i:data)if(q.isEmpty()||i.title.toLowerCase().contains(q)||i.type.toLowerCase().contains(q))x.add(i);table.setItems(x);}
    private void add(){Dialog<ButtonType>d=new Dialog<>();d.setTitle("Add Content");d.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);TextField t=new TextField(),type=new TextField("Page");GridPane g=new GridPane();g.setHgap(8);g.setVgap(8);g.addRow(0,new Label("Title"),t);g.addRow(1,new Label("Type"),type);d.getDialogPane().setContent(g);if(d.showAndWait().orElse(ButtonType.CANCEL)==ButtonType.OK&&!t.getText().isBlank()){Map<String,Object>m=new HashMap<>();m.put("title",t.getText().trim());m.put("type",type.getText().trim());m.put("status","Published");m.put("updated",new Date().toString());dao.create("adminContent",m);load();}}
    private void edit(Item i){if(i==null)return;TextInputDialog d=new TextInputDialog(i.title);d.setTitle("Edit Content");d.setHeaderText("Update title");d.showAndWait().ifPresent(v->{if(!v.isBlank()){Map<String,Object>m=new HashMap<>();m.put("title",v.trim());m.put("updated",new Date().toString());dao.update("adminContent",i.id,m);load();}});}
    private void remove(Item i){if(i!=null&&AdminCommon.confirm("Delete Content","Delete "+i.title+"?")){dao.delete("adminContent",i.id);load();}}
    static class Item{String id,title,type,status,updated;static Item of(Map<String,Object>m){Item i=new Item();i.id=AdminCommon.s(m.get("_id"));i.title=AdminCommon.s(m.get("title"));i.type=AdminCommon.s(m.get("type"));i.status=AdminCommon.s(m.get("status"));i.updated=AdminCommon.s(m.get("updated"));return i;}public String getTitle(){return title;}public String getType(){return type;}public String getStatus(){return status;}public String getUpdated(){return updated;}}
}
