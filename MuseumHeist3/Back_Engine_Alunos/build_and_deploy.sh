#javac interfaces/*.java registry/*.java serverSide/*.java clientSide/*.java
cp interfaces/RegisterInterface.class dir_registry/interfaces/
cp registry/*.class dir_registry/registry/
cp interfaces/*.class dir_serverSide/interfaces/
cp serverSide/*.class dir_serverSide/serverSide/
cp interfaces/*.class dir_clientSide/interfaces/
cp clientSide/*.class dir_clientSide/clientSide/
mkdir -p /home/mikael/Public/classes
mkdir -p /home/mikael/Public/classes/interfaces
mkdir -p /home/mikael/Public/classes/clientSide
cp interfaces/*.class /home/mikael/Public/classes/interfaces
cp clientSide/*.class /home/mikael/Public/classes/clientSide
cp set_rmiregistry.sh /home/mikael
cp set_rmiregistry_alt.sh /home/mikael
