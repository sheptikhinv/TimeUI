package ru.slavagm.timeui;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.data.PlayerConfigData;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import ru.slavagm.timeui.commands.TimeCommand;
import ru.slavagm.timeui.ui.hud.TimeHUD;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class TimeUIPlugin extends JavaPlugin {

    private static TimeUIPlugin instance;
    private final Map<Player, TimeHUD> activeHuds = new ConcurrentHashMap<>();

    public TimeUIPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    public static TimeUIPlugin getInstance() {
        return instance;
    }

    @Override
    protected void setup() {
        getLogger().at(Level.INFO).log("TimeUI plugin loaded");
        getCommandRegistry().registerCommand(new TimeCommand());

        // Обновляем раз в 1 секунду. Этого достаточно, чтобы поймать изменение каждой игровой минуты,
        // даже если игровые сутки ускорены.
        HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(() -> {
            activeHuds.forEach((player, hud) -> {
                World world = player.getWorld();
                if (world == null) return;

                WorldTimeResource wtr = world.getEntityStore().getStore().getResource(WorldTimeResource.getResourceType());
                Instant t = wtr.getGameTime();
                hud.updateTime(t);
            });
        }, 0, 1, TimeUnit.SECONDS);
    }

    public Map<Player, TimeHUD> getActiveHuds() {
        return activeHuds;
    }
}