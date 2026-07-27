# PokePark

**PokePark** est un jeu de gestion de parc Pokémon en Java (interface graphique Swing).

Tu n’es pas un dresseur qui combat : tu gères un **parc**. Tu ouvres des Mystery Boxes, tu accueilles des Pokémon de **1ʳᵉ génération**, tu les nourris, les soignes, les entraînes, les fais évoluer, et tu gagnes des Pokédollars chaque jour selon l’état de ton parc.

Disponible en **français** et **anglais (US)**.

---

## Prérequis

- **JDK 17+** ([Microsoft OpenJDK](https://learn.microsoft.com/java/openjdk/download) ou équivalent)
- Windows / macOS / Linux

---

## Lancer le jeu

### Option A — JAR (recommandé)

```bat
.\build.bat
.\run.bat
```

`run.bat` utilise le JDK 17 même si ton `java` par défaut est encore en Java 8.

Équivalent manuel :

```bat
"C:\Program Files\Microsoft\jdk-17.0.19.10-hotspot\bin\java.exe" -jar dist\PokePark.jar
```

Sous Linux / macOS (avec Java 17+ dans le PATH) :

```bash
java -jar dist/PokePark.jar
```

> **Note :** le projet nécessite **Java 17+**. Si tu vois `UnsupportedClassVersionError`, ton `java -version` est trop vieux — utilise `.\run.bat` ou un JDK 17.

### Option B — Console

```bat
.\build.bat
java -cp "out;lib\json-simple-1.1.1.jar" PokePark
```

(Sous Unix, remplace `;` par `:` dans le classpath.)

Au démarrage, choisis la langue (**Français** / **English (US)**), puis entre ton nom de gestionnaire.

Tu commences avec **1000 ₽**, un parc de **100** places et un inventaire de **100** slots.

---

## Objectif

Développer ton parc : collectionner, faire évoluer, maintenir tes Pokémon en bonne forme, et faire croître tes revenus journaliers.

---

## Règles du jeu

### Les 3 jauges d’un Pokémon

| Jauge | Rôle | Comment ça bouge |
|--------|------|------------------|
| **Stamina** | Efforts restants **aujourd’hui** | Baisse à chaque entraînement. Se **recharge à 100 %** en passant un jour. |
| **Faim** | Énergie / carburant | Baisse à l’entraînement et chaque jour. Remonte en **nourrissant**. |
| **PV** | Santé | Baisse à l’effort (entraînement) et si le Pokémon est mal nourri la nuit. Remonte avec des **potions**. |

Règles importantes :

- **Affamé (STARVING)** → impossible d’entraîner (il faut nourrir).
- **K.O. (0 PV)** → impossible d’entraîner (il faut soigner).
- **Pas assez de stamina** → impossible d’entraîner ce type d’effort (il faut passer un jour).
- Un Pokémon mal en point ou K.O. **rapporte moins** (ou rien) le jour suivant.

### Entraînements

Chaque type cible **une seule stat** :

| Entraînement | Effet | Coût stamina | Style |
|--------------|--------|--------------|--------|
| **Force** | + Attaque | 40 | Intense, plus d’XP, plus dur pour les PV |
| **Défense** | + Défense | 30 | Effort modéré |
| **Vitesse** | + Vitesse | 20 | Léger, un peu moins d’XP |

L’entraînement donne aussi de l’**XP** → montée de niveau → évolutions par niveau possibles.

### Passer un jour

Quand tu passes un jour :

1. Tu gagnes des **revenus** selon le parc (niveau, faim, stamina, PV).
2. Chaque Pokémon **repose** (stamina pleine).
3. La **faim baisse**.
4. S’il a encore faim / est affamé → il **perd des PV**.

Boucle typique : ouvrir une box → nourrir / soigner → entraîner → passer un jour → recommencer.

### Évolution

Les boxes ne donnent que des **formes de base**. Ensuite :

1. **Par niveau** — automatique à l’entraînement / XP  
   Ex. : Charmander → Charmeleon (niv. 16) → Charizard (niv. 36)
2. **Par pierre** (boutique) — pour les évolutions sans niveau  
   Ex. : Pikachu + Pierre Foudre → Raichu  
   Évoli : Feu → Flareon, Eau → Vaporeon, Foudre → Jolteon  
   Aussi : Pierre Plante, Pierre Lune, Cable Link (évolutions « échange »)

### Boutique

| Catégorie | Exemples | Utilité |
|-----------|----------|---------|
| Nourriture | Baie Oran, Repas, Festin | Remplir la faim |
| Soins | Potion, Super / Hyper Potion | Restaurer les PV |
| XP | Bonbon XP | Donner de l’expérience |
| Pierres | Feu, Eau, Foudre, Plante, Lune, Cable Link | Faire évoluer |

### Agrandissements

- **Parc** : +50 places pour **1000 ₽**
- **Inventaire** : +50 slots pour **500 ₽**

---

## Mystery Boxes

Les boxes donnent **uniquement des formes de base** (pas de Charizard / Venusaur directement).  
Les **doublons sont autorisés** (ex. : plusieurs Mew).

| Box | Prix | Contenu (formes de base) |
|-----|------|---------------------------|
| **COMMON** | 200 ₽ | 58 Pokémon « classiques » |
| **RARE** | 500 ₽ | 10 Pokémon |
| **EPIC** | 1200 ₽ | 6 Pokémon |
| **MYTHICAL** | 3000 ₽ | Mew uniquement |
| **LEGENDARY** | 5000 ₽ | Articuno, Zapdos, Moltres, Mewtwo |

### COMMON (200 ₽) — 58

Abra, Bellsprout, Bulbasaur, Caterpie, Charmander, Clefairy, Cubone, Diglett, Ditto, Doduo, Dratini, Drowzee, Eevee, Ekans, Exeggcute, Farfetch'd, Gastly, Geodude, Goldeen, Grimer, Growlithe, Horsea, Jigglypuff, Jynx, Kabuto, Koffing, Krabby, Lickitung, Machop, Magikarp, Magnemite, Mankey, Meowth, Mr. Mime, Nidoran♀, Nidoran♂, Oddish, Omanyte, Paras, Pidgey, Pikachu, Poliwag, Porygon, Psyduck, Rattata, Sandshrew, Seel, Shellder, Slowpoke, Spearow, Squirtle, Staryu, Tentacool, Venonat, Voltorb, Vulpix, Weedle, Zubat

### RARE (500 ₽) — 10

Chansey, Electabuzz, Hitmonchan, Hitmonlee, Lapras, Magmar, Onix, Ponyta, Rhyhorn, Tangela

### EPIC (1200 ₽) — 6

Aerodactyl, Kangaskhan, Pinsir, Scyther, Snorlax, Tauros

### MYTHICAL (3000 ₽) — 1

Mew

### LEGENDARY (5000 ₽) — 4

Articuno, Zapdos, Moltres, Mewtwo

### Comment la rareté est calculée

- **Mew** → toujours Mythical  
- **Articuno / Zapdos / Moltres / Mewtwo** → toujours Legendary  
- Sinon, selon le **BST** (somme PV + Attaque + Défense + Vitesse) de la forme de base :
  - BST &lt; 280 → Common  
  - BST &lt; 360 → Rare  
  - sinon → Epic  

---

## Noms des Pokémon

En **français**, les noms s’affichent en version française officielle (ex. Salamèche, Carapuce, Évoli).  
En **anglais (US)**, les noms restent en anglais (Charmander, Squirtle, Eevee).  
En interne, le jeu utilise toujours les noms anglais (JSON / évolutions).

---

## Interface

Onglets de l’application :

1. **Parc** — liste, détails, nourrir / soigner / entraîner / évoluer  
2. **Boutique** — acheter des objets  
3. **Mystery Box** — ouvrir une box selon la rareté  
4. **Inventaire** — voir ce que tu possèdes  
5. **Agrandir** — agrandir parc / inventaire  

Bouton **Passer un jour** en haut à droite.

---

## Structure du projet

```
PokePark/
├── PokeParkApp.java      # App graphique (entrée principale)
├── PokePark.java         # Version console
├── build.bat             # Build du JAR
├── lib/                  # json-simple
├── i18n/                 # Traductions FR / EN-US
├── ui/                   # Interface Swing
├── Pokemon/              # Pokémon, factory, entraînements, évolutions
├── Player/               # Joueur, parc, inventaire, revenus
├── Shop/                 # Boutique et objets
└── MisteryBox/           # Boxes et raretés
```

---

## Langues (traduction)

Fichiers :

- `i18n/Messages_fr.properties`
- `i18n/Messages_en_US.properties`

Pour ajouter une langue : créer `Messages_xx.properties` et l’enregistrer dans le sélecteur de `PokeParkApp`.

---

## Licence

Voir le fichier `LICENSE`.
