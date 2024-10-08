package me.trololo11.lifespluginseason3.tasks;

import me.trololo11.lifespluginseason3.menus.cardmenus.GoldLifeGetMenu;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

/**
 * This task is responsible for making the roulette spinning animation
 * while the gold life is randomizing in the {@link GoldLifeGetMenu}.
 */
public class GoldLifeRouletteTask extends BukkitRunnable  {

    private final Player player;
    private final GoldLifeGetMenu goldLifeGetMenu;
    private final Random random;
    private int spins = 5*8;
    private final int allSpins;
    private int currIndex = 0;
    private boolean running = true;
    private boolean stopped = false;

    public GoldLifeRouletteTask(GoldLifeGetMenu goldLifeGetMenu, Player player, int finalSpins){
        random = new Random();

        this.goldLifeGetMenu = goldLifeGetMenu;
        this.player = player;

        spins += 8*random.nextInt(3);
        spins += finalSpins;

        this.allSpins = this.spins;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {

        while (running && !stopped) {
            if (currIndex % 8 == 0) currIndex = 0;

            goldLifeGetMenu.setPointer(currIndex, player);

            spins--;
            currIndex++;

            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 0.9f );

            if (spins <= 0) {
                spins = 1;
                running = false;
            }

            long sleepTime = (long) ( ( (double) allSpins/spins)*25 ); //The sleep time is calculated based on the completed spin to make the later pointer changes slower

            if(spins <= 3){
                sleepTime += random.nextInt(30);
            }


            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(!running){
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 0.8f );
                goldLifeGetMenu.setPointerBrown(player);
            }
        }

        if(stopped) return;

        try {
            Thread.sleep(900);
            goldLifeGetMenu.stoppedRandomizing(player);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void stop(){
        stopped = true;
    }
}
