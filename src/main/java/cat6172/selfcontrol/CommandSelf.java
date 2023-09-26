package cat6172.selfcontrol;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class CommandSelf implements CommandExecutor {
    @Override

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            String name = sender.getName();

            Set<String> list;
            if (label.equals("toggleautokick")) list = Storage.playersToKick;
            else list = Storage.getPlayersToTempBan;

            if (list.contains(name)){
                list.remove(name);
                sender.sendMessage(Component.text("You have been removed from the list"));
            } else {
                list.add(name);
                sender.sendMessage(Component.text("You have been added to the list"));
            }
        } else {
            sender.sendMessage(Component.text("Command intended to be used by players only"));
        }
        return true;
    }
}
