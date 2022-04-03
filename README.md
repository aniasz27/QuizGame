# Starting template

This README will need to contain a description of your project, how to run it, how to set up the development
environment, and who worked on it. This information can be added throughout the course, except for the names of the
group members. Add your own name (do not add the names for others!) to the section below.

## Description of project

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
- Run the server through Gradle (type `.\gradlew bootRun`)
- Import the activities, by going to this URL: localhost:8080/api/activity/importActivities (make sure you have the JSON
  folder under `server\src\main\resources`. You can download the JSON folder
  from [here](https://gitlab.ewi.tudelft.nl/cse1105/2021-2022/activity-bank))

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

## Copyright / License (opt.)
