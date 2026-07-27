# PokePark

**PokePark** is a Pokémon park management game built in Java with a Swing graphical interface.

You're not a Pokémon Trainer battling opponents - you manage your own **Pokémon Park**. Open Mystery Boxes, collect **Generation I Pokémon**, feed them, heal them, train them, evolve them, and earn Pokédollars every day based on the condition of your park.

Available in **French** and **English (US)**.

---

## Requirements

- **JDK 17+** ([Microsoft OpenJDK](https://learn.microsoft.com/java/openjdk/download) or equivalent)
- Windows / macOS / Linux

---

## Running the Game

### Option A - JAR (Recommended)

```bat
.\build.bat
.\run.bat
```

`run.bat` uses JDK 17 even if your default `java` installation is still Java 8.

Manual equivalent:

```bat
"C:\Program Files\Microsoft\jdk-17.0.19.10-hotspot\bin\java.exe" -jar dist\PokePark.jar
```

On Linux / macOS (with Java 17+ in your PATH):

```bash
java -jar dist/PokePark.jar
```

> **Note:** This project requires **Java 17+**. If you see an `UnsupportedClassVersionError`, your `java -version` is too old. Use `.\run.bat` or install a JDK 17+.

### Option B - Console

```bat
.\build.bat
java -cp "out;lib\json-simple-1.1.1.jar" PokePark
```

(On Unix systems, replace `;` with `:` in the classpath.)

When the game starts, choose your language (**French** or **English (US)**), then enter your park manager's name.

You begin with **1000 ₽**, a park capacity of **100 Pokémon**, and an inventory with **100 slots**.

---

## Goal

Grow your park by collecting Pokémon, evolving them, keeping them healthy, and increasing your daily income.

---

## Game Rules

### A Pokémon's Three Stats

| Stat | Purpose | How it Changes |
|------|---------|----------------|
| **Stamina** | Remaining energy **for today** | Decreases after each training session. Fully restores to **100%** when a day passes. |
| **Hunger** | Food level | Decreases with training and each new day. Restored by feeding your Pokémon. |
| **HP** | Health | Decreases after training and if the Pokémon is underfed overnight. Restored with Potions. |

Important rules:

- **Starving** Pokémon cannot train until they are fed.
- **Fainted (0 HP)** Pokémon cannot train until they are healed.
- **Not enough Stamina** means that training session cannot be performed until the next day.
- Injured or fainted Pokémon generate reduced income or no income the following day.

### Training

Each training session improves **only one stat**.

| Training | Effect | Stamina Cost | Style |
|----------|--------|--------------|-------|
| **Strength** | + Attack | 40 | Intense, grants more XP, harder on HP |
| **Defense** | + Defense | 30 | Balanced effort |
| **Speed** | + Speed | 20 | Light workout, slightly less XP |

Training also grants **XP**, allowing Pokémon to level up and unlock level-based evolutions.

### Passing a Day

When you advance to the next day:

1. Earn daily income based on your park's condition (levels, hunger, stamina and HP).
2. Every Pokémon rests and fully restores its stamina.
3. Hunger decreases.
4. Hungry or starving Pokémon lose HP.

Typical gameplay loop:

> Open a Mystery Box → Feed → Heal → Train → Pass a Day → Repeat

### Evolution

Mystery Boxes only contain **base-form Pokémon**.

Pokémon can evolve in two ways:

1. **By Level** (automatic through training and XP)
   - Charmander → Charmeleon (Lv. 16) → Charizard (Lv. 36)

2. **By Evolution Stone** (purchased from the Shop)
   - Pikachu + Thunder Stone → Raichu
   - Eevee + Fire Stone → Flareon
   - Eevee + Water Stone → Vaporeon
   - Eevee + Thunder Stone → Jolteon
   - Also available: Leaf Stone, Moon Stone and Link Cable (trade evolutions)

### Shop

| Category | Examples | Purpose |
|----------|----------|---------|
| Food | Oran Berry, Meal, Feast | Restore Hunger |
| Healing | Potion, Super Potion, Hyper Potion | Restore HP |
| XP | XP Candy | Gain Experience |
| Evolution Items | Fire Stone, Water Stone, Thunder Stone, Leaf Stone, Moon Stone, Link Cable | Evolve Pokémon |

### Upgrades

- **Park Expansion:** +50 Pokémon capacity for **1000 ₽**
- **Inventory Expansion:** +50 inventory slots for **500 ₽**

---

## Mystery Boxes

Mystery Boxes contain **only base-form Pokémon** (you cannot obtain Charizard or Venusaur directly).

Duplicates are **allowed** (yes, you can own multiple Mew).

| Box | Price | Contents |
|------|------:|----------|
| **COMMON** | 200 ₽ | 58 Pokémon |
| **RARE** | 500 ₽ | 10 Pokémon |
| **EPIC** | 1200 ₽ | 6 Pokémon |
| **MYTHICAL** | 3000 ₽ | Mew only |
| **LEGENDARY** | 5000 ₽ | Articuno, Zapdos, Moltres, Mewtwo |

### COMMON (200 ₽) - 58 Pokémon

Abra, Bellsprout, Bulbasaur, Caterpie, Charmander, Clefairy, Cubone, Diglett, Ditto, Doduo, Dratini, Drowzee, Eevee, Ekans, Exeggcute, Farfetch'd, Gastly, Geodude, Goldeen, Grimer, Growlithe, Horsea, Jigglypuff, Jynx, Kabuto, Koffing, Krabby, Lickitung, Machop, Magikarp, Magnemite, Mankey, Meowth, Mr. Mime, Nidoran♀, Nidoran♂, Oddish, Omanyte, Paras, Pidgey, Pikachu, Poliwag, Porygon, Psyduck, Rattata, Sandshrew, Seel, Shellder, Slowpoke, Spearow, Squirtle, Staryu, Tentacool, Venonat, Voltorb, Vulpix, Weedle, Zubat

### RARE (500 ₽) - 10 Pokémon

Chansey, Electabuzz, Hitmonchan, Hitmonlee, Lapras, Magmar, Onix, Ponyta, Rhyhorn, Tangela

### EPIC (1200 ₽) - 6 Pokémon

Aerodactyl, Kangaskhan, Pinsir, Scyther, Snorlax, Tauros

### MYTHICAL (3000 ₽) - 1 Pokémon

Mew

### LEGENDARY (5000 ₽) - 4 Pokémon

Articuno, Zapdos, Moltres, Mewtwo

### How Rarity Is Determined

- **Mew** is always **Mythical**.
- **Articuno**, **Zapdos**, **Moltres**, and **Mewtwo** are always **Legendary**.
- All other Pokémon are classified using the **Base Stat Total (BST)** of their base form:
  - BST < 280 → Common
  - BST < 360 → Rare
  - BST ≥ 360 → Epic

---

## Pokémon Names

When playing in **French**, Pokémon names use the official French localization (e.g. *Salamèche*, *Carapuce*, *Évoli*).

When playing in **English (US)**, Pokémon names remain in English (e.g. *Charmander*, *Squirtle*, *Eevee*).

Internally, the game always uses the English names for JSON data and evolution logic.

---

## Interface

The application includes five tabs:

1. **Park** - View your Pokémon, inspect details, feed, heal, train, and evolve them.
2. **Shop** - Buy items.
3. **Mystery Box** - Open Mystery Boxes of different rarities.
4. **Inventory** - View your collected items.
5. **Expand** - Upgrade your park and inventory capacity.

The **Pass Day** button is located in the top-right corner.

---

## Project Structure

```text
PokePark/
├── PokeParkApp.java      # Graphical application (main entry point)
├── PokePark.java         # Console version
├── build.bat             # Builds the JAR
├── lib/                  # json-simple
├── i18n/                 # French / English translations
├── ui/                   # Swing interface
├── Pokemon/              # Pokémon, factory, training, evolutions
├── Player/               # Player, park, inventory, income
├── Shop/                 # Shop and items
└── MisteryBox/           # Mystery Boxes and rarities
```

---

## Localization

Translation files:

- `i18n/Messages_fr.properties`
- `i18n/Messages_en_US.properties`

To add another language, create a new `Messages_xx.properties` file and register it in the language selector inside `PokeParkApp`.

---

## License

See the `LICENSE` file.
