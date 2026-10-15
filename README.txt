GHOST SKIN - Fabric-Client-Mod fuer Minecraft 26.2
===================================================
Was macht die Mod?
  Jeder Spieler mit dem Unsichtbarkeits-Effekt wird fuer alle (die die Mod haben)
  als heller, durchsichtiger Geist mit DEINEM Skin gezeichnet, plus Partikel.
  Zusammen mit dem Datapack:  /function ghost:on  /  /function ghost:off

WICHTIG: Jeder Spieler braucht die Mod (in seinem Profil), sonst sieht er nur
         die normale Vanilla-Darstellung.

Selbst bauen (Java 25 / JDK 25 noetig):
  1. Am einfachsten: auf https://fabricmc.net/develop/template/ ein leeres Projekt
     fuer 26.2 erzeugen, dann den Ordner "src" aus diesem Projekt hineinkopieren
     (und die alten Beispieldateien loeschen). Die Build-Dateien aus dem Template
     sind dann garantiert aktuell.
  2. Oder dieses Projekt direkt: Gradle 9.5.1 installieren, im Ordner
         gradle wrapper --gradle-version 9.5.1
         ./gradlew build        (Windows: gradlew.bat build)
  3. Die fertige Datei liegt in build/libs/ghostskin-1.0.0.jar
     (NICHT die "-sources"-Datei). In den mods-Ordner des Profils kopieren.
     Als Voraussetzung braucht das Profil nur Fabric Loader (kein Fabric API).

Einstellungen zum Anpassen (oben in LivingEntityRendererMixin.java):
  GHOST_WHITE_MIX    0.0 - 1.0   wie hell/weiss der Skin wird
  GHOST_ALPHA_ARGB   0x66FFFFFF  wie sichtbar (0x26 = Vanilla, 0xAA = deutlich sichtbarer)

Hinweis: Dieser Code wurde ohne Minecraft-Test geschrieben.
