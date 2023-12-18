package cat6172.selfcontrol;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandSelf implements CommandExecutor {
    @Override

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            if (args[0].equals("at")){
                KickSchedule.addInstance((Player)sender, args[1], label.equals("banme"));
            } else if (args[0].equals("r")) {
                KickSchedule.removePlayer(sender.getName());
            } else return false;
        } else {
            sender.sendMessage(Component.text("Command intended to be used by players only"));
        }
        return true;
    }
}
