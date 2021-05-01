package local.movement.common.view;

import local.movement.common.model.MovementProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class ViewModel<E extends ViewModel> {

    protected MovementProperties movementProperties;

    public ViewModel(MovementProperties movementProperties) {
        this.movementProperties = movementProperties;
    }

}
