java -Djava.rmi.server.codebase="file:///home/mikael/Documents/distributed-systems---Museum-heist/MuseumHeist3/Back_Engine_Alunos/dir_serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.LoggerServer &
sleep 1
java -Djava.rmi.server.codebase="file:///home/mikael/Documents/distributed-systems---Museum-heist/MuseumHeist3/Back_Engine_Alunos/dir_serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.MuseumServer &
sleep 1
java -Djava.rmi.server.codebase="file:///home/mikael/Documents/distributed-systems---Museum-heist/MuseumHeist3/Back_Engine_Alunos/dir_serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.AssaultParty0Server &
sleep 1
java -Djava.rmi.server.codebase="file:///home/mikael/Documents/distributed-systems---Museum-heist/MuseumHeist3/Back_Engine_Alunos/dir_serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.AssaultParty1Server &
sleep 1
java -Djava.rmi.server.codebase="file:///home/mikael/Documents/distributed-systems---Museum-heist/MuseumHeist3/Back_Engine_Alunos/dir_serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.ControlCollectionServer &
sleep 1
java -Djava.rmi.server.codebase="file:///home/mikael/Documents/distributed-systems---Museum-heist/MuseumHeist3/Back_Engine_Alunos/dir_serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.ConcentrationSiteServer &