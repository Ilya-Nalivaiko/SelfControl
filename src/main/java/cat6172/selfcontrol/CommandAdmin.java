package cat6172.selfcontrol;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandAdmin implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 0) {
            return false;
        }
        if (args[0].equals("list")){
            if (Storage.playersToKick.isEmpty()){
                sender.sendMessage(Component.text("no players on list"));
            }
            StringBuilder temp = new StringBuilder();
            for (String name : Storage.playersToKick){
                temp.append(", ").append(name);
            }
            temp.deleteCharAt(0).deleteCharAt(1);
            sender.sendMessage(Component.text(temp.toString()));
            return true;
        } else if (args[0].equals("now")){
            for (String name : Storage.playersToKick){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick "+name);
            }
            Storage.playersToKick.clear();
        }
        return false;
    }
}
