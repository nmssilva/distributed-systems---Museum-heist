java -Djava.rmi.server.codebase="file:///home/mikael/Documents/distributed-systems---Museum-heist/MuseumHeist3/Back_Engine_Alunos/dir_clientSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.ThiefClient $1 &
sleep 3
java -Djava.rmi.server.codebase="file:///home/mikael/Documents/distributed-systems---Museum-heist/MuseumHeist3/Back_Engine_Alunos/dir_clientSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.MasterClient $1

