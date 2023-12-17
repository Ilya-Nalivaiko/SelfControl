package cat6172.selfcontrol;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.Locale;


public class KickSchedule {
    public static DateTimeFormatter formatter;
    public static String timeZoneId;
    private static HashMap<String, BukkitTask> scheduledTasks;
    private static BukkitScheduler scheduler;

    public static void initialize() {
        scheduler = Bukkit.getScheduler();
        scheduledTasks = new HashMap<>();
        formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("h:mma") //TODO make configurable
                .toFormatter(Locale.ENGLISH)
                .withResolverStyle(ResolverStyle.SMART);
        timeZoneId = "America/New_York"; //TODO make configurable
    }

    public static void addInstance(Player player, String givenTime, boolean ban) {
        // Get current and given time
        LocalTime givenLocalTime = LocalTime.parse(givenTime.toUpperCase(), formatter);
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime givenLocalDateTime = currentDateTime.with(givenLocalTime);

        // If the given time is already passed today, move to the next day
        if (givenLocalDateTime.isBefore(currentDateTime)) {
            givenLocalDateTime = givenLocalDateTime.plusDays(1);
        }

        // Schedule a task to run after the calculated delay
        long delaySeconds = Duration.between(LocalDateTime.now(), givenLocalDateTime).toSeconds();

        BukkitTask newTask;

        String name = player.getName();

        if (ban) newTask = scheduler.runTaskLater(SelfControl.getPlugin(SelfControl.class), () -> {
            Bukkit.getLogger().info("Executing tempban of " + player.name().toString() + " scheduled on " + currentDateTime + "(" + delaySeconds + " seconds ago)");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tempban " + name + " 1h");
            scheduledTasks.remove(name);
        }, delaySeconds * 20);
        else newTask = scheduler.runTaskLater(SelfControl.getPlugin(SelfControl.class), () -> {
            Bukkit.getLogger().info("Executing kick of " + name + " scheduled on " + currentDateTime + "(" + delaySeconds + " seconds ago)");
            player.kick();
            scheduledTasks.remove(name);
        }, delaySeconds * 20);

        Bukkit.getLogger().info("Task scheduled at " + currentDateTime + " to be executed at " + givenLocalDateTime + " (in " + delaySeconds + " seconds)");

        // New task will override old one, so cancel the old one
        BukkitTask existingTask = scheduledTasks.get(player.getName());
        if (existingTask != null) {
            Bukkit.getLogger().info("Cancelled existing task");
            existingTask.cancel();
        }

        // Save reference to task for future access
        scheduledTasks.put(player.getName(), newTask);

    }

    public static void removePlayer(String playerName) {
        scheduledTasks.remove(playerName);
    }
}
