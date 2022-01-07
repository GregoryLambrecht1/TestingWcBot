import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import java.util.Random;

@ScriptManifest(author = "Mob", name = "simple", info = "test", version = 0.1, logo = "")
public final class TestingBot extends Script  {
    @Override
    public final int onLoop() throws InterruptedException {
            //checking what wc lvl u are to check what to chop
       int startWcLevel = getSkills().getStatic(Skill.WOODCUTTING);
            //lvl < 20 and inv not full --> chop
       if (startWcLevel < 20 && !getInventory().isFull()){
           chop();
           //lvl > 20 and inv not full --> chopoak
       }else if (startWcLevel > 20 && !getInventory().isFull()){
           chopOak();
       }else bank();
        return 200;
    }
    public void chop() throws InterruptedException {
            //making area , if position is not in the area --> walk to random pos. in the area
        Area treeArea = new Area(3181,3235,3205,3256);
        if (!treeArea.contains(myPosition())){
            getWalking().webWalk(treeArea.getRandomPosition());
        }
            //getting closest tree in the area --> if not null and can interact
        RS2Object tree = getObjects().closest(treeArea,"tree");
        if (tree != null && tree.interact("Chop down")) {
            //sleeps while doing action to click the tree
            sleep(random(2500,4000));
            //new conditional sleep when animating , tree doesnt exist
            new ConditionalSleep(random(5000,7000)) {
                @Override
                public boolean condition() {
                    return !myPlayer().isAnimating() || !tree.exists();
                }
            }.sleep();
        }
    }

    public void chopOak() throws InterruptedException {
        Area treeOakArea = new Area(3186,3238,3207,3253);
        if (!treeOakArea.contains(myPosition())){
            getWalking().webWalk(treeOakArea.getRandomPosition());
        }
        RS2Object tree = getObjects().closest(treeOakArea,"Oak");
        if (tree != null && tree.interact("Chop down")) {
            sleep(random(1800,2500));
            new ConditionalSleep(random(5000,7000)) {
                @Override
                public boolean condition() {
                    return !myPlayer().isAnimating() || !tree.exists();
                }
            }.sleep();
        }


    }
    public void bank() throws InterruptedException {
        int randomSleepAfterBank = random(5000,25000);
        if (!Banks.LUMBRIDGE_UPPER.contains(myPosition())){
            getWalking().webWalk(Banks.LUMBRIDGE_UPPER);
        }else if(!getBank().isOpen()) {
            getBank().open();
        }else if (!getInventory().isEmpty()){
            getBank().depositAll("logs");
        }else
            getBank().close();
        sleep(random(5000,25000));
        }

    }


