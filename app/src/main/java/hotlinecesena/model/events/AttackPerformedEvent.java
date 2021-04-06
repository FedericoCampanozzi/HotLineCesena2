package hotlinecesena.model.events;

import hotlinecesena.model.entities.Entity;
import hotlinecesena.model.entities.items.Weapon;

public final class AttackPerformedEvent extends AbstractWeaponEvent {

    public AttackPerformedEvent(final Entity source, final Weapon weapon) {
        super(source, weapon);
    }
}
