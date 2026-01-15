package ru.slavagm.timeui.ui.hud;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeHUD extends CustomUIHud {
    private Player _player;

    public TimeHUD(@NonNullDecl PlayerRef playerRef, Player player) {
        _player = player;
        super(playerRef);
    }

    @Override
    protected void build(@NonNullDecl UICommandBuilder uiCommandBuilder) {
        uiCommandBuilder.append("HUD/TimeHUD.ui");
        World world = _player.getWorld();
        if (world != null) {
            WorldTimeResource wtr = world.getEntityStore().getStore().getResource(WorldTimeResource.getResourceType());
            Instant t = wtr.getGameTime();
            uiCommandBuilder.set("#MyLabel.TextSpans", Message.raw(formatTime(t)));
        }
    }

    public void updateTime(Instant time) {
        UICommandBuilder builder = new UICommandBuilder();
        builder.set("#MyLabel.TextSpans", Message.raw(formatTime(time)));
        update(false, builder);
    }

    protected String formatTime(Instant time) {
        String pattern = "HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern)
                .withZone(ZoneOffset.UTC);
        return formatter.format(time);
    }
}
