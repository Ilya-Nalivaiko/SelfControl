package cat6172.selfcontrol;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SelfControl extends JavaPlugin implements CommandExecutor {
    @Override
    public void onEnable() {
        try {
            this.saveDefaultConfig();
            Bukkit.getLogger().info("Initializing commands");
            getCommand("kickme").setExecutor(new CommandSelf());
            getCommand("banme").setExecutor(new CommandSelf());
            Bukkit.getLogger().info("Initializing task scheduler");
            FileConfiguration c = this.getConfig();
            KickSchedule.initialize(c.getString("kickMessage"), c.getString("banMessage"), c.getString("banTime"), c.getString("timeZone"), c.getString("timeFormat"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}