package me.trololo11.lifespluginseason3.tasks;

import me.trololo11.lifespluginseason3.menus.GetLifeMenu;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

/**
 * This task is responcilbe for making the roulette spining animation
 * while the gold life is randomizing in the {@link GetLifeMenu}.
 */
public class GoldLifeRouletteTask extends BukkitRunnable  {

    private final Player player;
    private final GetLifeMenu getLifeMenu;
    private final Random random;
    private int spins = 5*8;
    private final int allSpins;
    private int currIndex = 0;
    private boolean running = true;

    public GoldLifeRouletteTask(GetLifeMenu getLifeMenu,Player player, int finalSpins){
        random = new Random();

        this.getLifeMenu = getLifeMenu;
        this.player = player;

        spins += 8*random.nextInt(3);
        spins += finalSpins;

        this.allSpins = this.spins;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {

        while (running) {
            if (currIndex % 8 == 0) currIndex = 0;

            getLifeMenu.setPointer(currIndex, player);

            spins--;
            currIndex++;

            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 0.9f );

            if (spins <= 0) {
                spins = 1;
                running = false;
            }

            long sleepTime = (long) ( ( (double) allSpins/spins)*25 ); //The sleep time is calculated based on the completed sping to make the later pointer changes slower

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
                getLifeMenu.setPointerBrown(player);
            }
        }


        try {
//            Thread.sleep(1500);
//            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 0.6f );
//            getLifeMenu.setPointerBrown(player);
            Thread.sleep(1500+ random.nextInt(300));
            getLifeMenu.stoppedRandomizing(player);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void stop(){
        running = false;
    }
}