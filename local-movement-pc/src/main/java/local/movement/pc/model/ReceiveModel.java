package local.movement.pc.model;

import local.movement.common.ByteFormatter;
import local.movement.common.model.FileProperties;
import local.movement.common.model.MovementProperties;
import local.movement.common.view.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

@Getter
public class ReceiveModel extends ViewModel<ReceiveModel> {

    private StringProperty userName = new SimpleStringProperty();
    private StringProperty address = new SimpleStringProperty();
    private StringProperty fileName = new SimpleStringProperty();
    private StringProperty fileLength = new SimpleStringProperty();

    public ReceiveModel(MovementProperties movementProperties) {
        super(movementProperties);
        FileProperties fileProperties = movementProperties.getFileProperties();
        userName.setValue(fileProperties.getUserName());
        address.setValue(movementProperties.getAddress());
        fileName.setValue(fileProperties.getFileName());
        fileLength.setValue(ByteFormatter.length(
                movementProperties.getFileProperties().getFileLength()));
    }

}
