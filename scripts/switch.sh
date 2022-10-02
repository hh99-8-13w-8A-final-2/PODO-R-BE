echo "> 현재 구동중인 Port 확인"
CURRENT_PROFILE=$(curl -s http://localhost/api/nginx/profile)

# 쉬고 있는 set 찾기: port1이 사용중이면 port2가 쉬고 있고, 반대면 port1이 쉬고 있음
if [ $CURRENT_PROFILE == port1 ]
then
  IDLE_PORT=8082
elif [ $CURRENT_PROFILE == port2 ]
then
  IDLE_PORT=8081
else
  echo "> 일치하는 Profile이 없습니다. Profile: $CURRENT_PROFILE"
  echo "> 8081을 할당합니다."
  IDLE_PORT=8081
fi

echo "> 전환할 Port: $IDLE_PORT"
echo "> Port 전환"
echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" |sudo tee /etc/nginx/conf.d/service-url.inc

PROXY_PORT=$(curl -s http://localhost/api/nginx/profile)
echo "> Nginx Current Proxy Port: $PROXY_PORT"

echo "> Nginx Reload"
sudo nginx -s reload