package cat6172.selfcontrol;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class SelfControl extends JavaPlugin implements CommandExecutor {
    @Override
    public void onEnable() {
        try {
            getCommand("kicklist").setExecutor(new CommandAdmin());
            getCommand("addtokicklist").setExecutor(new CommandSelf());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}