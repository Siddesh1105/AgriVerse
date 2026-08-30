package com.mainproject.view.admin;

import com.mainproject.controller.ReviewController;
import com.mainproject.dao.AdminFirestoreDAO;
import com.mainproject.model.Review;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

/** Admin moderation screen for the new Buyer/Farmer review system and legacy admin feedback. */
public class FeedbackReviews {
    private final Stage stage; private final AdminDashboard dashboard;
    private final AdminFirestoreDAO legacyDAO = new AdminFirestoreDAO();
    private final ReviewController reviewController = new ReviewController();
    private BorderPane root; private TableView<Row> table;
    private final ObservableList<Row> data = FXCollections.observableArrayList();
    private TextField search; private String type="All";

    public FeedbackReviews(Stage s, AdminDashboard d){stage=s;dashboard=d;}
    public void show(){root=new BorderPane();root.setStyle("-fx-background-color:"+AdminCommon.BG+";");root.setLeft(AdminCommon.sidebar(stage,dashboard,"Feedback & Reviews"));root.setTop(AdminCommon.topBar("Feedback & Reviews",()->AdminCommon.collapse(root),this::load));root.setCenter(content());stage.getScene().setRoot(root);load();}

    private VBox content(){
        VBox box=new VBox(12);box.setPadding(new Insets(20));
        HBox tabs=new HBox(6);
        for(String t:new String[]{"All","Product","Rental","Legacy"}){Button b=new Button(t);b.setOnAction(e->{type=t;filter();});tabs.getChildren().add(b);}
        search=new TextField();search.setPromptText("Search reviews or feedback...");search.textProperty().addListener((o,a,n)->filter());
        table=new TableView<>();table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(col("Reviewer","user"),col("Reviewing","reviewee"),col("Type","type"),col("Rating","rating"),col("Comment","comment"),col("Status","status"));
        TableColumn<Row,Void> action=new TableColumn<>("Action");
        action.setCellFactory(c->new TableCell<>(){final Button b=new Button("Moderate");{b.setOnAction(e->moderate(getTableRow().getItem()));}protected void updateItem(Void x,boolean empty){super.updateItem(x,empty);setGraphic(empty?null:b);}});
        table.getColumns().add(action);
        VBox card=new VBox(10,tabs,search,table);card.setPadding(new Insets(18));AdminCommon.card(card);box.getChildren().add(card);return box;
    }
    private <T> TableColumn<Row,T> col(String h,String p){TableColumn<Row,T> c=new TableColumn<>(h);c.setCellValueFactory(new PropertyValueFactory<>(p));return c;}

    private void load(){
        data.clear();
        for(Review r:reviewController.getAllReviews()) data.add(Row.fromReview(r));
        // Preserve previously existing admin feedback records.
        for(Map<String,Object> m:legacyDAO.getAll("adminFeedback")) data.add(Row.fromLegacy(m));
        filter();
    }
    private void filter(){
        if(table==null)return;String q=search==null?"":search.getText().trim().toLowerCase();ObservableList<Row> out=FXCollections.observableArrayList();
        for(Row r:data){boolean tab="All".equals(type)||("Product".equals(type)&&r.type.toLowerCase().contains("product"))||("Rental".equals(type)&&r.type.toLowerCase().contains("rental"))||("Legacy".equals(type)&&"adminFeedback".equals(r.collection));
            if(tab&&(q.isEmpty()||r.user.toLowerCase().contains(q)||r.reviewee.toLowerCase().contains(q)||r.comment.toLowerCase().contains(q)))out.add(r);}
        table.setItems(out);
    }
    private void moderate(Row r){
        if(r==null)return;ChoiceDialog<String>d=new ChoiceDialog<>(r.status,"ACTIVE","HIDDEN","Approved","Pending","Under Review","Rejected");d.setTitle("Moderate Feedback");d.setHeaderText(r.user+" → "+r.reviewee);
        d.showAndWait().ifPresent(status->{boolean ok;
            if("reviews".equals(r.collection)) ok=reviewController.updateStatus(r.id,status.toUpperCase());
            else {Map<String,Object> m=new HashMap<>();m.put("status",status);ok=legacyDAO.update("adminFeedback",r.id,m);}
            if(ok){legacyDAO.audit("Super Admin","Moderated Feedback","Feedback & Reviews",r.id);load();}
        });
    }

    public static class Row {
        String id,user,reviewee,type,rating,comment,status,collection;
        static Row fromReview(Review r){Row x=new Row();x.id=r.getReviewId();x.user=s(r.getReviewerName(),r.getReviewerEmail());x.reviewee=s(r.getRevieweeName(),r.getRevieweeEmail());x.type=r.getReviewType();x.rating=r.getRating()+" / 5";x.comment=n(r.getComment());x.status=n(r.getStatus());x.collection="reviews";return x;}
        static Row fromLegacy(Map<String,Object> m){Row x=new Row();x.id=AdminCommon.s(m.get("_id"));x.user=AdminCommon.s(m.get("user"));x.reviewee="-";x.type=AdminCommon.s(m.get("type"));x.rating=AdminCommon.s(m.get("rating"));x.comment=AdminCommon.s(m.get("comment"));x.status=AdminCommon.s(m.get("status"));if(x.status.isBlank())x.status="Pending";x.collection="adminFeedback";return x;}
        static String n(String v){return v==null?"":v;}static String s(String a,String b){return !n(a).isBlank()?a:n(b);}
        public String getUser(){return user;} public String getReviewee(){return reviewee;} public String getType(){return type;} public String getRating(){return rating;} public String getComment(){return comment;} public String getStatus(){return status;}
    }
}
