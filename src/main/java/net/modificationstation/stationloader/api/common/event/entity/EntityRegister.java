package net.modificationstation.stationloader.api.common.event.entity;

import net.minecraft.entity.EntityBase;
import net.modificationstation.stationloader.api.common.event.GameEvent;
import uk.co.benjiweber.expressions.functions.TriConsumer;

import java.util.function.Consumer;

public interface EntityRegister {

    @SuppressWarnings("UnstableApiUsage")
    GameEvent<EntityRegister> EVENT = new GameEvent<>(EntityRegister.class,
            listeners ->
                    register -> {
                        for (EntityRegister listener : listeners)
                            listener.registerEntities(register);
                    },
            (Consumer<GameEvent<EntityRegister>>) entityRegister ->
                    entityRegister.register(register -> GameEvent.EVENT_BUS.post(new Data(register)))
    );

    void registerEntities(TriConsumer<Class<? extends EntityBase>, String, Integer> register);

    final class Data extends GameEvent.Data<EntityRegister> {

        private final TriConsumer<Class<? extends EntityBase>, String, Integer> register;

        private Data(TriConsumer<Class<? extends EntityBase>, String, Integer> register) {
            super(EVENT);
            this.register = register;
        }

        public void register(Class<? extends EntityBase> entityClass, String entityIdentifier, int entityId) {
            register.accept(entityClass, entityIdentifier, entityId);
        }
    }
}
