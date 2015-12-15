javac JDBCCompileEverytimePerformance.java
javac JDBCCompileOncePerformance.java
java -classpath .:/usr/share/java/mysql.jar:$classpath JDBCCompileEverytimePerformance
java -classpath .:/usr/share/java/mysql.jar:$classpath JDBCCompileOncePerformance
