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
        int duration = 60; //Change to configurable
        if (args.length != 1) {
            return false;
        }
        if (args[0].equals("list")){
            if (Storage.playersToKick.isEmpty()){
                sender.sendMessage(Component.text("Kick list: no players on list"));
                return true;
            }
            StringBuilder temp = new StringBuilder();
            temp.append("Kick list: ");
            for (String name : Storage.playersToKick){
                temp.append(", ").append(name);
            }
            temp.deleteCharAt(0).deleteCharAt(0);
            sender.sendMessage(Component.text(temp.toString()));

            if (Storage.getPlayersToTempBan.isEmpty()){
                sender.sendMessage(Component.text("Tempban list: no players on list"));
                return true;
            }
            temp = new StringBuilder();
            temp.append("Tempban list: ");
            for (String name : Storage.getPlayersToTempBan){
                temp.append(", ").append(name);
            }
            temp.deleteCharAt(0).deleteCharAt(0);
            sender.sendMessage(Component.text(temp.toString()));

            return true;
        } else if (args[0].equals("now")){
            for (String name : Storage.playersToKick){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick "+name);
            }
            Storage.playersToKick.clear();
            for (String name : Storage.getPlayersToTempBan){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempban " + name + " " + duration + "m");
            }
            Storage.playersToKick.clear();
            return true;
        }
        return false;
    }
}
