## CandyCrush (Minecraft 1.10.X)

---

Ce plugin permet l'ajout d'un mini-jeu type CandyCrush dans un inventaire. 
Dans ce jeu, des bonbons sont placés aléatoirement dans une grille.
A chaque coup, il est possible d'échanger de place deux bonbons si cet
échange permet d'aligner trois bonbons identiques.

---
### Commandes

- /candycrush - Ouvre le mini-jeu
- /candycrush setcake - Défini le gâteau visé comme ouvrant le mini-jeu lorsque l'on clique dessus

---

### Configs
**Attention**, avec ce plugin, les configs des niveaux ne sont pas générés automatiquement.
Il faut d'abord créer un dossier 'levels' dans le dossier du plugin, il faut ensuite placer
des fichiers JSON contenant les données des niveaux dans ce dossier.

Les fichiers doivent-être nommés avec le numéro du niveau auquel il correspond.

10 niveaux d'exemples sont disponibles dans ce repo dans le dossier resources/levels/

Structure d'une config de niveau : 

```JSON
{
  "level": 1, // Numéro du niveau
  "moves": 25, // Nombre de coups avant de perdre
  "score": 100, // Score minimum à effectuer
  "colors": { // Nombre de bonbons de chaque couleur à aligner
    "ORANGE": 15,
    "BLUE": 15,
    "GREEN": 15,
    "ROSE": 15,
    "PURPLE": 15
  }
}
```