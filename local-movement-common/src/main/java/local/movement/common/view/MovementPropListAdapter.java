package local.movement.common.view;

import local.movement.common.model.MovementProperties;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.List;

@Getter
public abstract class MovementPropListAdapter <E extends ViewModel> {

    protected List<E> list;

    protected MovementPropListAdapter(List<E> list) {
        this.list = list;
    }

    abstract public void add(MovementProperties movementProperties);

    public void remove(MovementProperties movementProperties) {
        list.removeIf(model -> model.getMovementProperties().equals(movementProperties));
    }

    public boolean existConnect(MovementProperties movementProperties) {
        InetSocketAddress address = movementProperties.getInetAddress();
        for (E model: list) {
            if (model.getMovementProperties().getInetAddress().equals(address)) {
                return true;
            }
        }
        return false;
    }

    public MovementProperties get(MovementProperties movementProperties) {
        for (ViewModel model: list) {
            if (model.getMovementProperties().equals(movementProperties)) {
                return model.getMovementProperties();
            }
        }
        return null;
    }

    public MovementProperties getByAddress(InetSocketAddress inetAddress) {
        for (ViewModel model: list) {
            if (model.getMovementProperties().getInetAddress().equals(inetAddress)) {
                return model.getMovementProperties();
            }
        }
        return null;
    }

}
