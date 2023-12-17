package cat6172.selfcontrol;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class SelfControl extends JavaPlugin implements CommandExecutor {
    @Override
    public void onEnable() {
        try {
            Bukkit.getLogger().info("Initializing commands");
            getCommand("autokick").setExecutor(new CommandSelf());
            getCommand("autoban").setExecutor(new CommandSelf());
            Bukkit.getLogger().info("Initializing task scheduler");
            KickSchedule.initialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}