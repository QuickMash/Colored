package cc.quickmash.colored;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.regex.Pattern;

public final class Colored extends JavaPlugin implements Listener {

    private static final Pattern AMP_CODES = Pattern.compile("(?i)&([0-9A-FK-ORX])");

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void c(AsyncChatEvent e) {
        final String plain = PlainTextComponentSerializer.plainText().serialize(e.message()).trim();
        if (plain.isEmpty()) {
            e.setCancelled(true);
            return;
        }

        if (!AMP_CODES.matcher(plain).find()) {
            return;
        }
        Component colored = LegacyComponentSerializer.legacyAmpersand().deserialize(plain);
        e.message(colored);
    }
}
