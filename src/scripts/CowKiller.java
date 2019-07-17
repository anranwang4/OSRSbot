package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

@Script.Manifest(name="Cow Killer", description="Kills cows and heals", properties="author=Anran; topic=999; client=4;")

public class CowKiller extends PollingScript<ClientContext> {

    final static int COW_IDS[] = {2790, 2791, 2792, 2793, 2794};
    final static int FOOD_ID = 2142; //cooked beef

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void poll() {
        // constant loop
        if (hasFood()) {
            if(needsHeal()) {
                heal();
            } else if (shouldAttack()) {
                attack();
            }
        }
    }

    public boolean needsHeal() {
        return ctx.combat.health() < 6;
    }

    public boolean shouldAttack() {
        return !ctx.players.local().inCombat();
    }

    public boolean hasFood() {
        return ctx.inventory.select().id(FOOD_ID).count() > 0;                //id unsure parameters
    }

    public void attack() {
        final Npc cowToAttack = ctx.npcs.select().id(COW_IDS).select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return !npc.inCombat();
            }
        }).nearest().poll();

        cowToAttack.interact("Attack");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().inCombat();
            }
        },250, 30);
    }

    public void heal() {
        Item foodToEat = ctx.inventory.select().id(FOOD_ID).poll();
        foodToEat.interact("Eat");

        final int startingHealth = ctx.combat.health();

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final int currentHealth = ctx.combat.health();
                return currentHealth != startingHealth;
            }
        },150, 20);
    }

}
