# PokePark

Gestionnaire de parc Pokémon en console : ouvre des Mystery Boxes, entretiens ta collection, entraîne-la, et gagne des revenus chaque jour.

## Lancer

```bash
javac -encoding UTF-8 -cp "lib/json-simple-1.1.1.jar" -d out PokePark.java Pokemon/*.java Player/*.java Shop/*.java MisteryBox/*.java
java -cp "out;lib/json-simple-1.1.1.jar" PokePark
```

Sous Linux/macOS, remplace `out;lib/...` par `out:lib/json-simple-1.1.1.jar`.

## Boucle de jeu

1. Acheter une Mystery Box → obtenir un **Pokémon de base** uniquement
2. Le faire évoluer : **par niveau** (entraînement / XP) ou **par pierre** (boutique)
3. Acheter nourriture / soins / boosts à la boutique
4. Passer un jour → revenus selon l'état du parc + repos
