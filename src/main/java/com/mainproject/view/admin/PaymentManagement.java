package com.mainproject.view.admin;

import com.mainproject.dao.EquipmentRentalDAO;
import com.mainproject.dao.OrderDAO;
import com.mainproject.model.EquipmentRental;
import com.mainproject.model.Order;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/** Admin payment management for existing product orders and equipment rentals. */
public class PaymentManagement {
    private final Stage stage;
    private final AdminDashboard dashboard;
    private final OrderDAO orders = new OrderDAO();
    private final EquipmentRentalDAO rentals = new EquipmentRentalDAO();
    private TableView<Row> table;
    private final ObservableList<Row> data = FXCollections.observableArrayList();
    private TextField search;

    public PaymentManagement(Stage stage, AdminDashboard dashboard) { this.stage=stage; this.dashboard=dashboard; }

    public void show() {
        BorderPane root=new BorderPane(); root.setStyle("-fx-background-color:"+AdminCommon.BG+";");
        root.setLeft(AdminCommon.sidebar(stage,dashboard,"Payment Management"));
        root.setTop(AdminCommon.topBar("Payment Management",()->AdminCommon.collapse(root),this::load));
        root.setCenter(content()); stage.getScene().setRoot(root); load();
    }

    private VBox content(){
        VBox content=new VBox(12); content.setPadding(new Insets(20));
        Label heading=new Label("Product and Equipment Rental Payments"); heading.setStyle("-fx-font-size:16px;-fx-font-weight:bold;");
        search=new TextField(); search.setPromptText("Search ID, payer, type, method or status..."); search.textProperty().addListener((o,a,b)->filter());
        table=new TableView<>(); table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); table.setPlaceholder(new Label("No payment records found."));
        TableColumn<Row,String> type=col("Type",Row::getType), id=col("Reference ID",Row::getId), payer=col("Payer",Row::getPayer), recipient=col("Recipient",Row::getRecipient), amount=col("Amount",Row::getAmount), method=col("Method",Row::getMethod), status=col("Status",Row::getStatus), date=col("Date",Row::getDate);
        table.getColumns().addAll(type,id,payer,recipient,amount,method,status,date); VBox.setVgrow(table,Priority.ALWAYS);
        Label note=new Label("Uses existing product orders and equipment rental records. No fake transactions are created."); note.setStyle("-fx-text-fill:#777;");
        VBox card=new VBox(10,heading,search,table,note); card.setPadding(new Insets(18)); AdminCommon.card(card); VBox.setVgrow(card,Priority.ALWAYS); content.getChildren().add(card); VBox.setVgrow(content,Priority.ALWAYS); return content;
    }
    private TableColumn<Row,String> col(String title, java.util.function.Function<Row,String> fn){ TableColumn<Row,String> c=new TableColumn<>(title); c.setCellValueFactory(x->new SimpleStringProperty(fn.apply(x.getValue()))); return c; }
    private void load(){ data.clear(); try { List<Order> os=orders.getAllOrders(); for(Order o:os) if(o!=null)data.add(Row.order(o)); } catch(Exception e){ AdminCommon.error("Order Payment Load Failed",safe(e.getMessage(),"Unable to load order payments.")); }
        try { com.google.cloud.firestore.Firestore db=com.google.firebase.cloud.FirestoreClient.getFirestore(); for(com.google.cloud.firestore.QueryDocumentSnapshot d:db.collection("equipmentRentals").get().get().getDocuments()){ EquipmentRental r=d.toObject(EquipmentRental.class); if(r!=null){r.setRentalId(d.getId());data.add(Row.rental(r));} } } catch(Exception e){ System.err.println("Rental payment load failed: "+e.getMessage()); }
        filter(); }
    private void filter(){ if(table==null||search==null)return; String q=search.getText()==null?"":search.getText().trim().toLowerCase(Locale.ROOT); ObservableList<Row> out=FXCollections.observableArrayList(); for(Row r:data){String all=(r.getType()+" "+r.getId()+" "+r.getPayer()+" "+r.getRecipient()+" "+r.getMethod()+" "+r.getStatus()).toLowerCase(Locale.ROOT);if(q.isEmpty()||all.contains(q))out.add(r);} table.setItems(out); }
    private static String safe(String s,String f){return s==null||s.trim().isEmpty()?f:s.trim();}
    public static class Row { private final String type,id,payer,recipient,amount,method,status,date; private Row(String t,String i,String p,String r,String a,String m,String s,String d){type=t;id=i;payer=p;recipient=r;amount=a;method=m;status=s;date=d;}
        static Row order(Order o){Date d=o.getPaymentDate()!=null?o.getPaymentDate():o.getOrderDate();return new Row("Product Order",safe(o.getOrderId(),"-"),safe(o.getBuyerName(),safe(o.getBuyerEmail(),"-")),"Product Seller",money(o.getTotalAmount()),safe(o.getPaymentMethod(),"Not selected"),safe(o.getPaymentStatus(),"Pending"),date(d));}
        static Row rental(EquipmentRental r){Date d=r.getPaymentDate()!=null?r.getPaymentDate():r.getCreatedAt();return new Row("Equipment Rental",safe(r.getRentalId(),"-"),safe(r.getBuyerName(),safe(r.getBuyerEmail(),"-")),safe(r.getEquipmentOwnerName(),safe(r.getEquipmentOwnerEmail(),"-")),money(r.getTotalAmount()),safe(r.getPaymentMethod(),"Pending"),safe(r.getPaymentStatus(),"Pending"),date(d));}
        static String money(double v){return "₹"+new DecimalFormat("0.00").format(v);} static String date(Date d){return d==null?"-":new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(d);} public String getType(){return type;} public String getId(){return id;} public String getPayer(){return payer;} public String getRecipient(){return recipient;} public String getAmount(){return amount;} public String getMethod(){return method;} public String getStatus(){return status;} public String getDate(){return date;}
    }
}
