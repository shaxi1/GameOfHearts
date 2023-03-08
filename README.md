# Game Of Hearts
TUI based, Java implementation of Game of Hearts (polish: Kierki), according to rules from [kurnik](https://www.kurnik.pl/kierki/zasady.phtml).
## Server side structure
Server holds multiple lobbies, inside lobbies class called "GameRunner", handles specific game type, in this case Game Of Hearts. Server is desigined in a way
that allows easily adding new GameRunners, running different types of games.
</br> Server itself supports a set of commands, that lets an admin to menage running games.
