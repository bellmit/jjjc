@echo off
echo.
echo [��Ϣ] ����gateway���̡�
echo.

cd %~dp0
cd ../taiji-gateway/target

set JAVA_OPTS=-Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

java -Dfile.encoding=utf-8 -jar %JAVA_OPTS% taiji-gateway.jar

cd bin
pause