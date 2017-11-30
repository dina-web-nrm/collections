#!/bin/bash
# able to check the TOKEN here -> https://jwt.io/ 
source .env

if [ ! -f .env ]; then
  echo "You need to create the file .env. DO:  'cp template.env .env' and add credentials"
  exit 1
fi 

dt=$(date '+%Y-%m-%d %H:%M:%S');

EXPECTED_RESULT="Hello from collections api"

## use alpha-keycloak ( running from your local-machine ) or alpha-cm (running from same-machine as alpha-cm)
KEYCLOAK_SERVER=https://alpha-keycloak.dina-web.net/auth/realms/dina/protocol/openid-connect/token
#KEYCLOAK_SERVER=https://alpha-cm.dina-web.net/auth/realms/dina/protocol/openid-connect/token ## testing, anton has a 'routing'
ALPHA_SERVER=https://alpha-api.dina-web.net

RESULT=`curl --data "grant_type=password&client_id=${client_id}&username=${username}&password=${password}" $KEYCLOAK_SERVER`
TOKEN=`echo $RESULT | sed 's/.*access_token":"//g' | sed 's/".*//g'`

# able to pass in '-v' for verbose 
if [ $1 = "-v" ]; then
   RESPONSE=`curl -vH "Authorization: bearer $TOKEN" ${ALPHA_SERVER}/collections/api/v01`
else
   RESPONSE=`curl -H "Authorization: bearer $TOKEN" ${ALPHA_SERVER}/collections/api/v01`
fi

#echo $TOKEN

echo ""
echo "Response from ${ALPHA_SERVER} is \"$RESPONSE\" "
[ "$RESPONSE" == "$EXPECTED_RESULT" ] && echo "TRUE : correct response" || echo "FALSE: wrong response"
echo ""

# writing to a log file
echo "$dt">> script.log
echo "Token is fetched from $KEYCLOAK_SERVER" >> script.log
echo "GET-Response from the ${ALPHA_SERVER} is \"$RESPONSE\" " >>script.log
echo "" >>script.log

exit 0
