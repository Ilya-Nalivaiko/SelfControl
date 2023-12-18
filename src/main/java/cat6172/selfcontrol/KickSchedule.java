package cat6172.selfcontrol;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.Locale;


public class KickSchedule {
    public static DateTimeFormatter formatter;
    public static ZoneId timeZoneId;
    private static HashMap<String, BukkitTask> scheduledTasks;
    private static BukkitScheduler scheduler;

    private static TextComponent kickMessage;
    private static String banMessage;
    private static String banTime;


    public static void initialize(String kickMsg, String banMsg, String banT, String timeZone, String timeForm) {
        scheduler = Bukkit.getScheduler();
        scheduledTasks = new HashMap<>();
        formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern(timeForm)
                .toFormatter(Locale.ENGLISH)
                .withResolverStyle(ResolverStyle.SMART);
        timeZoneId = ZoneId.of(timeZone);
        kickMessage = Component.text(kickMsg);
        banMessage = banMsg;
        banTime = banT;
    }

    public static void addInstance(Player player, String givenTime, boolean ban) {
        long delaySeconds;
        ZonedDateTime currentDateTime;
        ZonedDateTime givenDateTime;
        try {
            // Get current and given time
            LocalTime givenLocalTime = LocalTime.parse(givenTime.toUpperCase(), formatter);
            currentDateTime = ZonedDateTime.now(timeZoneId);
            givenDateTime = currentDateTime.with(givenLocalTime);

            // If the given time is already passed today, move to the next day
            if (givenDateTime.isBefore(currentDateTime)) {
                givenDateTime = givenDateTime.plusDays(1);
            }

            // Schedule a task to run after the calculated delay
            delaySeconds = Duration.between(currentDateTime, givenDateTime).toSeconds();
        } catch (Exception e){
            player.sendMessage("Error processing the time. Did you use the correct format? Ex. 11:34pm");
            throw new RuntimeException(e);
        }
        BukkitTask newTask;

        String name = player.getName();

        if (ban) newTask = scheduler.runTaskLater(SelfControl.getPlugin(SelfControl.class), () -> {
            Bukkit.getLogger().info("Executing tempban of " + name + " scheduled on " + currentDateTime + "(" + delaySeconds + " seconds ago)");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempban " + name + " " + banTime + " " + banMessage);
            scheduledTasks.remove(name);
        }, delaySeconds * 20);
        else newTask = scheduler.runTaskLater(SelfControl.getPlugin(SelfControl.class), () -> {
            Bukkit.getLogger().info("Executing kick of " + name + " scheduled on " + currentDateTime + "(" + delaySeconds + " seconds ago)");
            player.kick(kickMessage);
            scheduledTasks.remove(name);
        }, delaySeconds * 20);

        String formattedTime = convertTime(delaySeconds);
        if (ban) player.sendMessage("You will be temp banned (for " + banTime + ") in " + formattedTime +". Use /banme r to undo");
        else player.sendMessage("You will be kicked in " + formattedTime +". Use /kickme r to undo");
        Bukkit.getLogger().info("Task scheduled at " + currentDateTime + " to be executed at " + givenDateTime + " (in " + delaySeconds + " seconds (" +formattedTime+ ") )");

        // New task will override old one, so cancel the old one
        BukkitTask existingTask = scheduledTasks.get(name);
        if (existingTask != null) {
            Bukkit.getLogger().info("Cancelled existing task");
            existingTask.cancel();
        }

        // Save reference to task for future access
        scheduledTasks.put(name, newTask);

    }

    public static void removePlayer(String playerName) {
        Bukkit.getPlayer(playerName).sendMessage("Removed auto kick/ban request");
        scheduledTasks.get(playerName).cancel();
        scheduledTasks.remove(playerName);
    }

    private static String convertTime(long timeInSeconds) {
        if (timeInSeconds < 0) {
            return "Invalid time";
        }

        long hours = timeInSeconds / 3600;
        long minutes = (timeInSeconds % 3600) / 60;
        long seconds = timeInSeconds % 60;

        StringBuilder formattedTime = new StringBuilder();

        if (hours > 0) {
            formattedTime.append(hours).append("h ");
        }

        if (minutes > 0) {
            formattedTime.append(minutes).append("m ");
        }

        if (seconds > 0 || (hours == 0 && minutes == 0)) {
            formattedTime.append(seconds).append("s");
        }

        return formattedTime.toString();
    }
}
