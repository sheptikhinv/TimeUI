package ru.slavagm.timeui.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.HudComponent;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import ru.slavagm.timeui.TimeUIPlugin;
import ru.slavagm.timeui.ui.hud.TimeHUD;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static com.hypixel.hytale.logger.HytaleLogger.getLogger;

public class TimeCommand extends AbstractPlayerCommand {

    public TimeCommand() {
        super("timehud", "Show HUD with time", false);
        requirePermission("ru.slavagm.timeui");
    }

    @Override
    protected void execute(
            @NonNullDecl CommandContext commandContext,
            @NonNullDecl Store<EntityStore> store,
            @NonNullDecl Ref<EntityStore> ref,
            @NonNullDecl PlayerRef playerRef,
            @NonNullDecl World world
    ) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;

        Map<Player, TimeHUD> activeHuds = TimeUIPlugin.getInstance().getActiveHuds();


        if (!activeHuds.containsKey(player)) {
            TimeHUD hud = new TimeHUD(playerRef, player);
            player.getHudManager().setCustomHud(playerRef, hud);
            activeHuds.put(player, hud);
            commandContext.sendMessage(Message.raw("HUD enabled"));
        } else {
            commandContext.sendMessage(Message.raw("HUD already enabled"));
        }
    }
}
