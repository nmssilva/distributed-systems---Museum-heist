#Copy Heist.jar and configuration.txt to all computers
#for i in 01 03 04 05 07 09 10
#do
	#echo "Copying Heist.jar and configuration.txt to sd0309@l040101-ws$i.ua.pt"
	#sshpass -p qwerty scp "Heist.jar" sd0309@l040101-ws$i.ua.pt:~
	#sshpass -p qwerty scp "configuration.txt" sd0309@l040101-ws$i.ua.pt:~
#done

#l040101-ws01.ua.pt;Thieves
#l040101-ws03.ua.pt;22350;Logger
#l040101-ws04.ua.pt;22353;Museum
#l040101-ws05.ua.pt;22351;CS
#l040101-ws07.ua.pt;22352;CCS
#l040101-ws09.ua.pt;22354;AP0
#l040101-ws10.ua.pt;22355;AP1

#echo "Starting Logger Server"
#sshpass -p colateral305 ssh sd0305@l040101-ws03.ua.pt "nohup java -jar Logger/HeistToTheMuseum2.jar > /dev/null 2>&1"

#echo "Starting Museum Server"
#sshpass -p colateral305 ssh sd0309@l040101-ws04.ua.pt "nohup java -jar Museum/HeistToTheMuseum2.jar > /dev/null 2>&1"

echo "Starting Concetration Site Server"
sshpass -p colateral305 ssh sd0309@l040101-ws05.ua.pt "nohup java -jar CS/HeistToTheMuseum2.jar > /dev/null 2>&1"

echo "Starting ControlCollection Site Server"
sshpass -p colateral305 ssh sd0309@l040101-ws07.ua.pt "nohup java -jar CCS/HeistToTheMuseum2.jar > /dev/null 2>&1"

echo "Starting AssaultParty #0 Server"
sshpass -p colateral305 ssh sd0309@l040101-ws09.ua.pt "nohup java -jar AP0/HeistToTheMuseum2.jar > /dev/null 2>&1"

echo "Starting AssaultParty #1 Server"
sshpass -p colateral305 ssh sd0309@l040101-ws10.ua.pt "nohup java -jar AP1/HeistToTheMuseum2.jar > /dev/null 2>&1"

echo "Lauching Thieves"
sshpass -p colateral305 ssh sd0309@l040101-ws01.ua.pt "nohup java -jar Thief/HeistToTheMuseum2.jar > /dev/null 2>&1"
sshpass -p colateral305 ssh sd0309@l040101-ws01.ua.pt "java -jar Master/HeistToTheMuseum2.jar > /dev/null 2>&1"

#sshpass -p qwerty scp sd0309@l040101-ws01.ua.pt:~/log.txt .

