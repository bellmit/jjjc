@echo off
echo.
echo [��Ϣ] ����modules-gen���̡�
echo.

cd %~dp0
cd ../taiji-modules/taiji-gen/target

set JAVA_OPTS=-Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

java -Dfile.encoding=utf-8 -jar %JAVA_OPTS% taiji-modules-gen.jar

cd bin
pause