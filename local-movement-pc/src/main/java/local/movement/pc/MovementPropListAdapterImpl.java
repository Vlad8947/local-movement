package local.movement.pc;

import local.movement.common.view.MovementPropListAdapter;
import local.movement.common.model.MovementProperties;
import local.movement.common.view.ViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

@Getter
public abstract class MovementPropListAdapterImpl<E extends ViewModel> extends MovementPropListAdapter {

    protected ObservableList<E> list =
            FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    protected long lastUpdate = System.currentTimeMillis();

    protected MovementPropListAdapterImpl(List list) {
        super(list);
    }

    @Override
    abstract public void add(MovementProperties movementProperties);

    @Override
    public void remove(MovementProperties movementProperties) {
        list.removeIf(model -> model.getMovementProperties().equals(movementProperties));
    }

    public boolean exist(MovementProperties movementProperties) {
        InetSocketAddress address = movementProperties.getInetAddress();
        for (E model: list) {
            if (model.getMovementProperties().getInetAddress().equals(address)) {
                return true;
            }
        }
        return false;
    }
}
