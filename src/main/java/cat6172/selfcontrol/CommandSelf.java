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
            String name = sender.getName();
            if (Storage.playersToKick.contains(name)){
                Storage.playersToKick.remove(name);
                sender.sendMessage(Component.text("You have been removed from the list of players to be kicked"));
            } else {
                Storage.playersToKick.add(name);
                sender.sendMessage(Component.text("You have been added to the list of players to be kicked"));
            }
        } else {
            sender.sendMessage(Component.text("Command intended to be used by players only"));
        }
        return true;
    }
}
