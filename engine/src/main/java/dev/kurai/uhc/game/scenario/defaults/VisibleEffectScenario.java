package dev.kurai.uhc.game.scenario.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.game.GameStartEvent;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import dev.kurai.uhc.tablist.TabListService;
import dev.kurai.uhc.tablist.part.EffectTabListPart;
import dev.kurai.uhc.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public final class VisibleEffectScenario extends AbstractScenario implements Listener {

    public VisibleEffectScenario(final UltraHardcoreAPI ultraHardcore) {
        super("visible_effect", "Visible Effect", ultraHardcore, ScenarioCategory.GAMEPLAY);
    }

    @Override
    public ItemStack provideIcon() {
        return new ItemBuilder(Material.POTION)
                .editMeta(PotionMeta.class, potionMeta -> potionMeta.setMainEffect(PotionEffectType.REGENERATION))
                .asItemStack();
    }

    @Override
    public List<String> provideLore() {
        return List.of("Les valeurs des effets sont", "visibles dans la liste des joueurs.");
    }

    @Override
    public void onEnable() {
        if (this.ultraHardcore.gameService().playing()) {
            final TabListService tabListService = this.ultraHardcore.tabListService();
            tabListService.addPart(new EffectTabListPart(this.ultraHardcore.profileService()));
        }
    }

    @Override
    public void onDisable() {
        this.ultraHardcore.tabListService().removePart(EffectTabListPart.PART_KEY);
    }

    @EventHandler
    public void onStart(final GameStartEvent event) {
        final TabListService tabListService = this.ultraHardcore.tabListService();
        tabListService.addPart(new EffectTabListPart(this.ultraHardcore.profileService()));
    }
}
