package me.trololo11.lifespluginseason3.tasks;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.QuestType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class ChangePageTimeTask extends BukkitRunnable {

    private QuestManager questsManager;
    private final char whatChange;
    private final QuestType questType;
    private int startTime;
    private Date endDate;
    private String timerText;

    public ChangePageTimeTask(char whatChange, QuestType questType, QuestManager questsManager, int startTime, Date endDate){
        this.whatChange = whatChange;
        this.questType = questType;
        this.startTime = startTime;
        this.questsManager = questsManager;
        this.endDate = endDate;
        updateText();
    }

    @Override
    public void run() {
        startTime -= 1;
        if(startTime >= 0) updateText();
        if(startTime < 0 || (startTime == 0 && whatChange != 'm')){
            try {
                //waits a second bcs of the small incarrucy of the calculations
                Thread.sleep(1000);
                questsManager.checkPageQuestTimings();
            } catch (IOException | SQLException | InterruptedException e) {
                e.printStackTrace();
            }
            cancel();
        }
    }
    private void updateText(){
        //just updates the text
        if(whatChange == 'd') timerText = startTime + " d";
        if(whatChange == 'h') timerText =  startTime+" h";
        if(whatChange == 'm') timerText = (startTime+1) + " m";
        questsManager.setQuestPageTimeText(questType, timerText);
    }
}
