package cc.quickmash.colored;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public final class Colored extends JavaPlugin implements Listener {

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void c(AsyncChatEvent e) {
        String r = PlainTextComponentSerializer.plainText().serialize(e.message());
        r = r.replaceAll("\\s+", " ").trim();

        if (r.isEmpty() || r.matches("(?i)(?:&[0-9A-FK-ORX])+")) {
            e.setCancelled(true);
            return;
        }

        e.message(LegacyComponentSerializer.legacyAmpersand().deserialize(r));
    }
}
