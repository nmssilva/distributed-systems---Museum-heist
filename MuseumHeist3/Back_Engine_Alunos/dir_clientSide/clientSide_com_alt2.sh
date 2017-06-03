java -Djava.rmi.server.codebase="file:///home/sd0305/Back_Engine_Alunos/dir_registry/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.ThiefClient $1 &
sleep 3
java -Djava.rmi.server.codebase="file:///home/sd0305/Back_Engine_Alunos/dir_registry/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.MasterClient $1

