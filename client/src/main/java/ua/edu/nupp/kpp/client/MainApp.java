package ua.edu.nupp.kpp.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.logging.Logger;

public class MainApp extends Application {
    private static final Logger LOG=Logger.getLogger(MainApp.class.getName());
    private final HttpMuseumExhibitsClient api=new HttpMuseumExhibitsClient();
    private final TableView<Exhibit> exhibits=new TableView<>();
    private final TableView<Inspection> inspections=new TableView<>();
    private final TableView<ConservationRequest> requests=new TableView<>();
    private final TableView<Specialist> specialists=new TableView<>();

    @Override public void start(Stage stage) {
        TabPane tabs=new TabPane(
                new Tab("Експонати",exhibitPane()),
                new Tab("Огляди",inspectionPane()),
                new Tab("Заявки на консервацію",requestPane()),
                new Tab("Фахівці",specialistPane())
        );
        tabs.getTabs().forEach(t->t.setClosable(false));
        stage.setScene(new Scene(tabs,1050,650)); stage.setTitle("Контроль стану музейних експонатів"); stage.show();
        refreshAll();
    }

    private Pane exhibitPane() {
        exhibits.getColumns().setAll(col("ID","id"),col("Інвентарний код","inventoryCode"),col("Назва","name"),col("Матеріал","material"),col("Зала","hall"),col("F1","materialSensitivity"),col("F3","exhibitionSignificance"));
        TextField code=new TextField(),name=new TextField(),material=new TextField(),hall=new TextField(),f1=new TextField(),f3=new TextField();
        Button add=new Button("Додати"),update=new Button("Змінити"),delete=new Button("Видалити"),refresh=new Button("Оновити");
        add.setOnAction(e->safe(()->{ api.addExhibit(readExhibit(null,code,name,material,hall,f1,f3)); refreshExhibits(); }));
        update.setOnAction(e->safe(()->{ Exhibit x=selected(exhibits); api.updateExhibit(readExhibit(x.getId(),code,name,material,hall,f1,f3)); refreshExhibits(); }));
        delete.setOnAction(e->safe(()->{ api.deleteExhibit(selected(exhibits).getId()); refreshAll(); }));
        refresh.setOnAction(e->refreshExhibits());
        exhibits.getSelectionModel().selectedItemProperty().addListener((o,a,x)->{ if(x!=null){code.setText(x.getInventoryCode());name.setText(x.getName());material.setText(x.getMaterial());hall.setText(x.getHall());f1.setText(""+x.getMaterialSensitivity());f3.setText(""+x.getExhibitionSignificance());} });
        VBox form=new VBox(6,new Label("Інвентарний код"),code,new Label("Назва"),name,new Label("Матеріал"),material,new Label("Зала"),hall,new Label("F1 (чутливість матеріалу, 0..100)"),f1,new Label("F3 (значущість, 0..100)"),f3,new HBox(8,add,update,delete,refresh));
        return layout(exhibits,form);
    }

    private Pane inspectionPane() {
        inspections.getColumns().setAll(col("ID","id"),col("Експонат","exhibitId"),col("Дата","inspectionDate"),col("F2","damageSeverity"),col("Примітки","notes"));
        TextField exhibitId=new TextField(),date=new TextField(),severity=new TextField(),notes=new TextField();
        Button add=new Button("Додати"),update=new Button("Змінити"),delete=new Button("Видалити"),refresh=new Button("Оновити");
        add.setOnAction(e->safe(()->{ api.addInspection(readInspection(null,exhibitId,date,severity,notes)); refreshInspections(); }));
        update.setOnAction(e->safe(()->{ Inspection x=selected(inspections); api.updateInspection(readInspection(x.getId(),exhibitId,date,severity,notes)); refreshInspections(); }));
        delete.setOnAction(e->safe(()->{ api.deleteInspection(selected(inspections).getId()); refreshAll(); }));
        refresh.setOnAction(e->refreshInspections());
        inspections.getSelectionModel().selectedItemProperty().addListener((o,a,x)->{ if(x!=null){exhibitId.setText(""+x.getExhibitId());date.setText(x.getInspectionDate());severity.setText(""+x.getDamageSeverity());notes.setText(x.getNotes());} });
        VBox form=new VBox(6,new Label("ID експоната"),exhibitId,new Label("Дата YYYY-MM-DD"),date,new Label("F2 (тяжкість пошкодження, 0..100)"),severity,new Label("Примітки"),notes,new HBox(8,add,update,delete,refresh));
        return layout(inspections,form);
    }

    private Pane requestPane() {
        requests.getColumns().setAll(col("ID","id"),col("Огляд","inspectionId"),col("Фахівець","specialistId"),col("Стан","state"),col("F1","f1"),col("F2","f2"),col("F3","f3"),col("I","riskIndex"),col("Опис","description"));
        TextField inspectionId=new TextField(),specialistId=new TextField(),description=new TextField();
        ComboBox<ConservationRequestState> state=new ComboBox<>(FXCollections.observableArrayList(ConservationRequestState.values()));
        Button add=new Button("Створити"),update=new Button("Перерахувати"),change=new Button("Змінити стан"),delete=new Button("Видалити"),refresh=new Button("Оновити");
        add.setOnAction(e->safe(()->{ api.addRequest(Long.parseLong(inspectionId.getText()),Long.parseLong(specialistId.getText()),description.getText()); refreshRequests(); }));
        update.setOnAction(e->safe(()->{ api.updateRequest(selected(requests).getId(),Long.parseLong(inspectionId.getText()),Long.parseLong(specialistId.getText()),description.getText()); refreshRequests(); }));
        change.setOnAction(e->safe(()->{ ConservationRequest r=selected(requests); if(state.getValue()==null) throw new IllegalArgumentException("Виберіть новий стан"); api.changeRequestState(r.getId(),state.getValue()); refreshRequests(); }));
        delete.setOnAction(e->safe(()->{ api.deleteRequest(selected(requests).getId()); refreshAll(); }));
        refresh.setOnAction(e->refreshRequests());
        requests.getSelectionModel().selectedItemProperty().addListener((o,a,v)->{ if(v!=null){inspectionId.setText(""+v.getInspectionId());specialistId.setText(""+v.getSpecialistId());description.setText(v.getDescription());} });
        VBox form=new VBox(8,new Label("ID огляду"),inspectionId,new Label("ID фахівця"),specialistId,new Label("Опис"),description,new Label("Новий стан"),state,new HBox(8,add,update,change,delete,refresh));
        return layout(requests,form);
    }

    private Pane specialistPane() {
        specialists.getColumns().setAll(col("ID","id"),col("ПІБ","fullName"),col("Посада","position"),col("Спеціалізація","specialization"));
        TextField fullName=new TextField(),position=new TextField(),specialization=new TextField();
        Button add=new Button("Додати"),update=new Button("Змінити"),delete=new Button("Видалити"),refresh=new Button("Оновити");
        add.setOnAction(e->safe(()->{ api.addSpecialist(new Specialist(null,fullName.getText(),position.getText(),specialization.getText())); refreshSpecialists(); }));
        update.setOnAction(e->safe(()->{ Specialist x=selected(specialists); api.updateSpecialist(new Specialist(x.getId(),fullName.getText(),position.getText(),specialization.getText())); refreshSpecialists(); }));
        delete.setOnAction(e->safe(()->{ api.deleteSpecialist(selected(specialists).getId()); refreshAll(); }));
        refresh.setOnAction(e->refreshSpecialists());
        specialists.getSelectionModel().selectedItemProperty().addListener((o,a,x)->{ if(x!=null){fullName.setText(x.getFullName());position.setText(x.getPosition());specialization.setText(x.getSpecialization());} });
        return layout(specialists,new VBox(8,new Label("ПІБ"),fullName,new Label("Посада"),position,new Label("Спеціалізація"),specialization,new HBox(8,add,update,delete,refresh)));
    }

    private Exhibit readExhibit(Long id,TextField code,TextField name,TextField material,TextField hall,TextField f1,TextField f3) {
        return new Exhibit(id,code.getText(),name.getText(),material.getText(),hall.getText(),Double.parseDouble(f1.getText()),Double.parseDouble(f3.getText()));
    }
    private Inspection readInspection(Long id,TextField exhibitId,TextField date,TextField severity,TextField notes) {
        return new Inspection(id,Long.parseLong(exhibitId.getText()),date.getText(),Double.parseDouble(severity.getText()),notes.getText());
    }

    private void refreshAll() { refreshExhibits(); refreshInspections(); refreshRequests(); refreshSpecialists(); }
    private void refreshExhibits() { safe(()->exhibits.setItems(FXCollections.observableArrayList(api.getExhibits()))); }
    private void refreshInspections() { safe(()->inspections.setItems(FXCollections.observableArrayList(api.getInspections()))); }
    private void refreshRequests() { safe(()->requests.setItems(FXCollections.observableArrayList(api.getRequests()))); }
    private void refreshSpecialists() { safe(()->specialists.setItems(FXCollections.observableArrayList(api.getSpecialists()))); }

    private <T> T selected(TableView<T> table) { T v=table.getSelectionModel().getSelectedItem(); if(v==null) throw new IllegalArgumentException("Виберіть запис"); return v; }

    @FunctionalInterface private interface Action { void run() throws Exception; }
    private void safe(Action action) {
        try{ action.run(); }
        catch(Exception e){ if(e instanceof InterruptedException) Thread.currentThread().interrupt(); LOG.warning(e.getMessage()); showError(e); }
    }
    private void showError(Throwable e) { Alert d=new Alert(Alert.AlertType.ERROR,e==null||e.getMessage()==null?"Помилка":e.getMessage(),ButtonType.OK); d.showAndWait(); }
    private Pane layout(TableView<?> table,Pane form) { SplitPane p=new SplitPane(table,form); p.setDividerPositions(0.68); BorderPane root=new BorderPane(p); root.setPadding(new Insets(10)); return root; }
    private <S,T> TableColumn<S,T> col(String title,String property) { TableColumn<S,T> c=new TableColumn<>(title); c.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>(property)); return c; }
    public static void main(String[] args) { launch(args); }
}