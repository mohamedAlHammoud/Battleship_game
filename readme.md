This game allows two players to play the battleship game while being able to chat live together.   
There is a server that keeps a log of the real-time connections and game traffic between players.   


This game is developed using Java, JavaSwing

The project is divided into four parts, Client Domain, Server Domain, Server GUI, and Client GUI.    
Each section has its own classes broken down by responsibility.   

To run the server, java -jar Server.jar   

To run the game, java -jar Client.jar   




after running the server, this window will open,   
![image](https://user-images.githubusercontent.com/44746577/130891514-57531697-16db-42c5-8103-ee763fb5c115.png)   

Now, the server is waiting for 2 players to enter on the network to match them.   

both players are greeted with this window to fill the fields   

![image](https://user-images.githubusercontent.com/44746577/130891626-a319138a-2b7d-4cdd-b92c-da42ed627dc2.png)   


After entering their information, the players will then be taken to the game view where they can play, the game will start once a match has been made

![image](https://user-images.githubusercontent.com/44746577/130891747-aada389e-2b73-4bd1-bff6-c752972cd92e.png)   

Once the match starts, the server will show a message indicating that   
![image](https://user-images.githubusercontent.com/44746577/130891848-fc8a0dbb-e89f-468e-b931-f307d0b88846.png)   


Once both players place their ships, they can then start attacking each other   
![image](https://user-images.githubusercontent.com/44746577/130892017-bbb87dbd-d375-41b5-a1bd-31d56f92b738.png)   

If the player manages to hit a ship from the enemy, a fire icon will be placed on the square, otherwise it will be a water icon for the missed ones.   
![image](https://user-images.githubusercontent.com/44746577/130892206-cbf28db5-03dc-41e4-bab6-49791f0c9e0b.png)   

both players can use the chat feature to chat with each other   
![image](https://user-images.githubusercontent.com/44746577/130892369-2f641af0-cdf1-40e2-846b-a9bcc86b110a.png)


![image](https://user-images.githubusercontent.com/44746577/130892401-d13e626b-3625-466b-aab6-0fe6586004a7.png)    

Once a player hits all the opponent's ships, he will be asked if the wants to play again or not,   
![image](https://user-images.githubusercontent.com/44746577/130892604-d32b785a-aaeb-4e0c-95ae-92f6f125cb09.png)

The other player will be told that he lost and also asked if he would like to play again   
![image](https://user-images.githubusercontent.com/44746577/130892679-b90ddc60-2b4f-4b7a-bacd-e38e5123afb2.png)


The server will indicate which player has won the game   
![image](https://user-images.githubusercontent.com/44746577/130892720-50e1d94c-a352-4c0e-9137-6cbd32fc9eda.png)


if both players choose the option "no", the server will show that   

![image](https://user-images.githubusercontent.com/44746577/130892788-a9430b3e-ef2f-4a15-b2f0-80f10dc29b84.png)   

Otherwise, a new game would start.
