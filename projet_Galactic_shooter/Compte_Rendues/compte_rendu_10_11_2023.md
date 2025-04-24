# Compte Rendu du 10/11/2023 à 09h40

### Membres présents : Ziad, Rayane, Alexandre

## Objectif : Ajout d'une sous classe abstraite Collectible pour l'obtention de power-ups, pieces ,... et modification du code.

<ul>
<li> Il faut créer une nouvelle classe d'objets collectables uniquement par le player
er dont la hitbox ne sera touchable que par la hitbox du player, ainsi nous avons pour le moment prévu
de créer Collectible, dont héritera Coin, Power-UP (qui sera abstraite pour 
créer différents power-ups).</li>
<br>
<li> Modifier la manière dont les Bullet se déplacent, pour l'instant nous retournons
juste le sprite en fonction de si c'est un enemy ou un player, mais cela risque d'être
plus compliqué si nous voulons implémenter  différentes manières de tirer comme
des projectiles à tête chercheuses.</li>
<br>
<li> Créer une méthode pour l'animation des Sprites, car pour le moment on augmente juste
la luminosité au max de l'image, ce qui permet juste de mettre le sprite en blanc,
ainsi, chaque sprite aura des sprites différents en fonction des différentes actions.</li>
</ul>

