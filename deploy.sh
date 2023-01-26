# git clone https://disguiser:12070127Shuai@gitee.com/disguiser/store-api.git
#git pull origin master
#mvn clean
#mvn package -Dmaven.test.skip=true
#mv ./target/*.jar ./api.jar
docker rm store-api -f
docker rmi store-api
docker build -t store-api .
docker run -v /etc/timezone:/etc/timezone -v /etc/localtime:/etc/localtime -d --name store-api --network=host store-api