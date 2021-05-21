package local.movement.pc.model;

import local.movement.common.model.MovementProperties;
import local.movement.common.ByteFormatter;
import local.movement.common.view.ViewModel;
import local.movement.pc.Updatable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

@Getter
public class MovementModel extends ViewModel<MovementModel> implements Updatable {

    private IntegerProperty number = new SimpleIntegerProperty();
    private StringProperty movementType = new SimpleStringProperty();
    private StringProperty fileName = new SimpleStringProperty();
    private StringProperty fileLength = new SimpleStringProperty();
    private StringProperty doneBytes = new SimpleStringProperty();
    private StringProperty speed = new SimpleStringProperty();
    private StringProperty status = new SimpleStringProperty();

    public MovementModel(MovementProperties movementProperties) {
        super(movementProperties);
        fileName.setValue(movementProperties.getFile().getName());
        fileLength.setValue(ByteFormatter.length(movementProperties.getFileProperties().getFileLength()));
        speed.setValue(ByteFormatter.speedInSecond(0));
        movementType.setValue(movementProperties.getType().toString());
        doneBytes.setValue(ByteFormatter.length(movementProperties.getFile().length()));
    }

    @Override
    public void update() {
        doneBytes.setValue(ByteFormatter.length(movementProperties.getDoneBytes()));
        status.setValue(movementProperties.getStatus().toString());
    }

}
