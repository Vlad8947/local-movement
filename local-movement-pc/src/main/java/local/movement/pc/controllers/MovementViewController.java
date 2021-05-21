package local.movement.pc.controllers;

import local.movement.common.model.MovementProperties;
import local.movement.pc.MainApp;
import local.movement.pc.MovementPropListAdapterImpl;
import local.movement.pc.model.MovementModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;

import java.io.IOException;

import static local.movement.common.AppProperties.Localisation.messages;

public class MovementViewController {

    @FXML
    private TableView<MovementModel> movementTable;
    @FXML
    private ButtonBar movementBar;
    @FXML
    private Button deleteMovementButton;

    @Getter
    private static MovementPropListAdapterImpl<MovementModel> movementListAdapter =
            new MovementPropListAdapterImpl<MovementModel>(getMovementListAdapter().getList()) {
                @Override
                public void add(MovementProperties movementProperties) {
                    list.add(new MovementModel(movementProperties));
                }
            };

    public MovementViewController() {
        Runnable statisticUpdater = new Runnable() {
            private ObservableList<MovementModel> list = getMovementListAdapter().getList();
            @Override
            public void run() {
                try {
                    Object syncKey = new Object();
                    while (true) {
                        if(!list.isEmpty())
                        synchronized (syncKey) {
                            syncKey.wait(1000);
                            list.forEach(prop -> prop.update());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        MainApp.getExecutorService().submit(statisticUpdater);
    }

    @FXML
    private void initialize() {
        movementTableInit();
        buttonBarInit();
    }

    private void movementTableInit() {
        movementTable.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == -1) {
                movementBar.setDisable(true);
                return;
            }
            movementBar.setDisable(false);
        });
        columnsInit();
        movementTable.setItems(movementListAdapter.getList());
    }

    private void columnsInit() {
        TableColumn<MovementModel, String> fileNameColumn, movementTypeColumn, lengthColumn, doneLengthColumn, statusColumn;

        fileNameColumn = new TableColumn<>(messages.getString("file_name"));
        movementTable.getColumns().add(fileNameColumn);

        movementTypeColumn = new TableColumn<>(messages.getString("type"));
        movementTable.getColumns().add(movementTypeColumn);

        statusColumn = new TableColumn<>(messages.getString("status"));
        movementTable.getColumns().add(statusColumn);

        lengthColumn = new TableColumn<>(messages.getString("length"));
        movementTable.getColumns().add(lengthColumn);

        doneLengthColumn = new TableColumn<>(messages.getString("done"));
        movementTable.getColumns().add(doneLengthColumn);

        fileNameColumn.setCellValueFactory(cellData -> cellData.getValue().getFileName());
        movementTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getMovementType());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().getStatus());
        lengthColumn.setCellValueFactory(cellData -> cellData.getValue().getFileLength());
        doneLengthColumn.setCellValueFactory(cellData -> cellData.getValue().getDoneBytes());
    }

    private void buttonBarInit() {
        movementBar.setDisable(true);
        deleteMovementButton.setText(messages.getString("delete_movement"));
        deleteMovementButton.setOnAction(event -> deleteMovementAction());
    }

    //todo
    private void deleteMovementAction() {
        MovementModel movementModel = getSelectedModel();
        try {
            movementModel.getMovementProperties().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        movementTable.getItems().remove(movementModel);
    }

    private MovementModel getSelectedModel() {
        return movementTable.getSelectionModel().getSelectedItem();
    }

}
