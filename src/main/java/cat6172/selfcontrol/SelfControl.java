package cat6172.selfcontrol;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class SelfControl extends JavaPlugin implements CommandExecutor {
    @Override
    public void onEnable() {
        try {
            getCommand("selfcontrol").setExecutor(new CommandAdmin());
            getCommand("toggleautokick").setExecutor(new CommandSelf());
            getCommand("toggleautotempban").setExecutor(new CommandSelf());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}