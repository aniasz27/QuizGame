# Quizzzzz

## Description of project

This is a quiz consisting of 20 questions including Multiple Choice, Estimate, What Requires More Energy and Instead Of.
The game can be played either singleplayer or multiplayer. Depending on the type of the question, the player can either
click a button or enter the answer in the answer box. Each question must be answered within the 10 seconds time limit
indicated by the bar at the top of the screen. After the time runs out, the correct answer is displayed to the player.
For each correct answer, the player earns points depending on how fast they answered or if applicable, how close the
answer was. To make the game more engaging you can use the Jokers and Emoji reactions.

## Group members

| Profile Picture | Name | Email |
|---|---|---|
| ![](https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/4876/avatar.png?width=40) | Kazek Ciaś | k.j.cias@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/c87a70993d312931e28fff85d53a9adf?s=40&d=identicon) | Andrei Drăgoi | A.Dragoi@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/46f2a53214a98ce4cdda06c55c2cf62b?s=40&d=identicon) | Anna Szymkowiak | A.M.Szymkowiak@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/553291783a5c3c984536f965c0a15b9f?s=40&d=identicon) | Lucia Navarcikova | L.Navarcikova@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/c7c2380d93047b2eef861080b7af7ec4?s=40&d=identicon) | Santiago de Heredia | S.A.deherediatenorio@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/de2c8cce3c9d5f9e0ca0593bc3eb93b7?s=40&d=identicon) | Anthony Chen | A.Z.Chen@student.tudelft.nl |

## How to run it

### To start the server

- Clone the repository
- Open the terminal on the machine hosting the server and go to the folder which contains the parent folder
- Make sure you have the JSON folder under `server\src\main\resources`. You can download the JSON folder
  from [here](https://gitlab.ewi.tudelft.nl/cse1105/2021-2022/activity-bank)
- Run the server through Gradle (type `.\gradlew bootRun`)
- Import the activities, by going to this URL: `localhost:8080/api/activity/importActivities`

#### Play with someone playing from another computer

- Other players do **not** need to run the server, just the client. See the section below
- You need to be on the same Wi-Fi network and give them your IPv4 Address which you can find in the Terminal by
  typing `ipconfig`
- The server the other players need to enter in the server field is `your IPv4` + `:8080`

### To start the client

- Clone the repository
- Open the terminal (another instance if you are running the server on the same machine) and go to the folder which
  contains the parent folder. You can do this by typing `cd <file location>`
- Run the client through Gradle (type `.\gradlew run`)

Done! Now you can play!

## How to contribute to it

- You can fork the project from GitLab and work on that to implement your changes.
- If you find any issues, you can open them in our project directly.
