package com.example.npcmod.npcdata;

import com.example.npcmod.NpcMod;
import net.minecraft.util.Identifier;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NpcData {

    public enum NpcType {
        MERCHANT("§6🏪 Торговец"),
        TRANSPORTER("§b🚀 Проводник"),
        DIALOGUE("§a💬 Мудрец"),
        GUARD("§7🛡️ Стражник"),
        BANDIT("§4🗡️ Разбойник"),
        CITIZEN("§f👤 Житель");

        public final String displayName;
        NpcType(String displayName) { this.displayName = displayName; }
    }

    public static class NpcInfo {
        public final String name;
        public final NpcType type;
        public final Identifier texture;
        public final String faction;
        public final String location;

        public NpcInfo(String name, NpcType type, String texturePath, String faction, String location) {
            this.name = name;
            this.type = type;
            this.texture = new Identifier(NpcMod.MOD_ID, "textures/entity/" + texturePath);
            this.faction = faction;
            this.location = location;
        }
    }

    private static final Map<UUID, NpcInfo> NPC_INFO = new HashMap<>();

    // ========== ВСЕ 62 NPC ==========

    public static final String[] NPC_NAMES = {
            // КЛЮЧЕВЫЕ (19)
            "§aСерафима", "§aРейн", "§aКракен", "§aЛис", "§aБреск", "§aСтрелка", "§aМолчальник",
            "§6Бернардо", "§6Капитан Морган",
            "§bСтарый Эдмон", "§bЛука",
            "§4Морте", "§4Вульфгар", "§4Главарь Чёрных Крыс",
            "§eГуннар", "§eОлдрич", "§eЭхо", "§eФеликс",
            "§cЛюциус Вертиго",

            // ПРОСТЫЕ ЖИТЕЛИ - ПОРТ (5)
            "§fРыбак", "§fГрузчик", "§fТорговка рыбой", "§fСтаруха", "§fРебёнок",

            // ПРОСТЫЕ ЖИТЕЛИ - ТАВЕРНА (5)
            "§fПьяный", "§fПутешественник", "§fБард", "§fПарень", "§fДевушка",

            // ПРОСТЫЕ ЖИТЕЛИ - ХРАМ (4)
            "§fПаломник 1", "§fПаломник 2", "§fМонах", "§fСтарик",

            // ПРОСТЫЕ ЖИТЕЛИ - ЗОЛОТОЙ КВАРТАЛ (4)
            "§fБогатый торговец", "§fДевушка с собакой", "§fКлерк", "§fСтарик на лавочке",

            // ПРОСТЫЕ ЖИТЕЛИ - ЦИТАДЕЛЬ (4)
            "§fКузнец", "§fЖена кузнеца", "§fСын кузнеца", "§fУченик кузнеца",

            // ПРОСТЫЕ ЖИТЕЛИ - ТРУЩОБЫ (4)
            "§fБомж", "§fНищий ребёнок", "§fСтаруха с грибами", "§fТорговец зельями",

            // СТРАЖНИКИ - НЕЙТРАЛЬНЫЕ (3)
            "§7Стражник порта 1", "§7Стражник порта 2", "§7Стражник таверны",

            // СТРАЖНИКИ - ПАНЦИРЬ (5)
            "§6Стражник склада", "§6Стражник квартала 1", "§6Стражник квартала 2",
            "§6Стражник особняка", "§6Стражник банка",

            // СТРАЖНИКИ - КЛИНОК (3)
            "§8Стражник маяка 1", "§8Стражник маяка 2", "§8Стражник наверху",

            // СТРАЖНИКИ - ЯРЫЙ РОГ (4)
            "§cСтражник ворот 1", "§cСтражник ворот 2", "§cСтражник стены", "§cКапитан стражи",

            // СТРАЖНИКИ - ОРЛИНОЕ ОКО (2)
            "§eЛучник", "§eСнайпер"
    };

    public static final NpcType[] NPC_TYPES = {
            // Ключевые
            NpcType.DIALOGUE, NpcType.DIALOGUE, NpcType.DIALOGUE, NpcType.DIALOGUE,
            NpcType.DIALOGUE, NpcType.DIALOGUE, NpcType.DIALOGUE,
            NpcType.MERCHANT, NpcType.MERCHANT,
            NpcType.TRANSPORTER, NpcType.TRANSPORTER,
            NpcType.BANDIT, NpcType.BANDIT, NpcType.BANDIT,
            NpcType.DIALOGUE, NpcType.DIALOGUE, NpcType.DIALOGUE, NpcType.DIALOGUE,
            NpcType.DIALOGUE,

            // Жители порта
            NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN,
            // Таверна
            NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN,
            // Храм
            NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN,
            // Золотой квартал
            NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN,
            // Цитадель
            NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN,
            // Трущобы
            NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN,

            // Стражники
            NpcType.GUARD, NpcType.GUARD, NpcType.GUARD,
            NpcType.GUARD, NpcType.GUARD, NpcType.GUARD, NpcType.GUARD, NpcType.GUARD,
            NpcType.GUARD, NpcType.GUARD, NpcType.GUARD,
            NpcType.GUARD, NpcType.GUARD, NpcType.GUARD, NpcType.GUARD,
            NpcType.GUARD, NpcType.GUARD
    };

    public static final String[] TEXTURE_PATHS = {
            // Ключевые
            "serafima.png", "rein.png", "kraken.png", "lis.png", "bresk.png", "strelka.png", "molchalnik.png",
            "bernardo.png", "morgan.png",
            "edmon.png", "luka.png",
            "morte.png", "vulfgar.png", "rat_boss.png",
            "gunnar.png", "oldrich.png", "echo.png", "felix.png",
            "lucius.png",

            // Жители порта
            "fisherman.png", "loader.png", "fishseller.png", "oldwoman.png", "kid.png",
            // Таверна
            "drunkard.png", "traveler.png", "bard.png", "couple_man.png", "couple_woman.png",
            // Храм
            "pilgrim1.png", "pilgrim2.png", "monk.png", "oldman.png",
            // Золотой квартал
            "richtrader.png", "richgirl.png", "clerk.png", "benchman.png",
            // Цитадель
            "smith_father.png", "smith_mother.png", "smith_son.png", "smith_apprentice.png",
            // Трущобы
            "bum.png", "beggar_kid.png", "mushroom_woman.png", "darktrader.png",

            // Стражники
            "guard_neutral1.png", "guard_neutral2.png", "guard_neutral3.png",
            "guard_golden1.png", "guard_golden2.png", "guard_golden3.png", "guard_golden4.png", "guard_golden5.png",
            "guard_dark1.png", "guard_dark2.png", "guard_dark3.png",
            "guard_iron1.png", "guard_iron2.png", "guard_iron3.png", "guard_iron_captain.png",
            "guard_leather.png", "guard_sniper.png"
    };

    public static final String[] FACTIONS = {
            // Ключевые
            "neutral", "neutral", "black_claw", "neutral", "fierce_horn", "eagle_eye", "midnight_blade",
            "neutral", "neutral",
            "neutral", "neutral",
            "bandits", "bandits", "bandits",
            "fierce_horn", "neutral", "midnight_blade", "golden_shell",
            "golden_shell",

            // Жители
            "neutral", "neutral", "neutral", "neutral", "neutral",
            "neutral", "neutral", "neutral", "neutral", "neutral",
            "neutral", "neutral", "neutral", "neutral",
            "golden_shell", "golden_shell", "golden_shell", "golden_shell",
            "fierce_horn", "fierce_horn", "fierce_horn", "fierce_horn",
            "bandits", "bandits", "bandits", "bandits",

            // Стражники
            "neutral", "neutral", "neutral",
            "golden_shell", "golden_shell", "golden_shell", "golden_shell", "golden_shell",
            "midnight_blade", "midnight_blade", "midnight_blade",
            "fierce_horn", "fierce_horn", "fierce_horn", "fierce_horn",
            "eagle_eye", "eagle_eye"
    };

    public static final String[] LOCATIONS = {
            // Ключевые
            "Храм", "Таверна", "Порт", "Подвал таверны", "Цитадель", "Башни", "Маяк",
            "Золотой квартал", "Порт",
            "Порт/Цитадель/Пик", "Престол/Базар",
            "Трущобы", "Трущобы", "Порт",
            "Цитадель", "Порт", "Храм/Маяк", "Золотой квартал",
            "Золотой квартал",

            // Жители
            "Порт", "Порт", "Порт", "Порт", "Порт",
            "Таверна", "Таверна", "Таверна", "Таверна", "Таверна",
            "Храм", "Храм", "Храм", "Храм",
            "Золотой квартал", "Золотой квартал", "Золотой квартал", "Золотой квартал",
            "Цитадель", "Цитадель", "Цитадель", "Цитадель",
            "Трущобы", "Трущобы", "Трущобы", "Трущобы",

            // Стражники
            "Порт", "Порт", "Таверна",
            "Порт", "Золотой квартал", "Золотой квартал", "Золотой квартал", "Золотой квартал",
            "Маяк", "Маяк", "Маяк",
            "Цитадель", "Цитадель", "Цитадель", "Цитадель",
            "Башни", "Башни"
    };

    public static NpcInfo getInfo(UUID uuid) {
        return NPC_INFO.get(uuid);
    }

    public static void registerNpc(UUID uuid, int index) {
        if (index >= 0 && index < 62) {
            NpcInfo info = new NpcInfo(
                    NPC_NAMES[index],
                    NPC_TYPES[index],
                    TEXTURE_PATHS[index],
                    FACTIONS[index],
                    LOCATIONS[index]
            );
            NPC_INFO.put(uuid, info);
        }
    }

    public static int getTotalNpcCount() {
        return 62;
    }
}